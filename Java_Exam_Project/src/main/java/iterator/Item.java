package iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Item - Base class for all game items
 * Part of the inventory system using Collections and Generics
 */
public class Item implements Iterable<Item> {
    private final String name;
    private final ItemType type;
    private final int value;
    private final int statBonus;

    /**
     * ItemType enumeration - defines different item categories
     */
    public enum ItemType {
        WEAPON("Weapon", true),
        ARMOR("Armor", true),
        POTION("Potion", false),
        MISC("Miscellaneous", false);

        private final String displayName;
        private final boolean equippable;

        ItemType(String displayName, boolean equippable) {
            this.displayName = displayName;
            this.equippable = equippable;
        }

        public String getDisplayName() {
            return displayName;
        }

        public boolean isEquippable() {
            return equippable;
        }
    }

    /**
     * Constructor for creating a new item
     */
    public Item(String name, ItemType type, int value, int statBonus) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.statBonus = statBonus;
    }

    /**
     * Get the item's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the item's type
     */
    public ItemType getType() {
        return type;
    }

    /**
     * Get the item's gold value
     */
    public int getValue() {
        return value;
    }

    /**
     * Get the item's stat bonus
     */
    public int getStatBonus() {
        return statBonus;
    }

    /**
     * Check if the item is equippable
     */
    public boolean isEquippable() {
        return type.isEquippable();
    }

    @Override
    public String toString() {
        String bonus = statBonus > 0 ? String.format(" (+%d)", statBonus) : "";
        return String.format("%s [%s] Value: %d gold%s",
                name, type.getDisplayName(), value, bonus);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Item item = (Item) obj;
        return name.equals(item.name) && type == item.type;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
    
    /**
     * Implementazione dell'interfaccia Iterable
     * Permette di iterare su questo item (self-iterator)
     */
    @Override
    public Iterator<Item> iterator() {
        return new ItemIterator(this);
    }
    
    /**
     * Implementazione dell'Iterator Pattern
     * Questo iteratore permette di iterare su un singolo item
     * (utile per uniformità con container di item)
     */
    private class ItemIterator implements Iterator<Item> {
        private final Item item;
        private boolean hasNext = true;
        
        public ItemIterator(Item item) {
            this.item = item;
        }
        
        @Override
        public boolean hasNext() {
            return hasNext;
        }
        
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
