package me.koenn.messagebot.commands;

import me.koenn.messagebot.MessageBot;
import me.koenn.messagebot.levelsystem.LevelSystem;
import me.koenn.messagebot.util.Logger;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;

import java.awt.*;
import java.util.ArrayList;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, May 2017
 */
public final class Commands {

    public static final Command[] COMMANDS = new Command[]{
            new Command() {
                @Override
                public String getCommand() {
                    return "test";
                }

                @Override
                public Permission getPermission() {
                    return null;
                }

                @Override
                public void execute(User sender, TextChannel channel, Message message, String[] args) {
                    channel.sendMessage(
                            new MessageBuilder()
                                    .append("@everyone ").append("TheKoenn")
                                    .append(" is now live over at ")
                                    .append("http://twitch.tv/thekoenn")
                                    .setEmbed(
                                            new MessageEmbedImpl()
                                                    .setUrl("http://test.com")
                                                    .setTitle("Test message")
                                                    .setDescription("This is a test :D")
                                                    .setType(EmbedType.RICH)
                                                    .setFields(new ArrayList<>())
                                                    .setColor(Color.GREEN)
                                                    .setFooter(new MessageEmbed.Footer("Footer message", "", ""))
                                    )
                                    .build()
                    ).queue();
                }
            },
            new Command() {
                @Override
                public String getCommand() {
                    return "dm";
                }

                @Override
                public Permission getPermission() {
                    return Permission.ADMINISTRATOR;
                }

                @Override
                public void execute(User sender, TextChannel channel, Message message, String[] args) {
                    Member member = channel.getGuild().getMember(sender);
                    Guild guild = channel.getGuild();

                    StringBuilder stringBuilder = new StringBuilder();
                    for (String arg : args) {
                        stringBuilder.append(arg).append(" ");
                    }
                    String massMessage = stringBuilder.toString().trim();

                    for (Member guildMember : guild.getMembers()) {
                        if (guildMember.getUser().getId().equals(member.getUser().getId()) || guildMember.getUser().isBot()) {
                            continue;
                        }

                        if (!guildMember.getUser().hasPrivateChannel()) {
                            guildMember.getUser().openPrivateChannel().queue(
                                    success -> success.sendMessage(massMessage).queue(),
                                    failure -> Logger.error("Unable to create private channel with \'" + guildMember.getUser().getName() + "\'")
                            );
                        } else {
                            guildMember.getUser().getPrivateChannel().sendMessage(massMessage).queue();
                        }
                    }

                    channel.sendMessage(new MessageBuilder().append(sender.getAsMention()).append(" your message was successfully send to all members!").build()).queue();
                }
            },
            new Command() {
                @Override
                public String getCommand() {
                    return "level";
                }

                @Override
                public Permission getPermission() {
                    return null;
                }

                @Override
                public void execute(User sender, TextChannel channel, Message message, String[] args) {
                    String username = sender.getName();
                    int level = MessageBot.levelSystem.getLevel(username);
                    int exp = MessageBot.levelSystem.getExp(username);
                    int neededExp = LevelSystem.getExpAtLevel(level);

                    channel.sendMessage(new MessageBuilder().setEmbed(new MessageEmbedImpl()
                            .setAuthor(new MessageEmbed.AuthorInfo(username, "", sender.getEffectiveAvatarUrl(), ""))
                            .setTitle("Current level and experience:")
                            .setDescription("**Level:** " + level + "\n**Exp:** " + exp + "/" + neededExp)
                            .setColor(Color.GREEN)
                            .setFields(new ArrayList<>())
                    ).build()).queue();
                }
            }
    };
}
