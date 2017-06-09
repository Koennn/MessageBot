package me.koenn.messagebot;

import me.koenn.messagebot.util.Logger;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.impl.GameImpl;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, May 2017
 */
public class DiscordBot {

    private JDA jda;

    public DiscordBot(String token, String name) {
        Logger.info("Loading Discord Bot \'" + name + "\'...");
        try {
            this.jda = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .setEnableShutdownHook(true)
                    .setAutoReconnect(true)
                    .setGame(new GameImpl("with you!", "", Game.GameType.DEFAULT))
                    .buildAsync();
            this.jda.setAutoReconnect(true);
        } catch (LoginException e) {
            Logger.error("Failed to log in to Discord with token \'" + token + "\'");
        } catch (RateLimitedException e) {
            Logger.error("Rate limit exception! \'" + e + "\'");
        }
    }

    public void addListeners(ListenerAdapter... listeners) {
        for (ListenerAdapter listener : listeners) {
            this.jda.addEventListener(listener);
        }
    }

    public JDA getJDA() {
        return jda;
    }
}
