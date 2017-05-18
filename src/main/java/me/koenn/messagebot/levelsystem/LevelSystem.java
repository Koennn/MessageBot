package me.koenn.messagebot.levelsystem;

import org.jnbt.CompoundTag;
import org.jnbt.IntTag;
import org.jnbt.NBTInputStream;
import org.jnbt.Tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, May 2017
 */
public class LevelSystem {

    private HashMap<String, HashMap<String, Integer>> users;

    public void load() throws IOException {
        NBTInputStream stream = new NBTInputStream(new FileInputStream(new File("users.coffeemod")));
        this.users = new HashMap<>();

        Map<String, Tag> users = ((CompoundTag) stream.readTag()).getValue();
        for (String user : users.keySet()) {
            HashMap<String, Integer> userInfo = new HashMap<>();
            Map<String, Tag> userTag = ((CompoundTag) users.get(user)).getValue();
            for (String key : userTag.keySet()) {
                userInfo.put(key, ((IntTag) userTag.get(key)).getValue());
            }
            this.users.put(user, userInfo);
        }
    }

    public int getLevel(String user) {
        return 0;
    }

    public void setLevel(String user, int level) {

    }

    public int getExp(String user) {
        return 0;
    }

    public void setExp(String user, int exp) {

    }

    public static int getExpAtLevel(int level) {
        if (level <= 15) {
            return (2 * level + 7) * 10;
        }
        if ((level >= 16) && (level <= 30)) {
            return (5 * level - 38) * 10;
        }
        return (9 * level - 158) * 10;
    }
}
