package testcases_DoctorModules;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import methods.ExcelUtil;
import methods.OpenURL;
import methods.Utility;
import methods.commonmethods;

public class Curie_DOC_FUN_TS004_CalendarEvt extends OpenURL
{
  public Logger logger = Logger.getLogger(Curie_DOC_FUN_TS004_CalendarEvt.class.getName());
	
  @Test(dataProvider="dp")    
  public void calendar(String username,String password,String date) throws Exception
  {
	   
	   driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	   Utility util = new Utility(driver, prop, logger); 
	   
	   //To call login method to create slots for logged In User
	   commonmethods meth=new commonmethods(driver, prop);
	   meth.login(username, password);		
	   Thread.sleep(5000);
		
		String[] splitdate = date.split(";");			
		util.perform1("id","click","btn_setschedule","");	     
		logger.info("Clicking on the setschedule button");	 	 
		Thread.sleep(5000);
		
		//To click the Event button to create Events
		util.perform1("xpath","click","btn_Event","");
		
		for(int i=0;i<splitdate.length;i++)
	     {	
	         util.perform1("xpath","click","sel_date_cal","");
		     String Evtmonth=driver.findElement(By.xpath("//*[@id='embededcomponents']//following::header/span[2]")).getText();
		     System.out.println(Evtmonth);
		     Thread.sleep(3000);
		     meth.calendar(splitdate[i]);
		     logger.info("Select date is------->"  +splitdate[i]+ " ]");
		     Thread.sleep(5000);	
				
		//Getting Event type  list:
	    List<WebElement> liElements = driver.findElements(By.xpath(prop.getProperty("evttype_list")));
	    System.out.println(liElements.size());
	  	int size=liElements.size();
	  	String strsize=String.valueOf(size);	
	  	System.out.println(strsize);	   			  
				
		//Spliting the excel date:
		String[] daydate=splitdate[i].toString().split("-");
		System.out.println(daydate[0]+" "+daydate[1]+" "+daydate[2]);
		
		//For Clicking the calendar tab
		util.perform1("xpath", "click","href_calendar","");
		Thread.sleep(2000);
		
		//For clicking the month tab
		//WebElement cal=driver.findElement(By.xpath("//*[@id='embededcomponents']//following::nav/button[1]"));
		WebElement month=driver.findElement(By.xpath(prop.getProperty("month_tab")));
		month.click();
		
		//Getting the moth display in calendar
		String calmonth=driver.findElement(By.xpath(prop.getProperty("Evtcal_month"))).getText();
		System.out.println(calmonth);	
		JavascriptExecutor jse = (JavascriptExecutor) driver;
	    jse.executeScript("window.scrollBy(0,250)", "");
		
		//To check the month in Eventcalendar and main calendar are same
		if(Evtmonth.equalsIgnoreCase(calmonth))
		{				
		List<WebElement> tabrow= driver.findElements(By.xpath(prop.getProperty("cal_rows")));
	    System.out.println(tabrow.size());
	    List<WebElement> calcols= driver.findElements(By.xpath(prop.getProperty("cal_cols")));
	    System.out.println(calcols.size());
	    
	    for(int k=1;k<=tabrow.size();k++)
	    {
	    	for(int j=1;j<=calcols.size();j++)
	    	{	    		
	    	  List<WebElement> tabcol=driver.findElements(By.xpath("//table/tbody/tr["+k+"]/td["+j+"]/div[2]/div[1]"));    
	   	      for(WebElement ele1:tabcol)
	          {    	     
		       String celltext=ele1.getText();	
		       System.out.println("Row "+ k +" Column "+ j +" Data "+celltext );	   			    
			   if(celltext.equals(daydate[0]))  
			  	{  
				  WebElement cal_evt=driver.findElement(By.xpath("//table/tbody/tr["+k+"]/td["+j+"]/div[2]/div[3]/div/div/div"));
				  System.out.println(cal_evt.getText());
				  if(cal_evt.isDisplayed() && cal_evt.equals(strsize));
				  {	   
					 System.out.println("Event numbers are  same");
			  	     logger.info("Event list number and Event number in calendar are same ---->" +splitdate[i]+ " ]");
			  	     util.capturescreenshots("Curie_DOC_FUN_TS004_CalendarEvt", "CalendarEvt");
				  
			  	  }
			  	}
				  else
				  {	 	  
			  		System.out.println("Not same");
	  			    logger.info("Events numbers are not same");	  			   		
	  		}
	      }
	    }
	  }		    	
    }
   }
  }
  
    		    
   @DataProvider(name="dp")
   public Object[][] getDataFromDataprovider() throws IOException
   {
   Object[][] object = null; 
   ExcelUtil file = new ExcelUtil();
    
    //Read keyword sheet
    Sheet ElloraSheet = file.readExcel(System.getProperty("user.dir")+"\\excelImportAndExport\\","ElloraExcel.xlsx","CalEvt");
     
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
           object[i][j] = row.getCell(j);
           }
		   }    
     	}
	return object;
}
}

