package me.koenn.messagebot.listeners;

import me.koenn.messagebot.commands.Command;
import me.koenn.messagebot.commands.CommandManager;
import me.koenn.messagebot.util.Logger;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, May 2017
 */
public final class CommandListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        String content = message.getContent();

        if (!content.startsWith("!")) {
            return;
        }

        User user = event.getAuthor();
        TextChannel channel = event.getChannel();
        String command = content.split(" ", 2)[0].replace("!", "").toLowerCase();
        String[] args = content.replace("!" + command, "").trim().split(" ");

        Command cmd = CommandManager.commands.get(command);
        if (cmd == null) {
            return;
        }

        channel.sendTyping().queue();

        if (cmd.getPermission() != null && !event.getGuild().getMember(user).hasPermission(cmd.getPermission())) {
            channel.sendMessage(new MessageBuilder().append(user.getAsMention()).append(" you don't have permission to use this command!").build()).queue();
            Logger.info("User \'" + user.getName() + "\' was denied access to command \'!" + command + "\'");
            return;
        }

        Logger.info("User \'" + user.getName() + "\' executed command \'!" + command + "\'");
        try {
            cmd.execute(user, channel, message, args);
        } catch (Exception ex) {
            Logger.error("Error while executing command \'" + command + "\': " + ex.toString());
            ex.printStackTrace();
        }
    }
}
