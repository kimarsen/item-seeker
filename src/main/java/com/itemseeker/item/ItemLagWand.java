package com.itemseeker.item;

import com.itemseeker.ItemSeeker;
import com.itemseeker.admin.AdminHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

public class ItemLagWand extends Item {

    public ItemLagWand() {
        setRegistryName("lag_wand");
        setTranslationKey(ItemSeeker.MODID + ".lag_wand");
        setMaxStackSize(1);
        setCreativeTab(ItemSeeker.CREATIVE_TAB);
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

        if (worldIn.isRemote) {
            return new ActionResult<>(EnumActionResult.SUCCESS, held);
        }

        EntityPlayerMP admin = (EntityPlayerMP) playerIn;

        if (admin.isSneaking()) {
            double r = 64.0;
            AxisAlignedBB box = new AxisAlignedBB(
                admin.posX - r, admin.posY - r, admin.posZ - r,
                admin.posX + r, admin.posY + r, admin.posZ + r
            );

            List<Entity> entities = worldIn.getEntitiesWithinAABB(Entity.class, box);
            int purged = 0;
            for (Entity e : entities) {
                if (e instanceof EntityItem || e instanceof EntityXPOrb) {
                    e.setDead();
                    purged++;
                }
            }

            admin.sendMessage(new TextComponentString(
                TextFormatting.GREEN + "[Очистка] Удалено " + TextFormatting.YELLOW + purged
                    + TextFormatting.GREEN + " выброшенных предметов и сфер опыта в радиусе 64 блоков."
            ));
            return new ActionResult<>(EnumActionResult.SUCCESS, held);
        }

        MinecraftServer server = admin.getServer();
        double tps = 20.0;
        double meanTickTime = 50.0;
        if (server != null && server.tickTimeArray != null && server.tickTimeArray.length > 0) {
            meanTickTime = average(server.tickTimeArray) * 1.0E-6D;
            tps = Math.min(1000.0 / meanTickTime, 20.0);
        }

        long maxMem = Runtime.getRuntime().maxMemory() / 1024L / 1024L;
        long totalMem = Runtime.getRuntime().totalMemory() / 1024L / 1024L;
        long freeMem = Runtime.getRuntime().freeMemory() / 1024L / 1024L;
        long usedMem = totalMem - freeMem;

        admin.sendMessage(new TextComponentString(
            TextFormatting.GOLD + "=== Диагностика производительности сервера ==="
        ));

        TextFormatting tpsColor = tps >= 18.0 ? TextFormatting.GREEN : (tps >= 14.0 ? TextFormatting.YELLOW : TextFormatting.RED);
        admin.sendMessage(new TextComponentString(
            TextFormatting.WHITE + "TPS: " + tpsColor + String.format(Locale.ROOT, "%.1f", tps)
                + TextFormatting.GRAY + " (Tick: " + String.format(Locale.ROOT, "%.1f", meanTickTime) + "ms)"
                + TextFormatting.WHITE + " | RAM: " + TextFormatting.YELLOW + usedMem + "MB / " + maxMem + "MB"
        ));

        admin.sendMessage(new TextComponentString(
            TextFormatting.WHITE + "Загружено в измерении: "
                + TextFormatting.AQUA + worldIn.loadedEntityList.size() + " сущностей"
                + TextFormatting.GRAY + ", "
                + TextFormatting.AQUA + worldIn.loadedTileEntityList.size() + " TileEntities"
        ));

        if (worldIn instanceof WorldServer) {
            WorldServer ws = (WorldServer) worldIn;
            Map<ChunkPos, Integer> chunkEntities = new HashMap<>();

            for (Entity e : ws.loadedEntityList) {
                ChunkPos cp = new ChunkPos((int) Math.floor(e.posX) >> 4, (int) Math.floor(e.posZ) >> 4);
                chunkEntities.put(cp, chunkEntities.getOrDefault(cp, 0) + 1);
            }

            List<Map.Entry<ChunkPos, Integer>> sorted = new ArrayList<>(chunkEntities.entrySet());
            sorted.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

            admin.sendMessage(new TextComponentString(
                TextFormatting.RED + "Топ перегруженных чанков (Lag Hunter):"
            ));

            int limit = Math.min(sorted.size(), 3);
            for (int i = 0; i < limit; i++) {
                Map.Entry<ChunkPos, Integer> entry = sorted.get(i);
                ChunkPos cp = entry.getKey();
                int count = entry.getValue();
                int x = (cp.x << 4) + 8;
                int z = (cp.z << 4) + 8;

                ITextComponent line = new TextComponentString(
                    TextFormatting.GRAY + " " + (i + 1) + ". Чанк [" + cp.x + ", " + cp.z + "] "
                        + TextFormatting.AQUA + "[X: " + x + ", Z: " + z + "] "
                        + TextFormatting.YELLOW + "(" + count + " сущностей) "
                );

                ITextComponent tp = new TextComponentString(TextFormatting.GOLD + "[ТП]");
                tp.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + x + " 70 " + z));
                tp.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Телепортироваться в проблемный чанк")));
                line.appendSibling(tp);

                admin.sendMessage(line);
            }
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, held);
    }

    private static long average(long[] values) {
        long sum = 0L;
        for (long v : values) {
            sum += v;
        }
        return sum / values.length;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.RED + "" + TextFormatting.BOLD + "[Инструмент Администратора]");
        if (GuiScreen.isShiftKeyDown()) {
            tooltip.add(TextFormatting.GOLD + "ПКМ в воздух: " + TextFormatting.WHITE + "Замер TPS, RAM и поиск самых перегруженных чанков");
            tooltip.add(TextFormatting.GOLD + "Shift + ПКМ в воздух: " + TextFormatting.WHITE + "Очистить выброшенные предметы (EntityItem) в радиусе 64 блоков");
        } else {
            tooltip.add(TextFormatting.GRAY + "Зажмите Shift для просмотра функций");
        }
    }
}
