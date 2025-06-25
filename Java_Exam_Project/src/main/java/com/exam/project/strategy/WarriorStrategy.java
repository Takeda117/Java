package com.exam.project.strategy;

import com.exam.project.factory.Character;
import java.util.Random;

/**
 * Combat strategy implementation for Warrior characters
 */
public class WarriorStrategy implements CombatStrategy {

    private Random random = new Random();

    /**
     * Calculates damage for warrior attacks with error protection
     * @param attacker The warrior character
     * @return Calculated damage amount, minimum 1
     */
    @Override
    public int calculateDamage(Character attacker) {
        try {
            if (attacker == null) {
                return 1; // Safe minimum damage
            }

            int baseDamage = attacker.getBaseDamage();
            int variation = random.nextInt(3);
            int totalDamage = baseDamage + variation + 3;

            return Math.max(1, totalDamage); // Ensure minimum damage

        } catch (Exception e) {
            return 1; // Safe fallback
        }
    }

    /**
     * Gets stamina cost for warrior attacks
     * @return Stamina cost (5 points)
     */
    @Override
    public int getStaminaCost() {
        return 5;
    }
}