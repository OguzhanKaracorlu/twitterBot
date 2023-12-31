package com.oguzhan.karacorlu.twitterbot.service;

import com.oguzhan.karacorlu.twitterbot.dto.PostDTO;
import com.oguzhan.karacorlu.twitterbot.util.CreatePropertiesForFilter;
import com.oguzhan.karacorlu.twitterbot.util.OpenNLPLanguageDetection;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author : oguzhan.karacorlu
 * @project : twitterbot
 * @mailaddress : oguzhan.karacorlu@gmail.com
 * @created : 25.10.2023, Çarşamba
 */

@Component
@Log4j2
public class TwitterSeleniumService {

    @Value("${chrome.driver.path}")
    private String chromeDriverPath;

    @Value("${open.web.url}")
    private String openWebURL;

    @Value("${x.username}")
    private String twitterUsername;

    @Value("${x.password}")
    private String twitterPassword;

    private ConcurrentHashMap<String, PostDTO> postList = new ConcurrentHashMap<>();

    private final CreatePropertiesForFilter createPropertiesForFilter;

    public TwitterSeleniumService(CreatePropertiesForFilter createPropertiesForFilter) {
        this.createPropertiesForFilter = createPropertiesForFilter;
    }

    /**
     * Start Chrome Page and go to X login page.
     *
     * @throws InterruptedException
     */
    @PostConstruct
    private void openChromeWindow() {
        WebDriver chromeWebDriver = loadChromeDriver();
        fillInUsername(chromeWebDriver);
        fillInPassword(chromeWebDriver);
        checkUsernameAndPassword(chromeWebDriver);
        searchFilterOnX(chromeWebDriver);
        scrollToEndPage(chromeWebDriver);
        checkUserProfileSuitability(chromeWebDriver);
        deleteFalseUserProfileSuitability();
        sortList();
        closeChromeDriver(chromeWebDriver);
    }

    /**
     * Load Chrome Driver with in application.properties chrome.driver.path.
     *
     * @return
     */
    private WebDriver loadChromeDriver() {
        log.info("-----------> Load Chrome Driver");
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(openWebURL);
        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
        return driver;
    }

    /**
     * Fill in Twitter login page's username label.
     *
     * @param chromeWebDriver
     */
    private void fillInUsername(WebDriver chromeWebDriver) {
        log.info("-----------> Fill in username");
        WebElement inputFieldUsername = chromeWebDriver.findElement(By.xpath("//*[@autocomplete='username']"));
        inputFieldUsername.sendKeys(twitterUsername);
        chromeWebDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        WebElement clickNextButton = chromeWebDriver.findElement(By.xpath("/html/body/div/div/div/div[1]/div/div/div/div/div/div/div[2]/div[2]/div/div/div[2]/div[2]/div/div/div/div[6]"));
        chromeWebDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        clickNextButton.click();
        chromeWebDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    /**
     * Fill in Twitter login page's password label.
     *
     * @param chromeWebDriver
     */
    private void fillInPassword(WebDriver chromeWebDriver) {
        log.info("-----------> Fill in password");
        WebElement inputFieldPassword = chromeWebDriver.findElement(By.xpath("//*[@autocomplete='current-password']"));
        inputFieldPassword.sendKeys(twitterPassword);
        chromeWebDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    /**
     * Check username and password method.
     *
     * @param chromeWebDriver
     * @throws InterruptedException
     */
    private void checkUsernameAndPassword(WebDriver chromeWebDriver) {
        log.info("-----------> Fill in username and password");
        WebElement clickUsernameAndPasswordControl = chromeWebDriver.findElement(By.xpath("//*[@data-testid='LoginForm_Login_Button']"));
        clickUsernameAndPasswordControl.click();
        chromeWebDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

    /**
     * Search Filter on X.
     * Filter in the application.properties's twitter.filter.
     *
     * @param chromeWebDriver
     * @throws InterruptedException
     */
    private void searchFilterOnX(WebDriver chromeWebDriver) {
        log.info("-----------> Search filter on X");
        WebElement searchTextArea = chromeWebDriver.findElement(By.xpath("//*[@enterkeyhint='search']"));
        searchTextArea.sendKeys(createPropertiesForFilter.createFullFilter());
        searchTextArea.sendKeys(Keys.RETURN);
        chromeWebDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    /**
     * It lowers the scroll bar to the bottom to load all posts.
     *
     * @param chromeWebDriver
     */
    //TODO Ask Ömer if the scroll doesn't go all the way down to the end.
    private void scrollToEndPage(WebDriver chromeWebDriver) {
        getTweetsAnalytics(chromeWebDriver);
        JavascriptExecutor js = (JavascriptExecutor) chromeWebDriver;
        for (int i = 0; i < 10; i++) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight/3);");
            try {
                Thread.sleep(2000);
                getTweetsAnalytics(chromeWebDriver);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        chromeWebDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    /**
     * The tweets found as a result of the filtering process are converted into a java object.
     *
     * @param chromeWebDriver
     */
    private void getTweetsAnalytics(WebDriver chromeWebDriver) {

        List<WebElement> twits = chromeWebDriver.findElements(By.xpath("//*[@data-testid='cellInnerDiv']"));

        for (WebElement twit : twits) {
            PostDTO postDTO = new PostDTO();
            String tweetText = twit.getText();
            String[] tweet = tweetText.split("\n");
            if (!tweetText.isEmpty() && tweet.length > 4) {
                String postLink = chromeWebDriver.findElement(By.xpath("//a[starts-with(@href, '/" + tweet[1].replaceAll("@", "") + "/status/')]")).getAttribute("href");
                postDTO.setPostLink(postLink);
                postDTO.setUserName(tweet[1]);
                postDTO.setResponsesCount(Integer.valueOf(tweet[tweet.length - 4].replaceAll("B|K", "000").replaceAll("Mn|M", "00000").replaceAll("[,\\.\\p{Z}]", "")));
                postDTO.setRetweetsCount(Integer.valueOf(tweet[tweet.length - 3].replaceAll("B|K", "000").replaceAll("Mn|M", "00000").replaceAll("[,\\.\\p{Z}]", "")));
                postDTO.setLikesCount(Integer.valueOf(tweet[tweet.length - 2].replaceAll("B|K", "000").replaceAll("Mn|M", "00000").replaceAll("[,\\.\\p{Z}]", "")));
                postDTO.setViewsCount(Integer.valueOf(tweet[tweet.length - 1].replaceAll("B|K", "000").replaceAll("Mn|M", "00000").replaceAll("[,\\.\\p{Z}]", "")));
                if (OpenNLPLanguageDetection.getInstance().detectionTweetLanguage(twit.getText())) {
                    postList.put(postLink, postDTO);
                }
            }
        }
    }

    /**
     * Users' following and number of followers are checked.
     */
    private void checkUserProfileSuitability(WebDriver chromeWebDriver) {
        for (String key : postList.keySet()) {
            PostDTO postDTO = postList.get(key);
            String username = postDTO.getUserName().replaceAll("@", "");
            chromeWebDriver.get("https://twitter.com/" + username);
            chromeWebDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            List<WebElement> elements = chromeWebDriver.findElements(By.xpath("//*[@class='css-901oao css-16my406 r-1nao33i r-poiln3 r-1b43r93 r-b88u0q r-1cwl3u0 r-bcqeeo r-qvutc0']"));
            if (elements.size() >= 2) {
                WebElement firstElement = elements.get(0);
                WebElement secondElement = elements.get(1);
                int following = Integer.parseInt(firstElement.getAttribute("innerText").replaceAll("B|K", "00").replaceAll("Mn|M", "000000").replaceAll("[,\\.\\p{Z}]", ""));
                int followers = Integer.parseInt(secondElement.getAttribute("innerText").replaceAll("B|K", "00").replaceAll("Mn|M", "000000").replaceAll("[,\\.\\p{Z}]", ""));
                postDTO.setUserFollowedCount(following);
                postDTO.setUserFollowersCount(followers);
                postDTO.setUserProfileSuitability(following <= 2000 && followers <= 800000);
                postList.put(key, postDTO);
            }
        }
    }

    /**
     * Deletes unsuitable profiles from postList
     */
    private void deleteFalseUserProfileSuitability() {
        for (String key : postList.keySet()) {
            PostDTO postDTO = postList.get(key);
            if (!postDTO.getUserProfileSuitability()) {
                postList.remove(key,postDTO);
            } else {
                postDTO.setTotalInteraction(postDTO.getLikesCount() + postDTO.getResponsesCount() + postDTO.getRetweetsCount() + postDTO.getViewsCount());
            }
        }

        for (PostDTO postDTO2 : postList.values()) {
            System.out.println(postDTO2.toString());
        }

    }

    /**
     * Sorts the list by total number of interactions.
     */
    private void sortList() {
        List<Map.Entry<String, PostDTO>> sortedList = postList.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().getTotalInteraction().compareTo(entry1.getValue().getTotalInteraction()))
                .collect(Collectors.toList());

        System.out.println("--- " + sortedList);
    }

    /**
     * Close the Chrome Driver.
     *
     * @param chromeWebDriver
     */
    private void closeChromeDriver(WebDriver chromeWebDriver) {
        chromeWebDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        try {
            Thread.sleep(150000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("-----------> Close Chrome Driver");
        chromeWebDriver.close();
    }
}
