package com.exam.project.builder;

import com.exam.project.logger.GameLogger;
import java.util.logging.Logger;

/**
 * ConcreteDungeonBuilder - Implementazione del Builder Pattern
 *
 * Costruisce Dungeon step-by-step con validazione
 */
public class ConcreteDungeonBuilder implements DungeonBuilder {

    private static final Logger logger = GameLogger.getLogger();

    // Parametri di costruzione
    private String name;
    private String description = "";
    private int goldReward = 50;
    private String monsterType = "goblin";

    /**
     * Costruttore
     */
    public ConcreteDungeonBuilder() {
        logger.info("DungeonBuilder created");
    }

    @Override
    public DungeonBuilder setName(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                logger.warning("Invalid dungeon name provided");
                this.name = "Unknown Dungeon";
            } else {
                this.name = name.trim();
            }
            return this;
        } catch (Exception e) {
            logger.warning("Error setting name: " + e.getMessage());
            this.name = "Unknown Dungeon";
            return this;
        }
    }

    @Override
    public DungeonBuilder setDescription(String description) {
        try {
            this.description = (description != null) ? description.trim() : "";
            return this;
        } catch (Exception e) {
            logger.warning("Error setting description: " + e.getMessage());
            this.description = "";
            return this;
        }
    }

    @Override
    public DungeonBuilder setGoldReward(int gold) {
        try {
            this.goldReward = Math.max(0, gold);
            return this;
        } catch (Exception e) {
            logger.warning("Error setting gold reward: " + e.getMessage());
            this.goldReward = 50;
            return this;
        }
    }

    @Override
    public DungeonBuilder setMonsterType(String monsterType) {
        try {
            if (monsterType == null || monsterType.trim().isEmpty()) {
                logger.warning("Invalid monster type, using default");
                this.monsterType = "goblin";
            } else {
                String cleanType = monsterType.trim().toLowerCase();
                // Validate goblin, troll, and skeleton
                if (cleanType.equals("goblin") || cleanType.equals("troll") || cleanType.equals("skeleton")) {
                    this.monsterType = cleanType;
                } else {
                    logger.warning("Unknown monster type: " + monsterType + ", using goblin");
                    this.monsterType = "goblin";
                }
            }
            return this;
        } catch (Exception e) {
            logger.warning("Error setting monster type: " + e.getMessage());
            this.monsterType = "goblin";
            return this;
        }
    }

    @Override
    public Dungeon build() {
        try {
            // Validazione finale
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalStateException("Dungeon name is required");
            }

            // Crea il Dungeon
            Dungeon dungeon = new Dungeon(name, description, goldReward, monsterType);

            logger.info("Dungeon built successfully: " + name);
            return dungeon;

        } catch (Exception e) {
            logger.severe("Error building Dungeon: " + e.getMessage());
            throw new RuntimeException("Failed to build Dungeon", e);
        }
    }

    @Override
    public DungeonBuilder reset() {
        try {
            this.name = null;
            this.description = "";
            this.goldReward = 50;
            this.monsterType = "goblin";

            logger.info("DungeonBuilder reset");
            return this;
        } catch (Exception e) {
            logger.warning("Error resetting builder: " + e.getMessage());
            return this;
        }
    }
}
