package datadriventesting_excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Data_Provider_Using_Excel {


	public static WebDriver driver;
	public static String excel_file_path = ".\\Test_Data\\LoginData.xlsx";
	public static FileInputStream fis;
	public static XSSFWorkbook wb;
	public static XSSFSheet s;

	@BeforeTest
	public static void open_Browser() {

		driver = WebDriverManager.chromedriver().create();
		driver.manage().window().maximize();

	}

	@Test(dataProvider = "Login_Data")
	public static void login_logout(String user_name, String pass_word) throws InterruptedException, IOException {

		driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		WebElement username = driver.findElement(
				By.xpath("//*[@id=\"app\"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[1]/div/div[2]/input"));
		username.sendKeys(user_name);
		Thread.sleep(2000);
		WebElement password = driver.findElement(
				By.xpath("//*[@id=\"app\"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[2]/div/div[2]/input"));
		password.sendKeys(pass_word);
		Thread.sleep(2000);
		WebElement login = driver
				.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[3]/button"));
		login.click();
		Thread.sleep(2000);
		WebElement user_id = driver
				.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[1]/header/div[1]/div[2]/ul/li/span/p"));
		user_id.click();
		Thread.sleep(2000);
		WebElement logout = driver
				.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[1]/header/div[1]/div[2]/ul/li/ul/li[4]"));
		logout.click();
		Thread.sleep(2000);

	}

	@DataProvider(name = "Login_Data")
	String[][] get_Data() throws IOException {

		fis = new FileInputStream(excel_file_path);
		wb = new XSSFWorkbook(fis);
		s = wb.getSheet("Sheet1");
		int rows_count = s.getLastRowNum();
		int columns_count = s.getRow(rows_count).getLastCellNum();

		String login_data[][] = new String[rows_count][columns_count];
		for (int r = 1; r <= rows_count; r++) {
			XSSFRow row = s.getRow(r);
			for (int c = 0; c < columns_count; c++) {
				XSSFCell cell = row.getCell(c);
				String cell_value = cell.getStringCellValue();
				login_data[r - 1][c] = cell.getStringCellValue();
			}

		}
		return login_data;

	}

	@AfterTest
	public static void close_Browser() {

		driver.close();

	}


}
