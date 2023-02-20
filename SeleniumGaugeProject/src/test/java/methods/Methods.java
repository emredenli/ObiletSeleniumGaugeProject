package methods;

import driver.Driver;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.By;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static helpers.ProjectConsts.JSON_FILE_PATH;

public class Methods {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    WebDriver webDriver;
    FluentWait<WebDriver> wait;
    JavascriptExecutor jsdriver;
    long waitElementTimeout;
    long pollingEveryValue;

    public Methods(){

        this.webDriver = Driver.webDriver;
        wait = new FluentWait<>(webDriver);
        wait = setFluentWait(waitElementTimeout);
        jsdriver = (JavascriptExecutor) webDriver;
    }

    public void clickElement(WebElement webElement){

        Actions actions = new Actions(webDriver);
        actions.click(webElement).build().perform();
    }

    public void doubleClickElement(WebElement webElement){

        Actions actions = new Actions(webDriver);
        actions.doubleClick(webElement).build().perform();
    }

    public boolean isDisplayedAndEnabled(WebElement webElement){

        if (webElement.isDisplayed() && webElement.isEnabled()) {
            return true;
        } else {
            return false;
        }

    }
    public FluentWait<WebDriver> setFluentWait(long timeout){

        FluentWait<WebDriver> fluentWait = new FluentWait<WebDriver>(webDriver);
        fluentWait.withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(pollingEveryValue))
                .ignoring(NoSuchElementException.class);
        return fluentWait;
    }

    public void waitByMilliSeconds(long milliSeconds){

        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitBySeconds(long seconds){

        waitByMilliSeconds(seconds*1000);
    }

    public void hoverElement(WebElement webElement) {

        Actions hoverAction = new Actions(webDriver);
        hoverAction.moveToElement(webElement).build().perform();
    }

    public void sendKeys(WebElement webElement, String text) {

        Actions actions = new Actions(webDriver);
        actions.sendKeys(webElement, text).build().perform();
    }

    public WebElement getElementByKey(String key, String keyType){
        WebElement element = null;
        switch (keyType){

            case "id":
                element = webDriver.findElement(By.id(key));
                break;

            case "cssSelector":
                element = webDriver.findElement(By.cssSelector(key));
                break;

            case "xpath":
                element = webDriver.findElement(By.xpath(key));
                break;

            case "className":
                element = webDriver.findElement(By.className(key));
                break;

            case "tagName":
                element =  webDriver.findElement(By.tagName(key));
                break;

            case "name":
                element = webDriver.findElement(By.name(key));
                break;

            default:
                System.out.println("( " + key + " ) elementi icin -> Hatali 'keyType'(" + keyType + ") gonderildi!!!");
                logger.error("( " + key + " ) elementi icin -> Hatali 'keyType'(" + keyType + ") gonderildi!!!");
                webDriver.quit();
                break;

        }
        return element;
    }

    public List<WebElement> getElements(String key, String keyType){
        List<WebElement> elements = null;
        switch (keyType){

            case "id":
                elements = webDriver.findElements(By.id(key));
                break;

            case "cssSelector":
                elements = webDriver.findElements(By.cssSelector(key));
                break;

            case "xpath":
                elements = webDriver.findElements(By.xpath(key));
                break;

            case "className":
                elements = webDriver.findElements(By.className(key));
                break;

            case "tagName":
                elements =  webDriver.findElements(By.tagName(key));
                break;

            case "name":
                elements = webDriver.findElements(By.name(key));
                break;

            default:
                System.out.println("( " + key + " ) elementi icin -> Hatali 'keyType'(" + keyType + ") gonderildi!!!");
                logger.error("( " + key + " ) elementi icin -> Hatali 'keyType'(" + keyType + ") gonderildi!!!");
                webDriver.quit();
                break;

        }
        return elements;
    }

    public void getUrl(String url){

        String currentUrl = webDriver.getCurrentUrl();
        System.out.println("currentUrl : " + currentUrl);

        String getUrl = url;
        System.out.println("getUrl : " + getUrl);

        if ( currentUrl.equals(getUrl) ){
            System.out.println("Url'ler birbirine esit.");
        } else {
            System.out.println("Url'ler birbirine esit degil!");
            webDriver.quit();
        }

    }

    public void switchTab(int tabNumber){

        webDriver.switchTo().window(listTabs().get(tabNumber));
    }

    public List<String> listTabs(){
        List<String> list = new ArrayList<String>();
        for (String window: webDriver.getWindowHandles()){
            list.add(window);
        }
        return list;
    }

    public void openNewTab(String url){

        String openNewTab = "window.open('" + url + "','_blank');";
        jsdriver.executeScript(openNewTab);
    }

    public void openNewTabJs(String url){

        openNewTab(url);
        logger.info("Yeni tab açılıyor..." + " Url: " + url);
    }

    public void navigateToBack(){

        webDriver.navigate().back();
    }

    public void navigateToRefresh(){

        webDriver.navigate().refresh();
    }
    public void clickElementJs(String key){

        WebElement element = webDriver.findElement(getBy(key));
        JavascriptExecutor executor = (JavascriptExecutor)webDriver;
        executor.executeScript("arguments[0].click();", element);

    }

    public void hoverElement (String key){
        WebElement element = webDriver.findElement(getBy(key));
        Actions actions = new Actions(webDriver);
        jsdriver.executeScript("arguments[0].scrollIntoView();", element);
        actions.moveToElement(element).build().perform();
    }

    public void jsScrollElement(String key) {
        WebElement element = webDriver.findElement(getBy(key));
        jsdriver.executeScript("arguments[0].scrollIntoView();", element);
    }

    public void deleteAllCookies(){

        webDriver.manage().deleteAllCookies();
    }

    public void close(){

        webDriver.close();
    }

    public void randomClickElement (String key) {

        Random random = new Random();
        List<WebElement> items = webDriver.findElements(getBy(key));
        int itmCount = random.nextInt(items.size());
        items.get(itmCount).click();

    }

    public By getBy(String jsonKey){

        JSONObject jsonFile = getJsonFile();
        String value = getValue(jsonKey, jsonFile);
        String type = getType(jsonKey, jsonFile);
        By byElement = getElementInfoToBy(value, type);
        return byElement;
    }

    public JSONObject getJsonFile() {

        JSONObject JsonFile = null;
        try{
            String contents = new String((Files.readAllBytes(Paths.get(JSON_FILE_PATH))));
            JsonFile = new JSONObject(contents);
            return JsonFile;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return JsonFile;
    }

    public String getValue(String jsnKy, JSONObject jsonFile){

        String value = null;
        try {
            String jsonKey = jsnKy;
            Object getValue = jsonFile.getJSONObject(jsonKey).getString("value");
            value = (String) getValue;
            //System.out.println(jsonKey + " / value : " + value);

            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public String getType(String jsnKy, JSONObject jsonFile){

        String type = null;
        try {
            String jsonKey = jsnKy;
            Object getType = jsonFile.getJSONObject(jsonKey).getString("type");
            type = (String) getType;
            //System.out.println(jsonKey + " / type : " + type);

            return type;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }

    public By getElementInfoToBy(String byValue, String selectorType) {

        By by = null;
        switch (selectorType){
            case "cssSelector":
                by = By.cssSelector(byValue);
                break;
            case "id":
                by = By.id(byValue);
                break;
            case "xpath":
                by = By.xpath(byValue);
                break;
            case "className":
                by = By.className(byValue);
                break;
            case "tagName":
                by = By.tagName(byValue);
                break;
            case "name":
                by = By.name(byValue);
                break;
            default:
                throw new NullPointerException("'" + selectorType + "' Element tipi hatalı \n" +
                        "Element Types : id, cssSelector, xpath, className, tagName, name");
        }
        return by;
    }

}
