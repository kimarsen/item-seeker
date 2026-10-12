package com.itemseeker.network;

import com.itemseeker.ItemSeeker;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ItemSeeker.MODID);
    private static int packetId = 0;

    public static void registerMessages() {
        INSTANCE.registerMessage(PacketStartSearch.Handler.class, PacketStartSearch.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(PacketSearchResults.Handler.class, PacketSearchResults.class, packetId++, Side.CLIENT);
        INSTANCE.registerMessage(PacketTeleportToResult.Handler.class, PacketTeleportToResult.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(PacketUpdateWandSettings.Handler.class, PacketUpdateWandSettings.class, packetId++, Side.SERVER);
    }

    public static void sendTo(IMessage message, EntityPlayerMP player) {
        INSTANCE.sendTo(message, player);
    }

    public static void sendToServer(IMessage message) {
        INSTANCE.sendToServer(message);
    }
}
