package testcases_DoctorModules;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import methods.ExcelUtil;
import methods.OpenURL;
import methods.Utility;

public class Curie_DOC_FUN_TS002_InvLogin extends OpenURL
{
	Logger logger=Logger.getLogger(Curie_DOC_FUN_TS002_InvLogin.class.getName());
	
@Test(dataProvider="InvalidLoginData")
public void Invalidlogin(String rowno,String username,String password,String name,String Result) throws Exception
{
	prop=new Properties();
	FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"\\Objects\\objects.properties");
	prop.load(fis);
						 
	//To validate the title of URL
	AssertJUnit.assertEquals(prop.getProperty("Title"),driver.getTitle());
						  
	//Calling Utility method for elements xpath and screenshots
    Utility util = new Utility(driver, prop, logger);
    util.startTestCase("Curie_DOC_FUN_TS002_InvLogin");
	Thread.sleep(5000);
							 
    //To click on login button in Home page:	 
	util.perform1("xpath","click","btn_homelogin",""); 
   	util.perform1("id","settext","txt_username",username);
	util.perform1("id","settext","txt_password",password);		
	util.capturescreenshots("Curie_DOC_FUN_TS002_InvLogin", "Loginfailure");			
	Thread.sleep(5000);
	util.perform1("id","click","btn_login","");
	logger.info("Data Entered :------> " + "[" + username +"," + password +"]");
				
	if(driver.findElement(By.id("snackbarnew")).isDisplayed())
	{
		System.out.println("snackbar is displayed");
		String errmsg=driver.findElement(By.id("snackbarnew")).getText();
		logger.info("Error Message in login page is-------->" + "[" + errmsg +"]");
	}
	 //Write Excel
	BigDecimal bd = new BigDecimal(rowno);
    int rowno1 = bd.intValue();
			
	@SuppressWarnings("unused")
	Boolean dd3;
	try
	  {	
		
		dd3 =driver.findElement(By.id("setschedule")).isDisplayed();
	    //Clicking menu button				
		logger.info("Successfully Logged In");
		logger.info("Data Entered :----------> " + "[" + username +"," + password +"]");
		util.capturescreenshots("Curie_DOC_FUN_TS002_InvLogin", "SuccessLogin");
		writeexcelresult("FAIL",rowno1);
		}			
	catch(Exception e)
	 {
		writeexcelresult("PASS",rowno1);
		logger.info("**********LOGIN UNSUCCESSFULL*********");
		logger.info("Data Entered : --------->" + "[" + username +"," + password +"]");
		util.capturescreenshots("Curie_DOC_FUN_TS002_InvLogin", "UnSuccessLogin");
		util.endTestCase();
		}
	 }
private void writeexcelresult(String status, int rowno1) throws IOException
{        
    System.out.println(rowno1);
    ExcelUtil xlutil = new ExcelUtil();
    xlutil.writexl(System.getProperty("user.dir")+"\\excelImportAndExport\\", "ElloraExcel.xlsx", "InValidLoginData", rowno1,status);
    
}
	 
	 @DataProvider(name="InvalidLoginData")	  
	 public Object[][] getDataFromDataprovider() throws IOException
	  {
	    	Object[][] object = null; 
	    	ExcelUtil file = new ExcelUtil();
	    	//Read keyword sheet
	    	Sheet ElloraSheet = file.readExcel(System.getProperty("user.dir")+"\\ExcelImportAndExport\\","ElloraExcel.xlsx","InValidLoginData");
	        //Find number of rows in excel file
	     	int rowCount = ElloraSheet.getLastRowNum()-ElloraSheet.getFirstRowNum();
	     	int colCount = ElloraSheet.getRow(0).getLastCellNum();
	     	object = new Object[rowCount][colCount];
	     	for (int i = 0; i < rowCount; i++) 
	     	{
	    	 //Loop over all the rows
	    	 Row row = ElloraSheet.getRow(i+1);
	    	 //Create a loop to print cell values in a row
	    	 for (int j = 0; j < row.getLastCellNum(); j++) 
	    	 {
	    	   try
	               {
	                //Print excel data in console
	                object[i][j] = row.getCell(j).toString();
	                }
	                catch (NullPointerException e)
	                {
	                    object[i][j] ="";
	                }
	    		  }       
	     	   }
			return object;
	     }
      }

		
		  

	 
		
		 
		 
		 
		 
	 
