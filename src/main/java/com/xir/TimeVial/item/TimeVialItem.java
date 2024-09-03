package com.xir.TimeVial.item;

import static com.xir.TimeVial.config.Config.defaultTimeVialVolumeValue;
import static com.xir.TimeVial.config.Config.enableLogInfo;
import static com.xir.TimeVial.config.Config.enableTimeAcceleratorBoost;
import static com.xir.TimeVial.config.Config.limitOneTimeVial;
import static com.xir.TimeVial.main.TimeVial.LOG;

import java.util.List;
import java.util.Optional;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.xir.TimeVial.entity.EntityTimeAccelerator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TimeVialItem extends Item {

    public static final Item timeVialInstance = new TimeVialItem();

    protected static final int TIME_INIT_RATE = enableTimeAcceleratorBoost ? 8 : 4;
    // referenced RandomThings
    protected static final float[] SOUND_ARRAY_F = new float[] { 0.749154F, 0.793701F, 0.890899F, 1.059463F, 0.943874F,
        0.890899F, 0.690899F };
    protected static final int MAX_ACCELERATION = enableTimeAcceleratorBoost ? 256 : 128;
    protected static final int NUMBER_EER = -846280; // ha,.... 114514
    protected int storedTimeTick = 0;
    protected final double tHalfSize = 0.01D; // 实体一半的大小

    public TimeVialItem() {
        setMaxStackSize(1);
        setUnlocalizedName("TimeVial:TimeVial");
        setTextureName("timevial:TimeVial");
        setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final @NotNull ItemStack stack, final EntityPlayer player, final List<String> list,
        final boolean extraInformation) {
        getInfoFromNBT(stack, list);
    }

    @SideOnly(Side.CLIENT)
    protected void getInfoFromNBT(@NotNull ItemStack stack, List<String> list) {
        NBTTagCompound nbtTagCompound = stack.getTagCompound();
        if (nbtTagCompound == null) nbtTagCompound = new NBTTagCompound();
        int storedTimeSeconds = nbtTagCompound.getInteger("storedTimeTick") / 20;
        int hours = storedTimeSeconds / 3600;
        int minutes = (storedTimeSeconds % 3600) / 60;
        int seconds = storedTimeSeconds % 60;
        list.add(I18n.format("text.TimeVial.TimeVial.tips", hours, minutes, seconds));
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, @NotNull World world, int x, int y, int z,
        int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {

            // 实体中心坐标
            double targetPosX = x + 0.5D;
            double targetPosY = y + 0.5D;
            double targetPosZ = z + 0.5D;

            // 计算碰撞箱大小
            double minX = targetPosX - tHalfSize;
            double minY = targetPosY - tHalfSize;
            double minZ = targetPosZ - tHalfSize;
            double maxX = targetPosX + tHalfSize;
            double maxY = targetPosY + tHalfSize;
            double maxZ = targetPosZ + tHalfSize;

            // 获取碰撞箱对应实体
            Optional<EntityTimeAccelerator> box = world
                .getEntitiesWithinAABB(
                    EntityTimeAccelerator.class,
                    AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ))
                .stream()
                .findFirst();

            if (box.isPresent()) {

                EntityTimeAccelerator eta = box.get();

                int currentRate = eta.getTimeRate();
                int nextRateTimeRequired = currentRate * eta.getRemainingTime(); // no why

                if (currentRate < MAX_ACCELERATION && shouldAndConsumeTimeData(player, stack, nextRateTimeRequired)) {
                    eta.setTimeRate(currentRate * 2);
                    playSoundForRateChange(world, targetPosX, targetPosY, targetPosZ, eta.getTimeRate());
                }

                if (enableLogInfo) LOG.info("xxxxx {} xxxxx", nextRateTimeRequired);

            } else if (shouldAndConsumeTimeData(player, stack, TIME_INIT_RATE * 600)) {

                EntityTimeAccelerator eta = new EntityTimeAccelerator(world, x, y, z);

                world.spawnEntityInWorld(eta);

                playSoundForRateChange(world, targetPosX, targetPosY, targetPosZ, eta.getTimeRate());

                if (enableLogInfo) LOG.info(
                    "An entity entityTimeAccelerator has been spawned ({}, {}, {}).",
                    targetPosX,
                    targetPosY,
                    targetPosZ);
            }
            return true;
        }
        return false;
    }

    private void playSoundForRateChange(@NotNull World world, double posX, double posY, double posZ, int currentRate) {
        int i = (int) (Math.log(currentRate) / Math.log(2)) - (enableTimeAcceleratorBoost ? 3 : 2);
        // security considerations
        if (i < 0 || i >= SOUND_ARRAY_F.length) i = 0;
        world.playSoundEffect(posX, posY, posZ, "note.harp", defaultTimeVialVolumeValue, SOUND_ARRAY_F[i]);
    }

    protected boolean shouldAndConsumeTimeData(@NotNull EntityPlayer player, @NotNull ItemStack stack,
        int consumedTick) {
        if (player.capabilities.isCreativeMode) return true;
        int timeTick = stack.getTagCompound()
            .getInteger("storedTimeTick");
        if (timeTick >= consumedTick) {
            stack.getTagCompound()
                .setInteger("storedTimeTick", timeTick - consumedTick);
            return true;
        }
        return false;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity playerIn, int slot, boolean isHeld) {
        if (!(playerIn instanceof EntityPlayer player)) return;
        if (limitOneTimeVial && worldIn.getTotalWorldTime() % 600 == 0) {
            mergeSameVialTime(player, stack);
        }
        NBTTagCompound nbtTagCompound = stack.getTagCompound();
        if (nbtTagCompound == null) {
            nbtTagCompound = new NBTTagCompound();
            nbtTagCompound.setInteger("storedTimeTick", storedTimeTick);
            stack.setTagCompound(nbtTagCompound);
        } else if (worldIn.getTotalWorldTime() % 20 == 0) {
            int t = nbtTagCompound.getInteger("storedTimeTick");
            if (t == NUMBER_EER) return;
            nbtTagCompound.setInteger("storedTimeTick", t + 20);
            stack.setTagCompound(nbtTagCompound);
        }
    }

    protected void mergeSameVialTime(@NotNull EntityPlayer player, ItemStack stack) {
        for (ItemStack itemStack : player.inventory.mainInventory) {
            if (itemStack != null && itemStack.getItem() == this && itemStack != stack) {
                int thisTimeTick = stack.getTagCompound()
                    .getInteger("storedTimeTick");
                int otherTimeTick = itemStack.getTagCompound()
                    .getInteger("storedTimeTick");
                if (otherTimeTick >= 0 && thisTimeTick >= otherTimeTick) {
                    stack.getTagCompound()
                        .setInteger("storedTimeTick", thisTimeTick + otherTimeTick);
                    itemStack.getTagCompound()
                        .setInteger("storedTimeTick", NUMBER_EER);
                }
            }
        }
    }
}
