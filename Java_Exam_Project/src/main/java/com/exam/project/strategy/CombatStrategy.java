package com.exam.project.strategy;

import com.exam.project.factory.Character;

/**
 * Strategy interface for combat calculations
 */
public interface CombatStrategy {

    /**
     * Calculates damage for an attack
     * @param attacker The character performing the attack
     * @return The damage amount
     */
    int calculateDamage(Character attacker);

    /**
     * Gets the stamina cost for this attack type
     * @return The stamina cost
     */
    int getStaminaCost();
}
