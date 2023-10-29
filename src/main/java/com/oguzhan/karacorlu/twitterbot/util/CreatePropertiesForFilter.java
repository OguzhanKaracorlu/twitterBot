package com.oguzhan.karacorlu.twitterbot.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author oguzhan.karacorlu
 * @project twitterBot
 * @created 29.10.2023
 */

@Component
public class CreatePropertiesForFilter {

    @Value("${filter.minimum.retweet.count}")
    private String filterRetweetCount;

    @Value("${filter.minimum.likes.count}")
    private String filterLikesCount;

    @Value("${filter.language}")
    private String filterLanguage;

    /**
     * Creates the filter using other methods. It is the only method accessible from outside.
     *
     * @return
     */
    public String createFullFilter() {
        return createMinimumRetweetsFilter() + " and " + createMinimumLikesFilter() + createTodayFormatFilter() + createLanguageFilter();
    }

    /**
     * Create Language Filter for X's filter.
     *
     * @return
     */
    private String createLanguageFilter() {
        return " lang:" + filterLanguage;
    }

    /**
     * Create Minimum Likes Filter for X's filter.
     *
     * @return
     */
    private String createMinimumLikesFilter() {
        return "min_faves:" + filterLikesCount;
    }

    /**
     * Create Minimum Retweets Filter for X's filter.
     *
     * @return
     */
    private String createMinimumRetweetsFilter() {
        return " min_retweets:" + filterRetweetCount;
    }

    /**
     * Create Today Formatter for X's filter.
     *
     * @return
     */
    private String createTodayFormatFilter() {
        LocalDate todayDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return " since:" + todayDate.format(dateTimeFormatter);
    }

}
