package combat;

import factory.Character;
import factoryMonster.AbstractMonster;
import logger.GameLogger;
import java.util.logging.Logger;

/**
 * Sistema di combattimento semplificato
 */
public class CombatSystem {
    
    private static final Logger logger = GameLogger.getLogger();
    
    /**
     * Esegue un attacco del personaggio contro un mostro
     */
    public void executeAttack(Character character, AbstractMonster monster) {
        if (character == null || monster == null) {
            logger.warning("Attacco fallito: personaggio o mostro nullo");
            return;
        }
        
        int damage = character.attack();
        if (damage > 0) {
            monster.takeDamage(damage);
            logger.info(character.getName() + " ha inflitto " + damage + " danni a " + monster.getType());
            System.out.println("Hai inflitto " + damage + " danni!");
        } else {
            logger.info(character.getName() + " ha mancato il bersaglio");
            System.out.println("Hai mancato il bersaglio!");
        }

    }
    
    /**
     * Esegue un attacco del mostro contro un personaggio
     */
    public void executeMonsterAttack(AbstractMonster monster, Character character) {
        if (monster == null || character == null) {
            logger.warning("Attacco mostro fallito: mostro o personaggio nullo");
            return;
        }
        
        int damage = monster.attack();
        if (damage > 0) {
            character.takeDamage(damage);
            logger.info(monster.getType() + " ha inflitto " + damage + " danni a " + character.getName());
            System.out.println(monster.getType() + " ti ha inflitto " + damage + " danni!");
        } else {
            logger.info(monster.getType() + " ha mancato il colpo");
            System.out.println(monster.getType() + " ha mancato il colpo!");
        }

    }
}