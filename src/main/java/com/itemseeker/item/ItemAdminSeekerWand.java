package com.itemseeker.item;

import com.itemseeker.ItemSeeker;
import com.itemseeker.admin.AdminHelper;
import com.itemseeker.gui.GuiHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentString;

public class ItemAdminSeekerWand extends ItemSeekerWand {

    public ItemAdminSeekerWand() {
        setRegistryName("admin_seeker_wand");
        setTranslationKey(ItemSeeker.MODID + ".admin_seeker_wand");
        setMaxStackSize(1);
        setCreativeTab(null);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        // Excluded from all creative tabs - obtain via /give command only
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if (!worldIn.isRemote && entityIn instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entityIn;
            if (!AdminHelper.hasPermission(player)) {
                player.inventory.deleteStack(stack);
                player.inventoryContainer.detectAndSendChanges();
                player.sendMessage(new TextComponentString(
                    TextFormatting.RED + "[Изъятие] Предмет " + TextFormatting.YELLOW + stack.getDisplayName()
                        + TextFormatting.RED + " изъят: требуются права оператора (/op)."
                ));
            }
        }
    }

    @Override
    public boolean isAdminWand() {
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack held = playerIn.getHeldItem(handIn);
        if (!AdminHelper.hasPermission(playerIn)) {
            if (!worldIn.isRemote) {
                AdminHelper.sendNoPermission(playerIn);
            }
            return new ActionResult<>(EnumActionResult.FAIL, held);
        }

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

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        ItemStack target = getTargetItem(stack);
        int threshold = getThreshold(stack);
        int mode = getSearchMode(stack);
        int radius = getRadius(stack);

        tooltip.add(TextFormatting.RED + "" + TextFormatting.BOLD + "[Всевидящий Админ-Жезл]");
        tooltip.add(TextFormatting.DARK_RED + "Только для операторов (/op) • Выдача: /give");
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
            tooltip.add(TextFormatting.GOLD + "Без ограничений: поиск по всему миру и телепортация [ТП]");
            tooltip.add(TextFormatting.DARK_GRAY + I18n.format("tooltip.itemseeker.instructions.1"));
        } else {
            if (!target.isEmpty()) {
                tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.itemseeker.target", TextFormatting.AQUA + target.getDisplayName())
                    + TextFormatting.DARK_GREEN + " (x" + threshold + ")");
            }
            tooltip.add(TextFormatting.DARK_GRAY + "Глобальный поиск по миру и регионам (.mca)");
            tooltip.add(TextFormatting.DARK_GRAY + I18n.format("tooltip.itemseeker.shift_hint"));
        }
    }
}
