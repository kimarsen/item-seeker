package com.itemseeker.item;

import com.itemseeker.ItemSeeker;
import com.itemseeker.gui.GuiHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSeekerWand extends Item {

    public ItemSeekerWand() {
        setRegistryName("seeker_wand");
        setTranslationKey(ItemSeeker.MODID + ".seeker_wand");
        setMaxStackSize(1);
        setCreativeTab(ItemSeeker.CREATIVE_TAB);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack held = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote) {
            playerIn.openGui(
                ItemSeeker.instance,
                GuiHandler.GUI_SEEKER_WAND,
                worldIn,
                (int) playerIn.posX,
                (int) playerIn.posY,
                (int) playerIn.posZ
            );
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, held);
    }

    public static ItemStack getTargetItem(ItemStack wand) {
        if (!wand.hasTagCompound() || !wand.getTagCompound().hasKey("TargetItem")) {
            return ItemStack.EMPTY;
        }
        NBTTagCompound itemTag = wand.getTagCompound().getCompoundTag("TargetItem");
        return new ItemStack(itemTag);
    }

    public static void setTargetItem(ItemStack wand, ItemStack target) {
        NBTTagCompound tag = getOrCreateTag(wand);
        if (target.isEmpty()) {
            tag.removeTag("TargetItem");
        } else {
            NBTTagCompound itemTag = new NBTTagCompound();
            target.writeToNBT(itemTag);
            tag.setTag("TargetItem", itemTag);
        }
    }

    public static int getThreshold(ItemStack wand) {
        if (!wand.hasTagCompound() || !wand.getTagCompound().hasKey("Threshold")) {
            return 1;
        }
        int val = wand.getTagCompound().getInteger("Threshold");
        return val <= 0 ? 1 : val;
    }

    public static void setThreshold(ItemStack wand, int threshold) {
        NBTTagCompound tag = getOrCreateTag(wand);
        tag.setInteger("Threshold", Math.max(1, threshold));
    }

    public static int getSearchMode(ItemStack wand) {
        if (!wand.hasTagCompound() || !wand.getTagCompound().hasKey("SearchMode")) {
            return 0;
        }
        return wand.getTagCompound().getInteger("SearchMode");
    }

    public static void setSearchMode(ItemStack wand, int mode) {
        NBTTagCompound tag = getOrCreateTag(wand);
        tag.setInteger("SearchMode", mode);
    }

    public static int getRadius(ItemStack wand) {
        if (!wand.hasTagCompound() || !wand.getTagCompound().hasKey("Radius")) {
            return 256;
        }
        return wand.getTagCompound().getInteger("Radius");
    }

    public static void setRadius(ItemStack wand, int radius) {
        NBTTagCompound tag = getOrCreateTag(wand);
        tag.setInteger("Radius", radius);
    }

    public static boolean getMatchNbt(ItemStack wand) {
        if (!wand.hasTagCompound() || !wand.getTagCompound().hasKey("MatchNbt")) {
            return false;
        }
        return wand.getTagCompound().getBoolean("MatchNbt");
    }

    public static void setMatchNbt(ItemStack wand, boolean matchNbt) {
        NBTTagCompound tag = getOrCreateTag(wand);
        tag.setBoolean("MatchNbt", matchNbt);
    }

    private static NBTTagCompound getOrCreateTag(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        return stack.getTagCompound();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        ItemStack target = getTargetItem(stack);
        int threshold = getThreshold(stack);
        int mode = getSearchMode(stack);
        int radius = getRadius(stack);

        if (GuiScreen.isShiftKeyDown()) {
            String targetName = target.isEmpty()
                ? TextFormatting.GRAY + I18n.format("tooltip.itemseeker.no_target")
                : TextFormatting.AQUA + target.getDisplayName();

            tooltip.add(I18n.format("tooltip.itemseeker.target", targetName));
            tooltip.add(I18n.format("tooltip.itemseeker.threshold", threshold));

            String modeStr;
            if (mode == 0) {
                modeStr = I18n.format("tooltip.itemseeker.mode.loaded");
            } else if (mode == 1) {
                modeStr = I18n.format("tooltip.itemseeker.mode.radius", radius);
            } else {
                modeStr = I18n.format("tooltip.itemseeker.mode.world");
            }
            tooltip.add(I18n.format("tooltip.itemseeker.mode", TextFormatting.YELLOW + modeStr));

            tooltip.add("");
            tooltip.add(TextFormatting.GOLD + I18n.format("tooltip.itemseeker.instructions.1"));
            tooltip.add(TextFormatting.DARK_GRAY + I18n.format("tooltip.itemseeker.instructions.2"));
        } else {
            if (!target.isEmpty()) {
                tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.itemseeker.target", TextFormatting.AQUA + target.getDisplayName())
                    + TextFormatting.DARK_GREEN + " (x" + threshold + ")");
            }
            tooltip.add(TextFormatting.DARK_GRAY + I18n.format("tooltip.itemseeker.shift_hint"));
        }
    }
}
