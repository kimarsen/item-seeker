package com.itemseeker.init;

import com.itemseeker.ItemSeeker;
import com.itemseeker.item.ItemSeekerWand;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = ItemSeeker.MODID)
public class ModItems {

    public static final Item SEEKER_WAND = new ItemSeekerWand();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(SEEKER_WAND);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        if (SEEKER_WAND.getRegistryName() != null) {
            ModelLoader.setCustomModelResourceLocation(
                SEEKER_WAND,
                0,
                new ModelResourceLocation(SEEKER_WAND.getRegistryName(), "inventory")
            );
        }
    }
}
