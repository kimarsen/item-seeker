package com.itemseeker.proxy;

import com.itemseeker.client.SeekerRenderHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        MinecraftForge.EVENT_BUS.register(new SeekerRenderHandler());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }
}
