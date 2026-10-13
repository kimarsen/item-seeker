package com.itemseeker.network;

import com.itemseeker.item.ItemSeekerWand;
import com.itemseeker.search.ContainerSearchEngine;
import com.itemseeker.search.SearchResultEntry;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

public class PacketStartSearch implements IMessage {

    private ItemStack targetStack = ItemStack.EMPTY;
    private int threshold = 1;
    private int searchMode = 0;
    private int radius = 256;
    private boolean matchNbt = false;

    public PacketStartSearch() {
    }

    public PacketStartSearch(ItemStack targetStack, int threshold, int searchMode, int radius, boolean matchNbt) {
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

    public static class Handler implements IMessageHandler<PacketStartSearch, IMessage> {

        @Override
        public IMessage onMessage(PacketStartSearch message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            WorldServer world = player.getServerWorld();

            world.addScheduledTask(() -> {
                ItemStack wand = player.getHeldItem(EnumHand.MAIN_HAND);
                if (!(wand.getItem() instanceof ItemSeekerWand)) {
                    wand = player.getHeldItem(EnumHand.OFF_HAND);
                }

                if (!(wand.getItem() instanceof ItemSeekerWand)) {
                    return;
                }

                boolean isAdmin = ItemSeekerWand.isAdmin(wand) && com.itemseeker.admin.AdminHelper.hasPermission(player);
                int effectiveMode = isAdmin ? message.searchMode : 1;
                int effectiveRadius = isAdmin ? message.radius : Math.min(message.radius, ItemSeekerWand.MAX_SURVIVAL_RADIUS);

                ItemSeekerWand.setTargetItem(wand, message.targetStack);
                ItemSeekerWand.setThreshold(wand, message.threshold);
                ItemSeekerWand.setSearchMode(wand, effectiveMode);
                ItemSeekerWand.setRadius(wand, effectiveRadius);
                ItemSeekerWand.setMatchNbt(wand, message.matchNbt);

                String prefix = isAdmin ? "[Всевидящий Жезл] " : "[Жезл Поиска] ";

                if (message.targetStack.isEmpty()) {
                    player.sendMessage(new TextComponentString(
                        TextFormatting.RED + prefix + "Не выбран искомый предмет! Поместите предмет в слот фильтра."
                    ));
                    return;
                }

                List<SearchResultEntry> results = ContainerSearchEngine.performSearch(
                    world,
                    player,
                    message.targetStack,
                    message.threshold,
                    effectiveMode,
                    effectiveRadius,
                    message.matchNbt
                );

                PacketHandler.sendTo(new PacketSearchResults(results, message.targetStack, message.threshold), player);

                if (results.isEmpty()) {
                    player.sendMessage(new TextComponentString(
                        TextFormatting.RED + prefix + "Контейнеры с " + TextFormatting.AQUA + message.targetStack.getDisplayName()
                            + TextFormatting.RED + " в количестве от " + TextFormatting.YELLOW + message.threshold
                            + TextFormatting.RED + " не найдены."
                    ));
                } else {
                    player.sendMessage(new TextComponentString(
                        TextFormatting.GOLD + prefix + TextFormatting.WHITE + "Найдено "
                            + TextFormatting.YELLOW + results.size() + TextFormatting.WHITE + " контейнеров с "
                            + TextFormatting.AQUA + message.targetStack.getDisplayName()
                            + TextFormatting.WHITE + " (порог: " + TextFormatting.GREEN + message.threshold + TextFormatting.WHITE + "):"
                    ));

                    int displayLimit = Math.min(results.size(), 12);
                    for (int i = 0; i < displayLimit; i++) {
                        SearchResultEntry entry = results.get(i);
                        ITextComponent line = new TextComponentString(
                            TextFormatting.GRAY + "- " + TextFormatting.WHITE + entry.containerName
                                + TextFormatting.GRAY + " на " + TextFormatting.AQUA + "[" + entry.pos.getX() + ", " + entry.pos.getY() + ", " + entry.pos.getZ() + "] "
                                + TextFormatting.GREEN + "(x" + entry.count + ") "
                                + TextFormatting.YELLOW + "[" + (int) entry.distance + "м] "
                        );

                        if (isAdmin) {
                            ITextComponent tpBtn = new TextComponentString(TextFormatting.GOLD + "[ТП]");
                            tpBtn.getStyle().setClickEvent(new ClickEvent(
                                ClickEvent.Action.RUN_COMMAND,
                                "/tp " + entry.pos.getX() + " " + (entry.pos.getY() + 1) + " " + entry.pos.getZ()
                            ));
                            tpBtn.getStyle().setHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                new TextComponentString(TextFormatting.YELLOW + "Нажмите для телепортации")
                            ));
                            line.appendSibling(tpBtn);
                        }

                        player.sendMessage(line);
                    }

                    if (results.size() > displayLimit) {
                        player.sendMessage(new TextComponentString(
                            TextFormatting.DARK_GRAY + "... и еще " + (results.size() - displayLimit)
                                + " контейнеров (полный список доступен в интерфейсе жезла)."
                        ));
                    }
                }

                world.playSound(
                    null,
                    player.posX,
                    player.posY,
                    player.posZ,
                    SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                    SoundCategory.PLAYERS,
                    0.8F,
                    1.2F
                );
            });

            return null;
        }
    }
}
