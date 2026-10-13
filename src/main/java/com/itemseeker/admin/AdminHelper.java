package com.itemseeker.admin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.*;

public class AdminHelper {

    public static final Set<UUID> FROZEN_PLAYERS = new HashSet<>();
    public static final Map<UUID, BlockPos> FROZEN_POSITIONS = new HashMap<>();

    public static boolean hasPermission(EntityPlayer player) {
        if (player.world.isRemote) {
            return true;
        }
        return player.isCreative() || player.canUseCommand(2, "");
    }

    public static void sendNoPermission(EntityPlayer player) {
        player.sendMessage(new TextComponentString(
            TextFormatting.RED + "[Отказ в доступе] Этот инструмент доступен только администраторам и модераторам сервера."
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
