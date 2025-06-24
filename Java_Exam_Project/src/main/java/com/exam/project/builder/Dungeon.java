package com.exam.project.builder;

import java.util.List;
import java.util.ArrayList;

/**
 * Dungeon - Represents a dungeon that the player can explore
 *
 * This class uses the Builder Pattern to allow flexible creation
 * of dungeons with different parameters
 */
public class Dungeon {

    // Dungeon properties
    private final String name;
    private final int difficulty;
    private final int numberOfRooms;
    private final int minMonstersPerRoom;
    private final int maxMonstersPerRoom;
    private final int goldReward;
    private final int experienceReward;
    private final List<String> possibleMonsterTypes;

    /**
     * Private constructor - can only be created through Builder
     */
    private Dungeon(DungeonBuilder builder) {
        this.name = builder.name;
        this.difficulty = builder.difficulty;
        this.numberOfRooms = builder.numberOfRooms;
        this.minMonstersPerRoom = builder.minMonstersPerRoom;
        this.maxMonstersPerRoom = builder.maxMonstersPerRoom;
        this.goldReward = builder.goldReward;
        this.experienceReward = builder.experienceReward;
        this.possibleMonsterTypes = new ArrayList<>(builder.possibleMonsterTypes);
    }

    /**
     * Builder class for creating Dungeon instances
     * This implements the Builder Pattern
     */
    public static class DungeonBuilder {
        // Required parameters
        private final String name;
        private final int difficulty;

        // Optional parameters with default values
        private int numberOfRooms = 3;
        private int minMonstersPerRoom = 1;
        private int maxMonstersPerRoom = 3;
        private int goldReward = 100;
        private int experienceReward = 50;
        private List<String> possibleMonsterTypes = new ArrayList<>();

        /**
         * Constructor with required parameters
         */
        public DungeonBuilder(String name, int difficulty) {
            this.name = name;
            this.difficulty = difficulty;

            // Add default monster type
            this.possibleMonsterTypes.add("Goblin");
        }

        /**
         * Set the number of rooms in the dungeon
         */
        public DungeonBuilder withRooms(int rooms) {
            if (rooms > 0 && rooms <= 10) {
                this.numberOfRooms = rooms;
            }
            return this;
        }

        /**
         * Set the monster spawn range per room
         */
        public DungeonBuilder withMonstersPerRoom(int min, int max) {
            if (min > 0 && max >= min && max <= 10) {
                this.minMonstersPerRoom = min;
                this.maxMonstersPerRoom = max;
            }
            return this;
        }

        /**
         * Set the gold reward for completing the dungeon
         */
        public DungeonBuilder withGoldReward(int gold) {
            if (gold >= 0) {
                this.goldReward = gold;
            }
            return this;
        }

        /**
         * Set the experience reward for completing the dungeon
         */
        public DungeonBuilder withExperienceReward(int exp) {
            if (exp >= 0) {
                this.experienceReward = exp;
            }
            return this;
        }

        /**
         * Add a monster type that can spawn in this dungeon
         */
        public DungeonBuilder addMonsterType(String monsterType) {
            if (monsterType != null && !monsterType.trim().isEmpty()) {
                this.possibleMonsterTypes.add(monsterType);
            }
            return this;
        }

        /**
         * Clear default monsters and set custom list
         */
        public DungeonBuilder withMonsterTypes(List<String> types) {
            if (types != null && !types.isEmpty()) {
                this.possibleMonsterTypes = new ArrayList<>(types);
            }
            return this;
        }

        /**
         * Build the final Dungeon object
         */
        public Dungeon build() {
            return new Dungeon(this);
        }
    }

    // Getters for dungeon properties
    public String getName() {
        return name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public int getMinMonstersPerRoom() {
        return minMonstersPerRoom;
    }

    public int getMaxMonstersPerRoom() {
        return maxMonstersPerRoom;
    }

    public int getGoldReward() {
        return goldReward;
    }

    public int getExperienceReward() {
        return experienceReward;
    }

    public List<String> getPossibleMonsterTypes() {
        return new ArrayList<>(possibleMonsterTypes);
    }

    /**
     * Get a description of the dungeon
     */
    public String getDescription() {
        return String.format("%s (Difficulty: %d) - %d rooms, %d-%d monsters per room. Rewards: %d gold, %d exp",
                name, difficulty, numberOfRooms, minMonstersPerRoom, maxMonstersPerRoom,
                goldReward, experienceReward);
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
