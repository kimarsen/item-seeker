package com.itemseeker.item;

import com.itemseeker.ItemSeeker;
import com.itemseeker.admin.AdminHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemStaffWand extends Item {

    public ItemStaffWand() {
        setRegistryName("staff_wand");
        setTranslationKey(ItemSeeker.MODID + ".staff_wand");
        setMaxStackSize(1);
        setCreativeTab(ItemSeeker.CREATIVE_TAB);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (!AdminHelper.hasPermission(playerIn)) {
            AdminHelper.sendNoPermission(playerIn);
            return true;
        }

        if (target instanceof EntityPlayerMP && !playerIn.world.isRemote) {
            EntityPlayerMP admin = (EntityPlayerMP) playerIn;
            EntityPlayerMP targetPlayer = (EntityPlayerMP) target;

            if (admin.isSneaking()) {
                AdminHelper.toggleFreeze(admin, targetPlayer);
            } else {
                admin.displayGUIChest(targetPlayer.getInventoryEnderChest());
                admin.sendMessage(new TextComponentString(
                    TextFormatting.GOLD + "[Посох] " + TextFormatting.WHITE + "Открыт Эндер-сундук игрока "
                        + TextFormatting.YELLOW + targetPlayer.getName()
                ));
            }
            return true;
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
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
            EntityPlayerMP admin = (EntityPlayerMP) playerIn;

            if (admin.isSneaking()) {
                boolean newInvis = !admin.isInvisible();
                admin.setInvisible(newInvis);
                admin.sendMessage(new TextComponentString(
                    TextFormatting.GOLD + "[Посох] Режим скрытности (Vanish): "
                        + (newInvis ? TextFormatting.GREEN + "ВКЛЮЧЕН" : TextFormatting.RED + "ВЫКЛЮЧЕН")
                ));
            } else {
                MinecraftServer server = admin.getServer();
                if (server != null) {
                    List<EntityPlayerMP> players = server.getPlayerList().getPlayers();
                    admin.sendMessage(new TextComponentString(
                        TextFormatting.GOLD + "=== Модерация игроков онлайн (" + players.size() + ") ==="
                    ));

                    for (EntityPlayerMP p : players) {
                        if (p.equals(admin)) {
                            continue;
                        }
                        boolean frozen = AdminHelper.isFrozen(p);

                        ITextComponent line = new TextComponentString(
                            TextFormatting.GRAY + "- " + (frozen ? TextFormatting.RED + "[ЗАМОРОЖЕН] " : "")
                                + TextFormatting.YELLOW + p.getName() + " "
                        );

                        ITextComponent tpTo = new TextComponentString(TextFormatting.AQUA + "[ТП к нему] ");
                        tpTo.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + p.getName()));
                        tpTo.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Телепортироваться к " + p.getName())));
                        line.appendSibling(tpTo);

                        ITextComponent bring = new TextComponentString(TextFormatting.GREEN + "[ТП сюда] ");
                        bring.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + p.getName() + " " + admin.getName()));
                        bring.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Притянуть " + p.getName() + " к себе")));
                        line.appendSibling(bring);

                        ITextComponent freezeBtn = new TextComponentString(frozen ? TextFormatting.LIGHT_PURPLE + "[Разморозить]" : TextFormatting.RED + "[Заморозка]");
                        freezeBtn.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/kick " + p.getName() + " Проверка на читы"));
                        line.appendSibling(freezeBtn);

                        admin.sendMessage(line);
                    }
                }
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, held);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.RED + "" + TextFormatting.BOLD + "[Инструмент Администратора]");
        if (GuiScreen.isShiftKeyDown()) {
            tooltip.add(TextFormatting.GOLD + "ПКМ по игроку: " + TextFormatting.WHITE + "Скрытный досмотр эндер-сундука");
            tooltip.add(TextFormatting.GOLD + "Shift + ПКМ по игроку: " + TextFormatting.WHITE + "Заморозка/разморозка игрока");
            tooltip.add(TextFormatting.GOLD + "ПКМ в воздух: " + TextFormatting.WHITE + "Список игроков с кнопками модерации");
            tooltip.add(TextFormatting.GOLD + "Shift + ПКМ в воздух: " + TextFormatting.WHITE + "Переключение режима Vanish");
        } else {
            tooltip.add(TextFormatting.GRAY + "Зажмите Shift для просмотра функций");
        }
    }
}
