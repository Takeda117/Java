package com.exam.project.menu;

import com.exam.project.composite.*;
import com.exam.project.factory.Character;
import com.exam.project.factory.Warrior;
import com.exam.project.factory.Mage;
import com.exam.project.iterator.Item;
import com.exam.project.iterator.Inventory;
import com.exam.project.strategy.SortByTypeStrategy;
import com.exam.project.security.InputValidator;
import com.exam.project.logger.GameLogger;

import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.logging.Logger;

/**
 * InventoryMenuBuilder - Builds inventory menu
 * Uses Strategy Pattern for sorting by type
 */
public class InventoryMenuBuilder {

    private static final Logger logger = GameLogger.getLogger();
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Show inventory menu
     */
    public static void showInventoryMenu(Character character) {
        if (character == null) {
            logger.warning("Cannot show inventory menu: null character");
            return;
        }

        try {
            GameMenu inventoryMenu = buildInventoryMenu(character);
            inventoryMenu.execute();
        } catch (Exception e) {
            logger.severe("Error in inventory menu: " + e.getMessage());
            System.out.println("Errore nel menu inventario.");
        }
    }

    /**
     * Build inventory menu with required options only
     */
    private static GameMenu buildInventoryMenu(Character character) {
        GameMenu menu = new GameMenu("Inventario - " + character.getName());

        menu.add(new MenuItem("Mostra tutti gli oggetti", () -> showAllItems(character)));
        menu.add(new MenuItem("Mostra oggetti per tipologia", () -> showItemsByType(character)));
        menu.add(new MenuItem("Torna al menu personaggio", () -> {
            // Simply exit this menu
        }));

        return menu;
    }

    /**
     * Show all items in inventory
     */
    private static void showAllItems(Character character) {
        try {
            System.out.println("\n=== TUTTI GLI OGGETTI ===");

            Inventory inventory = getCharacterInventory(character);
            if (inventory == null) {
                System.out.println("Errore nell'accesso all'inventario!");
                return;
            }

            List<Item> allItems = inventory.getAllItems();

            if (allItems.isEmpty()) {
                System.out.println("L'inventario è vuoto.");
                return;
            }

            System.out.println("Oggetti nell'inventario (" + allItems.size() + "/" + inventory.getMaxCapacity() + "):");

            int index = 1;
            for (Item item : allItems) {
                System.out.printf("%d. %s [%s] - Valore: %d oro%n",
                        index++, item.getName(), item.getType().getDisplayName(), item.getValue());
            }

            System.out.println("\nValore totale: " + inventory.getTotalValue() + " oro");

            logger.info("Displayed all items for: " + character.getName());

        } catch (Exception e) {
            logger.warning("Error showing all items: " + e.getMessage());
            System.out.println("Errore nella visualizzazione degli oggetti.");
        }
    }

    /**
     * Show items grouped by type using Strategy Pattern
     */
    private static void showItemsByType(Character character) {
        try {
            System.out.println("\n=== OGGETTI PER TIPOLOGIA ===");

            Inventory inventory = getCharacterInventory(character);
            if (inventory == null) {
                System.out.println("Errore nell'accesso all’inventario!");
                return;
            }

            List<Item> allItems = inventory.getAllItems();

            if (allItems.isEmpty()) {
                System.out.println("L'inventario è vuoto.");
                return;
            }

            // Use Strategy Pattern to sort by type
            inventory.setSortStrategy(new SortByTypeStrategy());
            inventory.sort();

            // Group items by type using Java 8 Streams
            Map<Item.ItemType, List<Item>> itemsByType = allItems.stream()
                    .collect(Collectors.groupingBy(Item::getType));

            // Display items grouped by type
            for (Item.ItemType type : Item.ItemType.values()) {
                List<Item> itemsOfType = itemsByType.get(type);

                if (itemsOfType != null && !itemsOfType.isEmpty()) {
                    System.out.println("\n📦 " + type.getDisplayName().toUpperCase() + "S:");

                    for (Item item : itemsOfType) {
                        String bonus = item.getStatBonus() > 0 ? " (+" + item.getStatBonus() + ")" : "";
                        System.out.printf("  • %s - %d oro%s%n",
                                item.getName(), item.getValue(), bonus);
                    }
                }
            }

            System.out.println("\nTotale oggetti: " + allItems.size());
            System.out.println("Valore totale: " + inventory.getTotalValue() + " oro");

            logger.info("Displayed items by type for: " + character.getName());

        } catch (Exception e) {
            logger.warning("Error showing items by type: " + e.getMessage());
            System.out.println("Errore nella visualizzazione per tipologia.");
        }
    }

    /**
     * Get character inventory using reflection (since we can't access it directly)
     * This is a workaround for the simplified character system
     */
    private static Inventory getCharacterInventory(Character character) {
        try {
            if (character instanceof Warrior) {
                java.lang.reflect.Field inventoryField = Warrior.class.getDeclaredField("inventory");
                inventoryField.setAccessible(true);
                return (Inventory) inventoryField.get(character);
            } else if (character instanceof Mage) {
                java.lang.reflect.Field inventoryField = Mage.class.getDeclaredField("inventory");
                inventoryField.setAccessible(true);
                return (Inventory) inventoryField.get(character);
            }
        } catch (Exception e) {
            logger.warning("Error accessing character inventory: " + e.getMessage());
        }

        return null;
    }
}
