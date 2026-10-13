package com.itemseeker.item;

import com.itemseeker.ItemSeeker;
import com.itemseeker.admin.AdminHelper;
import com.itemseeker.search.ContainerSearchEngine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.NonNullList;

public class ItemInspectorWand extends Item {

    public ItemInspectorWand() {
        setRegistryName("inspector_wand");
        setTranslationKey(ItemSeeker.MODID + ".inspector_wand");
        setMaxStackSize(1);
        setCreativeTab(null);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        // Excluded from all creative tabs - obtain via /give command only
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if (!worldIn.isRemote && entityIn instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entityIn;
            if (!AdminHelper.hasPermission(player)) {
                player.inventory.deleteStack(stack);
                player.inventoryContainer.detectAndSendChanges();
                player.sendMessage(new TextComponentString(
                    TextFormatting.RED + "[Изъятие] Предмет " + TextFormatting.YELLOW + stack.getDisplayName()
                        + TextFormatting.RED + " изъят: требуются права оператора (/op)."
                ));
            }
        }
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
            ItemStack filterStack = admin.getHeldItemOffhand();
            if (filterStack.isEmpty()) {
                for (ItemStack s : admin.inventory.mainInventory) {
                    if (s.getItem() instanceof ItemSeekerWand) {
                        filterStack = ItemSeekerWand.getTargetItem(s);
                        break;
                    }
                }
            }

            if (filterStack.isEmpty()) {
                admin.sendMessage(new TextComponentString(
                    TextFormatting.RED + "[Ревизор] Для аудита игроков возьмите искомый предмет во вторую руку (Offhand) или настройте Жезл Поиска!"
                ));
                return new ActionResult<>(EnumActionResult.SUCCESS, held);
            }

            auditOnlinePlayers(admin, filterStack);
            return new ActionResult<>(EnumActionResult.SUCCESS, held);
        }

        RayTraceResult ray = rayTraceBlocks(admin, 64.0);
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = ray.getBlockPos();
            TileEntity te = worldIn.getTileEntity(pos);

            if (te != null) {
                if (te instanceof IInventory) {
                    admin.displayGUIChest((IInventory) te);
                }

                int totalItems = 0;
                int totalSlots = 0;

                IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                if (handler != null) {
                    totalSlots = handler.getSlots();
                    for (int i = 0; i < totalSlots; i++) {
                        ItemStack in = handler.getStackInSlot(i);
                        if (!in.isEmpty()) {
                            totalItems += in.getCount();
                        }
                    }
                } else if (te instanceof IInventory) {
                    IInventory inv = (IInventory) te;
                    totalSlots = inv.getSizeInventory();
                    for (int i = 0; i < totalSlots; i++) {
                        ItemStack in = inv.getStackInSlot(i);
                        if (!in.isEmpty()) {
                            totalItems += in.getCount();
                        }
                    }
                }

                String blockName = te.getBlockType() != null ? te.getBlockType().getLocalizedName() : te.getClass().getSimpleName();
                admin.sendMessage(new TextComponentString(
                    TextFormatting.GOLD + "[Ревизор] " + TextFormatting.WHITE + blockName
                        + TextFormatting.GRAY + " на " + TextFormatting.AQUA + "[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "] "
                        + TextFormatting.YELLOW + "Слотов: " + totalSlots + ", Предметов: " + totalItems
                ));
            } else {
                admin.sendMessage(new TextComponentString(
                    TextFormatting.GRAY + "[Ревизор] Блок на [" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "] не имеет хранилища."
                ));
            }
        } else {
            admin.sendMessage(new TextComponentString(
                TextFormatting.GRAY + "[Ревизор] Наведитесь на блок (дистанция до 64 блоков) или нажмите Shift+ПКМ для аудита игроков."
            ));
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, held);
    }

    private void auditOnlinePlayers(EntityPlayerMP admin, ItemStack target) {
        MinecraftServer server = admin.getServer();
        if (server == null) {
            return;
        }

        List<EntityPlayerMP> players = server.getPlayerList().getPlayers();
        admin.sendMessage(new TextComponentString(
            TextFormatting.GOLD + "=== Аудит предмета " + TextFormatting.AQUA + target.getDisplayName()
                + TextFormatting.GOLD + " у онлайн-игроков ==="
        ));

        int foundCount = 0;
        for (EntityPlayerMP p : players) {
            int invCount = ContainerSearchEngine.countInInventory(p.inventory, target, false);
            int enderCount = ContainerSearchEngine.countInInventory(p.getInventoryEnderChest(), target, false);

            if (invCount > 0 || enderCount > 0) {
                foundCount++;
                admin.sendMessage(new TextComponentString(
                    TextFormatting.YELLOW + "- " + p.getName() + ": "
                        + TextFormatting.WHITE + "Инвентарь: " + TextFormatting.GREEN + invCount + " шт."
                        + TextFormatting.GRAY + " | "
                        + TextFormatting.WHITE + "Эндер-сундук: " + TextFormatting.LIGHT_PURPLE + enderCount + " шт."
                ));
            }
        }

        if (foundCount == 0) {
            admin.sendMessage(new TextComponentString(
                TextFormatting.GREEN + "Предмет " + target.getDisplayName() + " не обнаружен ни у одного онлайн-игрока."
            ));
        }
    }

    private RayTraceResult rayTraceBlocks(EntityPlayer player, double maxDist) {
        Vec3d eye = player.getPositionEyes(1.0F);
        Vec3d look = player.getLook(1.0F);
        Vec3d end = eye.add(look.x * maxDist, look.y * maxDist, look.z * maxDist);
        return player.world.rayTraceBlocks(eye, end, false, false, true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.RED + "" + TextFormatting.BOLD + "[Инструмент Администратора]");
        tooltip.add(TextFormatting.DARK_RED + "Только для операторов (/op) • Выдача: /give");

        net.minecraft.client.entity.EntityPlayerSP clientPlayer = net.minecraft.client.Minecraft.getMinecraft().player;
        if (clientPlayer != null) {
            ItemStack target = clientPlayer.getHeldItemOffhand();
            String source = " (из левой руки [F])";
            if (target.isEmpty()) {
                for (ItemStack s : clientPlayer.inventory.mainInventory) {
                    if (s.getItem() instanceof ItemSeekerWand) {
                        target = ItemSeekerWand.getTargetItem(s);
                        source = " (из Жезла Поиска)";
                        break;
                    }
                }
            }

            if (!target.isEmpty()) {
                tooltip.add(TextFormatting.GOLD + "Искомый предмет: " + TextFormatting.AQUA + target.getDisplayName() + TextFormatting.DARK_GRAY + source);
            } else {
                tooltip.add(TextFormatting.GRAY + "Искомый предмет: " + TextFormatting.DARK_GRAY + "[Не выбран]");
                tooltip.add(TextFormatting.DARK_GRAY + "  (положите предмет в левую руку [F] или в Жезл Поиска)");
            }
        }

        if (GuiScreen.isShiftKeyDown()) {
            tooltip.add(TextFormatting.GOLD + "ПКМ по блоку: " + TextFormatting.WHITE + "Скрытый досмотр сундука на расстоянии (до 64 блоков)");
            tooltip.add(TextFormatting.GOLD + "Shift + ПКМ в воздух: " + TextFormatting.WHITE + "Аудит инвентарей и эндер-сундуков игроков");
        } else {
            tooltip.add(TextFormatting.GRAY + "Зажмите Shift для просмотра функций");
        }
    }
}
