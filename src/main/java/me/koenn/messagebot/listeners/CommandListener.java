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

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

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
        String content = event.getMessage().getContent();

        if (!content.startsWith("!")) {
            return;
        }

        TextChannel channel = event.getChannel();

        Message response = interperate(event.getMessage(), event.getAuthor(), channel);
        if (response != null) {
            channel.sendTyping().queue();
            channel.sendMessage(response).queueAfter(200, TimeUnit.MILLISECONDS, message -> new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    message.delete().queue();
                }
            }, 20000));
        }
    }

    public static Message interperate(Message message, User user, TextChannel channel) {
        String content = message.getContent();

        if (!content.startsWith("!")) {
            return null;
        }

        String command = content.split(" ", 2)[0].replace("!", "").toLowerCase();
        String[] args = content.replace("!" + command, "").trim().split(" ");

        Command cmd = CommandManager.commands.get(command);
        if (cmd == null) {
            return null;
        }

        if (cmd.getPermission() != null && !channel.getGuild().getMember(user).hasPermission(cmd.getPermission())) {
            Logger.info("User \'" + user.getName() + "\' was denied access to command \'!" + command + "\'");
            return new MessageBuilder().append(user.getAsMention()).append(" you don't have permission to use this command!").build();
        }

        Logger.info("User \'" + user.getName() + "\' executed command \'!" + command + "\'");
        try {
            return cmd.execute(user, channel, message, args);
        } catch (Exception ex) {
            Logger.error("Error while executing command \'" + command + "\': " + ex.toString());
            ex.printStackTrace();
            return null;
        }
    }
}
