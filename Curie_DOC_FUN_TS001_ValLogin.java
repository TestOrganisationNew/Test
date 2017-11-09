package testcases_DoctorModules;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import methods.ExcelUtil;
import methods.OpenURL;
import methods.Utility;

public class Curie_DOC_FUN_TS001_ValLogin extends OpenURL
{			
  public Logger logger = Logger.getLogger(Curie_DOC_FUN_TS001_ValLogin.class.getName());
 
  
  @Test(dataProvider="LoginData")
  public void login(String rowno,String username,String password,String Result) throws Exception 
   {
	  
	        prop=new Properties();
	        //path of Object properties
	        FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"\\Objects\\objects.properties");
	        prop.load(fis);
	     
	        //To validate the title of URL
	        AssertJUnit.assertEquals(prop.getProperty("Title"),driver.getTitle());
	       
	        //Calling Utility method for elements xpath and screenshots
	        Utility util = new Utility(driver, prop, logger);      
	        util.startTestCase("Curie_DOC_FUN_TS001_ValLogin");
	         
	        //To click on login button in Home page: 
	        Thread.sleep(5000);
	        util.perform1("xpath","click","btn_homelogin",""); 
	       
	        
	        //To click on Username textbox to enter username and password:
	        util.perform1("id","settext","txt_username",username);
	        util.perform1("id","settext","txt_password",password);
	        logger.info("Login Data Entered :--------------> " + "[" + username +"  , " + password +"]");
	        Thread.sleep(5000);	        
	        util.perform1("id","click","btn_login","");  
	        Thread.sleep(5000);  
	        util.perform1("xpath","click","btn_doctor_menu","");
	        WebElement mailid=driver.findElement(By.xpath(prop.getProperty("menu_Mailid")));
	        logger.info("The mailId displayed is:------>" +"["+ mailid.getText() +"]");
	        
	        //Write Excel
			BigDecimal bd = new BigDecimal(rowno);
	        int rowno1 = bd.intValue();
		   
	        
	        if(mailid.getText().equalsIgnoreCase(username))
	        {
	        	
	        writeexcelresult("PASS",rowno1);
	        logger.info("*******SUCCESSFULL LOGIN********");
	        util.capturescreenshots("Curie_DOC_FUN_TS001_ValLogin", "Logindata");    
	        Thread.sleep(2000);
	        util.perform1("xpath","click","btn_doctor_menu","");
	        }
	        else
	        {
	        	 writeexcelresult("FAIL",rowno1);
	        	logger.error("Unauthorised User");
	        }
	        Thread.sleep(2000);
	        util.perform1("xpath","click","btn_logout","");
	        logger.info("User logout successfully");
	        util.endTestCase();
	    }
  private void writeexcelresult(String status, int rowno1) throws IOException
  {        
      System.out.println(rowno1);
      ExcelUtil xlutil = new ExcelUtil();
      xlutil.writexl(System.getProperty("user.dir")+"\\excelImportAndExport\\", "ElloraExcel.xlsx", "login", rowno1,status);
      
  }
  

	@DataProvider(name="LoginData")
	public Object[][] getDataFromDataprovider() throws IOException
	{
  	Object[][] object = null; 
  	ExcelUtil file = new ExcelUtil();
    //Read keyword sheet
  	Sheet ElloraSheet = file.readExcel(System.getProperty("user.dir")+"\\excelImportAndExport\\","ElloraExcel.xlsx","login");
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

	
	  

