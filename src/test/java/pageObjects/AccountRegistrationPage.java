package pageObjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AccountRegistrationPage extends BasePage {


    public AccountRegistrationPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//input[@id='input-firstname']")
    WebElement txtFirstName;

    @FindBy(xpath = "//input[@id='input-lastname']")
    WebElement txtLastName;

    @FindBy(xpath = "//input[@id='input-email']")
    WebElement txtEmail;

    @FindBy(xpath = "//input[@id='input-telephone']")
    WebElement txtTelePhone;

    @FindBy(xpath = "//input[@id='input-password']")
    WebElement txtPassword;

    @FindBy(xpath = "//input[@id='input-confirm']")
    WebElement txtPasswordConfirm;

    @FindBy(xpath = "//input[@name='agree']")
    WebElement checkPolicy;

    @FindBy(xpath = "//input[@value='Continue']")
    WebElement btnContinue;

    @FindBy(xpath = "//h1[normalize-space()='Your Account Has Been Created!']")
    WebElement msgConfirmation;


    public void setFirstName(String fname) {
        txtFirstName.sendKeys(fname);
    }

    public void setLastName(String lname) {
        txtLastName.sendKeys(lname);
    }

    public void setEmail(String email) {
        txtEmail.sendKeys(email);
    }

    public void setTelePhone(String phone) {
        txtTelePhone.sendKeys(phone);
    }

    public void setPassword(String pwd) {
        txtPassword.sendKeys(pwd);
    }

    public void setConfPassword(String conpwd) {
        txtPasswordConfirm.sendKeys(conpwd);
    }

    public void clickCheckPolicy() {
        Actions act1=new Actions(driver);
       act1.moveToElement(checkPolicy).click().perform();
    }

    public void clickcontinue() {
        //soln1
//        btnContinue.click();

        //soln2
        //btnContinue.submit();

        //soln3
        Actions act = new Actions(driver);
        act.moveToElement(btnContinue).click().perform();



        //soln5
        //btnContinue.sendKeys(Keys.RETURN);

        //soln6
        //WebDriverWait mywait = new WebDriverWait(driver,Duration.ofSeconds(10));
        //mywait.until(ExpectedConditions.elementToBeClickable(btnContinue)).click();
    }

    public String getConfirmationMsg() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement msgElement = wait.until(ExpectedConditions.visibilityOf(msgConfirmation));
            return msgElement.getText();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }


    }
}
