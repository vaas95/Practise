import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TestWeb {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void simpleTest() {
        driver.navigate().to("https://www.ebay.com/");

        // Search for "Book"
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@title='Search']")));
        searchBox.sendKeys("Book");
        driver.findElement(By.xpath("//span[contains(text(),'Search')]")).click();

        // Wait for search results
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='srp-results srp-list clearfix']/li")));

        // Get all product elements
        List<WebElement> list = driver.findElements(By.xpath("//ul[@class='srp-results srp-list clearfix']/li/div/div"));

        if (!list.isEmpty()) {
            WebElement firstProduct = list.get(0);
            firstProduct.click();
        } else {
            Assert.fail("No products found in the search results.");
        }

        // Switch to the new tab
        String parentWindow = driver.getWindowHandle();
        Set<String> windows = driver.getWindowHandles();
        Iterator<String> iterator = windows.iterator();

        while (iterator.hasNext()) {
            String childWindow = iterator.next();
            if (!childWindow.equals(parentWindow)) {
                driver.switchTo().window(childWindow);
                System.out.println(childWindow);
                break;
            }
        }

        // Wait for 'Add to cart' button and click
        WebElement addToCart =driver.findElement(By.xpath("//span[contains(text(),'Add to cart')]"));
        wait.until(ExpectedConditions.elementToBeClickable(addToCart));
        addToCart.click();

        // Check if "Go to checkout" button is enabled
        WebElement checkout = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(),'Go to checkout')]")));
        Assert.assertTrue(checkout.isEnabled(), "Checkout button is not enabled!");
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
