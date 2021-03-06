package epicsquid.roots.entity.ritual;

import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityRitualFlowerGrowth extends EntityRitualBase {

  protected static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityRitualFlowerGrowth.class, DataSerializers.VARINT);

  public EntityRitualFlowerGrowth(World worldIn) {
    super(worldIn);
    getDataManager().register(lifetime, RitualRegistry.ritual_flower_growth.getDuration() + 20);
  }

  @Override
  public void onUpdate() {
    getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) < 0) {
      setDead();
    }

    if (this.ticksExisted % 100 == 0)
    {
      BlockPos topBlockPos = RitualUtil.getRandomPosRadial(new BlockPos(getPosition().getX(), getPosition().getY() - 20, getPosition().getZ()), 10, 10);
      while (!generateFlower(topBlockPos))
      {
        topBlockPos = topBlockPos.up();
        if (topBlockPos.getY() > 256)
          break;
      }
    }
  }

  private boolean generateFlower(BlockPos pos) {
    IBlockState flower = getRandomFlower();
    if (world.isAirBlock(pos) && flower.getBlock().canPlaceBlockAt(world, pos)) {
      world.setBlockState(pos, flower);
      return true;
    }
    return false;
  }

  private IBlockState getRandomFlower() {
    int meta = rand.nextInt(9);
    switch (meta) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
        return Blocks.RED_FLOWER.getStateFromMeta(meta);
      case 9:
        return Blocks.YELLOW_FLOWER.getStateFromMeta(meta - 9);
    }
    return Blocks.AIR.getDefaultState();
  }

  @Override
  public DataParameter<Integer> getLifetime() {
    return lifetime;
  }

}
