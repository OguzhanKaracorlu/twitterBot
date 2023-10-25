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

    @Value("${twitter.username}")
    private String twitterUsername;

    @Value("${twitter.password}")
    private String twitterPassword;

    @Value("${twitter.filter}")
    private String filterText;

    /**
     * Start Chrome Page and go to X login page.
     *
     * @throws InterruptedException
     */
    @PostConstruct
    private void openChromeWindow() throws InterruptedException {
        WebDriver chromeWebDriver = loadChromeDriver();
        fillInUsername(chromeWebDriver);
        fillInPassword(chromeWebDriver);
        checkUsernameAndPassword(chromeWebDriver);
        searchFilterOnX(chromeWebDriver);
        closeChromeDriver(chromeWebDriver);
    }

    /**
     * Close the Chrome Driver.
     *
     * @param chromeWebDriver
     */
    private void closeChromeDriver(WebDriver chromeWebDriver) throws InterruptedException {
        Thread.sleep(150000);
        log.info("-----------> Close Chrome Driver");
        chromeWebDriver.close();
    }

    /**
     * Search Filter on X.
     * Filter in the application.properties's twitter.filter.
     *
     * @param chromeWebDriver
     * @throws InterruptedException
     */
    private void searchFilterOnX(WebDriver chromeWebDriver) throws InterruptedException {
        log.info("-----------> Search filter on X");
        WebElement searchTextArea = chromeWebDriver.findElement(By.xpath("//*[@enterkeyhint='search']"));
        LocalDate todayDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayFilter = " since:" + todayDate.format(dateTimeFormatter);
        searchTextArea.sendKeys(filterText + todayFilter);
        Thread.sleep(3000);
        searchTextArea.sendKeys(Keys.RETURN);
    }

    /**
     * Check username and password method.
     *
     * @param chromeWebDriver
     * @throws InterruptedException
     */
    private void checkUsernameAndPassword(WebDriver chromeWebDriver) throws InterruptedException {
        log.info("-----------> Fill in username and password");
        WebElement clickUsernameAndPasswordControl = chromeWebDriver.findElement(By.xpath("//*[@data-testid='LoginForm_Login_Button']"));
        clickUsernameAndPasswordControl.click();
        Thread.sleep(2000);
    }

    /**
     * Fill in Twitter login page's password label.
     *
     * @param chromeWebDriver
     */
    private void fillInPassword(WebDriver chromeWebDriver) throws InterruptedException {
        log.info("-----------> Fill in password");
        WebElement inputFieldPassword = chromeWebDriver.findElement(By.xpath("//*[@autocomplete='current-password']"));
        inputFieldPassword.sendKeys(twitterPassword);
        Thread.sleep(2000);
    }

    /**
     * Fill in Twitter login page's username label.
     *
     * @param chromeWebDriver
     */
    private void fillInUsername(WebDriver chromeWebDriver) throws InterruptedException {
        log.info("-----------> Fill in username");
        WebElement inputFieldUsername = chromeWebDriver.findElement(By.xpath("//*[@autocomplete='username']"));
        inputFieldUsername.sendKeys(twitterUsername);
        Thread.sleep(2000);
        WebElement clickNextButton = chromeWebDriver.findElement(By.xpath("/html/body/div/div/div/div[1]/div/div/div/div/div/div/div[2]/div[2]/div/div/div[2]/div[2]/div/div/div/div[6]"));
        Thread.sleep(2000);
        clickNextButton.click();
        Thread.sleep(2000);
    }

    /**
     * Load Chrome Driver with in application.properties chrome.driver.path.
     *
     * @return
     */
    private WebDriver loadChromeDriver() throws InterruptedException {
        log.info("-----------> Load Chrome Driver");
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(openWebURL);
        Thread.sleep(4000);
        return driver;
    }
}
