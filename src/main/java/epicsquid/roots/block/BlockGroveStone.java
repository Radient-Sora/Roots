package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockTEBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockGroveStone extends BlockTEBase {
  public static final PropertyEnum<Half> HALF = PropertyEnum.create("half", Half.class);
  public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
  public static final PropertyBool VALID = PropertyBool.create("valid");

  public BlockGroveStone(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, teClass);

    this.setDefaultState(this.blockState.getBaseState().withProperty(VALID, false).withProperty(HALF, Half.BOTTOM).withProperty(FACING, EnumFacing.NORTH));
    this.setTickRandomly(true);
  }

  @Nonnull
  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }

  @Override
  public boolean isFullCube(@Nonnull IBlockState state) {
    return false;
  }

  @Override
  public boolean isOpaqueCube(@Nonnull IBlockState state) {
    return false;
  }

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    return new AxisAlignedBB(0, 0, 0, 1, 1, 1);
  }

  @Override
  @SuppressWarnings("deprecation")
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(VALID, (meta & 1) == 1).withProperty(HALF, Half.fromInt((meta >> 1 & 1))).withProperty(FACING, EnumFacing.byIndex(((meta >> 2) + 2)));
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return (state.getValue(FACING).ordinal() - 2) << 2 | state.getValue(HALF).ordinal() << 1 | (state.getValue(VALID) ? 1 : 0);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, HALF, VALID, FACING);
  }

  @Override
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    if (worldIn.getBlockState(pos.down()).getBlock() == this) return false;

    IBlockState up = worldIn.getBlockState(pos.up());
    IBlockState upup = worldIn.getBlockState(pos.up().up());

    return up.getBlock() != this && upup.getBlock() != this && super.canPlaceBlockAt(worldIn, pos) && up.getBlock().isReplaceable(worldIn, pos.up()) && upup.getBlock().isReplaceable(worldIn, pos.up().up());
  }

  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    worldIn.setBlockState(pos.up(), this.getDefaultState().withProperty(HALF, Half.TOP));
    worldIn.setBlockState(pos.up().up(), this.getDefaultState().withProperty(HALF, Half.TOP));
  }

  @Override
  @SuppressWarnings("deprecation")
  public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    // Work out the facing
    EnumFacing f = EnumFacing.fromAngle(placer.rotationYaw).getOpposite();
    if (f == EnumFacing.UP || f == EnumFacing.DOWN) {
      f = EnumFacing.NORTH;
    }
    return this.getDefaultState().withProperty(HALF, Half.BOTTOM).withProperty(FACING, f);
  }

  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    super.breakBlock(worldIn, pos, state);

    IBlockState down = worldIn.getBlockState(pos.down());
    IBlockState downdown = worldIn.getBlockState(pos.down().down());
    IBlockState up = worldIn.getBlockState(pos.up());
    IBlockState upup = worldIn.getBlockState(pos.up().up());

    if (down.getBlock() == this) {
      worldIn.setBlockToAir(pos.down());
    }
    if (downdown.getBlock() == this) {
      worldIn.setBlockToAir(pos.down().down());
    }
    if (up.getBlock() == this) {
      worldIn.setBlockToAir(pos.up());
    }
    if (upup.getBlock() == this) {
      worldIn.setBlockToAir(pos.up());
    }
  }

  @Override
  public int damageDropped(IBlockState state) {
    return 0;
  }

  public enum Half implements IStringSerializable {
    TOP,
    BOTTOM;

    @Override
    public String getName() {
      switch (this) {
        case TOP:
          return "top";
        default:
          return "bottom";
      }
    }

    public static Half fromInt(int x) {
      return values()[x];
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    super.randomDisplayTick(stateIn, worldIn, pos, rand);

    if (stateIn.getValue(VALID)) {
      for (int i = -2; i <= 2; ++i) {
        for (int j = -2; j <= 2; ++j) {
          if (i > -2 && i < 2 && j == -1) {
            j = 2;
          }

          if (rand.nextInt(16) == 0) {
            for (int k = 0; k <= 1; ++k) {
              if (!worldIn.isAirBlock(pos.add(i / 2, 0, j / 2))) {
                break;
              }

              worldIn.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, (double) pos.getX() + 0.5D, (double) pos.getY() + 2.0D, (double) pos.getZ() + 0.5D, (double) ((float) i + rand.nextFloat()) - 0.5D, (double) ((float) k - rand.nextFloat() - 1.0F), (double) ((float) j + rand.nextFloat()) - 0.5D);
            }
          }
        }
      }
    }
  }
}
