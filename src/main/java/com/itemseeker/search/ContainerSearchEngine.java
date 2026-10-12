package com.itemseeker.search;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class ContainerSearchEngine {

    public static List<SearchResultEntry> performSearch(
        World world,
        EntityPlayer player,
        ItemStack targetStack,
        int threshold,
        int searchMode,
        int radius,
        boolean matchNbt
    ) {
        List<SearchResultEntry> results = new ArrayList<>();
        if (targetStack.isEmpty() || threshold <= 0) {
            return results;
        }

        Set<BlockPos> visited = new HashSet<>();
        int dimId = world.provider.getDimension();

        for (TileEntity te : world.loadedTileEntityList) {
            if (te == null || te.isInvalid()) {
                continue;
            }
            BlockPos pos = te.getPos();
            if (visited.contains(pos)) {
                continue;
            }

            double dist = Math.sqrt(player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
            if (searchMode == 1 && dist > radius) {
                continue;
            }

            int count = 0;
            String name = null;

            if (te instanceof TileEntityChest) {
                TileEntityChest chest = (TileEntityChest) te;
                chest.checkForAdjacentChests();
                TileEntityChest adj = null;
                if (chest.adjacentChestXNeg != null) adj = chest.adjacentChestXNeg;
                else if (chest.adjacentChestXPos != null) adj = chest.adjacentChestXPos;
                else if (chest.adjacentChestZNeg != null) adj = chest.adjacentChestZNeg;
                else if (chest.adjacentChestZPos != null) adj = chest.adjacentChestZPos;

                count += countInInventory(chest, targetStack, matchNbt);
                visited.add(pos);

                if (adj != null && !adj.isInvalid()) {
                    count += countInInventory(adj, targetStack, matchNbt);
                    visited.add(adj.getPos());
                    name = "Большой сундук";
                } else {
                    name = "Сундук";
                }
            } else {
                visited.add(pos);
                IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                if (handler != null) {
                    count = countInItemHandler(handler, targetStack, matchNbt);
                } else if (te instanceof IInventory) {
                    count = countInInventory((IInventory) te, targetStack, matchNbt);
                }

                if (count > 0) {
                    name = te.getBlockType() != null ? te.getBlockType().getLocalizedName() : te.getClass().getSimpleName();
                }
            }

            if (count >= threshold) {
                if (name == null) {
                    name = te.getBlockType() != null ? te.getBlockType().getLocalizedName() : "Контейнер";
                }
                results.add(new SearchResultEntry(pos, name, count, dist, false, 0, dimId));
            }
        }

        for (Entity ent : world.loadedEntityList) {
            if (ent == null || ent.isDead) {
                continue;
            }

            double dist = Math.sqrt(player.getDistanceSq(ent.posX, ent.posY, ent.posZ));
            if (searchMode == 1 && dist > radius) {
                continue;
            }

            int count = 0;
            IItemHandler handler = ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (handler != null) {
                count = countInItemHandler(handler, targetStack, matchNbt);
            } else if (ent instanceof IInventory) {
                count = countInInventory((IInventory) ent, targetStack, matchNbt);
            }

            if (count >= threshold) {
                BlockPos pos = new BlockPos((int) Math.floor(ent.posX), (int) Math.floor(ent.posY), (int) Math.floor(ent.posZ));
                String name = ent.getName();
                results.add(new SearchResultEntry(pos, name, count, dist, true, ent.getEntityId(), dimId));
            }
        }

        if (searchMode == 2 && world instanceof WorldServer) {
            List<SearchResultEntry> diskResults = RegionFileScanner.scanRegionFiles(
                (WorldServer) world,
                player,
                targetStack,
                threshold,
                matchNbt
            );
            results.addAll(diskResults);
        }

        results.sort(Comparator.comparingDouble(a -> a.distance));
        return results;
    }

    public static int countInItemHandler(IItemHandler handler, ItemStack target, boolean matchNbt) {
        if (handler == null) {
            return 0;
        }
        int total = 0;
        int slots = handler.getSlots();
        for (int i = 0; i < slots; i++) {
            ItemStack inSlot = handler.getStackInSlot(i);
            if (isItemMatch(inSlot, target, matchNbt)) {
                total += inSlot.getCount();
            }
        }
        return total;
    }

    public static int countInInventory(IInventory inv, ItemStack target, boolean matchNbt) {
        if (inv == null) {
            return 0;
        }
        int total = 0;
        int size = inv.getSizeInventory();
        for (int i = 0; i < size; i++) {
            ItemStack inSlot = inv.getStackInSlot(i);
            if (isItemMatch(inSlot, target, matchNbt)) {
                total += inSlot.getCount();
            }
        }
        return total;
    }

    public static boolean isItemMatch(ItemStack inSlot, ItemStack target, boolean matchNbt) {
        if (inSlot.isEmpty() || target.isEmpty()) {
            return false;
        }
        if (inSlot.getItem() != target.getItem()) {
            return false;
        }

        int targetMeta = target.getMetadata();
        if (targetMeta != OreDictionary.WILDCARD_VALUE) {
            if (inSlot.getMetadata() != targetMeta) {
                return false;
            }
        }

        if (matchNbt) {
            NBTTagCompound inNbt = inSlot.getTagCompound();
            NBTTagCompound targetNbt = target.getTagCompound();
            if (targetNbt == null && inNbt != null) {
                return false;
            }
            if (targetNbt != null && !targetNbt.equals(inNbt)) {
                return false;
            }
        }

        return true;
    }
}
