package com.oguzhan.karacorlu.twitterbot.service;

import com.oguzhan.karacorlu.twitterbot.dto.PostDTO;
import com.oguzhan.karacorlu.twitterbot.util.CreatePropertiesForFilter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
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

    private HashMap<String,PostDTO> postList = new HashMap<>();

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
        //getTweetsAnalytics(chromeWebDriver);
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
        for (int i = 0; i < 10; i++) {
            JavascriptExecutor js = (JavascriptExecutor) chromeWebDriver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight/3);");
            try {
                Thread.sleep(2000);
                getTweetsAnalytics(chromeWebDriver);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (PostDTO postDTO : postList.values()){
            System.out.println(postDTO.toString());
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
                postDTO.setResponsesCount(Integer.valueOf(tweet[tweet.length - 4].replace("B", "000").replace("Mn", "00000").replaceAll("\\s", "")));
                postDTO.setRetweetsCount(Integer.valueOf(tweet[tweet.length - 3].replace("B", "000").replace("Mn", "00000").replaceAll("\\s", "")));
                postDTO.setLikesCount(Integer.valueOf(tweet[tweet.length - 2].replace("B", "000").replace("Mn", "00000").replaceAll("\\s", "")));
                postDTO.setViewsCount(Integer.valueOf(tweet[tweet.length - 1].replace("B", "000").replace("Mn", "00000").replaceAll("\\s", "")));
                postList.put(postLink, postDTO);
            }
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
