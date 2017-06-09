package me.koenn.messagebot.listeners;

import me.koenn.messagebot.MessageBot;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.UUID;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, January 2017
 */
public class ChatListener extends ListenerAdapter {

    private final JDA jda;

    public ChatListener() {
        this.jda = MessageBot.bot.getJDA();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getMessage().isMentioned(jda.getUserById(jda.getSelfUser().getId()))) {
            return;
        }

        String line = event.getMessage().getContent().split(" ", 2)[1].replace("2.0", "");
        System.out.println("Received: \'" + line + "\'");
        System.out.println("From: \'" + event.getAuthor().getName() + "\'");

        event.getChannel().sendTyping().queue();

        Thread thinkThread = new Thread(() -> {
            if (line.length() > 50) {
                jda.getTextChannelsByName("bot-commands", true).get(0).sendMessage("I can't process that. Your message is too long.");
                return;
            }

            String response = "I'm not sure what you mean.";
            if (line.toLowerCase().contains("call me")) {
                response = "No";
            } else {
                try {
                    response = MessageBot.chatBot.think(line);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            final String finalResponse = response;

            MessageBuilder message = new MessageBuilder().append(finalResponse);
            jda.getTextChannelsByName("bot-commands", true).get(0).sendMessage(message.build()).queue();
        }, "think-thread" + UUID.randomUUID().toString().substring(0, 4));
        thinkThread.start();
    }
}
