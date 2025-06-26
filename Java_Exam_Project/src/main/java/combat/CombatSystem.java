package java.combat;

import java.factory.Character;
import java.factoryMonster.AbstractMonster;
import java.logger.GameLogger;
import java.util.logging.Logger;

/**
 * CombatSystem - Implements a simplified combat system
 * <p>
 * This class handles combat interactions between characters and monsters,
 * including attack execution and damage calculation.
 * </p>
 */
public class CombatSystem {
    
    private static final Logger logger = GameLogger.getLogger();
    
    /**
     * Executes an attack from a character against a monster
     * <p>
     * This method handles the character's attack, calculates damage,
     * and applies it to the monster if the attack is successful.
     * </p>
     * 
     * @param character The character performing the attack
     * @param monster The monster being attacked
     */
    public void executeAttack(Character character, AbstractMonster monster) {
        if (character == null || monster == null) {
            logger.warning("Attack failed: character or monster is null");
            return;
        }
        
        int damage = character.attack();
        if (damage > 0) {
            monster.takeDamage(damage);
            logger.info(character.getName() + " dealt " + damage + " damage to " + monster.getType());
            System.out.println("You dealt " + damage + " damage!");
        } else {
            logger.info(character.getName() + " missed the target");
            System.out.println("You missed the target!");
        }

    }
    
    /**
     * Executes an attack from a monster against a character
     * <p>
     * This method handles the monster's attack, calculates damage,
     * and applies it to the character if the attack is successful.
     * </p>
     * 
     * @param monster The monster performing the attack
     * @param character The character being attacked
     */
    public void executeMonsterAttack(AbstractMonster monster, Character character) {
        if (monster == null || character == null) {
            logger.warning("Monster attack failed: monster or character is null");
            return;
        }
        
        int damage = monster.attack();
        if (damage > 0) {
            character.takeDamage(damage);
            logger.info(monster.getType() + " dealt " + damage + " damage to " + character.getName());
            System.out.println(monster.getType() + " dealt " + damage + " damage to you!");
        } else {
            logger.info(monster.getType() + " missed the attack");
            System.out.println(monster.getType() + " missed the attack!");
        }

    }
}