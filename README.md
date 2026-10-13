# 🔍 Item Seeker (Seeker's Wand & Admin Toolkit)

[![Minecraft 1.12.2 (MAIN)](https://img.shields.io/badge/Minecraft%201.12.2-MAIN%20%E2%98%85-gold.svg)](https://github.com/kimarsen/item-seeker/releases)
[![Minecraft 1.16.5](https://img.shields.io/badge/Minecraft%201.16.5-Supported-blue.svg)](https://github.com/kimarsen/item-seeker/releases)
[![Minecraft 1.18.2](https://img.shields.io/badge/Minecraft%201.18.2-Supported-blue.svg)](https://github.com/kimarsen/item-seeker/releases)
[![Minecraft 1.19.2](https://img.shields.io/badge/Minecraft%201.19.2-Supported-blue.svg)](https://github.com/kimarsen/item-seeker/releases)
[![Minecraft 1.20.1](https://img.shields.io/badge/Minecraft%201.20.1-Supported-blue.svg)](https://github.com/kimarsen/item-seeker/releases)
[![Minecraft 1.21.1](https://img.shields.io/badge/Minecraft%201.21.1-Supported-blue.svg)](https://github.com/kimarsen/item-seeker/releases)
[![ModLoader Forge](https://img.shields.io/badge/ModLoader-Forge%20%2F%20NeoForge-orange.svg)](https://files.minecraftforge.net/)
[![Release](https://img.shields.io/badge/Release-v1.1.0-purple.svg)](https://github.com/kimarsen/item-seeker/releases)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

**Item Seeker** is a utility modification for Minecraft that introduces the **Seeker's Wand** alongside a dedicated **Administrator & Moderation Toolkit**. 

It allows survival players to craft a mystical divination wand to search for **any item** across the world—whether vanilla or from any third-party mod (such as the *Charm of Life* from *The Twilight Forest*, items from *IndustrialCraft 2*, *Applied Energistics 2*, *Thaumcraft*, *Tinkers' Construct*, etc.)—and filter containers by a configurable **quantity threshold** (e.g., `64`).

For server administrators and moderators, the mod includes **creative-exclusive admin tools** (no crafting recipes) designed to streamline player moderation, cheat investigations, remote container audits, dupe detection, and lag hunting.

> 🌟 **Primary Target Version:** **Minecraft 1.12.2 (Forge)** is the **MAIN** release version. Ports and support for major subsequent Minecraft versions (1.16.5, 1.18.2, 1.19.2, 1.20.1, 1.21.1) are available.

---

## 📑 Table of Contents

1. [Version Compatibility Matrix](#-version-compatibility-matrix)
2. [Survival Crafting Recipe](#-survival-crafting-recipe-seekers-wand)
3. [Admin & Moderation Tools (OP / Creative Only)](#-admin--moderation-tools-op--creative-only)
4. [Seeker's Wand Features & GUI](#-seekers-wand-features--gui)
5. [Scanning Modes](#-scanning-modes)
6. [In-World 3D Visuals (ESP & Beacons)](#-in-world-3d-visuals-esp--beacons)
7. [Usage Example](#-usage-example)
8. [Installation](#-installation)
9. [🇷🇺 Описание на русском языке](#-русская-версия-описания)

---

## 🧭 Version Compatibility Matrix

| Minecraft Version | Mod Loader | Status | Target Java | Download |
| :---: | :---: | :---: | :---: | :---: |
| **`1.12.2`** | **Forge** | **★ MAIN (Primary / Production)** | **Java 8** | [itemseeker-1.0.0-1.12.2.jar](https://github.com/kimarsen/item-seeker/releases/download/v1.0.0/itemseeker-1.0.0-1.12.2.jar) |
| **`1.16.5`** | **Forge** | Supported | Java 8 / 11 | [Branch: 1.16.5](https://github.com/kimarsen/item-seeker/tree/1.16.5) |
| **`1.18.2`** | **Forge** | Supported | Java 17 | [Port Sources](https://github.com/kimarsen/item-seeker/tree/main/ports) |
| **`1.19.2`** | **Forge** | Supported | Java 17 | [Port Sources](https://github.com/kimarsen/item-seeker/tree/main/ports) |
| **`1.20.1`** | **Forge / NeoForge** | Supported (Modern LTS) | Java 17 | [Branch: 1.20.1](https://github.com/kimarsen/item-seeker/tree/1.20.1) |
| **`1.21.1`** | **NeoForge** | Supported (Modern) | Java 21 | [Port Sources](https://github.com/kimarsen/item-seeker/tree/main/ports) |

---

## 🛠️ Survival Crafting Recipe (Seeker's Wand)

The **Seeker's Wand** is fully craftable in survival mode on a standard Crafting Table:

```text
[   -   ] [ Eye of Ender ] [   -   ]
[ Gold  ] [   Compass    ] [ Gold  ]
[   -   ] [  Blaze Rod   ] [   -   ]
```

* **Ingredients:**
  * `1x` **Eye of Ender** (Mystical vision and location divination)
  * `1x` **Compass** (Cartographic navigation and coordinate tracking)
  * `2x` **Gold Ingot** (Magical conductivity)
  * `1x` **Blaze Rod** (Conductive rod shaft)

---

## 🛡️ Admin & Moderation Tools (OP / Creative Only)

The following admin tools **have NO crafting recipe** and are strictly restricted to server administrators and moderators with OP permissions (or Creative mode in singleplayer):

### 1. Staff Moderation Wand (`itemseeker:staff_wand`)
* **Right-Click Player:** Silently opens the target player's **Ender Chest** for inspection.
* **Shift + Right-Click Player:** **Freeze / Unfreeze Target**!
  * Freezes the player in place with Slowness and Blindness, locking their movement, block placing, breaking, and attacking.
  * Sends a clear notice: *«You have been frozen by moderator for an audit! Do not disconnect.»*
* **Right-Click Air:** Displays an interactive **Online Players Moderation List** in chat with quick-action buttons:
  * `[TP to Player]` (`/tp @s <Player>`)
  * `[Bring Player]` (`/tp <Player> @s`)
  * `[Freeze/Kick]` prompt
* **Shift + Right-Click Air:** Toggle **Vanish (Invisibility)** for stealth observation.

### 2. Inspector's Audit Wand (`itemseeker:inspector_wand`)
* **Right-Click Any Block / Container (up to 64 blocks away):**
  * **Stealth Container Inspection:** Silently opens chests, furnaces, hoppers, and modded inventories through walls, land claims, or lock protections **without chest lid animations and without opening sounds**!
  * Outputs block details, coordinates, slot count, and stored item count in chat.
* **Shift + Right-Click Air (Dupe & Hoard Auditor):**
  * Takes the target item configured in your Seeker's Wand (or held in offhand).
  * Scans **ALL online players' inventories and Ender Chests** simultaneously!
  * Lists any player holding that item with exact counts, making it effortless to catch dupers and hoarding rule-breakers.

### 3. Lag Buster Wand (`itemseeker:lag_wand`)
* **Right-Click Air (Server Health & Lag Hunter):**
  * Displays real-time **Server TPS** (and tick time in ms), memory usage (RAM used/max), total loaded entities, and TileEntities.
  * **Lag Hunter Algorithm:** Scans all loaded chunks and identifies the **Top 3 most overloaded chunks** (mob farms, entity clusters) within 512 blocks, providing a clickable **[TP]** shortcut to teleport directly to the lag source.
* **Shift + Right-Click Air (Drop & Orb Purge):**
  * Instantly deletes all dropped items (`EntityItem`) and experience orbs within a 64-block radius to resolve localized drop-lag.

---

## ✨ Seeker's Wand Features & GUI

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

## 🕹️ Controls

| Item | Action | Function |
| :--- | :--- | :--- |
| **Seeker's Wand** | **Right-Click (RMB)** | Open search GUI (set item filter, threshold, and search mode). |
| **Seeker's Wand** | **Shift + Right-Click** | Quick scan with current settings. |
| **Staff Wand** | **Right-Click Player** | Inspect player's Ender Chest. |
| **Staff Wand** | **Shift + Right-Click Player** | Freeze / unfreeze player for investigation. |
| **Staff Wand** | **Right-Click Air** | Open interactive online players list. |
| **Staff Wand** | **Shift + Right-Click Air** | Toggle Vanish (Invisibility). |
| **Inspector Wand** | **Right-Click Block (up to 64m)** | Stealth remote container inspection through walls. |
| **Inspector Wand** | **Shift + Right-Click Air** | Audit all online players for target item (Dupe detector). |
| **Lag Buster** | **Right-Click Air** | Check Server TPS, RAM, and Top-3 overloaded lag chunks. |
| **Lag Buster** | **Shift + Right-Click Air** | Purge dropped items & XP orbs in 64-block radius. |

---

## 💡 Usage Example

### Example: Finding a Charm of Life from The Twilight Forest
1. Craft the **Seeker's Wand** or take it from Creative.
2. **Right-Click** to open the interface.
3. Place a **Charm of Life** (`twilightforest:charm_of_life_1`) into the filter slot.
4. Click the **`[ 64 ]`** button (or enter `64` manually).
5. Select **«Chunks: Loaded»** or **«Entire World (Regions)»**.
6. Click **`[ START SEARCH ]`**.
7. The chat and GUI will display all coordinates of chests containing 64+ Charms of Life, with beacons highlighting them in the world!

---

## 📥 Installation

1. Install **Minecraft Forge 1.12.2** (build `14.23.5.2847` or later).
2. Download **`itemseeker-1.0.0.jar`** from [Releases](https://github.com/kimarsen/item-seeker/releases).
3. Place the `.jar` into your `.minecraft/mods` folder.
4. Launch the game!

---

<details>
<summary><b>🇷🇺 Русская версия описания (Нажмите, чтобы развернуть)</b></summary>

# 🔍 Item Seeker (Жезл Поиска & Админ-инструменты)

**Item Seeker** — мощный утилитарный мод для **Minecraft Forge 1.12.2 (MAIN)**, объединяющий поисковые возможности для игроков в выживании и профессиональный инструментарий для администрации сервера.

С помощью **Жезла Поиска** вы можете указать абсолютно любой предмет (как ванильный, так и из любых сторонних модификаций, например, *Амулет Жизни из Twilight Forest*, ресурсы из *IndustrialCraft 2*, *Applied Energistics 2*, *Thaumcraft* и т.д.), задать необходимый порог количества (например, `64`), и жезл мгновенно укажет координаты всех сундуков, печей, шалкеровых ящиков, загрузочных воронок, вагонеток с сундуками и любых блоков/сущностей с хранилищами, содержащих этот предмет в указанном количестве или больше.

---

### 🛠️ Рецепт крафта в выживании (Жезл Поиска)
Жезл Поиска имеет сбалансированный рецепт на обычном верстаке:

```text
[       -       ] [ Око Края ] [       -       ]
[ Золотой слиток] [  Компас  ] [ Золотой слиток]
[       -       ] [  Стержень ифрита  ] [       -       ]
```

---

### 🛡️ Инструменты Администратора и Модератора (Без крафта, только OP/Креатив)

Инструменты предназначены для упрощения задач модерации, проверок на читы/дюпы и оптимизации сервера:

1. **Посох Модератора (`itemseeker:staff_wand`)**:
   * **ПКМ по игроку:** Скрытый досмотр эндер-сундука игрока в реальном времени.
   * **Shift + ПКМ по игроку:** **Заморозка/разморозка игрока**. Цель лишается возможности двигаться, ломать/ставить блоки и атаковать на время проверки на читы.
   * **ПКМ в воздух:** Список онлайн-игроков в чате с быстрыми кнопками `[ТП к нему]`, `[ТП сюда]`, `[Заморозка]`.
   * **Shift + ПКМ в воздух:** Включение/выключение режима невидимости (**Vanish**).

2. **Жезл Ревизора (`itemseeker:inspector_wand`)**:
   * **ПКМ по блоку (до 64 блоков):** Скрытое дистанционное открытие сундука сквозь стены и приваты (без анимации крышки и звуков). Выводит координаты, количество слотов и предметов.
   * **Shift + ПКМ в воздух (Детектор дюпа):** Сканирует инвентари и эндер-сундуки **всех онлайн-игроков** на наличие предмета из Жезла Поиска (или предмета во второй руке). Показывает, у кого и сколько штук хранится.

3. **Жезл Оптимизации (`itemseeker:lag_wand`)**:
   * **ПКМ в воздух:** Замер TPS сервера (и времени тика в ms), оперативной памяти и алгоритм **Lag Hunter** (показывает Топ-3 самых перегруженных чанков с мобами/фермами в радиусе 512 блоков и кнопкой `[ТП]`).
   * **Shift + ПКМ в воздух:** Моментальная очистка выброшенного лута и сфер опыта в радиусе 64 блоков для устранения лаг-спайков.

---

### 🌟 Основные особенности Жезла Поиска:
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

---

### 📥 Установка:
Поместите `itemseeker-1.0.0.jar` в папку `.minecraft/mods` (требуется Forge 1.12.2).

</details>
