package com.itemseeker;

import com.itemseeker.item.ItemSeekerWand;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(ItemSeeker.MODID)
public class ItemSeeker {

    public static final String MODID = "itemseeker";
    public static final String VERSION = "1.0.0-1.16.5";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final ItemGroup CREATIVE_TAB = new ItemGroup(MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(SEEKER_WAND.get());
        }
    };

    public static final RegistryObject<Item> SEEKER_WAND = ITEMS.register("seeker_wand",
        () -> new ItemSeekerWand(new Item.Properties().tab(CREATIVE_TAB).stacksTo(1)));

    public ItemSeeker() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
