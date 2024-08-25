package com.xir.TimeVial.main;

import com.xir.TimeVial.client.RenderTimeAccelerator;
import com.xir.TimeVial.entity.EntityTimeAccelerator;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        RenderingRegistry.registerEntityRenderingHandler(EntityTimeAccelerator.class, new RenderTimeAccelerator());
    }
}
