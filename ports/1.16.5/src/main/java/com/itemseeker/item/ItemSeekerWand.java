package com.itemseeker.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSeekerWand extends Item {

    public ItemSeekerWand(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack held = playerIn.getItemInHand(handIn);
        if (!worldIn.isClientSide) {
            playerIn.displayClientMessage(
                new StringTextComponent(TextFormatting.GOLD + "[Item Seeker] " + TextFormatting.WHITE + "Wand ready. Configure search in GUI or press Shift+RMB to scan."),
                true
            );
        }
        return ActionResult.sidedSuccess(held, worldIn.isClientSide());
    }

    public static ItemStack getTargetItem(ItemStack wand) {
        if (!wand.hasTag() || !wand.getTag().contains("TargetItem")) {
            return ItemStack.EMPTY;
        }
        return ItemStack.of(wand.getTag().getCompound("TargetItem"));
    }

    public static void setTargetItem(ItemStack wand, ItemStack target) {
        CompoundNBT tag = wand.getOrCreateTag();
        if (target.isEmpty()) {
            tag.remove("TargetItem");
        } else {
            CompoundNBT itemTag = new CompoundNBT();
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
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        ItemStack target = getTargetItem(stack);
        int threshold = getThreshold(stack);
        if (!target.isEmpty()) {
            tooltip.add(new StringTextComponent(TextFormatting.GRAY + "Target: " + TextFormatting.AQUA + target.getHoverName().getString()
                + TextFormatting.GREEN + " (x" + threshold + ")"));
        } else {
            tooltip.add(new StringTextComponent(TextFormatting.DARK_GRAY + "Target: None"));
        }
        tooltip.add(new StringTextComponent(TextFormatting.GOLD + "RMB — Open search settings"));
    }
}
