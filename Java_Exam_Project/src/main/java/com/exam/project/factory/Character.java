package com.exam.project.factory;

import com.exam.project.iterator.Item;

/**
 * Character interface - defines what every character can do
 * Part of the Factory Pattern implementation
 *
 * Questa interfaccia definisce il contratto che tutti i personaggi devono rispettare.
 * È il cuore del Factory Pattern: permette di creare diversi tipi di personaggi
 * che condividono le stesse operazioni base ma le implementano diversamente.
 */
public interface Character {

    // === Core Combat Actions ===
    // Questi metodi gestiscono le azioni di combattimento base

    /**
     * Attacks an enemy - each class implements its own attack style
     * @return The damage dealt by the attack, 0 if attack fails
     */
    int attack();

    /**
     * Takes damage from an enemy attack
     * @param damage The amount of damage received (must be non-negative)
     */
    void takeDamage(int damage);

    /**
     * Checks if the character is still alive
     * @return true if health > 0, false otherwise
     */
    boolean isAlive();

    // === Character Progression ===
    // Metodi per la crescita e sviluppo del personaggio

    /**
     * Il personaggio si allena per migliorare le proprie statistiche
     * Costa stamina ma aumenta le capacità
     */
    void train();

    // === Basic Character Info ===
    // Metodi getter per accedere alle informazioni del personaggio

    String getName();
    int getHealth();
    int getMaxHealth();
    int getStamina();
    int getMaxStamina();
    int getBaseDamage();
    int getMoney();
    int getLevel();

    // === Recovery System ===
    // Sistema di recupero punti vita e stamina

    /**
     * Recupera una quantità specifica di stamina
     * @param amount la quantità di stamina da recuperare
     */
    void restoreStamina(int amount);

    /**
     * Riposo completo - recupera tutta la vita e stamina
     */
    void rest();

    // === Inventory Management ===
    // Sistema di gestione dell'inventario - CORRETTO dal bug precedente

    /**
     * Equipaggia un oggetto (armi, armature)
     * NOTA: Parametro corretto da 'sword' a 'item' per essere generico
     * @param item l'oggetto da equipaggiare
     */
    void equipItem(Item item);

    /**
     * Aggiunge un oggetto all'inventario
     * NOTA: Parametro corretto da 'sword' a 'item' per essere generico
     * @param item l'oggetto da aggiungere
     */
    void addItem(Item item);

    /**
     * Mostra il contenuto dell'inventario del personaggio
     */
    void showInventory();
}