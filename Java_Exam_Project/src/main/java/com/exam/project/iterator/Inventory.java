package com.exam.project.iterator;

import com.exam.project.strategy.InventorySortStrategy;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Inventory - Manages character items using Collections Framework
 * Implements Iterable to support the Iterator Pattern
 * Uses Generics for type safety
 */
public class Inventory implements Iterable<Item> {

    // Using ArrayList for the main storage (Collections requirement)
    private final List<Item> items;

    // Using HashMap for equipped items (Collections requirement)
    private final Map<Item.ItemType, Item> equippedItems;

    private final int maxCapacity;

    /**
     * Constructor with configurable capacity
     */
    public Inventory(int maxCapacity) {
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("Inventory capacity must be positive");
        }

        this.maxCapacity = maxCapacity;
        this.items = new ArrayList<>();
        this.equippedItems = new HashMap<>();
    }

    /**
     * Default constructor with standard capacity
     */
    public Inventory() {
        this(20); // Default capacity of 20 items
    }

    /**
     * Adds an item to the inventory
     * @return true if item was added, false if inventory is full
     */
    public boolean addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item to inventory");
        }

        if (items.size() >= maxCapacity) {
            System.out.println("Inventory is full! Cannot add " + item.getName());
            return false;
        }

        items.add(item);
        System.out.println("Added " + item.getName() + " to inventory");
        return true;
    }

    /**
     * Removes an item from the inventory
     */
    public boolean removeItem(Item item) {
        if (item == null) {
            return false;
        }

        // If item is equipped, unequip it first
        if (isEquipped(item)) {
            unequipItem(item);
        }

        boolean removed = items.remove(item);
        if (removed) {
            System.out.println("Removed " + item.getName() + " from inventory");
        }
        return removed;
    }

    /**
     * Equips an item (for weapons and armor)
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
     * Unequips an item
     */
    public boolean unequipItem(Item item) {
        if (item == null || !equippedItems.containsValue(item)) {
            return false;
        }

        equippedItems.remove(item.getType());
        System.out.println("Unequipped " + item.getName());
        return true;
    }

    /**
     * Checks if an item is currently equipped
     */
    public boolean isEquipped(Item item) {
        return equippedItems.containsValue(item);
    }

    /**
     * Gets all equipped items
     */
    public Collection<Item> getEquippedItems() {
        return new ArrayList<>(equippedItems.values());
    }

    /**
     * Gets items by type using Java 8 Streams (modern approach)
     */
    public List<Item> getItemsByType(Item.ItemType type) {
        return items.stream()
                .filter(item -> item.getType() == type)
                .collect(Collectors.toList());
    }

    /**
     * Calculates total value of all items
     */
    public int getTotalValue() {
        return items.stream()
                .mapToInt(Item::getValue)
                .sum();
    }

    /**
     * Gets total stat bonus from equipped items
     */
    public int getTotalStatBonus() {
        return equippedItems.values().stream()
                .mapToInt(Item::getStatBonus)
                .sum();
    }

    /**
     * Sells an item (removes it and returns its value)
     */
    public int sellItem(Item item) {
        if (item == null || !items.contains(item)) {
            return 0;
        }

        int value = item.getValue();
        removeItem(item);
        System.out.println("Sold " + item.getName() + " for " + value + " gold");
        return value;
    }

    /**
     * Iterator Pattern implementation
     * Allows for-each loops over inventory items
     */
    @Override
    public Iterator<Item> iterator() {
        return new InventoryIterator();
    }

    /**
     * Custom Iterator implementation
     * This satisfies the Iterator Pattern requirement
     */
    private class InventoryIterator implements Iterator<Item> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < items.size();
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more items in inventory");
            }
            return items.get(currentIndex++);
        }

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
     */
    public void displayInventory() {
        System.out.println("\n=== INVENTORY ===");
        System.out.println("Capacity: " + items.size() + "/" + maxCapacity);
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

    private InventorySortStrategy sortStrategy;

    /**
     * Sets the inventory sorting strategy.
     *
     * @param sortStrategy the sorting strategy to use
     */
    public void setSortStrategy(InventorySortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    /**
     * Sorts the inventory using the currently set strategy.
     */
    public void sort() {
        if (sortStrategy != null) {
            sortStrategy.sort(this.items); // 'items' is your List<Item>
        }
    }


    // Getters for inventory state
    public int getSize() {
        return items.size();
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public boolean isFull() {
        return items.size() >= maxCapacity;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Gets a read-only view of all items
     */
    public List<Item> getAllItems() {
        return Collections.unmodifiableList(items);
    }
}