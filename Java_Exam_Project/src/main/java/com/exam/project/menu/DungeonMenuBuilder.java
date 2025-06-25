package com.exam.project.menu;

import com.exam.project.composite.*;
import com.exam.project.factory.Character;
import com.exam.project.builder.DungeonFactory;
import com.exam.project.builder.Dungeon;
import com.exam.project.factoryMonster.AbstractMonster;
import com.exam.project.combat.CombatSystem;
import com.exam.project.iterator.Item;
import com.exam.project.logger.GameLogger;
import com.exam.project.menu.CharacterMenuBuilder.ReturnToMainMenuException;

import java.util.List;
import java.util.logging.Logger;

/**
 * DungeonMenuBuilder - Builds dungeon selection menu
 * Implements simple combat until death system
 */
public class DungeonMenuBuilder {

    private static final Logger logger = GameLogger.getLogger();
    private static final CombatSystem combatSystem = new CombatSystem();

    /**
     * Show dungeon menu
     */
    public static void showDungeonMenu(Character character) {
        if (character == null) {
            logger.warning("Cannot show dungeon menu: null character");
            return;
        }

        if (!character.isAlive()) {
            logger.warning("Cannot show dungeon menu: character is dead");
            System.out.println("Il tuo personaggio è morto! Non puoi esplorare dungeon.");
            throw new ReturnToMainMenuException(); // Torna al menu principale se il personaggio è morto
        }

        try {
            GameMenu dungeonMenu = buildDungeonMenu(character);
            dungeonMenu.execute();
        } catch (ReturnToMainMenuException e) {
            // Propagate the exception to return to main menu
            throw e;
        } catch (Exception e) {
            logger.severe("Error in dungeon menu: " + e.getMessage());
            System.out.println("Errore nel menu dungeon.");
        }
    }

    /**
     * Build dungeon menu with only required options
     */
    private static GameMenu buildDungeonMenu(Character character) {
        GameMenu menu = new GameMenu("Esplora Dungeon");

        menu.add(new MenuItem("Goblin Cave", () -> enterGoblinCave(character)));
        menu.add(new MenuItem("Swamp", () -> enterSwamp(character)));
        menu.add(new MenuItem("Torna al menu personaggio", () -> {
            // Simply exit this menu, will return to character menu
        }));

        return menu;
    }

    /**
     * Enter Goblin Cave and start combat
     */
    private static void enterGoblinCave(Character character) {
        try {
            System.out.println("\n=== GOBLIN CAVE ===");
            System.out.println("Stai entrando nella Goblin Cave...");

            Dungeon goblinCave = DungeonFactory.createGoblinCave();
            startDungeonCombat(character, goblinCave);

        } catch (ReturnToMainMenuException e) {
            // Propagate the exception to return to main menu
            throw e;
        } catch (Exception e) {
            logger.warning("Error in Goblin Cave: " + e.getMessage());
            System.out.println("Errore nella Goblin Cave.");
        }
    }

    /**
     * Enter Swamp and start combat
     */
    private static void enterSwamp(Character character) {
        try {
            System.out.println("\n=== SWAMP OF TROLLS ===");
            System.out.println("Stai entrando nella Swamp of Trolls...");

            Dungeon swamp = DungeonFactory.createSwampOfTrolls();
            startDungeonCombat(character, swamp);

        } catch (ReturnToMainMenuException e) {
            // Propagate the exception to return to main menu
            throw e;
        } catch (Exception e) {
            logger.warning("Error in Swamp: " + e.getMessage());
            System.out.println("Errore nella Swamp.");
        }
    }

    /**
     * Start dungeon combat - fight until character or monster dies
     */
    private static void startDungeonCombat(Character character, Dungeon dungeon) {
        logger.info("Starting dungeon combat: " + character.getName() + " vs " + dungeon.getName());

        try {
            System.out.println(dungeon.getDescription());
            System.out.println("Il tuo personaggio: " + character);

            // Create monsters for combat
            List<AbstractMonster> monsters = dungeon.createMonstersForRoom();

            if (monsters.isEmpty()) {
                System.out.println("Il dungeon è vuoto... strano.");
                return;
            }

            System.out.println("\nNemici incontrati:");
            for (AbstractMonster monster : monsters) {
                System.out.println("- " + monster);
            }

            // Simplified combat logic
            boolean victory = true;
            for (AbstractMonster monster : monsters) {
                while (monster.isAlive() && character.isAlive()) {
                    if (combatSystem.canFight(character)) {
                        combatSystem.executeAttack(character, monster);
                    }
                    
                    if (monster.isAlive()) {
                        combatSystem.executeMonsterAttack(monster, character);
                    }
                }
                
                if (!character.isAlive()) {
                    victory = false;
                    break;
                }
            }

            // Handle combat result
            if (victory) {
                handleVictory(character, dungeon);
            } else {
                handleGameOver(character);
                return; // Assicuriamoci di uscire dal metodo dopo handleGameOver
            }

        } catch (ReturnToMainMenuException e) {
            // Character died, propagate to return to main menu
            throw e; // Rilanciamo l'eccezione per tornare al menu principale
        } catch (Exception e) {
            logger.severe("Error in dungeon combat: " + e.getMessage());
            System.out.println("Errore durante il combattimento!");
        }
    }

    /**
     * Handle victory - give rewards and return to character menu
     */
    private static void handleVictory(Character character, Dungeon dungeon) {
        logger.info("Player victory in " + dungeon.getName());

        System.out.println("\n🎉 VITTORIA!");
        System.out.println("Hai sconfitto tutti i nemici nel " + dungeon.getName() + "!");
        System.out.println("🪙 Oro guadagnato: " + dungeon.getGoldReward());
        System.out.println("⭐ Esperienza guadagnata: " + dungeon.getExperienceReward());

        System.out.println("\nTorni al menu personaggio.");
    }

    /**
     * Handle game over - character died
     */
    private static void handleGameOver(Character character) {
        logger.info("Game over for character: " + character.getName());

        System.out.println("\n💀 GAME OVER 💀");
        System.out.println(character.getName() + " è stato sconfitto!");
        System.out.println("La tua avventura finisce qui...");
        System.out.println("\nTornando al menu principale.");

        // Game over returns to main menu
        throw new ReturnToMainMenuException();
    }
}
