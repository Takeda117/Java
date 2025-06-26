package com.exam.project.builder;

import com.exam.project.logger.GameLogger;
import java.util.logging.Logger;

/**
 * ConcreteDungeonBuilder - Implementazione concreta del DungeonBuilder
 */
public class ConcreteDungeonBuilder implements DungeonBuilder {
    
    private static final Logger logger = GameLogger.getLogger();
    
    private String name;
    private String description;
    private int goldReward;
    private String monsterType;
    
    /**
     * Resetta il builder
     */
    @Override
    public DungeonBuilder reset() {
        this.name = "Dungeon";
        this.description = "Un dungeon misterioso";
        this.goldReward = 50;
        this.monsterType = "goblin";
        logger.fine("DungeonBuilder resettato");
        return this;
    }
    
    /**
     * Imposta il nome del dungeon
     */
    @Override
    public DungeonBuilder setName(String name) {
        this.name = name;
        logger.fine("Nome dungeon impostato: " + name);
        return this;
    }
    
    /**
     * Imposta la descrizione del dungeon
     */
    @Override
    public DungeonBuilder setDescription(String description) {
        this.description = description;
        logger.fine("Descrizione dungeon impostata");
        return this;
    }
    
    /**
     * Imposta la ricompensa in oro del dungeon
     */
    @Override
    public DungeonBuilder setGoldReward(int goldReward) {
        this.goldReward = goldReward;
        logger.fine("Ricompensa dungeon impostata: " + goldReward);
        return this;
    }
    
    /**
     * Imposta il tipo di mostro del dungeon
     */
    @Override
    public DungeonBuilder setMonsterType(String monsterType) {
        this.monsterType = monsterType;
        logger.fine("Tipo mostro dungeon impostato: " + monsterType);
        return this;
    }
    
    /**
     * Costruisce e restituisce il dungeon
     */
    @Override
    public Dungeon build() {
        logger.info("Costruzione dungeon: " + name);
        return new Dungeon(name, description, goldReward, monsterType);
    }
}