package epicsquid.roots.integration.crafttweaker;

import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".AnimalHarvest")
public class AnimalHarvestTweaker {

  @ZenMethod
  public static void addEntity(IEntityDefinition entity) {
    CraftTweaker.LATE_ACTIONS.add(new Add((EntityEntry) entity.getInternal()));
  }

  @ZenMethod
  public static void removeEntity(IEntityDefinition entity) {
    CraftTweaker.LATE_ACTIONS.add(new Remove((EntityEntry) entity.getInternal()));
  }

  private static class Remove extends BaseAction {
    private final EntityEntry entry;

    public Remove(EntityEntry entry) {
      super("remove_animal_harvest");
      this.entry = entry;
    }

    @Override
    public void apply() {
      Class<? extends Entity> clz = entry.getEntityClass();
      ModRecipes.removeAnimalHarvestRecipe(clz);
    }

    @Override
    protected String getRecipeInfo() {
      return String.format("Recipe to remove %s from AnimalHarvest", entry.getName());
    }
  }

  private static class Add extends BaseAction {
    private final EntityEntry entry;

    public Add(EntityEntry entry) {
      super("add_animal_harvest");
      this.entry = entry;
    }

    @Override
    public void apply() {
      ModRecipes.addAnimalHarvestRecipe(entry.getName(), entry.getEntityClass());
    }

    @Override
    protected String getRecipeInfo() {
      return String.format("Recipe to add %s to AnimalHarvest", entry.getName());
    }
  }
}
