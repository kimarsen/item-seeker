package com.itemseeker.admin;

import com.itemseeker.item.ItemAdminSeekerWand;
import com.itemseeker.item.ItemInspectorWand;
import com.itemseeker.item.ItemLagWand;
import com.itemseeker.item.ItemStaffWand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.*;

public class AdminHelper {

    public static final Set<UUID> FROZEN_PLAYERS = new HashSet<>();
    public static final Map<UUID, BlockPos> FROZEN_POSITIONS = new HashMap<>();

    public static boolean hasPermission(EntityPlayer player) {
        if (player == null) {
            return false;
        }
        if (player.world.isRemote) {
            return true;
        }
        return player.canUseCommand(2, "");
    }

    public static boolean isAdminTool(net.minecraft.item.Item item) {
        return item instanceof ItemAdminSeekerWand
            || item instanceof ItemStaffWand
            || item instanceof ItemInspectorWand
            || item instanceof ItemLagWand;
    }

    public static boolean isAdminTool(ItemStack stack) {
        return stack != null && !stack.isEmpty() && isAdminTool(stack.getItem());
    }

    public static void sendNoPermission(EntityPlayer player) {
        player.sendMessage(new TextComponentString(
            TextFormatting.RED + "[Отказ в доступе] Этот инструмент доступен только операторам сервера (требуются права /op)."
        ));
    }

    public static boolean isFrozen(EntityPlayer player) {
        return FROZEN_PLAYERS.contains(player.getUniqueID());
    }

    public static void toggleFreeze(EntityPlayerMP admin, EntityPlayerMP target) {
        UUID uuid = target.getUniqueID();
        if (FROZEN_PLAYERS.contains(uuid)) {
            FROZEN_PLAYERS.remove(uuid);
            FROZEN_POSITIONS.remove(uuid);
            target.removePotionEffect(MobEffects.SLOWNESS);
            target.removePotionEffect(MobEffects.BLINDNESS);
            target.sendMessage(new TextComponentString(
                TextFormatting.GREEN + "[Модерация] Вы были разморожены модератором " + TextFormatting.YELLOW + admin.getName() + TextFormatting.GREEN + "."
            ));
            admin.sendMessage(new TextComponentString(
                TextFormatting.GOLD + "[Посох] Игрок " + TextFormatting.YELLOW + target.getName() + TextFormatting.GREEN + " РАЗМОРОЖЕН."
            ));
        } else {
            FROZEN_PLAYERS.add(uuid);
            FROZEN_POSITIONS.put(uuid, target.getPosition());
            target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 72000, 255, false, false));
            target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 72000, 0, false, false));
            target.sendMessage(new TextComponentString(
                TextFormatting.RED + "" + TextFormatting.BOLD + "ВНИМАНИЕ! "
                    + TextFormatting.RED + "Вы заморожены модератором "
                    + TextFormatting.YELLOW + admin.getName()
                    + TextFormatting.RED + " для проверки! Не выходите из игры."
            ));
            admin.sendMessage(new TextComponentString(
                TextFormatting.GOLD + "[Посох] Игрок " + TextFormatting.YELLOW + target.getName() + TextFormatting.RED + " ЗАМОРОЖЕН."
            ));
        }
    }
}
