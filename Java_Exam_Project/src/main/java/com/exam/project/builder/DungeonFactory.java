package com.exam.project.builder;

import java.util.Arrays;

/**
 * DungeonFactory - Creates predefined dungeon configurations
 *
 * This factory uses the DungeonBuilder to create various
 * pre-configured dungeons that players can explore
 */
public class DungeonFactory {

    /**
     * Create the Goblin Cave - a beginner dungeon
     *
     * @return A configured Goblin Cave dungeon
     */
    public Dungeon createGoblinCave() {
        return new Dungeon.DungeonBuilder("Goblin Cave", 1)
                .withRooms(3)
                .withMonstersPerRoom(1, 3)
                .withGoldReward(150)
                .withExperienceReward(100)
                .withMonsterTypes(Arrays.asList("Goblin", "Wolf"))
                .build();
    }

    /**
     * Create the Orc Stronghold - an intermediate dungeon
     *
     * @return A configured Orc Stronghold dungeon
     */
    public Dungeon createOrcStronghold() {
        return new Dungeon.DungeonBuilder("Orc Stronghold", 3)
                .withRooms(5)
                .withMonstersPerRoom(2, 4)
                .withGoldReward(300)
                .withExperienceReward(200)
                .withMonsterTypes(Arrays.asList("Orc", "Goblin", "Wolf"))
                .build();
    }

    /**
     * Create the Ancient Crypt - a harder dungeon
     *
     * @return A configured Ancient Crypt dungeon
     */
    public Dungeon createAncientCrypt() {
        return new Dungeon.DungeonBuilder("Ancient Crypt", 5)
                .withRooms(7)
                .withMonstersPerRoom(2, 5)
                .withGoldReward(500)
                .withExperienceReward(350)
                .withMonsterTypes(Arrays.asList("Skeleton", "Orc", "Wolf"))
                .build();
    }

    /**
     * Create a custom dungeon with specified parameters
     * This shows the flexibility of the Builder pattern
     *
     * @param name The dungeon name
     * @param difficulty The difficulty level (1-10)
     * @return A custom configured dungeon
     */
    public Dungeon createCustomDungeon(String name, int difficulty) {
        // Calculate parameters based on difficulty
        int rooms = Math.min(difficulty + 2, 10);
        int minMonsters = Math.max(1, difficulty / 2);
        int maxMonsters = Math.min(difficulty + 1, 8);
        int gold = difficulty * 100;
        int exp = difficulty * 75;

        Dungeon.DungeonBuilder builder = new Dungeon.DungeonBuilder(name, difficulty)
                .withRooms(rooms)
                .withMonstersPerRoom(minMonsters, maxMonsters)
                .withGoldReward(gold)
                .withExperienceReward(exp);

        // Add monster types based on difficulty
        if (difficulty <= 2) {
            builder.withMonsterTypes(Arrays.asList("Goblin", "Wolf"));
        } else if (difficulty <= 5) {
            builder.withMonsterTypes(Arrays.asList("Goblin", "Orc", "Wolf"));
        } else {
            builder.withMonsterTypes(Arrays.asList("Orc", "Skeleton", "Wolf"));
        }

        return builder.build();
    }

    /**
     * Show available predefined dungeons
     */
    public void showAvailableDungeons() {
        System.out.println("\n=== AVAILABLE DUNGEONS ===");
        System.out.println("1. Goblin Cave - Beginner dungeon (Difficulty 1)");
        System.out.println("2. Orc Stronghold - Intermediate dungeon (Difficulty 3)");
        System.out.println("3. Ancient Crypt - Advanced dungeon (Difficulty 5)");
        System.out.println("\nYou can also create custom dungeons!");
    }
}
