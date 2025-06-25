package com.exam.project.combat;

import com.exam.project.factory.Character;
import com.exam.project.factory.CharacterFactory;
import com.exam.project.factoryMonster.AbstractMonster;
import com.exam.project.logger.GameLogger;

import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Sistema di combattimento semplice
 */
public class CombatSystem {

    private static final Logger logger = GameLogger.getLogger();
    private Scanner scanner = new Scanner(System.in);

    /**
     * Esegue un combattimento tra personaggio e mostro
     */
    public Character doCombat(Character character, AbstractMonster monster) {
        System.out.println("\n=== COMBATTIMENTO ===");
        System.out.println(character.getName() + " vs " + monster.getName());
        
        while (character.isAlive() && monster.isAlive()) {
            // Turno del personaggio
            System.out.println("\nVita: " + character.getHealth() + "/" + character.getMaxHealth());
            System.out.println("Nemico: " + monster.getName() + " - Vita: " + monster.getHealth());
            System.out.println("\nPremi INVIO per attaccare...");
            scanner.nextLine();
            
            // Attacco del personaggio
            int damage = character.attack();
            if (damage > 0) monster.takeDamage(damage);
            
            if (!monster.isAlive()) {
                System.out.println("\nHai sconfitto " + monster.getName() + "!");
                return character;
            }
            
            // Turno del mostro
            System.out.println("\n--- Turno del mostro ---");
            int monsterDamage = monster.attack();
            if (monsterDamage > 0) character.takeDamage(monsterDamage);
            
            if (!character.isAlive()) {
                System.out.println("\nSei stato sconfitto da " + monster.getName() + "!");
                return createNewCharacter();
            }
        }
        
        return character;
    }
    
    /**
     * Verifica se un personaggio può combattere
     */
    public boolean canFight(Character character) {
        return character != null && character.isAlive();
    }
    
    /**
     * Verifica se un mostro può combattere
     */
    public boolean canFight(AbstractMonster monster) {
        return monster != null && monster.isAlive();
    }
    
    /**
     * Esegue un attacco del personaggio contro un mostro
     */
    public int executeAttack(Character character, AbstractMonster monster) {
        int damage = character.attack();
        if (damage > 0) monster.takeDamage(damage);
        return damage;
    }
    
    /**
     * Esegue un attacco del mostro contro un personaggio
     */
    public int executeMonsterAttack(AbstractMonster monster, Character character) {
        int damage = monster.attack();
        if (damage > 0) character.takeDamage(damage);
        return damage;
    }
    
    /**
     * Crea un nuovo personaggio quando il giocatore muore
     */
    private Character createNewCharacter() {
        System.out.println("\nDevi creare un nuovo personaggio!");
        
        CharacterFactory factory = new CharacterFactory();
        
        System.out.println("\nScegli il tipo di personaggio:");
        System.out.println("1. Guerriero");
        System.out.println("2. Mago");
        System.out.print("Scelta: ");
        
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Scelta non valida! Creato Guerriero di default.");
            choice = 1;
        }
        
        String type = (choice == 2) ? "mage" : "warrior";
        
        System.out.print("\nInserisci il nome del personaggio: ");
        String name = scanner.nextLine();
        if (name.trim().isEmpty()) name = "Eroe";
        
        Character newCharacter = factory.createCharacter(type, name);
        System.out.println("\nNuovo personaggio creato: " + newCharacter.getName());
        
        return newCharacter;
    }
}
