package me.koenn.messagebot.commands;

import java.util.HashMap;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, May 2017
 */
public final class CommandManager {

    public static final HashMap<String, Command> commands = new HashMap<>();

    public static void registerCommand(Command command) {
        commands.put(command.getCommand(), command);
    }

    public static void registerCommands() {
        for (Command command : Commands.COMMANDS) {
            registerCommand(command);
        }
    }
}
