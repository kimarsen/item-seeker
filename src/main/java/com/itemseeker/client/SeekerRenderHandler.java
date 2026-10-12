package com.itemseeker.client;

import com.itemseeker.init.ModItems;
import com.itemseeker.search.SearchResultEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.*;

@SideOnly(Side.CLIENT)
public class SeekerRenderHandler {

    public static List<SearchResultEntry> activeResults = new ArrayList<>();
    public static Set<BlockPos> markedPositions = new HashSet<>();
    public static ItemStack targetStack = ItemStack.EMPTY;
    public static int currentThreshold = 1;

    public static void setSearchResults(List<SearchResultEntry> results, ItemStack target, int threshold) {
        activeResults = new ArrayList<>(results);
        targetStack = target != null ? target.copy() : ItemStack.EMPTY;
        currentThreshold = threshold;
        markedPositions.clear();
        for (SearchResultEntry entry : results) {
            markedPositions.add(entry.pos);
        }
    }

    public static void toggleMark(BlockPos pos) {
        if (markedPositions.contains(pos)) {
            markedPositions.remove(pos);
        } else {
            markedPositions.add(pos);
        }
    }

    public static boolean isMarked(BlockPos pos) {
        return markedPositions.contains(pos);
    }

    public static void clearMarkers() {
        markedPositions.clear();
        activeResults.clear();
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        if (player == null || mc.world == null) {
            return;
        }

        ItemStack heldMain = player.getHeldItem(EnumHand.MAIN_HAND);
        ItemStack heldOff = player.getHeldItem(EnumHand.OFF_HAND);
        boolean holdingWand = (heldMain.getItem() == ModItems.SEEKER_WAND) || (heldOff.getItem() == ModItems.SEEKER_WAND);

        if (!holdingWand && markedPositions.isEmpty()) {
            return;
        }

        if (activeResults.isEmpty() && markedPositions.isEmpty()) {
            return;
        }

        double interpX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
        double interpY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
        double interpZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.glLineWidth(2.5F);

        List<SearchResultEntry> entriesToRender = new ArrayList<>(activeResults);

        for (SearchResultEntry entry : entriesToRender) {
            BlockPos pos = entry.pos;
            if (!markedPositions.contains(pos) && !holdingWand) {
                continue;
            }

            double minX = pos.getX() - interpX;
            double minY = pos.getY() - interpY;
            double minZ = pos.getZ() - interpZ;
            AxisAlignedBB box = new AxisAlignedBB(minX, minY, minZ, minX + 1.0, minY + 1.0, minZ + 1.0);

            float r, g, b;
            if (markedPositions.contains(pos)) {
                r = 1.0F;
                g = 0.85F;
                b = 0.15F;
            } else {
                r = 0.1F;
                g = 0.9F;
                b = 0.7F;
            }

            RenderGlobal.renderFilledBox(box, r, g, b, 0.30F);
            RenderGlobal.drawSelectionBoundingBox(box, r, g, b, 0.90F);

            AxisAlignedBB beamBox = new AxisAlignedBB(minX + 0.35, minY, minZ + 0.35, minX + 0.65, minY + 128.0, minZ + 0.65);
            RenderGlobal.renderFilledBox(beamBox, r, g, b, 0.12F);
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        renderTextTags(mc, player, entriesToRender, interpX, interpY, interpZ);
    }

    private void renderTextTags(
        Minecraft mc,
        EntityPlayerSP player,
        List<SearchResultEntry> entries,
        double interpX,
        double interpY,
        double interpZ
    ) {
        RenderManager rm = mc.getRenderManager();
        FontRenderer font = mc.fontRenderer;
        int maxTags = Math.min(entries.size(), 20);

        for (int i = 0; i < maxTags; i++) {
            SearchResultEntry entry = entries.get(i);
            BlockPos pos = entry.pos;
            if (!markedPositions.contains(pos)) {
                continue;
            }

            double x = pos.getX() + 0.5 - interpX;
            double y = pos.getY() + 1.3 - interpY;
            double z = pos.getZ() + 0.5 - interpZ;

            String text = entry.containerName + " (" + entry.count + ") [" + (int) entry.distance + "m]";

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-rm.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(rm.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-0.025F, -0.025F, 0.025F);

            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

            int width = font.getStringWidth(text) / 2;
            GlStateManager.disableTexture2D();
            RenderGlobal.renderFilledBox(
                new AxisAlignedBB(-width - 2, -2, 0.0, width + 2, 10, 0.0),
                0.0F, 0.0F, 0.0F, 0.6F
            );
            GlStateManager.enableTexture2D();

            font.drawString(text, -width, 0, 0xFFE040);

            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
}
