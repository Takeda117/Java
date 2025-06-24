package com.exam.project.factory;

import com.exam.project.iterator.Item;

import java.util.Random;

/**
 * Warrior class - Strong melee fighter
 * High health, consistent damage, slower stamina recovery
 */
public class Warrior extends AbstractCharacter {

    private static final Random random = new Random();

    public Warrior(String name) {
        // Warrior stats: high health, normal stamina, good damage
        super(name, 120, 100, 15);
    }

    @Override
    public int attack() {
        if (stamina < 5) {
            System.out.printf("%s is too tired to attack!%n", name);
            return 0;
        }

        stamina -= 5;

        // Warrior: consistent physical attack with small variance
        int damage = baseDamage + random.nextInt(5);  // 15-19 damage
        System.out.printf("%s swings their weapon for %d damage!%n", name, damage);

        return damage;
    }

    @Override
    public void equipItem(Item sword) {

    }

    @Override
    public void addItem(Item sword) {

    }

    @Override
    public void showInventory() {

    }

    @Override
    protected void performTraining() {
        // Warrior training: focus on physical strength
        baseDamage += 2;
        maxHealth += 5;
        health = maxHealth;  // Training also heals

        System.out.printf("%s trains with weapons! Strength increased.%n", name);
    }

    /**
     * Warriors recover stamina slower but more steadily
     * Used by future stamina recovery system
     */
    public double getStaminaRecoveryRate() {
        return 0.05;  // 5% of max stamina per second
    }

    @Override
    public String toString() {
        return String.format("Warrior %s [HP: %d/%d, Stamina: %d/%d, Damage: %d, Money: %d, Level: %d]",
                name, health, maxHealth, stamina, maxStamina, baseDamage, money, level);
    }
}