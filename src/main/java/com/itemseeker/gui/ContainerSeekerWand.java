package com.itemseeker.gui;

import com.itemseeker.item.ItemSeekerWand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSeekerWand extends Container {

    public final ItemStack wandStack;
    public final EntityPlayer player;

    public ContainerSeekerWand(InventoryPlayer playerInv, ItemStack wandStack) {
        this.wandStack = wandStack;
        this.player = playerInv.player;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 140 + row * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            addSlotToContainer(new Slot(playerInv, col, 8 + col * 18, 198));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return wandStack != null && wandStack.getItem() instanceof ItemSeekerWand;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        return ItemStack.EMPTY;
    }
}
