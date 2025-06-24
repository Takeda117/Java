package com.exam.project.composite;

import java.util.Collections;
import java.util.List;


public interface GameCommand {
    void execute();

    default void add(GameCommand command) {
        throw new UnsupportedOperationException("Not supported.");
    }

    default void remove(GameCommand command) {
        throw new UnsupportedOperationException("Not supported.");
    }

    default List<GameCommand> getChildren() {
        return Collections.emptyList();
    }
}

