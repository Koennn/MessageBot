package me.koenn.messagebot.levelsystem;

import me.koenn.messagebot.MessageBot;
import me.koenn.messagebot.util.JSONManager;
import me.koenn.messagebot.util.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, May 2017
 */
public class LevelSystem implements Runnable {

    private HashMap<String, LevelUser> users = new HashMap<>();
    private final JSONManager jsonManager = new JSONManager("users.json");

    public void load() throws IOException {
        if (jsonManager.getFromBody("users") != null) {
            JSONArray users = (JSONArray) jsonManager.getFromBody("users");
            for (Object userObject : users) {
                JSONObject user = (JSONObject) userObject;
                this.users.put((String) user.get("name"), new LevelUser(Math.toIntExact((long) user.get("level")), Math.toIntExact((long) user.get("exp"))));
            }
        }

        MessageBot.threadManager.createThread("level-saver-thread", this, true);
    }

    public void save() {
        JSONArray users = new JSONArray();
        for (String username : this.users.keySet()) {
            LevelUser levelUser = this.users.get(username);
            JSONObject userObject = new JSONObject();
            userObject.put("name", username);
            userObject.put("level", levelUser.getLevel());
            userObject.put("exp", levelUser.getExp());
            users.add(userObject);
        }
        jsonManager.setInBody("users", users);
    }

    public int getLevel(String user) {
        return this.users.containsKey(user) ? this.users.get(user).getLevel() : 0;
    }

    public void setLevel(String user, int level) {
        LevelUser levelUser = this.users.containsKey(user) ? this.users.get(user) : new LevelUser();
        levelUser.setLevel(level);
        this.users.put(user, levelUser);
    }

    public int getExp(String user) {
        return this.users.containsKey(user) ? this.users.get(user).getExp() : 0;
    }

    public void setExp(String user, int exp) {
        LevelUser levelUser = this.users.containsKey(user) ? this.users.get(user) : new LevelUser();
        levelUser.setExp(exp);
        this.users.put(user, levelUser);
    }

    public static int getExpAtLevel(int level) {
        if (level <= 15) {
            return (2 * level + 7) * 2;
        }
        if ((level >= 16) && (level <= 30)) {
            return (5 * level - 38) * 2;
        }
        return (9 * level - 158) * 2;
    }

    @Override
    public void run() {
        Logger.info("Saving user data...");
        this.save();
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread interrupted: " + e);
        }
    }
}
