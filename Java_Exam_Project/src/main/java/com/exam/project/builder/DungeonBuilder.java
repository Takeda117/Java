package com.exam.project.builder;

/**
 * DungeonBuilder - Interfaccia per il Builder Pattern
 */
public interface DungeonBuilder {
    
    /**
     * Resetta il builder
     */
    DungeonBuilder reset();
    
    /**
     * Imposta il nome del dungeon
     */
    DungeonBuilder setName(String name);
    
    /**
     * Imposta la descrizione del dungeon
     */
    DungeonBuilder setDescription(String description);
    
    /**
     * Imposta la ricompensa in oro del dungeon
     */
    DungeonBuilder setGoldReward(int goldReward);
    
    /**
     * Imposta il tipo di mostro del dungeon
     */
    DungeonBuilder setMonsterType(String monsterType);
    
    /**
     * Costruisce e restituisce il dungeon
     */
    Dungeon build();
}
