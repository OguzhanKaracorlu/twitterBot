package com.oguzhan.karacorlu.twitterbot.util;

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

    /**
     * Its function detects the language of the content of the sent post.
     * Returns the abbreviation of the most likely result as a String.
     *
     * @param tweet
     * @return
     */
    public String detectionTweetLanguage(String tweet){
        try {
            LanguageDetectorModel model = new LanguageDetectorModel(new File("src/main/resources/langdetect-183.bin"));
            LanguageDetector languageDetector = new LanguageDetectorME(model);
            Language[] languages = languageDetector.predictLanguages(tweet);
            String detectedLanguage = languages[0].getLang();

            System.out.println("Metin dili: " + detectedLanguage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
