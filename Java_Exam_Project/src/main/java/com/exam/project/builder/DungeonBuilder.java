package com.exam.project.builder;

/**
 * DungeonBuilder Interface - Builder Pattern
 *
 * Questa interfaccia definisce i metodi per costruire un Dungeon.
 * Permette diverse implementazioni del builder per diversi tipi di dungeon.
 */
public interface DungeonBuilder {

    /**
     * Imposta il nome del dungeon (obbligatorio)
     */
    DungeonBuilder setName(String name);

    /**
     * Imposta la descrizione del dungeon
     */
    DungeonBuilder setDescription(String description);

    /**
     * Imposta il reward in oro
     */
    DungeonBuilder setGoldReward(int gold);

    /**
     * Imposta il tipo di mostro che spawna
     */
    DungeonBuilder setMonsterType(String monsterType);

    /**
     * Costruisce e restituisce il Dungeon finale
     */
    Dungeon build();

    /**
     * Reset del builder per riutilizzo
     */
    DungeonBuilder reset();
}
