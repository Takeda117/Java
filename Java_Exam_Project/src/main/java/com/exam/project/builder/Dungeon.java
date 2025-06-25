
package com.exam.project.builder;

import com.exam.project.logger.GameLogger;
import com.exam.project.factoryMonster.AbstractMonster;
import com.exam.project.factoryMonster.MonsterFactory;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * Dungeon - Oggetto costruito dal Builder Pattern
 *
 * Versione semplificata: 1 stanza, 1 tipo di mostro
 */
public class Dungeon {

    private static final Logger logger = GameLogger.getLogger();
    private static final MonsterFactory monsterFactory = new MonsterFactory();

    // Attributi immutabili
    private final String name;
    private final String description;
    private final int goldReward;
    private final String monsterType;
    private final int numberOfRooms;

    /**
     * Costruttore package-private - solo il builder può creare Dungeon
     */
    Dungeon(String name, String description, int goldReward, String monsterType) {
        try {
            this.name = (name != null) ? name : "Unknown Dungeon";
            this.description = (description != null) ? description : "";
            this.goldReward = Math.max(0, goldReward);
            this.monsterType = (monsterType != null) ? monsterType : "goblin";
            
            // Determina il numero di stanze in base al tipo
            this.numberOfRooms = this.monsterType.equals("troll") ? 3 : 2;

            logger.info("Dungeon created: " + this.name + " with " + this.numberOfRooms + " rooms");
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
        return numberOfRooms;
    }

    public int getExperienceReward() {
        return goldReward / 2; // Exp = metà dell'oro
    }

    public java.util.List<String> getMonsterTypes() {
        return java.util.Collections.singletonList(monsterType);
    }

    public int getMonstersPerRoom() {
        // Più mostri nella palude dei troll
        return monsterType.equals("troll") ? 2 : 1;
    }

    public int getBaseDifficulty() {
        return monsterType.equals("troll") ? 2 : 1;
    }

    /**
     * Crea i mostri per questo dungeon usando MonsterFactory
     */
    public List<AbstractMonster> createMonstersForRoom() {
        logger.info("Creating monsters for room in " + name);
        List<AbstractMonster> monsters = new ArrayList<>();
        
        try {
            int count = getMonstersPerRoom();
            int difficulty = getBaseDifficulty();
            
            for (int i = 0; i < count; i++) {
                AbstractMonster monster = monsterFactory.createMonster(monsterType, difficulty);
                if (monster != null) {
                    monsters.add(monster);
                    logger.info("Created " + monsterType + " for " + name);
                }
            }
        } catch (Exception e) {
            logger.warning("Error creating monsters: " + e.getMessage());
        }
        
        return monsters;
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

            desc.append("Stanze: ").append(numberOfRooms).append("\n");
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
            return String.format("%s (%d stanze) - %d oro, %s",
                    getName(), getNumberOfRooms(), getGoldReward(), monsterType);
        } catch (Exception e) {
            return "Dungeon [Error]";
        }
    }

    /**
     * Builder interno statico per Dungeon
     * Nota: Questo è un builder alternativo interno che può essere usato
     * insieme al builder esterno
     */
    public static class Builder {
        private String name = "Unknown Dungeon";
        private String description = "";
        private int goldReward = 50;
        private String monsterType = "goblin";
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder goldReward(int goldReward) {
            this.goldReward = goldReward;
            return this;
        }
        
        public Builder monsterType(String monsterType) {
            this.monsterType = monsterType;
            return this;
        }
        
        public Dungeon build() {
            return new Dungeon(name, description, goldReward, monsterType);
        }
    }
}
