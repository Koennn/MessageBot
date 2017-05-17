package me.koenn.messagebot.commands;

import me.koenn.messagebot.util.Logger;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;

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
                public void execute(User sender, TextChannel channel, Message message, String[] args) {
                    channel.sendMessage(new MessageBuilder().append("Test successful ").append(sender.getAsMention()).build()).queue();
                }
            },
            new Command() {
                @Override
                public String getCommand() {
                    return "dm";
                }

                @Override
                public void execute(User sender, TextChannel channel, Message message, String[] args) {
                    Member member = channel.getGuild().getMember(sender);
                    Guild guild = channel.getGuild();

                    if (!member.hasPermission(Permission.ADMINISTRATOR)) {
                        return;
                    }

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
            }
    };
}
