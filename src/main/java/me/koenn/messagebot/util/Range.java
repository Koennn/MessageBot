package me.koenn.messagebot.util;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, May 2017
 */
public class Range {

    private final int int1;
    private final int int2;

    public Range(int int1, int int2) {
        this.int1 = int1;
        this.int2 = int2;
    }

    public boolean isInRange(int number) {
        return number >= int1 && number <= int2;
    }
}
