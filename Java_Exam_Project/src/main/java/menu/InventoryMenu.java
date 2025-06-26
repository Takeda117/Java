package java.menu;


import java.composite.GameMenu;
import java.composite.MenuItem;
import java.factory.Character;
import java.factory.Warrior;
import java.factory.Mage;
import java.iterator.Item;
import java.iterator.Inventory;
import java.strategy.SortByNameStrategy;
import java.strategy.SortByTypeStrategy;
import java.strategy.SortByValueStrategy;
import java.strategy.InventorySortStrategy;
import java.logger.GameLogger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.logging.Logger;

/**
 * InventoryMenu - Manages inventory display with sorting strategies
 * <p>
 * This class is responsible for building and executing the inventory menu
 * for a specific character. It provides options for viewing items with
 * different sorting strategies, implementing the Strategy pattern for
 * inventory sorting. The class uses the Composite pattern through the
 * GameMenu and MenuItem classes to structure the menu hierarchy.
 * </p>
 * <p>
 * The InventoryMenu integrates with the Inventory class to display and
 * manage the character's items, and uses reflection to access the
 * character's inventory when direct access is not available.
 * </p>
 */
public class InventoryMenu {
    private static final Logger logger = GameLogger.getLogger();

    /**
     * Shows inventory menu
     * <p>
     * Displays and executes the inventory menu for a specific character,
     * providing options for viewing items with different sorting strategies.
     * </p>
     * 
     * @param character The character whose inventory to display
     */
    public static void showInventoryMenu(Character character) {
        if (character == null) {
            logger.warning("Cannot show inventory menu: null character");
            return;
        }

        try {
            GameMenu menu = new GameMenu("Inventario - " + character.getName());
            menu.add(new MenuItem("Mostra tutti gli oggetti", () -> showItems(character, null, "TUTTI GLI OGGETTI")));
            menu.add(new MenuItem("Mostra oggetti per tipologia", () -> showItems(character, new SortByTypeStrategy(), "OGGETTI PER TIPOLOGIA")));
            menu.add(new MenuItem("Mostra oggetti per valore", () -> showItems(character, new SortByValueStrategy(), "OGGETTI PER VALORE")));
            menu.add(new MenuItem("Mostra oggetti per nome", () -> showItems(character, new SortByNameStrategy(), "OGGETTI PER NOME")));
            menu.add(new MenuItem("Torna al menu personaggio", () -> {}));
            menu.execute();
        } catch (Exception e) {
            logger.severe("Error in inventory menu: " + e.getMessage());
            System.out.println("Errore nel menu inventario.");
        }
    }

    /**
     * Shows items using the specified sorting strategy
     * <p>
     * Core method implementing the Strategy pattern for inventory sorting.
     * Retrieves the character's inventory, applies the specified sorting
     * strategy, and displays the items accordingly.
     * </p>
     * 
     * @param character The character whose items to display
     * @param strategy The sorting strategy to apply, or null for default sorting
     * @param title The title to display for the item list
     */
    private static void showItems(Character character, InventorySortStrategy strategy, String title) {
        System.out.println("\n=== " + title + " ===");
        Inventory inventory = getCharacterInventory(character);
        
        if (inventory == null) {
            System.out.println("Errore nell'accesso all’inventario!");
            return;
        }

        List<Item> items = inventory.getAllItems();
        if (items.isEmpty()) {
            System.out.println("L'inventario è vuoto.");
            return;
        }

        // Apply the sorting strategy (Strategy Pattern)
        inventory.setSortStrategy(strategy);
        inventory.sort();
        
        // Special display for items grouped by type
        if (strategy instanceof SortByTypeStrategy) {
            displayItemsByType(items, inventory);
        } else {
            displayItems(items, inventory);
        }
        
        logger.info("Displayed items for " + character.getName() + " using strategy: " + 
                    (strategy == null ? "none" : strategy.getClass().getSimpleName()));
    }

    /**
     * Displays items grouped by type
     * <p>
     * Shows items organized by their type categories, with additional
     * information about each item.
     * </p>
     * 
     * @param items The list of items to display
     * @param inventory The inventory containing the items
     */
    private static void displayItemsByType(List<Item> items, Inventory inventory) {
        Map<Item.ItemType, List<Item>> itemsByType = items.stream()
                .collect(Collectors.groupingBy(Item::getType));

        for (Item.ItemType type : Item.ItemType.values()) {
            List<Item> itemsOfType = itemsByType.get(type);
            if (itemsOfType != null && !itemsOfType.isEmpty()) {
                System.out.println("\n📦 " + type.getDisplayName().toUpperCase() + "S:");
                for (Item item : itemsOfType) {
                    String bonus = item.getStatBonus() > 0 ? " (+" + item.getStatBonus() + ")" : "";
                    System.out.printf("  • %s - %d oro%s%n", item.getName(), item.getValue(), bonus);
                }
            }
        }
        
        System.out.println("\nTotale oggetti: " + inventory.getSize());
        System.out.println("Valore totale: " + inventory.getTotalValue() + " oro");
    }

    /**
     * Displays items in a formatted list
     * <p>
     * Shows items in a simple numbered list format, with information
     * about each item.
     * </p>
     * 
     * @param items The list of items to display
     * @param inventory The inventory containing the items
     */
    private static void displayItems(List<Item> items, Inventory inventory) {
        System.out.println("Oggetti nell'inventario: " + items.size());
        
        int index = 1;
        for (Item item : items) {
            System.out.printf("%d. %s [%s] - Valore: %d oro%n",
                    index++, item.getName(), item.getType().getDisplayName(), item.getValue());
        }

        System.out.println("\nValore totale: " + inventory.getTotalValue() + " oro");
    }

    /**
     * Gets character inventory using reflection
     * <p>
     * Uses reflection to access the character's inventory field, which
     * may not be directly accessible due to encapsulation.
     * </p>
     * 
     * @param character The character whose inventory to retrieve
     * @return The character's inventory, or null if it cannot be accessed
     */
    private static Inventory getCharacterInventory(Character character) {
        try {
            Class<?> clazz = character instanceof Warrior ? Warrior.class : 
                            (character instanceof Mage ? Mage.class : null);
            
            if (clazz != null) {
                java.lang.reflect.Field inventoryField = clazz.getDeclaredField("inventory");
                inventoryField.setAccessible(true);
                return (Inventory) inventoryField.get(character);
            }
        } catch (Exception e) {
            logger.warning("Error accessing character inventory: " + e.getMessage());
        }
        return null;
    }
}