
package com.exam.project.builder;

import com.exam.project.logger.GameLogger;
import java.util.logging.Logger;

/**
 * Dungeon - Oggetto costruito dal Builder Pattern
 *
 * Versione semplificata: 1 stanza, 1 tipo di mostro
 */
public class Dungeon {

    private static final Logger logger = GameLogger.getLogger();

    // Attributi immutabili
    private final String name;
    private final String description;
    private final int goldReward;
    private final String monsterType;

    /**
     * Costruttore package-private - solo il builder può creare Dungeon
     */
    Dungeon(String name, String description, int goldReward, String monsterType) {
        try {
            this.name = (name != null) ? name : "Unknown Dungeon";
            this.description = (description != null) ? description : "";
            this.goldReward = Math.max(0, goldReward);
            this.monsterType = (monsterType != null) ? monsterType : "goblin";

            logger.info("Dungeon created: " + this.name);
        } catch (Exception e) {
            logger.severe("Error creating Dungeon: " + e.getMessage());
            throw new RuntimeException("Failed to create Dungeon", e);
        }
    }

    // Getters pubblici
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getGoldReward() {
        return goldReward;
    }

    public String getMonsterType() {
        return monsterType;
    }

    // Metodi per compatibilità con codice esistente
    public int getNumberOfRooms() {
        return 1; // Sempre 1 stanza nella versione semplificata
    }

    public int getExperienceReward() {
        return goldReward / 2; // Exp = metà dell'oro
    }

    public java.util.List<String> getMonsterTypes() {
        return java.util.Collections.singletonList(monsterType);
    }

    public int getMonstersPerRoom() {
        return 1; // Sempre 1 mostro
    }

    public int getBaseDifficulty() {
        return monsterType.equals("troll") ? 2 : 1;
    }

    /**
     * Descrizione completa per compatibilità
     */
    public String getFullDescription() {
        try {
            StringBuilder desc = new StringBuilder();
            desc.append(String.format("=== %s ===%n", getName().toUpperCase()));

            if (!description.isEmpty()) {
                desc.append(description).append("\n\n");
            }

            desc.append("Stanze: 1\n");
            desc.append(String.format("Ricompense: %d oro, %d esperienza%n",
                    getGoldReward(), getExperienceReward()));
            desc.append("Nemico: ").append(monsterType);

            return desc.toString();
        } catch (Exception e) {
            logger.severe("Error creating description: " + e.getMessage());
            return "Error: Cannot display dungeon information";
        }
    }

    @Override
    public String toString() {
        try {
            return String.format("%s (1 stanza) - %d oro, %s",
                    getName(), getGoldReward(), monsterType);
        } catch (Exception e) {
            return "Dungeon [Error]";
        }
    }
}