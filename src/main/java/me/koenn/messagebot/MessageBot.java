package me.koenn.messagebot;

import com.mb3364.twitch.api.models.Channel;
import me.koenn.messagebot.commands.CommandManager;
import me.koenn.messagebot.levelsystem.LevelSystem;
import me.koenn.messagebot.listeners.CommandListener;
import me.koenn.messagebot.listeners.MessageListener;
import me.koenn.messagebot.twitch.TwitchPinger;
import me.koenn.messagebot.util.Logger;
import me.koenn.messagebot.util.ThreadManager;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;
import net.dv8tion.jda.core.utils.SimpleLog;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.OffsetDateTime;
import java.util.ArrayList;

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
    public static ThreadManager threadManager;
    public static LevelSystem levelSystem;

    public MessageBot(String token) {
        Logger.LOG.setLevel(SimpleLog.Level.DEBUG);
        Logger.info("Initializing CoffeeMod v1.0...");

        Runtime.getRuntime().addShutdownHook(new Thread(this::exit, "shutdown-thread"));

        threadManager = new ThreadManager();

        levelSystem = new LevelSystem();
        try {
            levelSystem.load();
        } catch (IOException e) {
            Logger.error("Error while loading levelsystem: " + e);
        }

        bot = new DiscordBot(token, "CoffeeMod");
        bot.addListeners(
                new CommandListener(),
                new MessageListener()
        );

        CommandManager.registerCommands();

        /*pinger = new TwitchPinger();
        pinger.listeners.add((stream) -> {
            for (TextChannel channel : bot.getJDA().getTextChannelsByName("content-creators", false)) {
                Channel streamChannel = stream.getChannel();
                String displayName = streamChannel.getDisplayName();
                String url = streamChannel.getUrl();
                String title = streamChannel.getStatus();
                String thumbnail = streamChannel.getLogo();
                String preview = stream.getPreview().getTemplate().replace("{width}", "1920").replace("{height}", "1080");

                channel.sendMessage(new MessageBuilder()
                        .append("@everyone ")
                        .append(displayName)
                        .append(" is now live over at ")
                        .append(url)
                        .append(" and ")
                        .append("%youtube-url%")
                        .append("!")
                        .setEmbed(new MessageEmbedImpl()
                                .setColor(hex2Rgb("#6441a5"))
                                .setTitle(title)
                                .setUrl(url)
                                .setAuthor(new MessageEmbed.AuthorInfo(displayName, url, thumbnail, ""))
                                .setThumbnail(new MessageEmbed.Thumbnail(thumbnail, "", 300, 300))
                                .setImage(new MessageEmbed.ImageInfo(preview, "", 1920, 1080))
                                .setDescription(displayName + " is now live on Twitch and YouTube!\n\n**Playing**\n" + stream.getGame())
                                .setTimestamp(OffsetDateTime.now())
                                .setFields(new ArrayList<>())
                        ).build()).queue();
            }
        });*/

        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        threadManager.createThread("console-thread", () -> {
            try {
                String cmd = reader.readLine();
                if (cmd == null || cmd.isEmpty()) {
                    return;
                }

                if (cmd.equals("exit")) {
                    System.exit(0);
                }
            } catch (IOException e) {
                Logger.error("Exception while reading console: \'" + e + "\'");
            }
        }, true);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("No token provided!");
            return;
        }

        instance = new MessageBot(args[0]);
    }

    public void exit() {
        Logger.info("Shutting down application...");
        if (threadManager != null) {
            threadManager.disable();
        }

        if (bot != null) {
            bot.getJDA().setAutoReconnect(false);
            bot.getJDA().shutdown();
        }
    }

    public static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }
}
