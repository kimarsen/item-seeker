# 🔍 Item Seeker & Admin Tools

[![Minecraft 1.12.2 (MAIN)](https://img.shields.io/badge/Minecraft%201.12.2-MAIN%20%E2%98%85-gold.svg)](https://github.com/kimarsen/item-seeker/releases)
[![Minecraft 1.16.5](https://img.shields.io/badge/Minecraft%201.16.5-Supported-blue.svg)](https://github.com/kimarsen/item-seeker/releases)
[![Minecraft 1.18.2](https://img.shields.io/badge/Minecraft%201.18.2-Supported-blue.svg)](https://github.com/kimarsen/item-seeker/releases)
[![Minecraft 1.19.2](https://img.shields.io/badge/Minecraft%201.19.2-Supported-blue.svg)](https://github.com/kimarsen/item-seeker/releases)
[![Minecraft 1.20.1](https://img.shields.io/badge/Minecraft%201.20.1-Supported-blue.svg)](https://github.com/kimarsen/item-seeker/releases)
[![Minecraft 1.21.1](https://img.shields.io/badge/Minecraft%201.21.1-Supported-blue.svg)](https://github.com/kimarsen/item-seeker/releases)
[![ModLoader Forge](https://img.shields.io/badge/ModLoader-Forge%20%2F%20NeoForge-orange.svg)](https://files.minecraftforge.net/)
[![Release](https://img.shields.io/badge/Release-v1.1.0-purple.svg)](https://github.com/kimarsen/item-seeker/releases)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

**Item Seeker & Admin Tools** is a comprehensive utility and administration modification for Minecraft. It divides container tracking into a balanced **Survival Seeker's Wand** and an all-seeing **Admin Seeker's Wand**, complemented by a professional suite of **Administrator & Moderation Tools**.

Whether you want to locate misplaced items in your survival base or give server operators surgical precision over containers, player hoards, and server lag, this mod provides clean, high-performance tools.

> 🌟 **Primary Target Version:** **Minecraft 1.12.2 (Forge)** is the **MAIN** release version. Source ports for major subsequent Minecraft versions (1.16.5, 1.18.2, 1.19.2, 1.20.1, 1.21.1) are maintained and supported.

---

## 📑 Table of Contents

1. [Version Compatibility Matrix](#-version-compatibility-matrix)
2. [The Two Seeker Wands: Survival vs Admin](#-the-two-seeker-wands-survival-vs-admin)
3. [Survival Crafting Recipe](#-survival-crafting-recipe-seekers-wand)
4. [Admin & Moderation Tools (OP / Creative Only)](#-admin--moderation-tools-op--creative-only)
5. [Scanning Capabilities & Container Coverage](#-scanning-capabilities--container-coverage)
6. [In-World 3D Visuals (ESP & Beacons)](#-in-world-3d-visuals-esp--beacons)
7. [Controls & Hotkeys](#-controls--hotkeys)
8. [Usage Example](#-usage-example)
9. [Installation](#-installation)
10. [🇷🇺 Описание на русском языке](#-русская-версия-описания)

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

## 🔮 The Two Seeker Wands: Survival vs Admin

To ensure fair multiplayer gameplay without ruining survival balance or revealing distant private bases, the seeker functionality is split into two distinct wands:

| Feature | 🌿 Seeker's Wand (Survival) | 👑 Omniscient Seeker's Wand (Admin) |
| :--- | :--- | :--- |
| **Item ID** | `itemseeker:seeker_wand` | `itemseeker:admin_seeker_wand` |
| **Acquisition** | Craftable via standard crafting recipe | Exclusively via `/give` command (Hidden from Creative tabs) |
| **Required Permissions** | Any player (Survival friendly) | Server Operators Only (`/op` level >= 2) |
| **Crafting Recipe** | ✅ Yes (Ender Eye, Compass, Gold, Blaze Rod) | ❌ None (Uncraftable) |
| **Search Range** | Strictly local: **48 blocks (2–3 chunks)** | Unrestricted (Loaded chunks, Radius up to 1024m, Entire World) |
| **Region Files (.mca)** | ❌ Disabled (Cannot scan world files) | ✅ Asynchronous deep `.mca` scanner |
| **Teleportation (`[TP]`)** | ❌ Disabled | ✅ Enabled (Instant teleport button in Chat & GUI) |
| **Use Case** | Base sorting, local dungeon searches | Server auditing, dupe investigations, stash discovery |

---

## 🛠️ Survival Crafting Recipe (Seeker's Wand)

The **Survival Seeker's Wand** is crafted on a vanilla Crafting Table:

```text
[   -   ] [ Eye of Ender ] [   -   ]
[ Gold  ] [   Compass    ] [ Gold  ]
[   -   ] [  Blaze Rod   ] [   -   ]
```

* **Ingredients:**
  * `1x` **Eye of Ender** (Divination vision)
  * `1x` **Compass** (Coordinate orientation)
  * `2x` **Gold Ingot** (Magical resonance)
  * `1x` **Blaze Rod** (Conductive focus shaft)

---

## 🛡️ Admin & Moderation Tools (OP / `/give` Only)

All admin tools have **NO crafting recipes**, are **hidden from all Creative tabs / JEI**, and can **ONLY be obtained via the `/give` command**. Furthermore, they strictly require Server Operator (`/op`) status:
* If a non-operator player somehow acquires an admin tool, it is immediately confiscated.
* Non-operators cannot pick up dropped admin tools (`EntityItemPickupEvent` cancelled).
* Non-operators cannot execute any actions or commands with admin tools.

### 1. 👑 Omniscient Seeker's Wand (`itemseeker:admin_seeker_wand`)
* **Global Container Radar:** Scans loaded chunks, custom radii, or the entire world's `.mca` region files.
* **Filter by Quantity Threshold:** Find stashes containing hoarded items (e.g. `64`, `256`, or `1000+` diamonds or enchanted relics).
* **Direct Teleportation:** Click `[TP]` in chat or GUI to jump straight to the container.

### 2. ⚡ Staff Moderation Wand (`itemseeker:staff_wand`)
* **Right-Click Player:** Silently opens the target player's **Ender Chest** for inspection in real time.
* **Shift + Right-Click Player:** **Freeze / Unfreeze Target**!
  * Locks the player in place with Slowness and Blindness, cancelling movement, block breaks/places, and combat.
  * Sends an on-screen warning: *«You have been frozen by a moderator for inspection! Do not disconnect.»*
* **Right-Click Air:** Interactive **Online Players Moderation List** in chat with quick-action shortcuts:
  * `[TP to Player]`
  * `[Bring Player]`
  * `[Freeze/Kick]` prompt
* **Shift + Right-Click Air:** Toggle **Vanish (Invisibility)** for stealth observation.

### 3. 🕵️ Inspector's Audit Wand (`itemseeker:inspector_wand`)
* **Right-Click Any Block / Container (up to 64m away):**
  * **Stealth Container Inspection:** Silently opens chests, furnaces, hoppers, and modded inventories through walls, land claims, or lock protections **without chest lid animations and without opening sounds**!
  * Outputs block details, coordinates, slot count, and stored item count in chat.
* **Shift + Right-Click Air (Dupe & Hoard Auditor):**
  * Uses the target item configured in your Seeker's Wand (or held in offhand).
  * Scans **ALL online players' inventories and Ender Chests** simultaneously!
  * Outputs exact player names and quantities, making dupe detection instantaneous.

### 4. 🧹 Lag Buster Wand (`itemseeker:lag_wand`)
* **Right-Click Air (Server Health & Lag Hunter):**
  * Displays real-time **Server TPS**, tick duration in ms, and memory allocation (RAM used/max).
  * **Lag Hunter Algorithm:** Analyzes all loaded chunks within 512 blocks to identify the **Top 3 most overloaded chunks** (mob farms, entity clusters), providing a clickable **[TP]** shortcut directly to the bottleneck.
* **Shift + Right-Click Air (Drop & Orb Purge):**
  * Instantly deletes all dropped items (`EntityItem`) and experience orbs within a 64-block radius to eliminate localized lag spikes.

---

## 📦 Scanning Capabilities & Container Coverage

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

---

## 🌟 In-World 3D Visuals (ESP & Beacons)

* **X-Ray Bounding Boxes:** Rendered around found containers visible through solid walls.
* **128-Block Sky Beacons:** Vertical light beams pointing into the sky to reveal container locations across terrain.
* **3D Floating Text Tags:** Displays container type, item count, and distance in meters.
* **Toggleable Markers:** Keep containers highlighted while you travel to them or clear them with a single click.

---

## 🕹️ Controls & Hotkeys

| Item | Action | Function |
| :--- | :--- | :--- |
| **Seeker's Wand (Survival)** | **Right-Click** | Open search GUI (set item filter & threshold, locked to 48m radius). |
| **Seeker's Wand (Survival)** | **Shift + Right-Click** | Quick scan within 48-block local radius. |
| **Admin Seeker's Wand** | **Right-Click** | Open omniscient GUI (unrestricted range, world scan, teleport). |
| **Admin Seeker's Wand** | **Shift + Right-Click** | Quick scan with current admin settings. |
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
1. Take the **Seeker's Wand** (Survival) or **Omniscient Seeker's Wand** (Admin).
2. **Right-Click** to open the interface.
3. Place a **Charm of Life** (`twilightforest:charm_of_life_1`) into the filter slot.
4. Click the **`[ 64 ]`** button (or enter `64` manually).
5. Click **`[ START SEARCH ]`**.
6. The chat and GUI will display all coordinates of chests containing 64+ Charms of Life, with beacons highlighting them in the world!

---

## 📥 Installation

1. Install **Minecraft Forge 1.12.2** (build `14.23.5.2847` or later).
2. Download **`itemseeker-1.0.0.jar`** from [Releases](https://github.com/kimarsen/item-seeker/releases).
3. Place the `.jar` into your `.minecraft/mods` folder.
4. Launch the game!

---

<details>
<summary><b>🇷🇺 Русская версия описания (Нажмите, чтобы развернуть)</b></summary>

# 🔍 Item Seeker & Admin Tools (Жезл Поиска & Админ-инструменты)

**Item Seeker & Admin Tools** — продвинутый утилитарный мод для **Minecraft Forge 1.12.2 (MAIN)**, разделяющий поиск предметов на сбалансированный инструмент для выживания и всевидящий административный сканер, а также включающий профессиональный набор инструментов для управления сервером.

С помощью жезла вы можете внести **любой предмет** (как ванильный, так и из любых сторонних модов — например, *Амулет Жизни из Twilight Forest*, ресурсы из *IndustrialCraft 2*, *Applied Energistics 2*, *Thaumcraft*, *Tinkers' Construct* и др.), задать необходимый порог количества (например, `64`), и найти все хранилища с заданным запасом.

---

### 🔮 Разделение жезлов: Выживание vs Админ

| Характеристика | 🌿 Жезл Поиска (Выживание) | 👑 Всевидящий Жезл (Админ) |
| :--- | :--- | :--- |
| **ID предмета** | `itemseeker:seeker_wand` | `itemseeker:admin_seeker_wand` |
| **Получение** | Крафт на верстаке в выживании | Исключительно команда `/give` (скрыт из Креатива и JEI) |
| **Необходимые права** | Доступен всем игрокам (выживание) | Только операторы сервера (`/op` уровень >= 2) |
| **Крафт** | ✅ Есть (Око Края, Компас, Золото, Стержень ифрита) | ❌ Нет (без крафта) |
| **Радиус поиска** | Строго локальный: **48 блоков (2–3 чанка)** | Без ограничений (Загруженные чанки, Радиус до 1024м, Весь мир) |
| **Поиск по регион-файлам (.mca)** | ❌ Заблокирован | ✅ Глубокое асинхронное чтение файлов региона |
| **Кнопка телепортации `[ТП]`** | ❌ Недоступна | ✅ Доступна в чате и в графическом интерфейсе |
| **Назначение** | Поиск забытых сундуков на базе, зачистка данжей | Аудит сервера, расследование дюпов, поиск тайников |

---

### 🛠️ Рецепт крафта в выживании (Жезл Поиска)

```text
[       -       ] [ Око Края ] [       -       ]
[ Золотой слиток] [  Компас  ] [ Золотой слиток]
[       -       ] [  Стержень ифрита  ] [       -       ]
```

* **Ингредиенты:**
  * `1x` **Око Края** (мистическое зрение)
  * `1x` **Компас** (координатная привязка)
  * `2x` **Золотой слиток** (магическая проводимость)
  * `1x` **Стержень ифрита** (основа жезла)

---

### 🛡️ Инструменты Администратора и Модератора (Только OP / Только `/give`)

Все админские инструменты **не имеют крафта**, **полностью скрыты из вкладок Креатива и JEI** и могут быть получены **исключительно командой `/give`**. Для их использования строго требуются права оператора сервера (`/op`):
* При попытке получить или взять инструмент не-оператором, предмет моментально изымается из инвентаря.
* Обычные игроки не могут подобрать выброшенные инструменты с земли.
* Любые действия и клики инструментами блокируются для игроков без прав оператора.

1. **👑 Всевидящий Жезл Поиска (`itemseeker:admin_seeker_wand`)**:
   * Поиск по загруженным чанкам, настраиваемому радиусу и по незагруженным регион-файлам (`.mca`) всего мира без создания серверных лагов.
   * Быстрый телепорт `[ТП]` к любому найденному хранилищу.

2. **⚡ Посох Модератора (`itemseeker:staff_wand`)**:
   * **ПКМ по игроку:** Скрытый досмотр эндер-сундука игрока в реальном времени.
   * **Shift + ПКМ по игроку:** **Заморозка/разморозка игрока**. Цель блокируется на месте (эффект слепоты и замедления, запрет перемещения, ломания блоков и ударов) на время проверки на читы.
   * **ПКМ в воздух:** Интерактивное меню онлайн-игроков в чате с быстрыми кнопками `[ТП к нему]`, `[ТП сюда]`, `[Заморозка]`.
   * **Shift + ПКМ в воздух:** Режим невидимости (**Vanish**).

3. **🕵️ Жезл Ревизора (`itemseeker:inspector_wand`)**:
   * **ПКМ по блоку (до 64 блоков):** Скрытое дистанционное открытие сундука сквозь стены и приваты (без звуков и анимации крышки).
   * **Shift + ПКМ в воздух (Детектор дюпа):** Мгновенно проверяет инвентари и эндер-сундуки **всех игроков онлайн** на наличие искомого предмета из жезла.

4. **🧹 Жезл Оптимизации (`itemseeker:lag_wand`)**:
   * **ПКМ в воздух:** Мониторинг TPS, времени тика (мс), оперативной памяти и алгоритм **Lag Hunter** (показывает Топ-3 самых лагающих чанков с фермами/мобами в радиусе 512 блоков с кнопкой `[ТП]`).
   * **Shift + ПКМ в воздух:** Очистка дропа и сфер опыта в радиусе 64 блоков.

---

### 🌟 Основные особенности:
* **Поддержка всех контейнеров:** ванильные сундуки (с защитой от двойного подсчёта в Double Chest), печи, раздатчики, шалкеры, воронки, вагонетки с сундуками, ослы/мулы с сундуками, а также **все модовые контейнеры** (`IInventory` и Forge `CapabilityItemHandler`).
* **Фантомный фильтр предметов:** предмет не расходуется при установке в фильтр; поддержка точного совпадения NBT.
* **Порог количества:** фильтрация хранилищ, в которых накопилось от N предметов (быстрые кнопки: 1, 16, 32, 64, 128, 256).
* **Визуальные маркеры (ESP):** 3D-подсветка контейнеров сквозь стены с уходящими в небо лучами маяков и плавающими табличками расстояния.

---

### 📥 Установка:
Поместите `itemseeker-1.0.0.jar` в папку `.minecraft/mods` (требуется Forge 1.12.2).

</details>
