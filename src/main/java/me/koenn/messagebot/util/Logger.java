package me.koenn.messagebot.util;

import net.dv8tion.jda.core.utils.SimpleLog;

public final class Logger {

    public static final SimpleLog LOG = SimpleLog.getLog("DiscordBot");

    public static void log(Level level, Object message) {
        switch (level) {
            case INFO:
                LOG.info(message);
                break;
            case DEBUG:
                LOG.debug(message);
                break;
            case ERROR:
                LOG.fatal(message);
                break;
        }
    }

    public static void info(Object message) {
        log(Level.INFO, message);
    }

    public static void error(Object message) {
        log(Level.ERROR, message);
    }

    public static void debug(Object message) {
        log(Level.DEBUG, message);
    }

    public enum Level {
        DEBUG, INFO, ERROR
    }
}
