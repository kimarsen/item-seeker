package com.itemseeker.network;

import com.itemseeker.item.ItemSeekerWand;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateWandSettings implements IMessage {

    private ItemStack targetStack = ItemStack.EMPTY;
    private int threshold = 1;
    private int searchMode = 0;
    private int radius = 256;
    private boolean matchNbt = false;

    public PacketUpdateWandSettings() {
    }

    public PacketUpdateWandSettings(ItemStack targetStack, int threshold, int searchMode, int radius, boolean matchNbt) {
        this.targetStack = targetStack;
        this.threshold = threshold;
        this.searchMode = searchMode;
        this.radius = radius;
        this.matchNbt = matchNbt;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        targetStack = ByteBufUtils.readItemStack(buf);
        threshold = buf.readInt();
        searchMode = buf.readInt();
        radius = buf.readInt();
        matchNbt = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, targetStack);
        buf.writeInt(threshold);
        buf.writeInt(searchMode);
        buf.writeInt(radius);
        buf.writeBoolean(matchNbt);
    }

    public static class Handler implements IMessageHandler<PacketUpdateWandSettings, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdateWandSettings message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            WorldServer world = player.getServerWorld();

            world.addScheduledTask(() -> {
                ItemStack wand = player.getHeldItem(EnumHand.MAIN_HAND);
                if (!(wand.getItem() instanceof ItemSeekerWand)) {
                    wand = player.getHeldItem(EnumHand.OFF_HAND);
                }

                if (wand.getItem() instanceof ItemSeekerWand) {
                    ItemSeekerWand.setTargetItem(wand, message.targetStack);
                    ItemSeekerWand.setThreshold(wand, message.threshold);
                    ItemSeekerWand.setSearchMode(wand, message.searchMode);
                    ItemSeekerWand.setRadius(wand, message.radius);
                    ItemSeekerWand.setMatchNbt(wand, message.matchNbt);
                }
            });

            return null;
        }
    }
}
