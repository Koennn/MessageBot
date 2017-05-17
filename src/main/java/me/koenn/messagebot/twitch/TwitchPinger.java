package me.koenn.messagebot.twitch;

import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.handlers.StreamResponseHandler;
import com.mb3364.twitch.api.models.Stream;
import me.koenn.messagebot.util.Logger;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, May 2017
 */
public class TwitchPinger implements Runnable {

    private final Twitch twitch;
    private boolean enabled;

    public TwitchPinger() {
        Logger.info("Starting Twitch API pinger.");

        this.twitch = new Twitch();
        this.twitch.setClientId("fujr92llfqko02qx1zjh84fu8qwzcf");

        this.enabled = true;
        new Thread(this, "twitch-pinger-thread");
    }

    @Override
    public void run() {
        while (this.enabled) {
            this.twitch.streams().get("thekoenn", new StreamResponseHandler() {
                @Override
                public void onSuccess(Stream stream) {
                    Logger.debug(stream.getGame());
                }

                @Override
                public void onFailure(int statusCode, String statusMessage, String errorMessage) {
                    Logger.error("Twitch responded with error code \'" + statusCode + "\': \'" + statusMessage + "\': \'" + errorMessage + "\'");
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Logger.error("Unable to access Twitch API: \'" + throwable + "\'");
                }
            });
        }
        Logger.info("Shutting down Twitch API pinger.");
    }

    public void disable() {
        this.enabled = false;
    }
}
