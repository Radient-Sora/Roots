package epicsquid.roots.ritual;

import epicsquid.roots.config.RitualConfig;
import epicsquid.roots.ritual.natural.RitualFlowerGrowth;
import epicsquid.roots.ritual.natural.RitualWildGrowth;
import epicsquid.roots.ritual.wild.RitualSummoning;
import epicsquid.roots.tileentity.TileEntityBonfire;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;

public class RitualRegistry {

  public static Map<String, RitualBase> ritualRegistry = new HashMap<>();

  public static RitualBase ritual_life, ritual_storm, ritual_light, ritual_fire_storm, ritual_regrowth, ritual_windwall,
          ritual_warden, ritual_natural_aura, ritual_purity, ritual_frost, ritual_animal_harvest, ritual_summoning,
          ritual_wild_growth, ritual_overgrowth, ritual_flower_growth, ritual_transmutation;

  public static RitualBase getRitual(TileEntityBonfire tileEntity, EntityPlayer player) {
    for (int i = 0; i < ritualRegistry.size(); i++) {
      RitualBase ritual = ritualRegistry.values().toArray(new RitualBase[ritualRegistry.size()])[i];
      if (ritual.isRitualRecipe(tileEntity, player)) {
        return ritual;
      }
    }
    return null;
  }

  public static RitualBase getRitual(String ritualName) {
    if(ritualName == null){
      return null;
    }
    for (int i = 0; i < ritualRegistry.size(); i++) {
      RitualBase ritual = ritualRegistry.values().toArray(new RitualBase[ritualRegistry.size()])[i];
      if (ritual.getName().equalsIgnoreCase(ritualName)) {
        return ritual;
      }
    }
    return null;
  }

  public static void init() {
    if (!RitualConfig.disableRitualCategory.disableLife)
      addRitual(ritual_life = new RitualLife("ritual_life", 1200));
    if (!RitualConfig.disableRitualCategory.disableStorm)
      addRitual(ritual_storm = new RitualStorm("ritual_storm", 2400));
    if (!RitualConfig.disableRitualCategory.disableLight)
      addRitual(ritual_light = new RitualLight("ritual_light", 1200));
    if (!RitualConfig.disableRitualCategory.disableFireStorm)
      addRitual(ritual_fire_storm = new RitualFireStorm("ritual_fire_storm", 1200));
    if (!RitualConfig.disableRitualCategory.disableRegrowth)
      addRitual(ritual_regrowth = new RitualRegrowth("ritual_regrowth", 2400));
    if (!RitualConfig.disableRitualCategory.disableWindwall)
      addRitual(ritual_windwall = new RitualWindwall("ritual_windwall", 3000));
    if (!RitualConfig.disableRitualCategory.disableWarden)
      addRitual(ritual_warden = new RitualWarden("ritual_warden", 1200));
    if (!RitualConfig.disableRitualCategory.disableNaturalAura)
      addRitual(ritual_natural_aura = new RitualNaturalAura("ritual_natural_aura", 1200));
    if (!RitualConfig.disableRitualCategory.disablePurity)
      addRitual(ritual_purity = new RitualPurity("ritual_purity", 1200));
    if (!RitualConfig.disableRitualCategory.disableFrost)
      addRitual(ritual_frost = new RitualFrost("ritual_frost", 6400));
    if (!RitualConfig.disableRitualCategory.disableAnimalHarvest)
      addRitual(ritual_animal_harvest = new RitualAnimalHarvest("ritual_animal_harvest", 3200));
    if (!RitualConfig.disableRitualCategory.disableSummoning)
      addRitual(ritual_summoning = new RitualSummoning("ritual_summoning", 0));
    if (!RitualConfig.disableRitualCategory.disableWildGrowth)
      addRitual(ritual_wild_growth = new RitualWildGrowth("ritual_wild_growth", 300));
    if (!RitualConfig.disableRitualCategory.disableOvergrowth)
      addRitual(ritual_overgrowth = new RitualOvergrowth("ritual_overgrowth", 6400));
    if (!RitualConfig.disableRitualCategory.disableFlowerGrowth)
      addRitual(ritual_flower_growth = new RitualFlowerGrowth("ritual_flower_growth", 3200));
    if (!RitualConfig.disableRitualCategory.disableTransmutation)
      addRitual(ritual_transmutation = new RitualTransmutation("ritual_transmutation", 2400));
  }

  public static void addRitual(RitualBase ritual) {
    ritualRegistry.put(ritual.getName(), ritual);
  }

}