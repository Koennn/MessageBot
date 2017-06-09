package me.koenn.messagebot.listeners;

import me.koenn.messagebot.util.Logger;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.util.ArrayList;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, May 2017
 */
public class JoinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Logger.debug("Join");
        Member member = event.getMember();
        TextChannel channel = event.getGuild().getTextChannelsByName("main-lobby", true).get(0);
        channel.sendTyping().queue();
        channel.sendMessage(new MessageEmbedImpl()
                .setAuthor(new MessageEmbed.AuthorInfo(member.getUser().getName(), "", member.getUser().getEffectiveAvatarUrl(), ""))
                .setTitle("Welcome to the Coffee House")
                .setDescription("Please read #welcome")
                .setThumbnail(new MessageEmbed.Thumbnail(event.getGuild().getIconUrl(), "", 100, 100))
                .setColor(Color.ORANGE)
                .setFields(new ArrayList<>())
        ).queue();
    }
}
