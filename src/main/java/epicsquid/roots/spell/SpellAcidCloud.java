package epicsquid.roots.spell;

import java.util.List;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageAcidCloudFX;
import epicsquid.roots.spell.modules.ModuleRegistry;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SpellAcidCloud extends SpellBase {
  public static Property<Integer> PROP_COOLDOWN = new Property<>("cooldown", 24);
  public static Property<EnumCastType> PROP_CAST_TYPE = new Property<>("cast_type", EnumCastType.CONTINUOUS);
  public static Property<SpellCost> PROP_COST_1 = new Property<>("cost_1", new SpellCost("terra_moss", 0.0625));
  public static Property<SpellCost> PROP_COST_2 = new Property<>("cost_2", new SpellCost("baffle_Cap", 0.125));
  public static Property<Float> PROP_DAMAGE = new Property<>("damage", 1.0f);
  public static Property<Integer> PROP_DURATION = new Property<>("duration", 80);
  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 5);

  public static String spellName = "spell_acid_cloud";
  public static SpellAcidCloud instance = new SpellAcidCloud(spellName);

  private float damage;
  private int duration;
  private int fireDuration;

  public SpellAcidCloud(String name) {
    super(name, TextFormatting.DARK_GREEN, 80f / 255f, 160f / 255f, 40f / 255f, 64f / 255f, 96f / 255f, 32f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_DAMAGE, PROP_DURATION, PROP_FIRE_DURATION);

    addIngredients(
        new ItemStack(Items.SPIDER_EYE),
        new ItemStack(Item.getItemFromBlock(ModBlocks.baffle_cap_mushroom)),
        new ItemStack(Items.SUGAR),
        new ItemStack(ModItems.terra_moss),
        new ItemStack(ModItems.terra_moss)
    );
  }

  public void finalise () {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
    this.damage = properties.getProperty(PROP_DAMAGE);
    this.duration = properties.getProperty(PROP_DURATION);
    this.fireDuration = properties.getProperty(PROP_FIRE_DURATION);

    SpellCost cost = properties.getProperty(PROP_COST_1);
    addCost(HerbRegistry.getHerbByName(cost.herb), cost.cost);
    cost = properties.getProperty(PROP_COST_2);
    addCost(HerbRegistry.getHerbByName(cost.herb), cost.cost);
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    if (!player.world.isRemote) {
      List<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class,
          new AxisAlignedBB(player.posX - 4.0, player.posY - 1.0, player.posZ - 4.0, player.posX + 4.0, player.posY + 3.0, player.posZ + 4.0));
      for (EntityLivingBase e : entities) {
        if (!(e instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())
            && e.getUniqueID().compareTo(player.getUniqueID()) != 0) {
          e.attackEntityFrom(DamageSource.causeMobDamage(player), damage);
          e.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("poison"), duration, 0));
          e.setRevengeTarget(player);
          e.setLastAttackedEntity(player);

          if(modules.contains(ModuleRegistry.module_fire)){
            e.setFire(fireDuration);
          }
        }
      }
      PacketHandler.sendToAllTracking(new MessageAcidCloudFX(player.posX, player.posY + player.getEyeHeight(), player.posZ), player);
    }
    return true;
  }

}
