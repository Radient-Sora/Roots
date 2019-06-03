package epicsquid.roots.spell;

import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellSmeltersLick extends SpellBase {
    public static String spellName = "spell_smelters_lick";
    public static SpellSmeltersLick instance = new SpellSmeltersLick(spellName);

    public SpellSmeltersLick(String name) {
        super(name, TextFormatting.RED, 244f / 255f, 175f / 255f, 55f / 255f, 238f / 255f, 224f / 255f, 70f / 255f);
        this.castType = EnumCastType.INSTANTANEOUS;
        this.cooldown = 20;

        addCost(HerbRegistry.getHerbByName("infernal_bulb"), 0.125f);
        addIngredients(
                new ItemStack(ModItems.infernal_bulb),
                new OreIngredient("ingotIron"),
                new ItemStack(Items.COAL),
                new ItemStack(Items.BLAZE_POWDER),
                new ItemStack(Items.FLINT_AND_STEEL)
        );
    }
//TODO Add a wow factor maybe particle to hand and add a cast time so the wand charges up and releases the spell
    @Override
    public boolean cast(EntityPlayer player, List<SpellModule> modules) {
        World world = player.world;
        ItemStack offStack = player.getHeldItemOffhand().copy();
        if (!world.isRemote) {
            if(!player.getHeldItemOffhand().isEmpty()){
                ItemStack smeltedItem = FurnaceRecipes.instance().getSmeltingResult(offStack);
                ItemStack result = smeltedItem.copy();
                if(!smeltedItem.isEmpty()){
                    player.getHeldItemOffhand().shrink(1);
                    player.addItemStackToInventory(result);
                    player.inventory.markDirty();
                }
            }
        }
        return true;
    }
}
