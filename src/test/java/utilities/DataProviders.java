package utilities;

import org.testng.annotations.DataProvider;

import java.io.IOException;

public class DataProviders {

    //DataProvider 1

    @DataProvider(name = "LoginData")
    public Object[][] getData() throws IOException {

        String path = ".\\testData\\Opencart_LoginData.xlsx"; //taking xl file from testData

        ExcelUtility xlutil = new ExcelUtility(path); //creating an object for XLUtility

        int totalrows = xlutil.getRowCount("Sheet1");
        int totalcols = xlutil.getCellCount("Sheet1", 1);

        String logindata[][] = new String[totalrows][totalcols]; //created for two dimension array

        for (int i = 1; i <= totalrows; i++) { //1 //read the data from xl storing in two dimensional array
            for (int j = 0; j < totalcols; j++) //0 // j is rows j is col
            {
                logindata[i - 1][j] = xlutil.getCellData("Sheet1", i, j);
            }

        }
        return logindata; //returning two dimensional array
    }
}
