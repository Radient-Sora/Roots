package epicsquid.roots.spell;

import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellSecondWind extends SpellBase {
  public static String spellName = "spell_second_wind";
  public static SpellSecondWind instance = new SpellSecondWind(spellName);

  public SpellSecondWind(String name) {
    super(name, TextFormatting.BLUE, 64f / 255f, 64f / 255f, 64f / 255f, 192f / 255f, 32f / 255f, 255f / 255f);
    this.castType = EnumCastType.INSTANTANEOUS;
    this.cooldown = 24;

    addCost(HerbRegistry.getHerbByName("terra_moss"), 0.125f);
    addIngredients(
        new ItemStack(ModItems.terra_spores),
        new OreIngredient("sugarcane"),
        new ItemStack(Items.CLAY_BALL),
        new ItemStack(Items.GLASS_BOTTLE),
        new OreIngredient("ingotIron")
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    player.setAir(300);
    if (player.world.isRemote) {
      player.playSound(SoundEvents.ENTITY_BOAT_PADDLE_WATER, 1, 1);
    }
    return true;
  }
}
