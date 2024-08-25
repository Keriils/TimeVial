package com.xir.TimeVial.main;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.xir.TimeVial.config.Config;
import com.xir.TimeVial.entity.EntityTimeAccelerator;
import com.xir.TimeVial.item.TimeVialItem;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.syncConfig(event.getSuggestedConfigurationFile());
        GameRegistry.registerItem(TimeVialItem.timeVialInstance, "TimeVial");
    }

    public void init(FMLInitializationEvent event) {
        GameRegistry.addShapedRecipe(
            new ItemStack(TimeVialItem.timeVialInstance),
            "GGG",
            "DCD",
            "QBQ",
            'B',
            Items.glass_bottle,
            'G',
            Items.gold_ingot,
            'C',
            Items.clock,
            'D',
            Items.diamond,
            'Q',
            new ItemStack(Items.dye, 1, 4));
        GameRegistry.addShapedRecipe(
            new ItemStack(TimeVialItem.timeVialInstance),
            "CCC",
            "CTC",
            "CCC",
            'C',
            Blocks.cobblestone,
            'T',
            TimeVialItem.timeVialInstance);
        EntityRegistry
            .registerModEntity(EntityTimeAccelerator.class, "Accelerator", 0, TimeVial.INSTANCE, 64, 10, false);
    }

    public void postInit(FMLPostInitializationEvent event) {}

}
