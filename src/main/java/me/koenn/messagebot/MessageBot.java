package me.koenn.messagebot;

import me.koenn.messagebot.commands.CommandManager;
import me.koenn.messagebot.listeners.CommandListener;
import me.koenn.messagebot.twitch.TwitchPinger;
import me.koenn.messagebot.util.Logger;
import net.dv8tion.jda.core.utils.SimpleLog;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, May 2017
 */
public final class MessageBot {

    public static MessageBot instance;
    public static DiscordBot bot;
    public static TwitchPinger pinger;

    public MessageBot(String token) {
        Runtime.getRuntime().addShutdownHook(new Thread(this::exit, "shutdown-thread"));
        Logger.LOG.setLevel(SimpleLog.Level.DEBUG);

        bot = new DiscordBot(token, "Message Bot");
        bot.addListeners(
                new CommandListener()
        );

        CommandManager.registerCommands();

        pinger = new TwitchPinger();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("No token provided!");
            return;
        }

        instance = new MessageBot(args[0]);
    }

    public void exit() {
        if (pinger != null) {
            pinger.disable();
        }

        if (bot != null) {
            bot.getJDA().shutdown();
        }
    }
}
