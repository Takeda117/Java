package com.exam.project.factory;

import java.util.Random;

/**
 * Mage class - Magical spellcaster
 * Lower health, variable damage with mana, faster stamina recovery
 */
public class Mage extends AbstractCharacter {

  private static final Random random = new Random();

  // Mage-specific stats
  private int mana;
  private int maxMana;

  public Mage(String name) {
    // Mage stats: lower health, higher stamina, lower base damage
    super(name, 80, 120, 10);
    this.mana = 50;
    this.maxMana = 50;
  }

  @Override
  public int attack() {
    if (stamina < 3) {
      System.out.printf("%s is too tired to cast spells!%n", name);
      return 0;
    }

    stamina -= 3;  // Mages use less stamina for attacks

    // Mage: variable damage based on mana availability
    if (mana >= 10) {
      // Powerful spell attack
      mana -= 10;
      int magicDamage = baseDamage + 5 + random.nextInt(10);  // 15-24 damage
      System.out.printf("%s casts a spell for %d magic damage! Mana: %d/%d%n",
              name, magicDamage, mana, maxMana);
      return magicDamage;
    } else {
      // Basic staff attack when low on mana
      int basicDamage = baseDamage + random.nextInt(3);  // 10-12 damage
      System.out.printf("%s attacks with their staff for %d damage! (Low mana)%n",
              name, basicDamage);
      return basicDamage;
    }
  }

  @Override
  protected void performTraining() {
    // Mage training: focus on magical power
    baseDamage += 1;  // Less physical improvement
    maxMana += 10;    // More magical capacity
    mana = maxMana;   // Training restores mana

    System.out.printf("%s studies ancient spells! Magic power increased.%n", name);
  }

  @Override
  public void rest() {
    super.rest();  // Call parent rest method for health and stamina
    this.mana = maxMana;  // Also restore mana
    System.out.printf("%s meditates and restores mana!%n", name);
  }

  /**
   * Mages recover stamina faster due to their magical nature
   * Used by future stamina recovery system
   */
  public double getStaminaRecoveryRate() {
    return 0.10;  // 10% of max stamina per second (faster than Warrior)
  }

  // Mage-specific getters
  public int getMana() {
    return mana;
  }

  public int getMaxMana() {
    return maxMana;
  }

  @Override
  public String toString() {
    return String.format("Mage %s [HP: %d/%d, Stamina: %d/%d, Mana: %d/%d, Damage: %d, Money: %d, Level: %d]",
            name, health, maxHealth, stamina, maxStamina, mana, maxMana, baseDamage, money, level);
  }
}