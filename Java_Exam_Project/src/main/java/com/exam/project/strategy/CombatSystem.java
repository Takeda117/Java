package com.exam.project.strategy;

import com.exam.project.factory.Character;
import com.exam.project.factory.Warrior;
import com.exam.project.factory.Mage;
import com.exam.project.factoryMonster.AbstractMonster;
import com.exam.project.security.ExceptionHandler;
import com.exam.project.logger.GameLogger;

import java.util.logging.Logger;

/**
 * Manages combat operations using Strategy pattern with crash protection
 */
public class CombatSystem {

    private static final Logger logger = GameLogger.getLogger();
    private final WarriorStrategy warriorStrategy;
    private final MageStrategy mageStrategy;

    /**
     * Creates combat system with strategy instances
     */
    public CombatSystem() {
        try {
            this.warriorStrategy = new WarriorStrategy();
            this.mageStrategy = new MageStrategy();
            logger.info("CombatSystem initialized successfully");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize CombatSystem", e);
        }
    }

    /**
     * Executes an attack from character to monster with full error protection
     * @param attacker The attacking character
     * @param target The target monster
     * @return Damage dealt, 0 if attack failed
     */
    public int executeAttack(Character attacker, AbstractMonster target) {
        try {
            // Input validation
            if (attacker == null) {
                System.out.println("Error: No attacker provided!");
                logger.warning("executeAttack called with null attacker");
                return 0;
            }

            if (target == null) {
                System.out.println("Error: No target provided!");
                logger.warning("executeAttack called with null target");
                return 0;
            }

            // State validation
            if (!attacker.isAlive()) {
                System.out.println(attacker.getName() + " is defeated and cannot attack!");
                return 0;
            }

            if (!target.isAlive()) {
                System.out.println("Target is already defeated!");
                return 0;
            }

            // Get strategy safely
            CombatStrategy strategy = getStrategySafely(attacker);
            if (strategy == null) {
                System.out.println("Combat error: No strategy available!");
                return 0;
            }

            // Check stamina safely
            if (attacker.getStamina() < strategy.getStaminaCost()) {
                System.out.println(attacker.getName() + " is too tired to attack!");
                return 0;
            }

            // Execute attack with error handling
            int damage = strategy.calculateDamage(attacker);
            attacker.restoreStamina(-strategy.getStaminaCost());
            target.takeDamage(damage);

            // Log successful attack
            logger.info(String.format("Attack: %s -> %s, damage: %d",
                    attacker.getName(), target.getName(), damage));

            return damage;

        } catch (Exception e) {
            ExceptionHandler.handleException(e, "Combat attack failed");
            return 0;
        }
    }

    /**
     * Gets combat strategy with null safety
     * @param character The character to get strategy for
     * @return The combat strategy or null if error
     */
    private CombatStrategy getStrategySafely(Character character) {
        try {
            if (character == null) {
                return null;
            }

            if (character instanceof Warrior) {
                return warriorStrategy;
            } else if (character instanceof Mage) {
                return mageStrategy;
            } else {
                logger.warning("Unknown character type: " + character.getClass().getSimpleName());
                return warriorStrategy; // Safe fallback
            }

        } catch (Exception e) {
            logger.severe("Error getting strategy for character: " + e.getMessage());
            return warriorStrategy; // Safe fallback
        }
    }

    /**
     * Executes monster attack with full error protection
     * @param attacker The attacking monster
     * @param target The target character
     * @return Damage dealt, 0 if attack failed
     */
    public int executeMonsterAttack(AbstractMonster attacker, Character target) {
        try {
            // Input validation
            if (attacker == null) {
                System.out.println("Error: No monster attacker provided!");
                return 0;
            }

            if (target == null) {
                System.out.println("Error: No target character provided!");
                return 0;
            }

            // State validation
            if (!attacker.isAlive()) {
                return 0; // Silent fail for dead monsters
            }

            if (!target.isAlive()) {
                return 0; // Silent fail for dead targets
            }

            // Execute monster attack
            int damage = attacker.attack();

            // Log monster attack
            logger.info(String.format("Monster attack: %s -> %s, damage: %d",
                    attacker.getName(), target.getName(), damage));

            return damage;

        } catch (Exception e) {
            ExceptionHandler.handleException(e, "Monster attack failed");
            return 0;
        }
    }

    /**
     * Validates if a character can participate in combat
     * @param character The character to validate
     * @return true if character can fight, false otherwise
     */
    public boolean canFight(Character character) {
        try {
            return character != null && character.isAlive() && character.getStamina() > 0;
        } catch (Exception e) {
            logger.warning("Error checking if character can fight: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validates if a monster can participate in combat
     * @param monster The monster to validate
     * @return true if monster can fight, false otherwise
     */
    public boolean canFight(AbstractMonster monster) {
        try {
            return monster != null && monster.isAlive();
        } catch (Exception e) {
            logger.warning("Error checking if monster can fight: " + e.getMessage());
            return false;
        }
    }
}