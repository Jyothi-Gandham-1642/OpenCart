package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import testBase.BaseClass;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExtentReportManager implements ITestListener {
    public ExtentSparkReporter sparkReporter; // UI of the report
    public ExtentReports extent; //populate common info on the report
    public ExtentTest test; //creating test case entries in the report and update status of the test methods

    String repName;

    public void onStart(ITestContext testContext) {

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        repName = "Test-Report-" + timeStamp + ".html";
        sparkReporter = new ExtentSparkReporter(".\\reports\\" + repName);

        sparkReporter.config().setDocumentTitle("Opencart Automation Report"); //Title of report
        sparkReporter.config().setReportName("opencart Functional Testing"); //name of the report
        sparkReporter.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Application", "opencart");
        extent.setSystemInfo("Module", "Admin");
        extent.setSystemInfo("Sub Module", "Customers");
        extent.setSystemInfo("User Name", System.getProperty("user.name"));
        extent.setSystemInfo("Environment", "QA");

        String os = testContext.getCurrentXmlTest().getParameter("os");
        extent.setSystemInfo("Operating System", os);

        String browser = testContext.getCurrentXmlTest().getParameter("browser");
        extent.setSystemInfo("Browser", browser);

        List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
        if (!includedGroups.isEmpty()) {
            extent.setSystemInfo("Groups", includedGroups.toString());
        }
    }


    public void onTestSuccess(ITestResult result){

        test = extent.createTest(result.getTestClass().getName()); //create a new entry in the report
        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.PASS,result.getName()+"got successfully executed"); //update status p/f/s

    }

    public void onTestFailure(ITestResult result) {
        test = extent.createTest(result.getTestClass().getName());
        test.assignCategory(result.getMethod().getGroups());

        test.log(Status.FAIL, result.getName() + " got failed");
        test.log(Status.INFO, result.getThrowable().getMessage());

        try {
            // Get the test instance and capture screenshot
            BaseClass base = (BaseClass) result.getInstance();
            String imgPath = base.captureScreen(result.getName()).getAbsolutePath();

            // Attach the screenshot to the report
            test.addScreenCaptureFromPath(imgPath);
        } catch (Exception e) { // Handle any unexpected errors
            e.printStackTrace();
        }
    }

    public void onTestSkipped(ITestResult result){
        test=extent.createTest(result.getTestClass().getName());
        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.SKIP,result.getName()+"got skipped");
        test.log(Status.INFO,result.getThrowable().getMessage());
    }

    public void onFinish(ITestContext testContext){
        extent.flush();

        File pathOfExtentReport = new File("C:\\OpenCart\\reports\\" +repName);
        File extentReport = new File(String.valueOf(pathOfExtentReport));

        try{
            Desktop.getDesktop().browse(extentReport.toURI());
        }catch(Exception e){
            e.printStackTrace();
        }


//        try{
//
//            URL url=new URL("C:\\\\OpenCart\\\\reports\\\\\" +repName");
//            //create the email message
//            ImageHtmlEmail email new ImageHtmlEmail();
//            email.setDataSourceResolver(new DataSourceUrlResolver(url));
//            email.setHostName("smtp.googlemail.com");
//            email.setSmtpPort(465);
//            email.setAuthenticator(new DefaultAuthenticator("jyothi@mailinator.com","password"));
//            email.setSSLOnConnect(true);
//            email.setFrom("jyothi@mailinmator.com");
//            email.setSubject("Test Result");
//            email.setMsg("Please find Attached Report....");
//            email.addTo("jyothi.email.com");
//            email.attach(url,"extent report","please check report...");
//            email.send();
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//
  }
//




}





