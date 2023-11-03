package com.oguzhan.karacorlu.twitterbot.util;

import lombok.SneakyThrows;
import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetector;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;

import java.io.File;

/**
 * @author oguzhan.karacorlu
 * @project twitterBot
 * @created 02.11.2023
 */
public class OpenNLPLanguageDetection {


    private static OpenNLPLanguageDetection instance;

    public static OpenNLPLanguageDetection getInstance() {
        if (instance == null) {
            instance = new OpenNLPLanguageDetection();
        }
        return instance;
    }

    /**
     * Its function detects the language of the content of the sent post.
     * Returns the abbreviation of the most likely result as a String.
     *
     * @param tweet
     * @return
     */
    @SneakyThrows
    public Boolean detectionTweetLanguage(String tweet) {
        LanguageDetectorModel model = new LanguageDetectorModel(new File("src/main/resources/langdetect-183.bin"));
        LanguageDetector languageDetector = new LanguageDetectorME(model);
        Language[] languages = languageDetector.predictLanguages("selam merhaba");
        return languages[0].getLang().contains("tur");
    }
}
