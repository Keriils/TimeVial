package com.xir.TimeVial.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.xir.TimeVial.Tags;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = TimeVial.MODID, version = Tags.VERSION, name = TimeVial.MOD_NAME, acceptedMinecraftVersions = "[1.7.10]")
public class TimeVial {

    public static final String MODID = "TimeVial";
    public static final String MOD_NAME = "Time Vial";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @SidedProxy(clientSide = "com.xir.TimeVial.main.ClientProxy", serverSide = "com.xir.TimeVial.main.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(TimeVial.MODID)
    public static TimeVial INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
