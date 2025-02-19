package testBase;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

public class BaseClass {

    public WebDriver driver;
    public Logger logger;
    public Properties p;

    @BeforeClass(groups = {"Sanity", "Regression", "Master"})
    @Parameters({"os", "browser"})
    public void setup(String os, String br) throws IOException {

        FileReader file = new FileReader("./src/test/resources/config.properties");
        p = new Properties();
        p.load(file);

        logger = LogManager.getLogger(this.getClass());
        WebDriverManager.chromedriver().setup();
        WebDriverManager.edgedriver().setup();


        ChromeOptions chromeOptions = new ChromeOptions();
        EdgeOptions edgeOptions = new EdgeOptions();

        chromeOptions.addArguments("--remote-allow-origins=*");
        edgeOptions.addArguments("--remote-allow-origins=*");

        if (p.getProperty("execute_env").equalsIgnoreCase("remote")) {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            if (os.equalsIgnoreCase("windows")) {
                capabilities.setPlatform(Platform.WIN10);
            } else if (os.equalsIgnoreCase("mac")) {
                capabilities.setPlatform(Platform.MAC);
            } else if (os.equalsIgnoreCase("linux")) {
                capabilities.setPlatform(Platform.LINUX);
            } else {
                logger.error("No matching OS");
                return;
            }

            int retries = 3;
            while (retries > 0) {
                try {
                    if (br.equalsIgnoreCase("chrome")) {
                        capabilities.setBrowserName("chrome");
                        chromeOptions.merge(capabilities);
                        driver = new RemoteWebDriver(new URL("http://localhost:4444"), chromeOptions);
                    } else if (br.equalsIgnoreCase("edge")) {
                        capabilities.setBrowserName("MicrosoftEdge");
                        edgeOptions.merge(capabilities);
                        driver = new RemoteWebDriver(new URL("http://localhost:4444"), edgeOptions);
                    } else {
                        logger.error("Invalid Browser name for remote execution");
                        return;
                    }
                    break;
                } catch (Exception e) {
                    logger.error("Remote WebDriver initialization failed. Retrying...");
                    retries--;
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ignored) {}
                }
            }
            if (driver == null) throw new WebDriverException("Failed to initialize WebDriver after retries.");

        } else {
            if (br.equalsIgnoreCase("chrome")) {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(chromeOptions);
            } else if (br.equalsIgnoreCase("edge")) {
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver(edgeOptions);
            } else {
                logger.error("Invalid Browser name");
                return;
            }

        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().deleteAllCookies();
        driver.get(p.getProperty("appURL1"));
        driver.manage().window().maximize();
    }

    @AfterClass(groups = {"Sanity", "Regression", "Master"})
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public String randomeString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public String randomeNumber() {
        return RandomStringUtils.randomNumeric(10);
    }

    public String randomAlphaNumeric() {
        return RandomStringUtils.randomAlphabetic(3) + "*" + RandomStringUtils.randomAlphanumeric(3);
    }

    public File captureScreen(String tname) {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;

        File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
        File targetFile = new File("C:\\OpenCart\\screenshots\\" + tname + "_" + timeStamp + ".png");

        try {
            FileUtils.copyFile(sourceFile, targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return targetFile;
    }
}
