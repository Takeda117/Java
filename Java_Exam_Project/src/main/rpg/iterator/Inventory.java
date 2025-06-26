package rpg.iterator;

import rpg.strategy.InventorySortStrategy;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Inventory - Manages character items using Collections Framework
 * <p>
 * This class implements the inventory system for game characters,
 * providing storage and management of items. It uses the Java Collections
 * Framework for internal storage and implements the Iterable interface
 * to support the Iterator Pattern.
 * </p>
 * <p>
 * The inventory supports operations like adding, removing, and equipping items,
 * as well as various query methods. It also integrates with the Strategy Pattern
 * for flexible sorting of items.
 * </p>
 */
public class Inventory implements Iterable<Item> {

    // Using ArrayList for the main storage (Collections requirement)
    private final List<Item> items;

    // Using HashMap for equipped items (Collections requirement)
    private final Map<Item.ItemType, Item> equippedItems;

    private final int maxCapacity;

    /**
     * Constructor with configurable capacity
     *
     * @param maxCapacity The maximum number of items the inventory can hold
     */
    public Inventory(int maxCapacity) {
        // We ignore the maxCapacity parameter as there's no longer a limit. It will be implemented in the future.
        this.maxCapacity = Integer.MAX_VALUE; // Set to a very high value
        this.items = new ArrayList<>();
        this.equippedItems = new HashMap<>();
    }



    /**
     * Adds an item to the inventory
     *
     * @param item The item to add
     * @return true if the item was added successfully, false otherwise
     * @throws IllegalArgumentException if the item is null
     */
    public boolean addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item to inventory");
        }

        // Capacity check removed as inventory is no longer limited
        items.add(item);
        System.out.println("Added " + item.getName() + " to inventory");
        return true;
    }

    /**
     * Equips an item (for weapons and armor)
     * 
     * @param item The item to equip
     * @return true if the item was equipped successfully, false otherwise
     */
    public boolean equipItem(Item item) {
        if (item == null || !item.isEquippable()) {
            return false;
        }

        if (!items.contains(item)) {
            System.out.println("Item not in inventory!");
            return false;
        }

        // Unequip current item of same type if any
        Item currentEquipped = equippedItems.get(item.getType());
        if (currentEquipped != null) {
            System.out.println("Unequipping " + currentEquipped.getName());
        }

        equippedItems.put(item.getType(), item);
        System.out.println("Equipped " + item.getName());
        return true;
    }

    /**
     * Checks if an item is currently equipped
     * 
     * @param item The item to check
     * @return true if the item is equipped, false otherwise
     */
    public boolean isEquipped(Item item) {
        return equippedItems.containsValue(item);
    }


    /**
     * Calculates total value of all items
     * 
     * @return The total gold value of all items in the inventory
     */
    public int getTotalValue() {
        return items.stream()
                .mapToInt(Item::getValue)
                .sum();
    }

    /**
     * Gets total stat bonus from equipped items
     * 
     * @return The total stat bonus from all equipped items
     */
    public int getTotalStatBonus() {
        return equippedItems.values().stream()
                .mapToInt(Item::getStatBonus)
                .sum();
    }


    /**
     * Iterator Pattern implementation
     * <p>
     * Allows for-each loops over inventory items.
     * </p>
     * 
     * @return An iterator over the items in the inventory
     */
    @Override
    public Iterator<Item> iterator() {
        return new InventoryIterator();
    }

    /**
     * Custom Iterator implementation
     * <p>
     * This private inner class provides the Iterator Pattern implementation
     * for the inventory, allowing iteration over all items.
     * </p>
     */
    private class InventoryIterator implements Iterator<Item> {
        private int currentIndex = 0;

        /**
         * Checks if there are more items to iterate over
         * 
         * @return true if there are more items, false otherwise
         */
        @Override
        public boolean hasNext() {
            return currentIndex < items.size();
        }

        /**
         * Returns the next item in the iteration
         * 
         * @return The next item
         * @throws NoSuchElementException if there are no more items
         */
        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more items in inventory");
            }
            return items.get(currentIndex++);
        }

        /**
         * Removes the last item returned by the iterator
         * 
         * @throws IllegalStateException if next() has not been called, or remove() has already been called after the last call to next()
         */
        @Override
        public void remove() {
            if (currentIndex <= 0) {
                throw new IllegalStateException("Cannot remove before calling next()");
            }
            items.remove(--currentIndex);
        }
    }

    /**
     * Displays inventory contents
     * <p>
     * Prints a formatted representation of the inventory to the console,
     * including all items grouped by type and their equipped status.
     * </p>
     */
    public void displayInventory() {
        System.out.println("\n=== INVENTORY ===");
        System.out.println("Total items: " + items.size());
        System.out.println("Total value: " + getTotalValue() + " gold");

        if (items.isEmpty()) {
            System.out.println("Inventory is empty");
            return;
        }

        // Group items by type using Collections
        Map<Item.ItemType, List<Item>> itemsByType = items.stream()
                .collect(Collectors.groupingBy(Item::getType));

        for (Item.ItemType type : Item.ItemType.values()) {
            List<Item> typeItems = itemsByType.get(type);
            if (typeItems != null && !typeItems.isEmpty()) {
                System.out.println("\n" + type.getDisplayName() + "s:");
                for (Item item : typeItems) {
                    String equipped = isEquipped(item) ? " [EQUIPPED]" : "";
                    System.out.println("  - " + item + equipped);
                }
            }
        }
    }

    // Strategy Pattern implementation for sorting
    private InventorySortStrategy sortStrategy;

    /**
     * Sets the inventory sorting strategy
     * <p>
     * This method implements the Strategy Pattern for sorting inventory items.
     * </p>
     * 
     * @param sortStrategy The sorting strategy to use
     */
    public void setSortStrategy(InventorySortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    /**
     * Sorts the inventory using the currently set strategy
     * <p>
     * If no strategy is set, this method does nothing.
     * </p>
     */
    public void sort() {
        if (sortStrategy != null) {
            sortStrategy.sort(this.items);
        }
    }

    // Getters for inventory state

    /**
     * Gets the current number of items in the inventory
     * 
     * @return The number of items
     */
    public int getSize() {
        return items.size();
    }


    /**
     * Checks if the inventory is empty
     * 
     * @return true if the inventory is empty, false otherwise
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Gets a read-only view of all items
     * 
     * @return An unmodifiable list of all items in the inventory
     */
    public List<Item> getAllItems() {
        return Collections.unmodifiableList(items);
    }
}
