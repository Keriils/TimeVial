package com.xir.TimeVial.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static boolean enableBlockMode = true;
    public static int accelerateBlockInterval = 2;
    public static boolean enableLogInfo = false;
    public static boolean limitOneTimeVial = true;
    public static float defaultTimeVialVolumeValue = 0.5F;
    public static boolean enableTimeAcceleratorBoost = false;
    public static boolean enableNumberMultiplierTexture = false;

    private static final String CATEGORY_TIME_VIAL = "Time_Vial";

    public static void syncConfig(File configFile) {
        Configuration configuration = new Configuration(configFile);

        configuration.addCustomCategoryComment(CATEGORY_TIME_VIAL, "The TimeVial config setting");

        enableBlockMode = configuration
            .getBoolean("enableBlockMode", CATEGORY_TIME_VIAL, enableBlockMode, "enable Block Mode");
        enableLogInfo = configuration
            .getBoolean("enableLogInfo", CATEGORY_TIME_VIAL, enableLogInfo, "enable log info debug");
        limitOneTimeVial = configuration
            .getBoolean("limitOneTimeVial", CATEGORY_TIME_VIAL, limitOneTimeVial, "limit One TimeVial");
        defaultTimeVialVolumeValue = configuration.getFloat(
            "defaultTimeVialVolume",
            CATEGORY_TIME_VIAL,
            defaultTimeVialVolumeValue,
            0.0F,
            5.0F,
            "set time vial volume");
        enableTimeAcceleratorBoost = configuration.getBoolean(
            "enableTimeAcceleratorBoost",
            CATEGORY_TIME_VIAL,
            enableTimeAcceleratorBoost,
            "enable Time Accelerator Boost, boost to 256X");
        enableNumberMultiplierTexture = configuration.getBoolean(
            "enableNumberMultiplierTexture",
            CATEGORY_TIME_VIAL,
            enableNumberMultiplierTexture,
            "enable Number Multiplier Texture");
        accelerateBlockInterval = configuration.getInt(
            "accelerateBlockInterval",
            CATEGORY_TIME_VIAL,
            accelerateBlockInterval,
            2,
            200,
            "accelerate Block Interval");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
