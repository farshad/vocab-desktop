package com.vocab.utils;

import java.io.File;
import java.io.IOException;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 2/4/25 - 11:13 AM
 */
public class VoiceHelper {


    private static final String CACHE_DIR = System.getProperty("user.home") + "/vocab";

    public static void speak(String text, String language) {
        try {
            // Ensure cache directory exists
            File dir = new File(CACHE_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Generate a filename based on the text
            String safeText = text.replaceAll("[^a-zA-Z0-9]", "_");
            String filename = CACHE_DIR + "/" + safeText + "_" + language + ".mp3";

            // Check if file already exists
            File audioFile = new File(filename);
            if (!audioFile.exists()) {
                String generateCommand = String.format("gtts-cli -l %s \"%s\" -o \"%s\"", language, text, filename);
                Process generateProcess = new ProcessBuilder("bash", "-c", generateCommand).start();
                generateProcess.waitFor();
            }

            // Play the cached file
            String playCommand = String.format("ffplay -autoexit -nodisp \"%s\"", filename);
            Process playProcess = new ProcessBuilder("bash", "-c", playCommand).start();
            playProcess.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
