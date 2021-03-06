package epicsquid.roots.ritual.natural;

import epicsquid.roots.entity.ritual.EntityRitualWildGrowth;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.ritual.RitualBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class RitualWildGrowth extends RitualBase {

    public RitualWildGrowth(String name, int duration) {
        super(name, duration);

        addCondition(new ConditionItems(
                new ItemStack(ModItems.wildroot),
                new ItemStack(ModItems.bark_oak),
                new ItemStack(ModItems.bark_oak),
                new ItemStack(ModItems.bark_dark_oak),
                new ItemStack(Items.DYE, 1, 15))
        );
        setIcon(ModItems.ritual_wild_growth);
        setColor(TextFormatting.DARK_GRAY);
        setBold(true);
    }

    @Override
    public void doEffect(World world, BlockPos pos) {
        this.spawnEntity(world, pos, EntityRitualWildGrowth.class);
    }
}
