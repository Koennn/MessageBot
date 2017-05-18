package me.koenn.messagebot.listeners;

import me.koenn.messagebot.MessageBot;
import me.koenn.messagebot.levelsystem.LevelSystem;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Random;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, May 2017
 */
public final class MessageListener extends ListenerAdapter {

    private static final Random random = new Random();

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String username = event.getAuthor().getName();
        int currentExp = MessageBot.levelSystem.getExp(username);
        int currentLevel = MessageBot.levelSystem.getLevel(username);
        MessageBot.levelSystem.setExp(username, currentExp + 1);

        if (currentExp == LevelSystem.getExpAtLevel(currentLevel)) {
            MessageBot.levelSystem.setExp(username, 0);
            MessageBot.levelSystem.setLevel(username, currentLevel + 1);
        }
    }
}
