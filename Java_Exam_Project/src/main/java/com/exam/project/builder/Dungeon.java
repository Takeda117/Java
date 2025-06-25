package com.exam.project.builder;

import com.exam.project.logger.GameLogger;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Dungeon class implementing Builder Pattern for two specific dungeons
 */
public class Dungeon {

    private static final Logger logger = GameLogger.getLogger();

    private final String name;
    private final String description;
    private final int numberOfRooms;
    private final int goldReward;
    private final int experienceReward;
    private final List<String> monsterTypes;

    /**
     * Private constructor - only Builder can create instances
     */
    private Dungeon(DungeonBuilder builder) {
        try {
            this.name = builder.name;
            this.description = builder.description;
            this.numberOfRooms = builder.numberOfRooms;
            this.goldReward = builder.goldReward;
            this.experienceReward = builder.experienceReward;
            this.monsterTypes = Collections.unmodifiableList(new ArrayList<>(builder.monsterTypes));

            logger.info("Dungeon built: " + this.name);

        } catch (Exception e) {
            logger.severe("Error creating Dungeon: " + e.getMessage());
            throw new RuntimeException("Failed to create Dungeon", e);
        }
    }

    /**
     * DungeonBuilder - Simple Builder Pattern implementation
     */
    public static class DungeonBuilder {
        private final String name;
        private String description = "";
        private int numberOfRooms = 3;
        private int goldReward = 100;
        private int experienceReward = 50;
        private List<String> monsterTypes = new ArrayList<>();

        /**
         * Constructor with required name parameter
         */
        public DungeonBuilder(String name) {
            try {
                if (name == null || name.trim().isEmpty()) {
                    throw new IllegalArgumentException("Dungeon name required");
                }
                this.name = name.trim();
            } catch (Exception e) {
                logger.severe("Error creating DungeonBuilder: " + e.getMessage());
                throw new IllegalArgumentException("Invalid dungeon name", e);
            }
        }

        /**
         * Sets description
         */
        public DungeonBuilder withDescription(String description) {
            try {
                this.description = (description != null) ? description.trim() : "";
                return this;
            } catch (Exception e) {
                logger.warning("Error setting description: " + e.getMessage());
                this.description = "";
                return this;
            }
        }

        /**
         * Sets number of rooms
         */
        public DungeonBuilder withRooms(int rooms) {
            try {
                this.numberOfRooms = Math.max(1, Math.min(10, rooms));
                return this;
            } catch (Exception e) {
                logger.warning("Error setting rooms: " + e.getMessage());
                return this;
            }
        }

        /**
         * Sets gold reward
         */
        public DungeonBuilder withGoldReward(int gold) {
            try {
                this.goldReward = Math.max(0, gold);
                return this;
            } catch (Exception e) {
                logger.warning("Error setting gold: " + e.getMessage());
                return this;
            }
        }

        /**
         * Sets experience reward
         */
        public DungeonBuilder withExperienceReward(int exp) {
            try {
                this.experienceReward = Math.max(0, exp);
                return this;
            } catch (Exception e) {
                logger.warning("Error setting experience: " + e.getMessage());
                return this;
            }
        }

        /**
         * Adds monster type
         */
        public DungeonBuilder addMonsterType(String type) {
            try {
                if (type != null && !type.trim().isEmpty()) {
                    this.monsterTypes.add(type.trim());
                }
                return this;
            } catch (Exception e) {
                logger.warning("Error adding monster type: " + e.getMessage());
                return this;
            }
        }

        /**
         * Builds the final Dungeon
         */
        public Dungeon build() {
            try {
                if (monsterTypes.isEmpty()) {
                    monsterTypes.add("Monster");
                }
                return new Dungeon(this);
            } catch (Exception e) {
                logger.severe("Error building Dungeon: " + e.getMessage());
                throw new RuntimeException("Failed to build Dungeon", e);
            }
        }
    }

    // Safe getters
    public String getName() {
        return (name != null) ? name : "Unknown";
    }

    public String getDescription() {
        return (description != null) ? description : "";
    }

    public int getNumberOfRooms() {
        return Math.max(1, numberOfRooms);
    }

    public int getGoldReward() {
        return Math.max(0, goldReward);
    }

    public int getExperienceReward() {
        return Math.max(0, experienceReward);
    }

    public List<String> getMonsterTypes() {
        try {
            if (monsterTypes == null || monsterTypes.isEmpty()) {
                return Collections.singletonList("Monster");
            }
            return monsterTypes;
        } catch (Exception e) {
            logger.warning("Error getting monster types: " + e.getMessage());
            return Collections.singletonList("Monster");
        }
    }

    /**
     * Gets monsters per room - simple calculation
     */
    public int getMonstersPerRoom() {
        try {
            if (numberOfRooms <= 4) {
                return 2; // Goblin Cave: 2 monsters per room
            } else {
                return 1; // Swamp of Trolls: 1 monster per room
            }
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * Gets base difficulty for monster scaling
     */
    public int getBaseDifficulty() {
        try {
            if (numberOfRooms <= 4) {
                return 1; // Goblin Cave: difficulty 1
            } else {
                return 3; // Swamp of Trolls: difficulty 3
            }
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * Gets full description
     */
    public String getFullDescription() {
        try {
            StringBuilder desc = new StringBuilder();
            desc.append(String.format("=== %s ===%n", getName().toUpperCase()));

            String description = getDescription();
            if (!description.isEmpty()) {
                desc.append(description).append("\n\n");
            }

            desc.append(String.format("Rooms: %d%n", getNumberOfRooms()));
            desc.append(String.format("Rewards: %d gold, %d experience%n",
                    getGoldReward(), getExperienceReward()));

            List<String> types = getMonsterTypes();
            if (!types.isEmpty()) {
                desc.append("Enemies: ");
                desc.append(String.join(", ", types));
            }

            return desc.toString();

        } catch (Exception e) {
            logger.severe("Error creating description: " + e.getMessage());
            return "Error: Cannot display dungeon information";
        }
    }

    @Override
    public String toString() {
        try {
            return String.format("%s (%d rooms) - %d gold, %d exp",
                    getName(), getNumberOfRooms(), getGoldReward(), getExperienceReward());
        } catch (Exception e) {
            return "Dungeon [Error]";
        }
    }
}