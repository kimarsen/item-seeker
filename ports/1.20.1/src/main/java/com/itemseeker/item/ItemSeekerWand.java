package com.itemseeker.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSeekerWand extends Item {

    public ItemSeekerWand(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (!level.isClientSide) {
            player.displayClientMessage(
                Component.literal("[Item Seeker] Wand ready. Configure search in GUI or press Shift+RMB to scan.").withStyle(ChatFormatting.GOLD),
                true
            );
        }
        return InteractionResultHolder.sidedSuccess(held, level.isClientSide());
    }

    public static ItemStack getTargetItem(ItemStack wand) {
        if (!wand.hasTag() || !wand.getTag().contains("TargetItem")) {
            return ItemStack.EMPTY;
        }
        return ItemStack.of(wand.getTag().getCompound("TargetItem"));
    }

    public static void setTargetItem(ItemStack wand, ItemStack target) {
        CompoundTag tag = wand.getOrCreateTag();
        if (target.isEmpty()) {
            tag.remove("TargetItem");
        } else {
            CompoundTag itemTag = new CompoundTag();
            target.save(itemTag);
            tag.put("TargetItem", itemTag);
        }
    }

    public static int getThreshold(ItemStack wand) {
        if (!wand.hasTag() || !wand.getTag().contains("Threshold")) {
            return 1;
        }
        return Math.max(1, wand.getTag().getInt("Threshold"));
    }

    public static void setThreshold(ItemStack wand, int threshold) {
        wand.getOrCreateTag().putInt("Threshold", Math.max(1, threshold));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        ItemStack target = getTargetItem(stack);
        int threshold = getThreshold(stack);
        if (!target.isEmpty()) {
            tooltip.add(Component.literal("Target: ").withStyle(ChatFormatting.GRAY)
                .append(target.getHoverName().copy().withStyle(ChatFormatting.AQUA))
                .append(Component.literal(" (x" + threshold + ")").withStyle(ChatFormatting.GREEN)));
        } else {
            tooltip.add(Component.literal("Target: None").withStyle(ChatFormatting.DARK_GRAY));
        }
        tooltip.add(Component.literal("RMB — Open search settings").withStyle(ChatFormatting.GOLD));
    }
}
