package com.itemseeker.network;

import com.itemseeker.client.SeekerRenderHandler;
import com.itemseeker.gui.GuiSeekerWand;
import com.itemseeker.search.SearchResultEntry;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class PacketSearchResults implements IMessage {

    private List<SearchResultEntry> results = new ArrayList<>();
    private ItemStack targetStack = ItemStack.EMPTY;
    private int threshold = 1;

    public PacketSearchResults() {
    }

    public PacketSearchResults(List<SearchResultEntry> results, ItemStack targetStack, int threshold) {
        this.results = results;
        this.targetStack = targetStack;
        this.threshold = threshold;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        targetStack = ByteBufUtils.readItemStack(buf);
        threshold = buf.readInt();
        int size = buf.readInt();
        results = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            results.add(SearchResultEntry.readFromBuffer(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, targetStack);
        buf.writeInt(threshold);
        buf.writeInt(results.size());
        for (SearchResultEntry entry : results) {
            entry.writeToBuffer(buf);
        }
    }

    public static class Handler implements IMessageHandler<PacketSearchResults, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketSearchResults message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                SeekerRenderHandler.setSearchResults(message.results, message.targetStack, message.threshold);

                if (Minecraft.getMinecraft().currentScreen instanceof GuiSeekerWand) {
                    GuiSeekerWand gui = (GuiSeekerWand) Minecraft.getMinecraft().currentScreen;
                    gui.setSearchResults(message.results);
                }
            });
            return null;
        }
    }
}
