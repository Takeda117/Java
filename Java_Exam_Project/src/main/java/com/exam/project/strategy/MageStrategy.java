package com.exam.project.strategy;

import com.exam.project.factory.Character;
import com.exam.project.factory.Mage;
import java.util.Random;

/**
 * Combat strategy implementation for Mage characters
 */
public class MageStrategy implements CombatStrategy {

    private Random random = new Random();

    /**
     * Calculates damage for mage attacks with error protection
     * @param attacker The mage character
     * @return Calculated damage amount, minimum 1
     */
    @Override
    public int calculateDamage(Character attacker) {
        try {
            if (attacker == null) {
                return 1; // Safe minimum damage
            }

            int baseDamage = attacker.getBaseDamage();

            if (attacker instanceof Mage) {
                Mage mage = (Mage) attacker;
                int currentMana = mage.getMana(); // This method is now safe

                if (currentMana >= 10) {
                    int spellDamage = baseDamage + random.nextInt(8) + 2;
                    return Math.max(1, spellDamage);
                } else {
                    int staffDamage = baseDamage + random.nextInt(2);
                    return Math.max(1, staffDamage);
                }
            }

            // Fallback for non-mage characters
            int damage = baseDamage + random.nextInt(3);
            return Math.max(1, damage);

        } catch (Exception e) {
            return 1; // Safe fallback
        }
    }

    /**
     * Gets stamina cost for mage attacks
     * @return Stamina cost (3 points)
     */
    @Override
    public int getStaminaCost() {
        return 3;
    }
}