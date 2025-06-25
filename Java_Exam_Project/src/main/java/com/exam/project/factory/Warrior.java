package com.exam.project.factory;

import com.exam.project.iterator.Item;
import com.exam.project.iterator.Inventory;
import com.exam.project.logger.GameLogger;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Warrior character class with comprehensive crash protection
 */
public class Warrior extends AbstractCharacter {

    private static final Logger logger = GameLogger.getLogger();
    private static final Random random = new Random();
    private final Inventory inventory;

    /**
     * Creates a new Warrior with crash-safe initialization
     * @param name The warrior's name
     */
    public Warrior(String name) {
        super(name, 120, 100, 15);
        try {
            this.inventory = new Inventory(25);
            logger.info("Warrior created successfully: " + (name != null ? name : "Unknown"));
        } catch (Exception e) {
            logger.severe("Failed to initialize Warrior inventory: " + e.getMessage());
            throw new RuntimeException("Critical error creating Warrior", e);
        }
    }

    /**
     * Warrior attack with full error protection
     * @return Damage dealt, 0 if attack failed
     */
    @Override
    public int attack() {
        try {
            if (stamina < 5) {
                String safeName = (name != null) ? name : "Unknown Warrior";
                System.out.printf("%s is too tired to attack!%n", safeName);
                return 0;
            }

            stamina -= 5;

            // Safe damage calculation
            int equipmentBonus = 0;
            if (inventory != null) {
                try {
                    equipmentBonus = inventory.getTotalStatBonus();
                } catch (Exception e) {
                    logger.warning("Error getting equipment bonus: " + e.getMessage());
                    equipmentBonus = 0; // Safe fallback
                }
            }

            int damage = baseDamage + equipmentBonus + random.nextInt(5);
            String safeName = (name != null) ? name : "Warrior";
            System.out.printf("%s attacks for %d damage!%n", safeName, damage);
            return Math.max(1, damage); // Ensure at least 1 damage

        } catch (Exception e) {
            logger.severe("Error in Warrior attack: " + e.getMessage());
            System.out.println("Attack failed due to error!");
            return 0;
        }
    }

    /**
     * Warrior training with error protection
     */
    @Override
    protected void performTraining() {
        try {
            baseDamage += 2;
            maxHealth += 5;
            health = maxHealth;
            String safeName = (name != null) ? name : "Warrior";
            System.out.printf("%s trains with weapons!%n", safeName);
        } catch (Exception e) {
            logger.warning("Error in Warrior training: " + e.getMessage());
            System.out.println("Training completed with some difficulties.");
        }
    }

    /**
     * Equips an item with comprehensive error handling
     * @param item The item to equip
     */
    @Override
    public void equipItem(Item item) {
        try {
            if (item == null) {
                System.out.println("Cannot equip null item!");
                return;
            }

            if (!item.isEquippable()) {
                System.out.println("This item cannot be equipped!");
                return;
            }

            if (inventory == null) {
                System.out.println("Inventory error - cannot equip item!");
                logger.severe("Warrior inventory is null in equipItem");
                return;
            }

            if (!inventory.getAllItems().contains(item)) {
                System.out.println("Item not in inventory!");
                return;
            }

            inventory.equipItem(item);
            String safeName = (name != null) ? name : "Warrior";
            String itemName = (item.getName() != null) ? item.getName() : "Unknown Item";
            System.out.printf("%s equipped %s!%n", safeName, itemName);

        } catch (Exception e) {
            logger.warning("Error equipping item: " + e.getMessage());
            System.out.println("Failed to equip item!");
        }
    }

    /**
     * Adds an item with full error protection
     * @param item The item to add
     */
    @Override
    public void addItem(Item item) {
        try {
            if (item == null) {
                System.out.println("Cannot add null item!");
                return;
            }

            if (inventory == null) {
                System.out.println("Inventory error - cannot add item!");
                logger.severe("Warrior inventory is null in addItem");
                return;
            }

            if (inventory.addItem(item)) {
                String safeName = (name != null) ? name : "Warrior";
                String itemName = (item.getName() != null) ? item.getName() : "Unknown Item";
                System.out.printf("%s added %s!%n", safeName, itemName);
            } else {
                System.out.println("Failed to add item to inventory!");
            }

        } catch (Exception e) {
            logger.warning("Error adding item: " + e.getMessage());
            System.out.println("Failed to add item!");
        }
    }

    /**
     * Shows inventory with error protection
     */
    @Override
    public void showInventory() {
        try {
            String safeName = (name != null) ? name : "Unknown Warrior";
            System.out.printf("\n=== %s's Equipment ===%n", safeName);

            if (inventory == null) {
                System.out.println("Inventory unavailable!");
                logger.warning("Warrior inventory is null in showInventory");
                return;
            }

            inventory.displayInventory();

            int totalBonus = inventory.getTotalStatBonus();
            if (totalBonus > 0) {
                System.out.printf("Equipment bonus: +%d damage%n", totalBonus);
            }

        } catch (Exception e) {
            logger.warning("Error showing inventory: " + e.getMessage());
            System.out.println("Error displaying inventory!");
        }
    }

    /**
     * Gets stamina recovery rate safely
     * @return Recovery rate or default value on error
     */
    public double getStaminaRecoveryRate() {
        try {
            return 0.05;
        } catch (Exception e) {
            logger.warning("Error getting stamina recovery rate: " + e.getMessage());
            return 0.05; // Safe default
        }
    }

    /**
     * Crash-safe toString implementation
     * @return Formatted string with warrior stats
     */
    @Override
    public String toString() {
        try {
            // Safe damage calculation
            int totalDamage = baseDamage;
            if (inventory != null) {
                try {
                    totalDamage += inventory.getTotalStatBonus();
                } catch (Exception e) {
                    logger.warning("Error getting stat bonus in toString: " + e.getMessage());
                    // Continue with base damage only
                }
            }

            // Safe name handling
            String safeName = (name != null) ? name : "Unknown";

            // Safe bounds checking
            int safeHealth = Math.max(0, health);
            int safeMaxHealth = Math.max(1, maxHealth);
            int safeStamina = Math.max(0, stamina);
            int safeMaxStamina = Math.max(1, maxStamina);
            int safeMoney = Math.max(0, money);
            int safeLevel = Math.max(1, level);

            return String.format("Warrior %s [HP: %d/%d, Stamina: %d/%d, Damage: %d, Money: %d, Level: %d]",
                    safeName, safeHealth, safeMaxHealth, safeStamina, safeMaxStamina,
                    totalDamage, safeMoney, safeLevel);

        } catch (Exception e) {
            logger.severe("Critical error in Warrior toString: " + e.getMessage());
            return "Warrior [Error displaying stats]";
        }
    }
}