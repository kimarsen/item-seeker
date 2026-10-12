package com.itemseeker.gui;

import com.itemseeker.item.ItemSeekerWand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static final int GUI_SEEKER_WAND = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI_SEEKER_WAND) {
            ItemStack wand = getWand(player);
            return new ContainerSeekerWand(player.inventory, wand);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI_SEEKER_WAND) {
            ItemStack wand = getWand(player);
            return new GuiSeekerWand(new ContainerSeekerWand(player.inventory, wand), player, wand);
        }
        return null;
    }

    private ItemStack getWand(EntityPlayer player) {
        ItemStack held = player.getHeldItem(EnumHand.MAIN_HAND);
        if (held.getItem() instanceof ItemSeekerWand) {
            return held;
        }
        held = player.getHeldItem(EnumHand.OFF_HAND);
        if (held.getItem() instanceof ItemSeekerWand) {
            return held;
        }
        return ItemStack.EMPTY;
    }
}
