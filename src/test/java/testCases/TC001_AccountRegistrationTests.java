package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBase.BaseClass;
import java.time.Duration;

public class TC001_AccountRegistrationTests extends BaseClass {

    @Test(groups = {"Regression", "Master"})
    public void verify_account_registration() {
        logger.info("***** Starting TC001_AccountRegistrationTests *****");

        try {
            HomePage hp = new HomePage(driver);
            hp.clickMyAccount();
            logger.info("Clicked on MYAccount Link");

            hp.clickRegister();
            logger.info("Clicked on Register Link");

            AccountRegistrationPage regpage = new AccountRegistrationPage(driver);

            logger.info("Providing Customer Details.....");
            regpage.setFirstName(randomeString().toUpperCase());
            regpage.setLastName(randomeString().toUpperCase());
            regpage.setEmail(randomeString() + "@gmail.com");
            regpage.setTelePhone(randomeNumber());

            String password = randomAlphaNumeric();
            regpage.setPassword(password);
            regpage.setConfPassword(password);

            regpage.clickCheckPolicy();
            regpage.clickcontinue();

            logger.info("Waiting for confirmation message...");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement confirmationElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Your Account Has Been Created!')]")));

            String confmsg = confirmationElement.getText();
            logger.info("Confirmation Message: " + confmsg);

            // Check for unexpected warnings/errors
            if (driver.getPageSource().contains("Warning")) {
                logger.error("Warning found on page! Registration may have failed.");
                Assert.fail("Registration failed due to a warning message.");
            }

            // Validate confirmation message
            Assert.assertEquals(confmsg, "Your Account Has Been Created!", "Registration message did not match");

        } catch (Exception e) {
            logger.error("Exception in verify_account_registration: " + e.getMessage());
            Assert.fail();
        }
    }
}
