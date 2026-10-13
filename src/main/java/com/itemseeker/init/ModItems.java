package com.itemseeker.init;

import com.itemseeker.ItemSeeker;
import com.itemseeker.item.ItemInspectorWand;
import com.itemseeker.item.ItemLagWand;
import com.itemseeker.item.ItemSeekerWand;
import com.itemseeker.item.ItemStaffWand;
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
    public static final Item STAFF_WAND = new ItemStaffWand();
    public static final Item INSPECTOR_WAND = new ItemInspectorWand();
    public static final Item LAG_WAND = new ItemLagWand();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
            SEEKER_WAND,
            STAFF_WAND,
            INSPECTOR_WAND,
            LAG_WAND
        );
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        registerModel(SEEKER_WAND);
        registerModel(STAFF_WAND);
        registerModel(INSPECTOR_WAND);
        registerModel(LAG_WAND);
    }

    @SideOnly(Side.CLIENT)
    private static void registerModel(Item item) {
        if (item.getRegistryName() != null) {
            ModelLoader.setCustomModelResourceLocation(
                item,
                0,
                new ModelResourceLocation(item.getRegistryName(), "inventory")
            );
        }
    }
}
