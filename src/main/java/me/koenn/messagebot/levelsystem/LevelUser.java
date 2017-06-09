package me.koenn.messagebot.levelsystem;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, May 2017
 */
public class LevelUser {

    private int level;
    private int exp;

    public LevelUser() {
        this(0, 0);
    }

    public LevelUser(int level, int exp) {
        this.level = level;
        this.exp = exp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
