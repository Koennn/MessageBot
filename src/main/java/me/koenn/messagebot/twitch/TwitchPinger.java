package me.koenn.messagebot.twitch;

import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.handlers.StreamResponseHandler;
import com.mb3364.twitch.api.models.Stream;
import me.koenn.messagebot.MessageBot;
import me.koenn.messagebot.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, May 2017
 */
public class TwitchPinger implements Runnable {

    public final List<StreamListener> listeners = new ArrayList<>();
    public final String[] streamers = new String[]{"thekoenn"};
    private final Twitch twitch;
    private boolean online;

    public TwitchPinger() {
        Logger.info("Starting Twitch API pinger.");

        this.twitch = new Twitch();
        this.twitch.setClientId("fujr92llfqko02qx1zjh84fu8qwzcf");

        MessageBot.threadManager.createThread("twitch-pinger-thread", this, true);
    }

    @Override
    public void run() {
        MessageBot.threadManager.createThread("ping-receive-thread", () -> {
            for (String streamer : this.streamers) {
                this.twitch.streams().get(streamer, new StreamResponseHandler() {
                    @Override
                    public void onSuccess(Stream stream) {
                        Logger.debug(stream);
                        if (/*!online && */stream != null) {
                            listeners.forEach(streamListener -> streamListener.onStreamStart(stream));
                        }
                        online = stream != null;
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
        });

        Logger.debug("Waiting for ping response...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Logger.error("Pinger thread interrupted: " + e);
        }
        Logger.debug("Starting new ping cycle...");
    }
}
