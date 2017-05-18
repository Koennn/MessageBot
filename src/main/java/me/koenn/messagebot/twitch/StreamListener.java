package me.koenn.messagebot.twitch;

import com.mb3364.twitch.api.models.Stream;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, May 2017
 */
public interface StreamListener {

    void onStreamStart(Stream stream);
}
