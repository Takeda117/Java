package com.exam.project.factory;

import com.exam.project.iterator.Item;
import com.exam.project.iterator.Inventory;
import com.exam.project.logger.GameLogger;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Mage character class with comprehensive crash protection
 */
public class Mage extends AbstractCharacter {

  private static final Logger logger = GameLogger.getLogger();
  private static final Random random = new Random();
  private int mana;
  private int maxMana;
  private final Inventory inventory;

  /**
   * Creates a new Mage with crash-safe initialization
   * @param name The mage's name
   */
  public Mage(String name) {
    super(name, 80, 120, 10);
    try {
      this.mana = 50;
      this.maxMana = 50;
      this.inventory = new Inventory(20);
      logger.info("Mage created successfully: " + (name != null ? name : "Unknown"));
    } catch (Exception e) {
      logger.severe("Failed to initialize Mage: " + e.getMessage());
      throw new RuntimeException("Critical error creating Mage", e);
    }
  }

  /**
   * Mage attack with full error protection
   * @return Damage dealt, 0 if attack failed
   */
  @Override
  public int attack() {
    try {
      if (stamina < 3) {
        String safeName = (name != null) ? name : "Unknown Mage";
        System.out.printf("%s is too tired!%n", safeName);
        return 0;
      }

      stamina -= 3;

      // Safe damage calculation
      int equipmentBonus = 0;
      if (inventory != null) {
        try {
          equipmentBonus = inventory.getTotalStatBonus();
        } catch (Exception e) {
          logger.warning("Error getting equipment bonus: " + e.getMessage());
          equipmentBonus = 0;
        }
      }

      int baseAttack = baseDamage + equipmentBonus;
      String safeName = (name != null) ? name : "Mage";

      // Mana-based attack logic with safe bounds
      int safeMana = Math.max(0, mana);
      if (safeMana >= 10) {
        mana = Math.max(0, mana - 10);
        int magicDamage = baseAttack + 5 + random.nextInt(10);
        System.out.printf("%s casts a spell for %d damage! Mana: %d/%d%n",
                safeName, magicDamage, Math.max(0, mana), Math.max(1, maxMana));
        return Math.max(1, magicDamage);
      } else {
        int staffDamage = baseAttack + random.nextInt(3);
        System.out.printf("%s attacks with staff for %d damage!%n", safeName, staffDamage);
        return Math.max(1, staffDamage);
      }

    } catch (Exception e) {
      logger.severe("Error in Mage attack: " + e.getMessage());
      System.out.println("Attack failed due to error!");
      return 0;
    }
  }

  /**
   * Mage training with error protection
   */
  @Override
  protected void performTraining() {
    try {
      baseDamage += 1;
      maxMana += 10;
      mana = maxMana;
      maxStamina += 5;
      String safeName = (name != null) ? name : "Mage";
      System.out.printf("%s studies magic!%n", safeName);
    } catch (Exception e) {
      logger.warning("Error in Mage training: " + e.getMessage());
      System.out.println("Training completed with some difficulties.");
    }
  }

  /**
   * Mage rest with error protection
   */
  @Override
  public void rest() {
    try {
      super.rest();
      this.mana = maxMana;
      String safeName = (name != null) ? name : "Mage";
      System.out.printf("%s meditates and restores mana!%n", safeName);
    } catch (Exception e) {
      logger.warning("Error in Mage rest: " + e.getMessage());
      System.out.println("Rest completed with some difficulties.");
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
        logger.severe("Mage inventory is null in equipItem");
        return;
      }

      if (!inventory.getAllItems().contains(item)) {
        System.out.println("Item not in inventory!");
        return;
      }

      inventory.equipItem(item);
      String safeName = (name != null) ? name : "Mage";
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
        logger.severe("Mage inventory is null in addItem");
        return;
      }

      if (inventory.addItem(item)) {
        String safeName = (name != null) ? name : "Mage";
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
      String safeName = (name != null) ? name : "Unknown Mage";
      System.out.printf("\n=== %s's Equipment ===%n", safeName);

      if (inventory == null) {
        System.out.println("Inventory unavailable!");
        logger.warning("Mage inventory is null in showInventory");
        return;
      }

      inventory.displayInventory();

      int totalBonus = inventory.getTotalStatBonus();
      if (totalBonus > 0) {
        System.out.printf("Magic bonus: +%d power%n", totalBonus);
      }
      System.out.printf("Mana: %d/%d%n", Math.max(0, mana), Math.max(1, maxMana));

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
      return 0.10;
    } catch (Exception e) {
      logger.warning("Error getting stamina recovery rate: " + e.getMessage());
      return 0.10;
    }
  }

  /**
   * Gets current mana safely
   * @return Current mana amount
   */
  public int getMana() {
    try {
      return Math.max(0, mana);
    } catch (Exception e) {
      logger.warning("Error getting mana: " + e.getMessage());
      return 0;
    }
  }

  /**
   * Gets maximum mana safely
   * @return Maximum mana amount
   */
  public int getMaxMana() {
    try {
      return Math.max(1, maxMana);
    } catch (Exception e) {
      logger.warning("Error getting max mana: " + e.getMessage());
      return 1;
    }
  }

  /**
   * Crash-safe toString implementation
   * @return Formatted string with mage stats
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

      // Safe value handling
      String safeName = (name != null) ? name : "Unknown";
      int safeHealth = Math.max(0, health);
      int safeMaxHealth = Math.max(1, maxHealth);
      int safeStamina = Math.max(0, stamina);
      int safeMaxStamina = Math.max(1, maxStamina);
      int safeMana = Math.max(0, mana);
      int safeMaxMana = Math.max(1, maxMana);
      int safeMoney = Math.max(0, money);
      int safeLevel = Math.max(1, level);

      return String.format("Mage %s [HP: %d/%d, Stamina: %d/%d, Mana: %d/%d, Power: %d, Money: %d, Level: %d]",
              safeName, safeHealth, safeMaxHealth, safeStamina, safeMaxStamina,
              safeMana, safeMaxMana, totalDamage, safeMoney, safeLevel);

    } catch (Exception e) {
      logger.severe("Critical error in Mage toString: " + e.getMessage());
      return "Mage [Error displaying stats]";
    }
  }
}