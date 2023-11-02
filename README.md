# TwitterBot
It is a project that aims to log in to Twitter and find popular tweets with Selenium.

It filters the specified minimum number of retweets and likes in the specified language in the Twitter searches section. Converts the posts found in the filter result to PostDTO. 

It deletes from the list the owners of the posts it finds who have more than 800 thousand followers or more than 2000 people they follow(This part has not been developed yet).

It detects the language of the posts it finds with the Apache  OpenNLP algorithm. It continues with those in Turkish, and posts that are not marked as Turkish will not be added to the list. For more information https://opennlp.apache.org/ .

Finally, it aims to share the resulting list as "The 10 Most Interacted Posts of the Day" on a Twitter account every day at 22:00(This part has not been developed yet).

This project was developed in memory of the @fikrithebot account, which works with the Twitter v1 API, but became deactivated due to the end of Twitter's v1 API support and requiring a fee to perform these operations with the v2 API (https://twitter.com/fikrithebot). Best regards @fikrithebot!


## application.properties
To download the Chrome Driver, download the driver suitable for the Chrome version installed from the link and specify its path in application.properties <chrome.driver.path> (https://googlechromelabs.github.io/chrome-for-testing).

set X's username and password in application.properties <twitter.username>, <twitter.password>

<filter.minimum.retweet.count>, <filter.minimum.likes.count> enter the minimum number of likes and retweets to filter.

<filter.language> Enter the abbreviation for the language you want the filter to be valid for. For example at
en, fr. Twitter language abbreviations for https://developer.twitter.com/en/docs/twitter-for-websites/supported-languages.