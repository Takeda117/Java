package java.factoryMonster;

import java.logger.GameLogger;
import java.rpgSecurity.ExceptionHandler;
import java.util.logging.Logger;

/**
 * MonsterFactory - Creates monsters based on type
 * <p>
 * This class implements the Factory Method Pattern for monsters,
 * providing a centralized way to create different monster types.
 * It encapsulates the instantiation logic and provides a common
 * interface for creating various monster types.
 * </p>
 */
public class MonsterFactory {

    private static final Logger logger = GameLogger.getLogger();

    /**
     * Creates a monster based on the specified type
     * 
     * @param type The type of monster to create
     * @return A new AbstractMonster instance of the specified type, or null if creation fails
     */
    public AbstractMonster createMonster(String type) {
        if (type == null) {
            logger.warning("Null monster type, creation failed");
            return null;
        }

        try {
            AbstractMonster monster = switch (type.toLowerCase()) {
                case "goblin" -> {
                    logger.info("Creating Goblin");
                    yield new Goblin();
                }
                case "troll" -> {
                    logger.info("Creating Troll");
                    yield new Troll();
                }
                default -> {
                    logger.warning("Unknown monster type: " + type + ", creating default Goblin");
                    yield new Goblin();
                }
            };

            return monster;
        } catch (Exception e) {
            logger.severe("Error creating monster: " + e.getMessage());
            ExceptionHandler.handleException(e, "Error creating monster.");
            return null;
        }
    }
}