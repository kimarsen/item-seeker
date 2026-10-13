package com.itemseeker;

import com.itemseeker.admin.AdminEventHandler;
import com.itemseeker.gui.GuiHandler;
import com.itemseeker.init.ModItems;
import com.itemseeker.network.PacketHandler;
import com.itemseeker.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(
    modid = ItemSeeker.MODID,
    name = ItemSeeker.NAME,
    version = ItemSeeker.VERSION,
    acceptedMinecraftVersions = "[1.12.2]"
)
public class ItemSeeker {

    public static final String MODID = "itemseeker";
    public static final String NAME = "Item Seeker & Admin Tools";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MODID)
    public static ItemSeeker instance;

    @SidedProxy(
        clientSide = "com.itemseeker.proxy.ClientProxy",
        serverSide = "com.itemseeker.proxy.CommonProxy"
    )
    public static CommonProxy proxy;

    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.SEEKER_WAND);
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PacketHandler.registerMessages();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new AdminEventHandler());
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
