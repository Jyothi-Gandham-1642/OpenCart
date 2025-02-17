package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccount;
import testBase.BaseClass;
import java.time.Duration;

public class TC002_LoginTests extends BaseClass {

    @Test(groups = {"Sanity", "Master"})
    public void verify_login() {
        logger.info("********** Starting TC002_LoginTests *********");

        try {
            // HomePage
            HomePage hp = new HomePage(driver);
            hp.clickMyAccount();
            hp.clickLogin();

            // Login Page
            LoginPage lp = new LoginPage(driver);
            lp.getEmailAddress(p.getProperty("email"));
            lp.getPassword(p.getProperty("password"));
            lp.clickLogin();

            // Wait for the MyAccount page to be visible
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.urlContains("route=account"));

            // MyAccount
            MyAccount macc = new MyAccount(driver);
            boolean targetPage = macc.IsMyAccountPageExist();

            if (!targetPage) {
                logger.error("Login failed. Page source:\n" + driver.getPageSource());
            }

            Assert.assertTrue(targetPage, "Login failed. My Account page not found.");

        } catch (Exception e) {
            logger.error("Exception in verify_login: " + e.getMessage());
            Assert.fail();
        }

        logger.info("********* Finished TC002_LoginTests ********");
    }
}
