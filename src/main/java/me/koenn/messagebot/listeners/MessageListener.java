package me.koenn.messagebot.listeners;

import me.koenn.messagebot.MessageBot;
import me.koenn.messagebot.levelsystem.LevelSystem;
import me.koenn.messagebot.util.Range;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, May 2017
 */
public final class MessageListener extends ListenerAdapter {

    public static HashMap<Range, Role> roles = new HashMap<>();

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        User author = event.getAuthor();
        if (author.isBot()) {
            return;
        }

        String username = author.getName() + "#" + author.getId();
        int currentExp = MessageBot.levelSystem.getExp(username);
        int currentLevel = MessageBot.levelSystem.getLevel(username);
        MessageBot.levelSystem.setExp(username, currentExp + 1);

        if (currentExp == LevelSystem.getExpAtLevel(currentLevel)) {
            MessageBot.levelSystem.setExp(username, 0);
            MessageBot.levelSystem.setLevel(username, currentLevel + 1);
            event.getChannel().sendTyping();
            event.getChannel().sendMessage(new MessageEmbedImpl()
                    .setAuthor(new MessageEmbed.AuthorInfo(author.getName(), "", author.getEffectiveAvatarUrl(), ""))
                    .setTitle("You leveled up!")
                    .setDescription("**Level:** " + (currentLevel + 1) + "\n**Exp:** " + 0 + "/" + LevelSystem.getExpAtLevel(currentLevel + 1))
                    .setColor(Color.GREEN)
                    .setFields(new ArrayList<>())
            ).queue();

            Range currentRange = null;
            for (Range range : roles.keySet()) {
                if (range.isInRange(currentLevel + 1)) {
                    currentRange = range;
                }
            }

            if (currentRange != null) {
                Role role = roles.get(currentRange);
                event.getGuild().getController().addRolesToMember(event.getGuild().getMember(author), role);
            }
        }
    }
}
