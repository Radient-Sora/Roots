package epicsquid.roots.recipe.irecipe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RotatingShapedOreRecipe extends ShapedOreRecipe {
  protected NonNullList<Ingredient> input_90;
  protected NonNullList<Ingredient> input_180;
  protected NonNullList<Ingredient> input_270;

  public RotatingShapedOreRecipe(ResourceLocation group, @Nonnull ItemStack result, RotatingShapedPrimer primer) {
    super(group, result, primer);
  }

  /**
   * Used to check if a recipe matches current crafting inventory
   */
  @Override
  public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World world) {
    for (int x = 0; x <= inv.getWidth() - width; x++) {
      for (int y = 0; y <= inv.getHeight() - height; ++y) {
        if (checkMatch(inv, x, y, false)) {
          return true;
        }
      }
    }

    return false;
  }

  public static RotatingShapedOreRecipe factory(JsonContext context, JsonObject json) {
    String group = JsonUtils.getString(json, "group", "");

    Map<Character, Ingredient> ingMap = Maps.newHashMap();
    for (Map.Entry<String, JsonElement> entry : JsonUtils.getJsonObject(json, "key").entrySet()) {
      if (entry.getKey().length() != 1)
        throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
      if (" ".equals(entry.getKey()))
        throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");

      ingMap.put(entry.getKey().toCharArray()[0], CraftingHelper.getIngredient(entry.getValue(), context));
    }

    ingMap.put(' ', Ingredient.EMPTY);

    JsonArray patternJ = JsonUtils.getJsonArray(json, "pattern");

    if (patternJ.size() == 0)
      throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");

    String[] pattern = new String[patternJ.size()];
    for (int x = 0; x < pattern.length; ++x) {
      String line = JsonUtils.getString(patternJ.get(x), "pattern[" + x + "]");
      if (x > 0 && pattern[0].length() != line.length())
        throw new JsonSyntaxException("Invalid pattern: each row must  be the same width");
      pattern[x] = line;
    }

    RotatingShapedPrimer primer = new RotatingShapedPrimer();
    primer.width = pattern[0].length();
    primer.height = pattern.length;
    primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
    primer.input = NonNullList.withSize(primer.width * primer.height, Ingredient.EMPTY);
    primer.input_90 = NonNullList.withSize(primer.width * primer.height, Ingredient.EMPTY);
    primer.input_180 = NonNullList.withSize(primer.width * primer.height, Ingredient.EMPTY);
    primer.input_270 = NonNullList.withSize(primer.width * primer.height, Ingredient.EMPTY);

    Set<Character> keys = Sets.newHashSet(ingMap.keySet());
    keys.remove(' ');

    List<char[]> _base = new ArrayList<>();

    int x = 0;
    for (String line : pattern) {
      _base.add(line.toCharArray());
      for (char chr : line.toCharArray()) {
        Ingredient ing = ingMap.get(chr);
        if (ing == null)
          throw new JsonSyntaxException("Pattern references symbol '" + chr + "' but it's not defined in the key");
        primer.input.set(x++, ing);
        keys.remove(chr);
      }
    }

    char[][] base = _base.toArray(new char[][]{});
    char[][] rotated_90 = new char[patternJ.size()][patternJ.size()];
    for (int y2 = 0; y2 < base.length; y2++) {
      for (int x2 = 0; x2 < base[y2].length; x2++) {
        rotated_90[y2][base[y2].length - 1 - x2] = base[x2][y2];
      }
    }
    char[][] rotated_180 = new char[patternJ.size()][patternJ.size()];
    for (int y2 = 0; y2 < rotated_90.length; y2++) {
      for (int x2 = 0; x2 < rotated_90[y2].length; x2++) {
        rotated_180[y2][rotated_90[y2].length - 1 - x2] = rotated_90[x2][y2];
      }
    }
    char[][] rotated_270 = new char[patternJ.size()][patternJ.size()];
    for (int y2 = 0; y2 < rotated_270.length; y2++) {
      for (int x2 = 0; x2 < rotated_270[y2].length; x2++) {
        rotated_270[y2][rotated_270[y2].length - 1 - x2] = rotated_270[x2][y2];
      }
    }

    x=0;
    for (char[] line : rotated_90) {
      for (char chr : line) {
        primer.input_90.set(x++, ingMap.get(chr));
      }
    }
    x=0;
    for (char[] line : rotated_180) {
      for (char chr : line) {
        primer.input_180.set(x++, ingMap.get(chr));
      }
    }
    x=0;
    for (char[] line : rotated_270) {
      for (char chr : line) {
        primer.input_270.set(x++, ingMap.get(chr));
      }
    }

    if (!keys.isEmpty())
      throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + keys);

    ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
    return new RotatingShapedOreRecipe(group.isEmpty() ? null : new ResourceLocation(group), result, primer);
  }

  public static class RotatingShapedPrimer extends CraftingHelper.ShapedPrimer {
    public RotatingShapedPrimer() {
    }

    public NonNullList<Ingredient> input_90;
    public NonNullList<Ingredient> input_180;
    public NonNullList<Ingredient> input_270;
  }
}
