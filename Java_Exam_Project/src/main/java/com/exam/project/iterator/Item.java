package com.exam.project.iterator;

/**
 * Item - Base class for all game items
 * Part of the inventory system using Collections and Generics
 */
public class Item {
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
     * Constructor for creating items
     */
    public Item(String name, ItemType type, int value, int statBonus) {
        // Input validation
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Item type cannot be null");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Item value cannot be negative");
        }

        this.name = name.trim();
        this.type = type;
        this.value = value;
        this.statBonus = statBonus;
    }

    // Getters
    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public int getStatBonus() {
        return statBonus;
    }

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
}
