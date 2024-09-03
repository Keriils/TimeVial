package com.xir.TimeVial.mixins;

import static com.xir.TimeVial.main.TimeVial.LOG;

import java.time.LocalTime;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileEntityFurnace.class, remap = true)
public class Furnace_Mixin {

    private boolean istrusss = false;
    private boolean threadTrace = false;

    @Shadow
    public int furnaceCookTime;

    @Inject(method = "updateEntity", at = @At(value = "TAIL"))
    private void sendMessageInfo(CallbackInfo ci) {
        if (threadTrace && this.furnaceCookTime > 0) {
            StackTraceElement[] stackTrace = Thread.currentThread()
                .getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                LOG.info(
                    " {}.{} ({}, line {})",
                    stackTraceElement.getClassName(),
                    stackTraceElement.getMethodName(),
                    stackTraceElement.getFileName(),
                    stackTraceElement.getLineNumber());
            }
            // StackTraceElement caller = stackTrace[2];
            // LOG.info(" {}.{} ({}, line {})",
            // caller.getClassName(), caller.getMethodName(),
            // caller.getFileName(), caller.getLineNumber());
        }
        if (istrusss && this.furnaceCookTime > 0) {
            LOG.info(printCurrentTime());
        }
    }

    @Inject(method = "readFromNBT", at = @At(value = "HEAD"))
    private void getSKey(NBTTagCompound compound, CallbackInfo ci) {
        istrusss = compound.getBoolean("istrusss");
        threadTrace = compound.getBoolean("threadTrace");
    }

    @Inject(method = "writeToNBT", at = @At(value = "HEAD"))
    private void setSKey(NBTTagCompound compound, CallbackInfo ci) {
        compound.setBoolean("istrusss", istrusss);
        compound.setBoolean("threadTrace", threadTrace);
    }

    private static String printCurrentTime() {
        LocalTime currentTime = LocalTime.now();
        long currentTimeMillis = System.currentTimeMillis();
        int milliseconds = (int) (currentTimeMillis % 1000);
        return currentTime.toString() + "." + String.format("%03d", milliseconds);
    }
}
