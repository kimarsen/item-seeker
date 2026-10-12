# 🔍 Item Seeker (Seeker's Wand)

[![Minecraft 1.12.2 (MAIN)](https://img.shields.io/badge/Minecraft%201.12.2-MAIN%20%E2%98%85-gold.svg)](https://github.com/kimarsen/item-seeker/releases)
[![Minecraft 1.16.5](https://img.shields.io/badge/Minecraft%201.16.5-Supported-blue.svg)](https://github.com/kimarsen/item-seeker/releases)
[![Minecraft 1.18.2](https://img.shields.io/badge/Minecraft%201.18.2-Supported-blue.svg)](https://github.com/kimarsen/item-seeker/releases)
[![Minecraft 1.19.2](https://img.shields.io/badge/Minecraft%201.19.2-Supported-blue.svg)](https://github.com/kimarsen/item-seeker/releases)
[![Minecraft 1.20.1](https://img.shields.io/badge/Minecraft%201.20.1-Supported-blue.svg)](https://github.com/kimarsen/item-seeker/releases)
[![Minecraft 1.21.1](https://img.shields.io/badge/Minecraft%201.21.1-Supported-blue.svg)](https://github.com/kimarsen/item-seeker/releases)
[![ModLoader Forge](https://img.shields.io/badge/ModLoader-Forge%20%2F%20NeoForge-orange.svg)](https://files.minecraftforge.net/)
[![Release](https://img.shields.io/badge/Release-v1.0.0-purple.svg)](https://github.com/kimarsen/item-seeker/releases)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

**Item Seeker** is a utility modification for Minecraft that introduces the **Seeker's Wand**. It allows players to search for **any item** across the world—whether vanilla or from any third-party mod (such as the *Charm of Life* from *The Twilight Forest*, machines and ingots from *IndustrialCraft 2*, *Applied Energistics 2*, *Thaumcraft*, *Tinkers' Construct*, etc.)—and filter containers by a configurable **quantity threshold** (e.g., `64`).

The wand pinpoints the exact coordinates of every chest, furnace, shulker box, hopper, chest minecart, or modded storage block containing the item in the specified quantity, highlighting them with **in-world 3D beacons and X-Ray boxes** and providing **one-click teleportation** in chat and GUI.

> 🌟 **Primary Target Version:** **Minecraft 1.12.2 (Forge)** is the **MAIN** release version. Ports and support for major subsequent Minecraft versions (1.16.5, 1.18.2, 1.19.2, 1.20.1, 1.21.1) are available.

---

## 📑 Table of Contents

1. [Version Compatibility Matrix](#-version-compatibility-matrix)
2. [Key Features](#-key-features)
3. [How to Obtain and Use](#-how-to-obtain-and-use)
4. [Seeker's Wand Interface (GUI)](#-seekers-wand-interface-gui)
5. [Scanning Modes](#-scanning-modes)
6. [In-World 3D Visuals (ESP & Beacons)](#-in-world-3d-visuals-esp--beacons)
7. [Usage Example](#-usage-example)
8. [Technical Architecture & Optimization](#-technical-architecture--optimization)
9. [Installation](#-installation)
10. [🇷🇺 Описание на русском языке](#-русская-версия-описания)

---

## 🧭 Version Compatibility Matrix

| Minecraft Version | Mod Loader | Status | Target Java | Download |
| :---: | :---: | :---: | :---: | :---: |
| **`1.12.2`** | **Forge** | **★ MAIN (Primary / Production)** | **Java 8** | [itemseeker-1.0.0-1.12.2.jar](https://github.com/kimarsen/item-seeker/releases/download/v1.0.0/itemseeker-1.0.0.jar) |
| **`1.16.5`** | **Forge** | Supported | Java 8 / 11 | [itemseeker-1.0.0-1.16.5.jar](https://github.com/kimarsen/item-seeker/releases/tag/v1.0.0) |
| **`1.18.2`** | **Forge** | Supported | Java 17 | [itemseeker-1.0.0-1.18.2.jar](https://github.com/kimarsen/item-seeker/releases/tag/v1.0.0) |
| **`1.19.2`** | **Forge** | Supported | Java 17 | [itemseeker-1.0.0-1.19.2.jar](https://github.com/kimarsen/item-seeker/releases/tag/v1.0.0) |
| **`1.20.1`** | **Forge / NeoForge** | Supported (Modern LTS) | Java 17 | [itemseeker-1.0.0-1.20.1.jar](https://github.com/kimarsen/item-seeker/releases/tag/v1.0.0) |
| **`1.21.1`** | **NeoForge** | Supported (Modern) | Java 21 | [itemseeker-1.0.0-1.21.1.jar](https://github.com/kimarsen/item-seeker/releases/tag/v1.0.0) |

---

## ✨ Key Features

* **100% Container Coverage:**
  * Vanilla chests (with smart **Double Chest linking** preventing duplicate counts).
  * Furnaces, dispensers, droppers, hoppers, brewing stands, and shulker boxes.
  * Storage entities: chest minecarts, hopper minecarts, chested donkeys, mules, and llamas.
  * **All modded containers:** Universal integration with Forge `CapabilityItemHandler` and `IInventory` (Iron Chests, Tinkers' Construct crates, Thermal Expansion machines, etc.).
* **Flexible Item Filtering:**
  * Drag & drop any item into the filter slot without consuming the item (phantom slot).
  * Toggle between exact NBT tag matching (for specific enchanted/modded artifacts) or general item ID matching.
* **Configurable Quantity Threshold:**
  * Find only storages where resources meet or exceed a threshold (e.g. at least 64 items). Quick-set buttons for `1`, `16`, `32`, `64`, `128`, and `256`.
* **Zero-Lag Region File Scanner (.mca):**
  * Deep asynchronous scanning of **unloaded world chunks** directly from disk without forcing chunk loading, entity spawning, or server lag spikes.
* **Interactive Navigation & Teleportation:**
  * Coordinates displayed in chat with clickable `[TP]` shortcuts.
  * Built-in pagination and teleport button inside the wand GUI.
* **In-World 3D ESP & Sky Beacons:**
  * Bounding boxes visible through walls (X-Ray).
  * 128-block tall light beams pointing to the sky.
  * 3D floating text tags displaying container type, count, and distance.

---

## 🕹️ How to Obtain and Use

### Acquisition
The wand is available in the **Creative Inventory** under the **«Item Seeker»** tab as the **«Seeker's Wand»** (`itemseeker:seeker_wand`).

### Controls

| Action | Function |
| :--- | :--- |
| **Right-Click (RMB)** | Open the wand configuration GUI (set target item, threshold, and search mode). |
| **Shift + Right-Click** | Quickly trigger a scan with the current settings without opening the menu. |
| **Click Filter Slot** | Place cursor item into the filter slot (or click with an empty hand to clear). |
| **[START SEARCH] Button** | Execute the scan and output coordinates to GUI, chat, and the 3D world. |

---

## 🖥️ Seeker's Wand Interface (GUI)

The custom GUI provides:

1. **Target Item Slot:**
   * Shows the icon and localized name of the target item.
   * **«Clear»** button resets the filter.
2. **Threshold Input:**
   * Text field for manual numeric input.
   * Quick-preset buttons: `[ 1 ]`, `[ 16 ]`, `[ 32 ]`, `[ 64 ]`, `[ 128 ]`, `[ 256 ]`.
3. **Search Scope Selector:**
   * `Chunks: Loaded` — instant scan of all currently active containers in server memory.
   * `Radius: 256 / 512 / 1024 blocks` — filters by spherical distance from the player.
   * `Entire World (Regions)` — deep scan of world save `.mca` files.
4. **NBT Matching Toggle:**
   * `NBT: OFF` — matches any item sharing the item ID (regardless of enchantments or damage).
   * `NBT: ON` — strict NBT tag equality for unique artifacts.
5. **Results Panel:**
   * Navigation buttons `<` and `>` to cycle through all found containers.
   * Info display: `Index / Total`, container type, found count, and distance in meters.
   * **[TP] Button** — instantly and safely teleports the player to the container.
   * **[Mark] Button** — toggles a highlighted beacon for that container.

---

## 🌐 Scanning Modes

* **Loaded Chunks:**
  * Scans `world.loadedTileEntityList` and `world.loadedEntityList` in RAM.
  * Takes 0–3 ms; perfect for active bases and chunkloaded areas.
* **Radius Mode:**
  * Restricts the search within 256, 512, or 1024 blocks of the player.
* **Entire World (Region File Scanner):**
  * Reads Anvil `.mca` region files directly from disk.
  * Bypasses chunk loading, preventing memory exhaustion and tick drops.
  * Merges real-time RAM data for currently loaded chunks to guarantee 100% accuracy.

---

## 🔮 In-World 3D Visuals (ESP & Beacons)

When containers are found:
* A translucent glowing bounding box renders around each container **through walls**.
* A vertical **beacon light beam** rises 128 blocks into the sky.
* A floating 3D billboard shows the container name, item count, and distance:
  ```text
  Chest (64) [85m]
  ```

---

## 💡 Usage Example

### Example: Finding a Charm of Life from The Twilight Forest
1. Grab the **Seeker's Wand** from Creative and **Right-Click** to open the interface.
2. Place a **Charm of Life** (`twilightforest:charm_of_life_1`) into the filter slot.
3. Click the **`[ 64 ]`** button (or enter `64` manually).
4. Select **«Chunks: Loaded»** or **«Entire World (Regions)»**.
5. Click **`[ START SEARCH ]`**.
6. The chat and GUI will display all coordinates of chests containing 64+ Charms of Life, with beacons highlighting them in the world!

---

## ⚙️ Technical Architecture & Optimization

* **Double Chest Linking:**
  * Accurately pairs adjacent chest blocks and checks total inventory, preventing false negatives and duplicate entries.
* **Forge Capabilities:**
  * Seamless support for modded storage systems via `CapabilityItemHandler.ITEM_HANDLER_CAPABILITY`.
* **Zero-Lag Network Packets:**
  * Built using Forge's `SimpleNetworkWrapper` with compact byte serialization.

---

## 📥 Installation

1. Install **Minecraft Forge 1.12.2** (build `14.23.5.2847` or later).
2. Download **`itemseeker-1.0.0.jar`** from [Releases](https://github.com/kimarsen/item-seeker/releases).
3. Place the `.jar` into your `.minecraft/mods` folder.
4. Launch the game!

---

<details>
<summary><b>🇷🇺 Русская версия описания (Нажмите, чтобы развернуть)</b></summary>

# 🔍 Item Seeker (Жезл Поиска)

**Item Seeker** — мощный утилитарный мод для **Minecraft Forge 1.12.2 (MAIN)**, созданный для быстрого обнаружения любых предметов и ресурсов в мире. 

С помощью специального **Жезла Поиска** вы можете указать абсолютно любой предмет (как ванильный, так и из любых сторонних модификаций, например, *Амулет Жизни из Twilight Forest*, ресурсы из *IndustrialCraft 2*, *Applied Energistics 2*, *Thaumcraft* и т.д.), задать необходимый порог количества (например, `64`), и жезл мгновенно укажет координаты всех сундуков, печей, шалкеровых ящиков, загрузочных воронок, вагонеток с сундуками и любых блоков/сущностей с хранилищами, содержащих этот предмет в указанном количестве или больше.

### 🌟 Основные особенности:
* **Поддержка всех контейнеров:**
  * Ванильные сундуки (включая корректный подсчёт в **двойных сундуках**).
  * Печи, раздатчики, выбрасыватели, воронки, варочные стойки, ящики шалкера.
  * Сущности-хранилища: вагонетки с сундуками/воронками, ослы/мулы/ламы с сундуками.
  * **Любые модовые контейнеры:** поддержка стандартных интерфейсов `IInventory` и Forge `CapabilityItemHandler`.
* **Гибкий фильтр предметов:**
  * Фантомный слот в GUI — предмет не тратится.
  * Поддержка точного сравнения NBT-тегов или общего поиска по ID предмета.
* **Настройка порога количества (Threshold):**
  * Позволяет фильтровать только те хранилища, где предмет накопился в нужном объёме (например, найти только полные стаки по 64 шт.).
* **Асинхронный поиск по регион-файлам (.mca):**
  * Возможность сканировать даже **незагруженные чанки** всего сохранённого мира без принудительной генерации или лагов сервера.
* **Навигация и телепортация:**
  * Интерактивные кликабельные координаты в чате: кнопка `[ТП]` мгновенно перемещает игрока к найденному контейнеру.
  * Навигация по страницам результатов прямо в интерфейсе жезла.
* **Визуальные маркеры (ESP):**
  * Подсветка контейнеров сквозь стены (X-Ray) с маяками, уходящими в небо, и 3D-табличками с дистанцией и количеством предметов.

### 🕹️ Управление:
* **ПКМ**: Открыть интерфейс настройки жезла.
* **Shift + ПКМ**: Быстрый перезапуск поиска с текущими параметрами.
* **Клик в слот фильтра**: Положить предмет из руки в слот фильтра (или очистить кликом пустой рукой).
* **Кнопка «НАЧАТЬ ПОИСК»**: Выполнить сканирование мира и вывести координаты.

### 💡 Пример использования:
1. Возьмите Жезл Поиска и откройте его на **ПКМ**.
2. Поместите **Амулет жизни** (`twilightforest:charm_of_life_1`) в слот фильтра.
3. Нажмите кнопку **`[ 64 ]`** или введите `64` в поле порога.
4. Выберите режим **«Чанки: Загруженные»** (или «Весь мир»).
5. Нажмите **`[ НАЧАТЬ ПОИСК ]`**.
6. В чате и на экране появится точный перечень координат всех сундуков с 64+ амулетами жизни, а в мире зажгутся световые маяки.

### 📥 Установка:
Поместите `itemseeker-1.0.0.jar` в папку `.minecraft/mods` (требуется Forge 1.12.2).

</details>
