package com.itemseeker.admin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AdminEventHandler {

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (AdminHelper.isFrozen(player)) {
                BlockPos lockPos = AdminHelper.FROZEN_POSITIONS.get(player.getUniqueID());
                if (lockPos != null) {
                    player.motionX = 0;
                    player.motionY = 0;
                    player.motionZ = 0;
                    if (player.getDistanceSqToCenter(lockPos) > 1.0) {
                        player.setPositionAndUpdate(lockPos.getX() + 0.5, lockPos.getY(), lockPos.getZ() + 0.5);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (AdminHelper.isFrozen(event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.PlaceEvent event) {
        if (AdminHelper.isFrozen(event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (AdminHelper.isFrozen(event.getEntityPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (AdminHelper.isFrozen(event.getEntityPlayer())) {
            event.setCanceled(true);
        }
    }
}
