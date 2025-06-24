package com.exam.project.composite;

import com.exam.project.logger.GameLogger;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameAction implements GameCommand {

    private static final Logger logger = GameLogger.getLogger();

    private final String label;
    private final Runnable action;

    public GameAction(String label, Runnable action) {
        this.label = Objects.requireNonNull(label, "Label must not be null.");
        this.action = Objects.requireNonNull(action, "Action must not be null.");
    }

    @Override
    public void execute() {
        logger.log(Level.INFO, "Executing action: {0}", label);
        action.run();
    }

    public String getLabel() {
        return label;
    }
}