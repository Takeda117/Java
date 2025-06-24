package com.exam.project.composite;

import com.exam.project.logger.GameLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameMenu implements GameCommand {

    private static final Logger logger = GameLogger.getLogger();

    private final String title;
    private final List<GameCommand> commands = new ArrayList<>();
    private final Scanner input = new Scanner(System.in);

    public GameMenu(String title) {
        this.title = title;
    }

    @Override
    public void execute() {
        while (true) {
            System.out.println("\n=== " + title + " ===");
            for (int i = 0; i < commands.size(); i++) {
                GameCommand cmd = commands.get(i);
                String label = (cmd instanceof GameAction)
                        ? ((GameAction) cmd).getLabel()
                        : ((GameMenu) cmd).getTitle();
                System.out.println((i + 1) + ". " + label);
            }
            System.out.println("0. Back");

            System.out.print("Choose an option: ");
            String userInput = input.nextLine();

            int choice;
            try {
                choice = Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
                logger.warning("Invalid input: not a number.");
                System.out.println("Please enter a valid number.");
                continue;
            }

            if (choice == 0) {
                logger.info("Exiting menu: " + title);
                break;
            }

            if (choice < 1 || choice > commands.size()) {
                logger.warning("Invalid input: out of range.");
                System.out.println("Please select a valid option.");
                continue;
            }

            GameCommand selected = commands.get(choice - 1);
            logger.info("Executing command from menu '" + title + "': " + (selected instanceof GameAction
                    ? ((GameAction) selected).getLabel()
                    : ((GameMenu) selected).getTitle()));
            selected.execute();
        }
    }

    @Override
    public void add(GameCommand command) {
        commands.add(command);
    }

    @Override
    public void remove(GameCommand command) {
        commands.remove(command);
    }

    @Override
    public List<GameCommand> getChildren() {
        return List.copyOf(commands);
    }

    public String getTitle() {
        return title;
    }
}