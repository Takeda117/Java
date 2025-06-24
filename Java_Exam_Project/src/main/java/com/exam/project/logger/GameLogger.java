package com.exam.project.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GameLogger {
//Using singleton + logger

    private static final Logger logger = Logger.getLogger("It's me, a logger!");

    static {
        logger.setUseParentHandlers(false); // disattiva il logger di default

        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.INFO);
        handler.setFormatter(new SimpleFormatter());

        logger.addHandler(handler);
        logger.setLevel(Level.INFO);
    }

    private GameLogger() {
        // private constructor to prevent instantiation
    }

    public static Logger getLogger() {
        return logger;
    }
}
