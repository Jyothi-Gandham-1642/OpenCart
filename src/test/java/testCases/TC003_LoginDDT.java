package testCases;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccount;
import testBase.BaseClass;

public class TC003_LoginDDT extends BaseClass {

    @Test(dataProvider = "LoginData", dataProviderClass = utilities.DataProviders.class,groups="Datadriven")

    public void verify_loginDDT(String email, String pwd, String exp) {

//        logger.info("********** Starting TC002_LoginTests *********");
try {
    //HomePage
    HomePage hp = new HomePage(driver);
    hp.clickMyAccount();
    hp.clickLogin();

    //Login
    LoginPage lp = new LoginPage(driver);
    lp.getEmailAddress(email);
    lp.getPassword(pwd);
    lp.clickLogin();

    //MyAccount
    MyAccount macc = new MyAccount(driver);
    boolean targetPage = macc.IsMyAccountPageExist();

    if (exp.equalsIgnoreCase("Valid")) {
        if (targetPage == true) {
            macc.clickLogout();
            Assert.assertTrue(true);
        } else {
            Assert.assertTrue(false);
        }
    }

    if (exp.equalsIgnoreCase("Invalid")) {
        if (targetPage == true) {
            macc.clickLogout();
            Assert.assertTrue(false);
        } else {
            Assert.assertTrue(true);
        }
    }

}catch(Exception e ){
    Assert.fail();
}
//        logger.info("********* Finished TC002_LoginTests ********");


    }
}
