package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;
import java.util.Map;

public class BaseClass {

    private static WebDriver driver;

    public static void initializeDriver(String browserType){
        switch (browserType.toLowerCase()){
            case "chrome" :
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("prefs", Map.of("profile.password_manager_leak_detection",false));
                driver = new ChromeDriver(options);
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                driver.manage().window().maximize();
                break;
            case "edge" :
                driver = new EdgeDriver();
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                driver.manage().window().maximize();
                break;
            case "firefox" :
                driver = new FirefoxDriver();
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                driver.manage().window().maximize();
            default:
                driver = null;
                System.out.println("Please provide a valid browser type !!!!");
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void navigateTo(String url){
        driver.get(url);
    }
}
