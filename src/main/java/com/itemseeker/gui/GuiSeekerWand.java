package com.itemseeker.gui;

import com.itemseeker.client.SeekerRenderHandler;
import com.itemseeker.item.ItemAdminSeekerWand;
import com.itemseeker.item.ItemSeekerWand;
import com.itemseeker.network.PacketHandler;
import com.itemseeker.network.PacketStartSearch;
import com.itemseeker.network.PacketTeleportToResult;
import com.itemseeker.network.PacketUpdateWandSettings;
import com.itemseeker.search.SearchResultEntry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiSeekerWand extends GuiContainer {

    private final EntityPlayer player;
    private final ItemStack wandStack;
    private ItemStack targetStack;
    private int threshold;
    private int searchMode;
    private int radius;
    private boolean matchNbt;

    private List<SearchResultEntry> results = new ArrayList<>();
    private int currentResultIndex = 0;

    private GuiTextField thresholdField;
    private GuiButton modeButton;
    private GuiButton nbtButton;
    private GuiButton scanButton;
    private GuiButton clearMarkersButton;
    private GuiButton clearFilterButton;
    private GuiButton prevResultButton;
    private GuiButton nextResultButton;
    private GuiButton tpButton;
    private GuiButton markButton;

    public GuiSeekerWand(ContainerSeekerWand inventorySlotsIn, EntityPlayer player, ItemStack wandStack) {
        super(inventorySlotsIn);
        this.player = player;
        this.wandStack = wandStack;
        this.xSize = 248;
        this.ySize = 222;

        this.targetStack = ItemSeekerWand.getTargetItem(wandStack);
        this.threshold = ItemSeekerWand.getThreshold(wandStack);
        this.searchMode = ItemSeekerWand.getSearchMode(wandStack);
        this.radius = ItemSeekerWand.getRadius(wandStack);
        this.matchNbt = ItemSeekerWand.getMatchNbt(wandStack);

        if (!SeekerRenderHandler.activeResults.isEmpty()) {
            this.results = new ArrayList<>(SeekerRenderHandler.activeResults);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);

        boolean isAdmin = isAdminWand();

        thresholdField = new GuiTextField(0, fontRenderer, guiLeft + 84, guiTop + 54, 30, 16);
        thresholdField.setText(String.valueOf(threshold));
        thresholdField.setMaxStringLength(7);
        thresholdField.setTextColor(isAdmin ? 0xFFAAAA : 0xFFFFFF);

        buttonList.clear();

        addButton(new GuiButton(10, guiLeft + 118, guiTop + 53, 18, 18, "1"));
        addButton(new GuiButton(11, guiLeft + 138, guiTop + 53, 18, 18, "16"));
        addButton(new GuiButton(12, guiLeft + 158, guiTop + 53, 18, 18, "32"));
        addButton(new GuiButton(13, guiLeft + 178, guiTop + 53, 18, 18, "64"));
        addButton(new GuiButton(14, guiLeft + 198, guiTop + 53, 20, 18, "128"));
        addButton(new GuiButton(15, guiLeft + 220, guiTop + 53, 20, 18, "256"));

        modeButton = addButton(new GuiButton(4, guiLeft + 12, guiTop + 74, 142, 18, getModeString()));
        nbtButton = addButton(new GuiButton(5, guiLeft + 158, guiTop + 74, 78, 18, getNbtString()));

        String scanText = (isAdmin ? TextFormatting.RED : TextFormatting.GREEN) + "" + TextFormatting.BOLD + "НАЧАТЬ ПОИСК";
        scanButton = addButton(new GuiButton(1, guiLeft + 12, guiTop + 95, 126, 20, scanText));
        clearMarkersButton = addButton(new GuiButton(3, guiLeft + 142, guiTop + 95, 94, 20, TextFormatting.RED + "Сброс меток"));

        clearFilterButton = addButton(new GuiButton(2, guiLeft + 172, guiTop + 24, 64, 18, "Очистить"));

        prevResultButton = addButton(new GuiButton(6, guiLeft + 12, guiTop + 117, 18, 18, "<"));
        nextResultButton = addButton(new GuiButton(7, guiLeft + 32, guiTop + 117, 18, 18, ">"));
        tpButton = addButton(new GuiButton(8, guiLeft + 178, guiTop + 117, 28, 18, TextFormatting.GOLD + "ТП"));
        markButton = addButton(new GuiButton(9, guiLeft + 208, guiTop + 117, 28, 18, TextFormatting.YELLOW + "Метка"));

        updateButtonStates();
    }

    public boolean isAdminWand() {
        return wandStack.getItem() instanceof ItemAdminSeekerWand;
    }

    private void updateButtonStates() {
        boolean hasResults = !results.isEmpty();
        prevResultButton.enabled = hasResults && currentResultIndex > 0;
        nextResultButton.enabled = hasResults && currentResultIndex < results.size() - 1;
        boolean isAdmin = isAdminWand();
        tpButton.visible = isAdmin;
        tpButton.enabled = isAdmin && hasResults;
        markButton.enabled = hasResults;
        clearFilterButton.enabled = !targetStack.isEmpty();
        if (!isAdmin) {
            modeButton.enabled = false;
            modeButton.displayString = "Радиус: " + ItemSeekerWand.MAX_SURVIVAL_RADIUS + " блоков";
        }
    }

    public void setSearchResults(List<SearchResultEntry> newResults) {
        this.results = new ArrayList<>(newResults);
        this.currentResultIndex = 0;
        updateButtonStates();
    }

    private String getModeString() {
        if (!isAdminWand()) {
            return "Радиус: " + ItemSeekerWand.MAX_SURVIVAL_RADIUS + " блоков";
        }
        if (searchMode == 0) return "Чанки: Загруженные";
        if (searchMode == 1) return "Радиус: " + radius + " блоков";
        return "Весь мир (Регионы)";
    }

    private String getNbtString() {
        return matchNbt ? "NBT: " + TextFormatting.GREEN + "ВКЛ" : "NBT: " + TextFormatting.GRAY + "ВЫКЛ";
    }

    private void syncSettings() {
        PacketHandler.sendToServer(new PacketUpdateWandSettings(
            targetStack,
            threshold,
            searchMode,
            radius,
            matchNbt
        ));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) { // Scan
            parseThresholdField();
            PacketHandler.sendToServer(new PacketStartSearch(
                targetStack,
                threshold,
                searchMode,
                radius,
                matchNbt
            ));
        } else if (button.id == 2) { // Clear filter
            targetStack = ItemStack.EMPTY;
            syncSettings();
            updateButtonStates();
        } else if (button.id == 3) { // Clear markers
            SeekerRenderHandler.clearMarkers();
            results.clear();
            currentResultIndex = 0;
            updateButtonStates();
        } else if (button.id == 4) { // Mode cycle
            if (!isAdminWand()) {
                return;
            }
            if (searchMode == 0) {
                searchMode = 1;
                radius = 256;
            } else if (searchMode == 1 && radius == 256) {
                radius = 512;
            } else if (searchMode == 1 && radius == 512) {
                radius = 1024;
            } else if (searchMode == 1) {
                searchMode = 2;
            } else {
                searchMode = 0;
            }
            modeButton.displayString = getModeString();
            syncSettings();
        } else if (button.id == 5) { // NBT
            matchNbt = !matchNbt;
            nbtButton.displayString = getNbtString();
            syncSettings();
        } else if (button.id == 6) { // Prev
            if (currentResultIndex > 0) {
                currentResultIndex--;
                updateButtonStates();
            }
        } else if (button.id == 7) { // Next
            if (currentResultIndex < results.size() - 1) {
                currentResultIndex++;
                updateButtonStates();
            }
        } else if (button.id == 8) { // TP
            if (!results.isEmpty() && currentResultIndex < results.size()) {
                SearchResultEntry cur = results.get(currentResultIndex);
                PacketHandler.sendToServer(new PacketTeleportToResult(cur.pos, cur.dimensionId));
            }
        } else if (button.id == 9) { // Mark
            if (!results.isEmpty() && currentResultIndex < results.size()) {
                SearchResultEntry cur = results.get(currentResultIndex);
                SeekerRenderHandler.toggleMark(cur.pos);
            }
        } else if (button.id >= 10 && button.id <= 15) { // Quick numbers
            int[] vals = {1, 16, 32, 64, 128, 256};
            threshold = vals[button.id - 10];
            thresholdField.setText(String.valueOf(threshold));
            syncSettings();
        }
    }

    private void parseThresholdField() {
        try {
            int val = Integer.parseInt(thresholdField.getText().trim());
            threshold = Math.max(1, val);
        } catch (NumberFormatException ignored) {
            threshold = 1;
            thresholdField.setText("1");
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (thresholdField.textboxKeyTyped(typedChar, keyCode)) {
            parseThresholdField();
            syncSettings();
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        thresholdField.mouseClicked(mouseX, mouseY, mouseButton);

        int slotX = guiLeft + 12;
        int slotY = guiTop + 24;
        if (mouseX >= slotX && mouseX <= slotX + 18 && mouseY >= slotY && mouseY <= slotY + 18) {
            ItemStack cursorStack = player.inventory.getItemStack();
            if (!cursorStack.isEmpty()) {
                targetStack = cursorStack.copy();
                targetStack.setCount(1);
            } else {
                targetStack = ItemStack.EMPTY;
            }
            syncSettings();
            updateButtonStates();
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);

        int slotX = guiLeft + 12;
        int slotY = guiTop + 24;
        if (mouseX >= slotX && mouseX <= slotX + 18 && mouseY >= slotY && mouseY <= slotY + 18) {
            if (!targetStack.isEmpty()) {
                renderToolTip(targetStack, mouseX, mouseY);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        boolean isAdmin = isAdminWand();

        // 1. Window Background (Burgundy for admin, dark slate for survival)
        int bgColor = isAdmin ? 0xF2220912 : 0xEE1A1A22;
        drawRect(guiLeft, guiTop, guiLeft + xSize, guiTop + ySize, bgColor);

        // 2. Outer Border (Rich crimson burgundy vs gold)
        int borderOuter = isAdmin ? 0xFF8B1229 : 0xFFC8963E;
        drawRect(guiLeft, guiTop, guiLeft + xSize, guiTop + 1, borderOuter);
        drawRect(guiLeft, guiTop + ySize - 1, guiLeft + xSize, guiTop + ySize, borderOuter);
        drawRect(guiLeft, guiTop + 1, guiLeft + 1, guiTop + ySize, borderOuter);
        drawRect(guiLeft + xSize - 1, guiTop, guiLeft + xSize, guiTop + ySize, borderOuter);

        // 3. Inner Border Accent
        int borderInner = isAdmin ? 0xFF4A0A16 : 0xFF3E2D13;
        drawRect(guiLeft + 1, guiTop + 1, guiLeft + xSize - 1, guiTop + 2, borderInner);
        drawRect(guiLeft + 1, guiTop + ySize - 2, guiLeft + xSize - 1, guiTop + ySize - 1, borderInner);
        drawRect(guiLeft + 1, guiTop + 1, guiLeft + 2, guiTop + ySize - 1, borderInner);
        drawRect(guiLeft + xSize - 2, guiTop + 1, guiLeft + xSize - 1, guiTop + ySize - 1, borderInner);

        // 4. Target Item Slot
        int slotX = guiLeft + 12;
        int slotY = guiTop + 24;
        int targetSlotBorder = isAdmin ? 0xFFC42343 : 0xFF3EC896;
        int targetSlotInner = isAdmin ? 0xFF350E1A : 0xFF2A2A35;
        drawRect(slotX - 1, slotY - 1, slotX + 19, slotY + 19, targetSlotBorder);
        drawRect(slotX, slotY, slotX + 18, slotY + 18, targetSlotInner);

        // 5. Results bar
        int resBg = isAdmin ? 0xFF320D1A : 0xFF22222E;
        int resBorder = isAdmin ? 0xFF65172B : 0xFF3E3E4E;
        drawRect(guiLeft + 10, guiTop + 116, guiLeft + xSize - 10, guiTop + 136, resBg);
        drawRect(guiLeft + 10, guiTop + 116, guiLeft + xSize - 10, guiTop + 117, resBorder);
        drawRect(guiLeft + 10, guiTop + 135, guiLeft + xSize - 10, guiTop + 136, resBorder);

        // 6. Inventory Panel (Centered behind slots)
        int invLeft = (xSize - 162) / 2; // 43
        int invPanelBg = isAdmin ? 0xFF19060D : 0xFF14141A;
        int invPanelBorder = isAdmin ? 0xFF540E1D : 0xFF2A2A35;
        drawRect(guiLeft + invLeft - 5, guiTop + 136, guiLeft + invLeft + 162 + 5, guiTop + 219, invPanelBg);
        drawRect(guiLeft + invLeft - 5, guiTop + 136, guiLeft + invLeft + 162 + 5, guiTop + 137, invPanelBorder);
        drawRect(guiLeft + invLeft - 5, guiTop + 218, guiLeft + invLeft + 162 + 5, guiTop + 219, invPanelBorder);
        drawRect(guiLeft + invLeft - 5, guiTop + 136, guiLeft + invLeft - 4, guiTop + 219, invPanelBorder);
        drawRect(guiLeft + invLeft + 162 + 4, guiTop + 136, guiLeft + invLeft + 162 + 5, guiTop + 219, invPanelBorder);

        // 7. Render all inventory slot frames
        for (Slot slot : inventorySlots.inventorySlots) {
            drawSlotFrame(guiLeft + slot.xPos, guiTop + slot.yPos, isAdmin);
        }
    }

    private void drawSlotFrame(int x, int y, boolean isAdmin) {
        int darkBorder = isAdmin ? 0xFF100307 : 0xFF0D0D12;
        int lightBorder = isAdmin ? 0xFF6A1728 : 0xFF424254;
        int innerBg = isAdmin ? 0xFF2A0B15 : 0xFF1C1C26;

        // Top edge (dark)
        drawRect(x - 1, y - 1, x + 17, y, darkBorder);
        // Left edge (dark)
        drawRect(x - 1, y - 1, x, y + 17, darkBorder);
        // Right edge (highlight)
        drawRect(x + 16, y, x + 17, y + 17, lightBorder);
        // Bottom edge (highlight)
        drawRect(x, y + 16, x + 17, y + 17, lightBorder);
        // Slot interior (16x16)
        drawRect(x, y, x + 16, y + 16, innerBg);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        boolean isAdmin = isAdminWand();
        String title = isAdmin
            ? TextFormatting.RED + "" + TextFormatting.BOLD + "Всевидящий Жезл"
            : TextFormatting.GREEN + "" + TextFormatting.BOLD + "Жезл Поиска";
        fontRenderer.drawString(title, 12, 10, 0xFFFFFF);

        int slotX = 12;
        int slotY = 24;

        if (!targetStack.isEmpty()) {
            RenderHelper.enableGUIStandardItemLighting();
            itemRender.renderItemAndEffectIntoGUI(targetStack, slotX + 1, slotY + 1);
            itemRender.renderItemOverlayIntoGUI(fontRenderer, targetStack, slotX + 1, slotY + 1, null);
            RenderHelper.disableStandardItemLighting();

            String name = targetStack.getDisplayName();
            if (fontRenderer.getStringWidth(name) > 130) {
                name = fontRenderer.trimStringToWidth(name, 125) + "...";
            }
            fontRenderer.drawString(TextFormatting.AQUA + name, slotX + 24, slotY + 5, 0xFFFFFF);
        } else {
            fontRenderer.drawString(TextFormatting.DARK_GRAY + "[ Кликните предметом для выбора ]", slotX + 24, slotY + 5, 0xFFFFFF);
        }

        // Threshold label to the left of the number buttons
        String thresholdLabel = "Порог поиска:";
        fontRenderer.drawString(thresholdLabel, 12, 58, isAdmin ? 0xFFE0CC : 0xCCCCCC);

        if (results.isEmpty()) {
            fontRenderer.drawString(TextFormatting.GRAY + "Результаты поиска отсутствуют", 54, 122, 0xFFFFFF);
        } else {
            SearchResultEntry cur = results.get(currentResultIndex);
            String display = (currentResultIndex + 1) + "/" + results.size() + " "
                + cur.containerName + " (" + cur.count + ") " + (int) cur.distance + "м";
            if (fontRenderer.getStringWidth(display) > 120) {
                display = fontRenderer.trimStringToWidth(display, 115) + "..";
            }
            fontRenderer.drawString(TextFormatting.YELLOW + display, 54, 122, 0xFFFFFF);
        }

        thresholdField.drawTextBox();
    }
}
