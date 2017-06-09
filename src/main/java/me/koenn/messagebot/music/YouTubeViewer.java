package me.koenn.messagebot.music;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.*;
import me.koenn.messagebot.util.Logger;

import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.util.Locale;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, May 2017
 */
public class YouTubeViewer {

    public static void main(String[] args) {
        NativeInterface.open();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("YouTube Player");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.getContentPane().add(getBrowserPanel(), BorderLayout.CENTER);
            frame.setSize(300, 300);
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
            frame.setLocale(Locale.ENGLISH);
        });
        NativeInterface.runEventPump();
        Runtime.getRuntime().addShutdownHook(new Thread(NativeInterface::close));
    }

    public static JPanel getBrowserPanel() {
        JPanel webBrowserPanel = new JPanel(new BorderLayout());
        JWebBrowser webBrowser = new JWebBrowser();
        webBrowser.setLocale(Locale.ENGLISH);
        webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
        webBrowserPanel.setLocale(Locale.ENGLISH);
        webBrowser.setBarsVisible(false);
        webBrowser.setHTMLContent("" +
                "<iframe\n" +
                "        width=\"0\" height=\"0\"\n" +
                "        style=\"visibility: hidden;\"\n" +
                "        src=\"https://www.youtube.com/embed/1-AODuJpCG4?autoplay=1&disablekb=1&rel=0&iv_load_policy=3&vq=small\"\n" +
                ">\n" +
                "</iframe>\n");
        webBrowser.addWebBrowserListener(new WebBrowserAdapter() {
            @Override
            public void statusChanged(WebBrowserEvent e) {
                Logger.info("Status changed to \'" + e.getWebBrowser().getStatusText() + "\'");
                String status = e.getWebBrowser().getStatusText();
                if (status.startsWith("Afbeelding downloaden: http") && status.endsWith("...")) {
                    System.exit(0);
                }
            }
        });
        return webBrowserPanel;
    }
}
