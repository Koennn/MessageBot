package me.koenn.messagebot;

import me.koenn.messagebot.listeners.CommandListener;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, May 2017
 */
@SuppressWarnings("ConstantConditions")
public class MessageBotTest {

    @Test
    void testMessageBot() throws InterruptedException {
        MessageBot.main(new String[]{"MzE0MDU3MjQyMTYzMDE5Nzc2.C_yoIA.kumcPIZL9g-weRZLmk49z-QqNMc"});

        assertNotNull(MessageBot.instance, "MessageBot instance is null");
        assertNotNull(MessageBot.bot, "DiscordBot is null");
        assertNotNull(MessageBot.levelSystem, "LevelSystem is null");
        assertNotNull(MessageBot.bot.getJDA(), "JDA is null");

        Thread.sleep(500);

        JDA jda = MessageBot.bot.getJDA();

        do {
            Thread.sleep(100);
        } while (jda.getGuilds().isEmpty());

        assertTrue(!jda.getGuilds().get(0).getMembers().isEmpty(), "No members in guild");
    }

    @Test
    void testLevelCommand() throws InterruptedException {
        MessageBot.main(new String[]{"MzE0MDU3MjQyMTYzMDE5Nzc2.C_yoIA.kumcPIZL9g-weRZLmk49z-QqNMc"});

        Thread.sleep(500);

        JDA jda = MessageBot.bot.getJDA();

        do {
            Thread.sleep(100);
        } while (jda.getGuilds().isEmpty());

        Message levelResponse = CommandListener.interperate(new MessageBuilder().append("!level").build(), jda.getGuilds().get(0).getMembers().get(0).getUser(), jda.getTextChannels().get(0));

        assertNotNull(levelResponse.getEmbeds(), "Message doesn't contain any embeds");
        assertTrue(!levelResponse.getEmbeds().isEmpty(), "Message doesn't contain any embeds");

        MessageEmbed embed = levelResponse.getEmbeds().get(0);

        assertNotNull(embed.getDescription());
        String description = embed.getDescription().toLowerCase();

        assertTrue(description.contains("level"), "Response description doesn't contain level info");
        assertTrue(description.contains("exp"), "Response description doesn't contain exp info");
    }
}
