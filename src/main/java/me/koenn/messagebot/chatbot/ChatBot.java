package me.koenn.messagebot.chatbot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, April 2017
 */
public class ChatBot {

    private static final String PATTERN = "http://api.program-o.com/v2/chatbot/?bot_id=%s&say=%s&convo_id=%s&format=json";

    public String session;
    public int botId;

    public ChatBot(String session, int botId) {
        this.session = session;
        this.botId = botId;
    }

    public String think(String input) {
        try {
            JSONObject response = readJsonFromUrl(String.format(PATTERN, this.botId, input.replace(" ", "%20"), this.session));
            return response.getString("botsay");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Wow, idk what's happening, but I can't answer you. Ask Koenn for help please!";
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
