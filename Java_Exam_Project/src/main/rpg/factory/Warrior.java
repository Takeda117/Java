package rpg.factory;

import rpg.iterator.Item;
import rpg.iterator.Inventory;
import java.util.Random;

/**
 * Warrior character class
 * <p>
 * This class represents a warrior character type in the game.
 * It extends AbstractCharacter and implements warrior-specific
 * behaviors. Warriors have high health and physical damage.
 * </p>
 */
public class Warrior extends AbstractCharacter {

    private static final Random random = new Random();
    private final Inventory inventory;

    /**
     * Creates a new Warrior
     * 
     * @param name The warrior's name
     */
    public Warrior(String name) {
        super(name, 120, 100, 15);
        this.inventory = new Inventory(25);
    }

    /**
     * Performs a warrior attack
     * 
     * @return Damage dealt by the attack, or 0 if the attack fails
     */
    @Override
    public int attack() {
        if (stamina < 5) {
            System.out.printf("%s is too tired to attack!%n", name);
            return 0;
        }

        stamina -= 5;

        int equipmentBonus = inventory != null ? inventory.getTotalStatBonus() : 0;
        int damage = baseDamage + equipmentBonus + random.nextInt(5);
        
        System.out.printf("%s attacks for %d damage!%n", name, damage);
        return Math.max(1, damage);
    }

    /**
     * Implements warrior-specific training
     */
    @Override
    protected void performTraining() {
        baseDamage += 2;
        maxHealth += 5;
        health = maxHealth;
        System.out.printf("%s trains with weapons!%n", name);
    }

    /**
     * Equips an item to the warrior
     * 
     * @param item The item to equip
     */
    @Override
    public void equipItem(Item item) {
        if (item == null || inventory == null || !item.isEquippable()) {
            return;
        }

        if (!inventory.getAllItems().contains(item)) {
            System.out.println("Item not in inventory!");
            return;
        }

        inventory.equipItem(item);
        System.out.printf("%s equipped %s!%n", name, item.getName());
    }

    /**
     * Adds an item to the warrior's inventory
     * 
     * @param item The item to add
     */
    @Override
    public void addItem(Item item) {
        if (item == null || inventory == null) {
            return;
        }

        if (inventory.addItem(item)) {
            System.out.printf("%s added %s!%n", name, item.getName());
        }
    }

    /**
     * Shows the warrior's inventory
     */
    @Override
    public void showInventory() {
        if (inventory == null) {
            return;
        }

        System.out.printf("\n=== %s's Equipment ===%n", name);
        inventory.displayInventory();

        int totalBonus = inventory.getTotalStatBonus();
        if (totalBonus > 0) {
            System.out.printf("Equipment bonus: +%d damage%n", totalBonus);
        }
    }

    /**
     * Gets the warrior's stamina recovery rate
     * 
     * @return The stamina recovery rate as a decimal
     */
    public double getStaminaRecoveryRate() {
        return 0.05;
    }

    /**
     * Returns a string representation of the warrior
     * 
     * @return A formatted string with the warrior's stats
     */
    @Override
    public String toString() {
        int totalDamage = baseDamage;
        if (inventory != null) {
            totalDamage += inventory.getTotalStatBonus();
        }

        return String.format("Warrior %s [HP: %d/%d, Stamina: %d/%d, Damage: %d, Money: %d, Level: %d]",
                name, health, maxHealth, stamina, maxStamina, totalDamage, money, level);
    }
}
