package com.itemseeker.search;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.storage.RegionFile;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.oredict.OreDictionary;

import java.io.DataInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RegionFileScanner {

    public static List<SearchResultEntry> scanRegionFiles(
        WorldServer world,
        EntityPlayer player,
        ItemStack targetStack,
        int threshold,
        boolean matchNbt
    ) {
        List<SearchResultEntry> results = new ArrayList<>();
        if (targetStack.isEmpty() || targetStack.getItem().getRegistryName() == null) {
            return results;
        }

        String targetId = targetStack.getItem().getRegistryName().toString();
        int targetMeta = targetStack.getMetadata();
        boolean wildcardMeta = (targetMeta == OreDictionary.WILDCARD_VALUE);
        NBTTagCompound targetNbt = targetStack.getTagCompound();
        int dimId = world.provider.getDimension();

        File worldDir = world.getSaveHandler().getWorldDirectory();
        File regionDir;
        if (dimId == 0) {
            regionDir = new File(worldDir, "region");
        } else {
            regionDir = new File(new File(worldDir, "DIM" + dimId), "region");
        }

        if (!regionDir.exists() || !regionDir.isDirectory()) {
            return results;
        }

        File[] mcaFiles = regionDir.listFiles((dir, name) -> name.endsWith(".mca"));
        if (mcaFiles == null) {
            return results;
        }

        for (File mcaFile : mcaFiles) {
            RegionFile regionFile = null;
            try {
                regionFile = new RegionFile(mcaFile);
                for (int cx = 0; cx < 32; cx++) {
                    for (int cz = 0; cz < 32; cz++) {
                        if (!regionFile.isChunkSaved(cx, cz)) {
                            continue;
                        }

                        DataInputStream dis = regionFile.getChunkDataInputStream(cx, cz);
                        if (dis == null) {
                            continue;
                        }

                        try {
                            NBTTagCompound chunkRoot = CompressedStreamTools.read(dis);
                            if (chunkRoot == null || !chunkRoot.hasKey("Level", Constants.NBT.TAG_COMPOUND)) {
                                continue;
                            }

                            NBTTagCompound level = chunkRoot.getCompoundTag("Level");
                            int chunkX = level.getInteger("xPos");
                            int chunkZ = level.getInteger("zPos");

                            if (world.getChunkProvider().chunkExists(chunkX, chunkZ)) {
                                continue;
                            }

                            if (level.hasKey("TileEntities", Constants.NBT.TAG_LIST)) {
                                NBTTagList teList = level.getTagList("TileEntities", Constants.NBT.TAG_COMPOUND);
                                for (int i = 0; i < teList.tagCount(); i++) {
                                    NBTTagCompound te = teList.getCompoundTagAt(i);
                                    if (!te.hasKey("Items", Constants.NBT.TAG_LIST)) {
                                        continue;
                                    }

                                    NBTTagList items = te.getTagList("Items", Constants.NBT.TAG_COMPOUND);
                                    int totalCount = countMatchingInNbtList(items, targetId, targetMeta, wildcardMeta, targetNbt, matchNbt);

                                    if (totalCount >= threshold) {
                                        int x = te.getInteger("x");
                                        int y = te.getInteger("y");
                                        int z = te.getInteger("z");
                                        BlockPos pos = new BlockPos(x, y, z);
                                        String teId = te.getString("id");
                                        String cleanName = getFriendlyName(teId);
                                        double dist = Math.sqrt(player.getDistanceSq(x + 0.5, y + 0.5, z + 0.5));

                                        results.add(new SearchResultEntry(pos, cleanName, totalCount, dist, false, 0, dimId));
                                    }
                                }
                            }

                            if (level.hasKey("Entities", Constants.NBT.TAG_LIST)) {
                                NBTTagList entityList = level.getTagList("Entities", Constants.NBT.TAG_COMPOUND);
                                for (int i = 0; i < entityList.tagCount(); i++) {
                                    NBTTagCompound ent = entityList.getCompoundTagAt(i);
                                    if (!ent.hasKey("Items", Constants.NBT.TAG_LIST)) {
                                        continue;
                                    }

                                    NBTTagList items = ent.getTagList("Items", Constants.NBT.TAG_COMPOUND);
                                    int totalCount = countMatchingInNbtList(items, targetId, targetMeta, wildcardMeta, targetNbt, matchNbt);

                                    if (totalCount >= threshold) {
                                        NBTTagList posList = ent.getTagList("Pos", Constants.NBT.TAG_DOUBLE);
                                        double ex = posList.tagCount() > 0 ? posList.getDoubleAt(0) : 0;
                                        double ey = posList.tagCount() > 1 ? posList.getDoubleAt(1) : 0;
                                        double ez = posList.tagCount() > 2 ? posList.getDoubleAt(2) : 0;

                                        BlockPos pos = new BlockPos((int) Math.floor(ex), (int) Math.floor(ey), (int) Math.floor(ez));
                                        String entId = ent.getString("id");
                                        String cleanName = getFriendlyName(entId);
                                        double dist = Math.sqrt(player.getDistanceSq(ex, ey, ez));

                                        results.add(new SearchResultEntry(pos, cleanName, totalCount, dist, true, 0, dimId));
                                    }
                                }
                            }
                        } finally {
                            dis.close();
                        }
                    }
                }
            } catch (Exception ignored) {
            } finally {
                if (regionFile != null) {
                    try {
                        regionFile.close();
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        return results;
    }

    private static int countMatchingInNbtList(
        NBTTagList items,
        String targetId,
        int targetMeta,
        boolean wildcardMeta,
        NBTTagCompound targetNbt,
        boolean matchNbt
    ) {
        int total = 0;
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound itemTag = items.getCompoundTagAt(i);
            String id = itemTag.getString("id");
            if (!id.equals(targetId)) {
                continue;
            }

            if (!wildcardMeta) {
                int damage = itemTag.getShort("Damage");
                if (damage != targetMeta) {
                    continue;
                }
            }

            if (matchNbt) {
                NBTTagCompound itemNbt = itemTag.hasKey("tag", Constants.NBT.TAG_COMPOUND)
                    ? itemTag.getCompoundTag("tag")
                    : null;
                if (targetNbt == null && itemNbt != null) {
                    continue;
                }
                if (targetNbt != null && !targetNbt.equals(itemNbt)) {
                    continue;
                }
            }

            int count = itemTag.getByte("Count");
            if (count <= 0 && itemTag.hasKey("Count", Constants.NBT.TAG_INT)) {
                count = itemTag.getInteger("Count");
            }
            total += Math.max(0, count);
        }
        return total;
    }

    public static String getFriendlyName(String id) {
        if (id == null || id.isEmpty()) {
            return "Container";
        }
        if (id.equals("minecraft:chest")) return "Сундук";
        if (id.equals("minecraft:trapped_chest")) return "Сундук-ловушка";
        if (id.equals("minecraft:furnace")) return "Печь";
        if (id.equals("minecraft:shulker_box")) return "Ящик шалкера";
        if (id.equals("minecraft:dispenser")) return "Раздатчик";
        if (id.equals("minecraft:dropper")) return "Выбрасыватель";
        if (id.equals("minecraft:hopper")) return "Воронка";
        if (id.equals("minecraft:brewing_stand")) return "Зельеварочная стойка";
        if (id.equals("minecraft:chest_minecart")) return "Вагонетка с сундуком";
        if (id.equals("minecraft:hopper_minecart")) return "Вагонетка с воронкой";

        String clean = id.contains(":") ? id.substring(id.indexOf(':') + 1) : id;
        clean = clean.replace('_', ' ');
        StringBuilder sb = new StringBuilder();
        for (String word : clean.split(" ")) {
            if (word.length() > 0) {
                sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }
        return sb.toString().trim();
    }
}
