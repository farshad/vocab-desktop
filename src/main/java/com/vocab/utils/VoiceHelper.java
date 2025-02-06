package com.vocab.utils;

import java.io.IOException;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 2/4/25 - 11:13 AM
 */
public class VoiceHelper {

    public static void speak(String text, String language) {
        String command = String.format("gtts-cli -l %s \"%s\" | ffplay -autoexit -nodisp -", language, text);
        try {
            Process process = new ProcessBuilder("bash", "-c", command).start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
