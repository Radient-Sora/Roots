package epicsquid.roots.entity.ritual;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class EntityRitualHarvest extends EntityRitualBase {
  protected static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityRitualHarvest.class, DataSerializers.VARINT);
  private List<ItemStack> itemStacksList;
  private int dropTime, dropTickInterval, totalDuration;

  public EntityRitualHarvest(World world) {
    super(world);
    // There is no point doing anything here because it isn't going to do anything.
    getDataManager().register(lifetime, 0);
  }

  public EntityRitualHarvest(World world, List<ItemStack> itemStacksList, int totalDuration) {
    super(world);
    this.itemStacksList = itemStacksList;
    this.dropTime = 0;
    this.dropTickInterval = (totalDuration * 20) / itemStacksList.size();
    this.totalDuration = dropTickInterval * itemStacksList.size();
    getDataManager().register(lifetime, this.totalDuration + 20);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    this.itemStacksList = new ArrayList<>();
    NBTTagList itemList = compound.getTagList("itemList", Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < itemList.tagCount(); i++) {

      ItemStack temp = new ItemStack((Item)null);
      temp.deserializeNBT(itemList.getCompoundTagAt(i));
      this.itemStacksList.add(temp);
    }
    this.dropTime = compound.getInteger("dropTime");
    this.dropTickInterval = compound.getInteger("dropTickInterval");
    this.totalDuration = compound.getInteger("totalDuration");
    super.readFromNBT(compound);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    NBTTagList itemList = new NBTTagList();
    for (ItemStack item : this.itemStacksList) {
      itemList.appendTag(item.serializeNBT());
    }
    compound.setTag("itemList", itemList);
    compound.setInteger("dropTime", this.dropTime);
    compound.setInteger("dropTickInterval", this.dropTickInterval);
    compound.setInteger("totalDuration", this.totalDuration);
    return super.writeToNBT(compound);
  }

  @Override
  public void onUpdate () {
    if (dropTime % dropTickInterval == 0) {
      ItemStack temp = this.itemStacksList.remove(this.itemStacksList.size()-1);
      world.spawnEntity(new EntityItem(world, posX, posY, posZ, temp));
    }
    dropTime++;
    super.onUpdate();
  }

  @Override
  public DataParameter<Integer> getLifetime() {
    return lifetime;
  }
}
