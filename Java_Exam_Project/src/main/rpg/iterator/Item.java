package rpg.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Item - Base class for all game items
 * <p>
 * This class represents the fundamental item entity in the game.
 * It implements the Iterable interface to support the Iterator pattern,
 * allowing items to be used in for-each loops and other collection operations.
 * </p>
 * <p>
 * Each item has basic properties like name, type, value, and stat bonus.
 * The class also defines an inner enum for item types and provides
 * a self-iterator implementation.
 * </p>
 */
public class Item implements Iterable<Item> {
    private final String name;
    private final ItemType type;
    private final int value;
    private final int statBonus;

    /**
     * ItemType enumeration - defines different item categories
     * <p>
     * This enum categorizes items and determines whether they can be equipped.
     * Each type has a display name and an equippable flag.
     * </p>
     */
    public enum ItemType {
        WEAPON("Weapon", true),
        ARMOR("Armor", true),
        POTION("Potion", false),
        MISC("Miscellaneous", false);

        private final String displayName;
        private final boolean equippable;

        /**
         * Constructor for ItemType enum
         * 
         * @param displayName The human-readable name of the item type
         * @param equippable Whether items of this type can be equipped
         */
        ItemType(String displayName, boolean equippable) {
            this.displayName = displayName;
            this.equippable = equippable;
        }

        /**
         * Gets the display name of the item type
         * 
         * @return The human-readable name
         */
        public String getDisplayName() {
            return displayName;
        }

        /**
         * Checks if items of this type can be equipped
         * 
         * @return true if equippable, false otherwise
         */
        public boolean isEquippable() {
            return equippable;
        }
    }

    /**
     * Constructor for creating a new item
     * 
     * @param name The name of the item
     * @param type The type of the item
     * @param value The gold value of the item
     * @param statBonus The stat bonus provided by the item
     */
    public Item(String name, ItemType type, int value, int statBonus) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.statBonus = statBonus;
    }

    /**
     * Gets the item's name
     * 
     * @return The name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the item's type
     * 
     * @return The type of the item
     */
    public ItemType getType() {
        return type;
    }

    /**
     * Gets the item's gold value
     * 
     * @return The gold value of the item
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the item's stat bonus
     * 
     * @return The stat bonus provided by the item
     */
    public int getStatBonus() {
        return statBonus;
    }

    /**
     * Checks if the item is equippable
     * 
     * @return true if the item can be equipped, false otherwise
     */
    public boolean isEquippable() {
        return type.isEquippable();
    }

    /**
     * Returns a string representation of the item
     * 
     * @return A formatted string with the item's details
     */
    @Override
    public String toString() {
        String bonus = statBonus > 0 ? String.format(" (+%d)", statBonus) : "";
        return String.format("%s [%s] Value: %d gold%s",
                name, type.getDisplayName(), value, bonus);
    }

    /**
     * Compares this item with another object for equality
     * 
     * @param obj The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Item item = (Item) obj;
        return name.equals(item.name) && type == item.type;
    }

    /**
     * Generates a hash code for the item
     * 
     * @return The hash code value
     */
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
    
    /**
     * Implementation of the Iterable interface
     * <p>
     * Allows iteration over this item (self-iterator pattern).
     * This enables uniform treatment of single items and collections of items.
     * </p>
     * 
     * @return An iterator over this item
     */
    @Override
    public Iterator<Item> iterator() {
        return new ItemIterator(this);
    }
    
    /**
     * Implementation of the Iterator Pattern
     * <p>
     * This private inner class provides an iterator that yields only this item.
     * It's useful for uniformity when dealing with containers of items.
     * </p>
     */
    private class ItemIterator implements Iterator<Item> {
        private final Item item;
        private boolean hasNext = true;
        
        /**
         * Constructor for the item iterator
         * 
         * @param item The item to iterate over
         */
        public ItemIterator(Item item) {
            this.item = item;
        }
        
        /**
         * Checks if there are more items to iterate over
         * 
         * @return true if there are more items, false otherwise
         */
        @Override
        public boolean hasNext() {
            return hasNext;
        }
        
        /**
         * Returns the next item in the iteration
         * 
         * @return The next item
         * @throws NoSuchElementException if there are no more items
         */
        @Override
        public Item next() {
            if (!hasNext) {
                throw new NoSuchElementException("No more items");
            }
            hasNext = false;
            return item;
        }
    }
}
