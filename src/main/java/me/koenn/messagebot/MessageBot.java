package me.koenn.messagebot;

import com.mb3364.twitch.api.models.Channel;
import me.koenn.messagebot.chatbot.ChatBot;
import me.koenn.messagebot.commands.CommandManager;
import me.koenn.messagebot.levelsystem.LevelSystem;
import me.koenn.messagebot.listeners.ChatListener;
import me.koenn.messagebot.listeners.CommandListener;
import me.koenn.messagebot.listeners.JoinListener;
import me.koenn.messagebot.listeners.MessageListener;
import me.koenn.messagebot.twitch.TwitchPinger;
import me.koenn.messagebot.util.Logger;
import me.koenn.messagebot.util.Range;
import me.koenn.messagebot.util.ThreadManager;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;
import net.dv8tion.jda.core.utils.SimpleLog;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

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
    public static ChatBot chatBot;

    public MessageBot(String token) throws InterruptedException {
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

        chatBot = new ChatBot(UUID.randomUUID().toString(), 6);

        bot = new DiscordBot(token, "CoffeeMod");
        bot.addListeners(
                new CommandListener(),
                new MessageListener(),
                new JoinListener(),
                new ChatListener()
        );

        CommandManager.registerCommands();

        pinger = new TwitchPinger();
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
        });

        Thread.sleep(500);

        Guild guild = bot.getJDA().getGuilds().get(0);
        for (int i = 0; i < 100; i += 10) {
            final int index = i;
            java.util.List<Role> roles = guild.getRolesByName(i + " - " + (i + 10) + " Plug", true);
            if (roles.isEmpty()) {
                final Random random = new Random();
                guild.getController().createRole().queue(role -> role.getManager().setName(index + " - " + (index + 10) + " Plug").queue(aVoid -> role.getManager().setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat())).queue(aVoid1 -> MessageListener.roles.put(new Range(index, index + 10), role))));
            } else {
                MessageListener.roles.put(new Range(i, i + 10), roles.get(0));
            }
        }

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

    public static void main(String[] args) throws InterruptedException {
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
