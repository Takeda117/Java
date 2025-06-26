package menu;

import composite.*;
import factory.Character;
import factory.Warrior;
import factory.Mage;
import iterator.Item;
import iterator.Inventory;
import strategy.SortByNameStrategy;
import strategy.SortByTypeStrategy;
import strategy.SortByValueStrategy;
import strategy.InventorySortStrategy;
import logger.GameLogger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.logging.Logger;

/**
 * InventoryMenu - Manages inventory display with sorting strategies
 * Implements Strategy Pattern for inventory sorting
 */
public class InventoryMenu {
    private static final Logger logger = GameLogger.getLogger();

    /**
     * Shows inventory menu
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
     * Core method implementing the Strategy pattern
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
