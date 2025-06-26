package factoryMonster;

import logger.GameLogger;
import java.util.logging.Logger;

/**
 * MonsterFactory - Crea mostri in base al tipo
 */
public class MonsterFactory {

    private static final Logger logger = GameLogger.getLogger();

    /**
     * Crea un mostro in base al tipo
     */
    public AbstractMonster createMonster(String type) {
        if (type == null) {
            logger.warning("Tipo di mostro nullo, creazione fallita");
            return null;
        }

        AbstractMonster monster = switch (type.toLowerCase()) {
            case "goblin" -> {
                logger.info("Creazione Goblin");
                yield new Goblin();
            }
            case "troll" -> {
                logger.info("Creazione Troll");
                yield new Troll();
            }
            default -> {
                logger.warning("Tipo di mostro sconosciuto: " + type + ", creazione Goblin di default");
                yield new Goblin();
            }
        };

        return monster;
    }
}