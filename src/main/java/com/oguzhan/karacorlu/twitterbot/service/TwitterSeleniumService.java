package com.oguzhan.karacorlu.twitterbot.service;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Value("${filter.minimum.retweet.count}")
    private String filterRetweetCount;

    @Value("${filter.minimum.likes.count}")
    private String filterLikesCount;

    @Value("${filter.language}")
    private String filterLanguage;

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
        getTweetsAnalytics(chromeWebDriver);
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
        searchTextArea.sendKeys(createMinimumRetweetsFilter() + " and " + createMinimumLikesFilter() + createTodayFormatFilter() + createLanguageFilter());
        chromeWebDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        searchTextArea.sendKeys(Keys.RETURN);
    }

    /**
     * The tweets found as a result of the filtering process are converted into a java object.
     *
     * @param chromeWebDriver
     */
    private void getTweetsAnalytics(WebDriver chromeWebDriver) {

        List<WebElement> twits = chromeWebDriver.findElements(By.xpath("//*[@data-testid='cellInnerDiv']"));

        for (WebElement twit : twits) {
            String tweetText = twit.getText();
            System.out.println(tweetText);
        }
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
