package driver;

import chromeDriver.chromeDriverMethods.ChromeDriverMethods;
import com.thoughtworks.gauge.*;
import helpers.ProjectConsts;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Driver {
    public static WebDriver webDriver;
    public static ConcurrentHashMap<String,Object> TestMap;

    ChromeDriverMethods chromeDriverMethods;

    public Driver (){

        this.chromeDriverMethods = new ChromeDriverMethods();
    }

    @BeforeSuite
    public void beforeSuite() {

        ChromeDriverMethods.compareDriverVersion();

    }

    @BeforeScenario
    public void setUp(ExecutionContext executionContext) {

        System.out.println("==========================================================================================================================================");
        System.out.println("--------------------------------------------------------- ObiletAutomationTest -----------------------------------------------------------");
        System.out.println("==========================================================================================================================================");

        DesiredCapabilities capabilities;

        if (StringUtils.isEmpty(System.getenv("key"))) {
            capabilities = DesiredCapabilities.chrome();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-notifications");
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            System.setProperty(ProjectConsts.WEBDRIVER_CHROME_DRIVER, ProjectConsts.WEBDRIVER_CHROME_DRIVER_PATH);
            webDriver = new ChromeDriver(capabilities);
        } else {
            capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability("key", System.getenv("key"));
            ((RemoteWebDriver) webDriver).setFileDetector(new LocalFileDetector());
        }

        webDriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS).implicitlyWait(3, TimeUnit.SECONDS);
        webDriver.manage().window().maximize();
        webDriver.get(ProjectConsts.PLATFORM_URL);

        System.out.println("\r\n");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");
        String specName = ( executionContext.getCurrentSpecification().getName() );
        System.out.println("----------------------------------------------Spec Name : " + specName);
        String scenarioName = ( executionContext.getCurrentScenario().getName() );
        System.out.println("----------------------------------------------Scenario Name : " + scenarioName);
        System.out.println("\r\n");

    }

    @AfterScenario
    public void tearDown(ExecutionContext executionContext) {

        if (executionContext.getCurrentScenario().getIsFailing()) {

            System.out.println("---------------------------------------------------------  TEST BASARISIZ ------------------------------------------------------------- ");
        } else {

            System.out.println("-------------------------------------------------------------  TEST BASARILI ------------------------------------------------------------- ");
        }

        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("\r\n");

        if (webDriver != null) {
            webDriver.close();
            webDriver.quit();
        }
    }

    @AfterSpec
    public void afterSpec() {

        System.out.println("==========================================================================================================================================");
        //System.out.println("==========================================================================================================================================");
        System.out.println("\r\n");
    }

}
