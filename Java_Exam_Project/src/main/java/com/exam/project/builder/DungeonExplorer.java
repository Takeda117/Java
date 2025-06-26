package com.exam.project.builder;

import com.exam.project.combat.CombatSystem;
import com.exam.project.factory.Character;
import com.exam.project.factoryMonster.AbstractMonster;
import com.exam.project.factoryMonster.MonsterFactory;
import com.exam.project.iterator.Item;
import com.exam.project.observer.StaminaRecoverySystem;
import com.exam.project.logger.GameLogger;
import java.util.logging.Logger;
import java.util.Scanner;
import java.util.List;

/**
 * DungeonExplorer - Gestisce l'esplorazione di un dungeon
 */
public class DungeonExplorer {
    
    private static final Logger logger = GameLogger.getLogger();
    
    private Character character;
    private Dungeon dungeon;
    private final Scanner  scanner;
    private final CombatSystem combatSystem;
    private final MonsterFactory monsterFactory;
    
    /**
     * Costruttore
     */
    public DungeonExplorer() {
        this.scanner = new Scanner(System.in);
        this.combatSystem = new CombatSystem();
        this.monsterFactory = new MonsterFactory();
    }
    
    /**
     * Imposta il personaggio
     */
    public DungeonExplorer withCharacter(Character character) {
        this.character = character;
        return this;
    }
    
    /**
     * Imposta il dungeon
     */
    public DungeonExplorer withDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
        return this;
    }
    
    /**
     * Avvia l'esplorazione
     */
    public boolean build() {
        if (character == null || dungeon == null) {
            return false;
        }
        
        logger.info(character.getName() + " esplora " + dungeon.getName());
        System.out.println("\nStai esplorando " + dungeon.getName());
        System.out.println(dungeon.getDescription());
        
        // Crea un mostro
        AbstractMonster monster = monsterFactory.createMonster(dungeon.getMonsterType());
        if (monster == null) {
            System.out.println("Non ci sono mostri qui.");
            return false;
        }
        
        System.out.println("\nHai incontrato un " + monster.getType() + "!");
        
        // Combatti
        boolean victory = combat(monster);
        
        // Recupera stamina dopo il dungeon
        if (victory) {
            StaminaRecoverySystem.recoverStamina(character);
        }
        
        return victory;
    }
    
    /**
     * Sistema di combattimento
     */
    private boolean combat(AbstractMonster monster) {
        while (monster.isAlive() && character.isAlive()) {
            // Mostra stato
            System.out.println("\nHP: " + character.getHealth() + "/" + character.getMaxHealth());
            System.out.println("Nemico: " + monster.getHealth() + " HP");
            
            // Turno del giocatore
            System.out.println("\n1. Attacca");
            System.out.print("Cosa fai? ");
            scanner.nextLine();
            
            combatSystem.executeAttack(character, monster);
            
            // Controlla se il mostro è morto
            if (!monster.isAlive()) {
                System.out.println("\nHai vinto!");
                System.out.println("Hai guadagnato " + dungeon.getGoldReward() + " oro!");
                
                // Aggiungi gli oggetti droppati all'inventario del personaggio
                List<Item> droppedItems = monster.getDroppedItems();
                if (!droppedItems.isEmpty()) {
                    System.out.println("\nHai trovato:");
                    for (Item item : droppedItems) {
                        character.addItem(item);
                        // Il messaggio di conferma viene già mostrato nel metodo addItem
                    }
                }
                
                return true;
            }
            
            // Turno del mostro
            combatSystem.executeMonsterAttack(monster, character);
            
            // Controlla se il personaggio è morto
            if (!character.isAlive()) {
                System.out.println("\nSei stato sconfitto!");
                return false;
            }
        }
        
        return character.isAlive();
    }
}