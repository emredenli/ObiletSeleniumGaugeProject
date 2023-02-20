package step;

import com.thoughtworks.gauge.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import driver.Driver;
import methods.Methods;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class StepImplementation extends Driver {

    Methods methods;
    Logger logger = LogManager.getLogger(Methods.class);

    public StepImplementation (){

        this.methods = new Methods();
    }

    @Step("<seconds> saniye bekle")
    public void waitElement(long seconds) throws InterruptedException {

        Thread.sleep(seconds * 1000);
        System.out.println(seconds + " saniye beklendi");
        logger.info(seconds + " saniye beklendi");

    }

    @Step("<key> elementine tıklanır")
    public void clickElement(String key) {

        checkVisibleElement(key);
        WebElement element = webDriver.findElement(methods.getBy(key));

        if (element == null) {
            System.out.println("!!! HATA ( Element == Null ) HATA !!!");
            logger.error("!!! HATA ( Element == Null ) HATA !!!");
            webDriver.quit();
        }

        if (methods.isDisplayedAndEnabled(element)) {
            methods.clickElement(element);
            System.out.println("( " + key + " ) elementine tiklandi");
            logger.info("( " + key + " ) elementine tiklandi");
        } else {
            System.out.println("( " + key + " ) elementine tiklanamadi");
            logger.info("( " + key + " ) elementine tiklanamadi");
        }

    }

    @Step("<key> elementine <text> değerini yaz")
    public void sendKeysElement(String key, String text) {

        checkVisibleElement(key);
        WebElement element = webDriver.findElement(methods.getBy(key));
        text = text.endsWith("KeyValue") ? Driver.TestMap.get(text).toString() : text;

        if (element.isDisplayed() && element.isEnabled()) {
            methods.clickElement(element);
            methods.sendKeys(element, text);
            System.out.println("( " + key + " ) elementine ( " + text + " ) degeri yazildi");
            logger.info("( " + key + " ) elementine ( " + text + " ) degeri yazildi");
        } else {
            System.out.println("( " + key + " ) elementi sayfada goruntulenemedi.");
            logger.info("( " + key + " ) elementi sayfada goruntulenemedi.");
            webDriver.quit();
        }

    }

    @Step("<key> elementinin sayfada görünür olmadığı kontrol edilir")
    public void checkElementVisible(String key){

        List<WebElement> elements = webDriver.findElements(methods.getBy(key));
        int elementSize = elements.size();

        if(elementSize > 0) {
            System.out.println("( " + key + ") elementi gorunur ");
            logger.info("( " + key + ") elementi gorunur ");
            webDriver.quit();
        } else {
            System.out.println("( " + key + ") elementinin sayfada gorunur olmadigi onaylandi ");
            logger.info("( " + key + ") elementinin sayfada gorunur olmadigi onaylandi ");
        }

    }

    @Step("<key> elementinin görünür olması kontrol edilir")
    public void checkVisibleElement(String key) {

        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        WebElement element = webDriver.findElement(methods.getBy(key));
        By locator = methods.getBy(key);

        try {

            element = wait.until(visibilityOfElementLocated(locator));

            if (element != null && element.isDisplayed()){
                System.out.println("( " + key + " ) elementi sayfada goruntulendi");
                logger.info("( " + key + " ) elementi sayfada goruntulendi");
            }

        } catch (Exception e) {
            System.out.println("( " + key + " ) elementi sayfada goruntulenemedi");
            logger.info("( " + key + " ) elementi sayfada goruntulenemedi");
            e.printStackTrace();
        }

    }

    @Step("<key> elementine scroll yapılır")
    public void scrollElement(String key) throws InterruptedException{

        WebElement element = webDriver.findElement(methods.getBy(key));

        if (methods.isDisplayedAndEnabled(element)) {
            System.out.println("( " + key + " elementine scroll yapildi ");
            logger.info("( " + key + " elementine scroll yapildi ");
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element);
            Thread.sleep(500);
        } else {
            System.out.println("( " + key + " ) elementine scroll yapilamadi");
            logger.info("( " + key + " ) elementine scroll yapilamadi");
        }

    }

    @Step("<url> gelen url ile aynı mı")
    public void urlControl(String url){

        methods.getUrl(url);

    }
    @Step("<fileName> adında yeni bir dosya oluştur")
    public void createFile(String fileName) throws IOException {

        String filePath = "C://Users//testinium//Desktop//" + fileName;
        File f = new File(filePath);
        if(f.exists()) {
            System.out.println("File already exists.");
        }
        else {
            System.out.println("File created.");
            f.createNewFile();
        }

    }

    @Step("Switch tab <switchTabNumber>")
    public void switchTab(int switchTabNumber){

        methods.switchTab(switchTabNumber);
    }

    @Step("Open new tab <url>")
    public void openNewTabJs(String url){

        methods.openNewTabJs(url);
        System.out.println("( " + url + " ) url'i yeni sekmede acildi.");
        logger.info("( " + url + " ) url'i yeni sekmede acildi.");
    }

    @Step("Navigate to back")
    public void navigateToBack(){

        methods.navigateToBack();

    }

    @Step("Navigate to refresh")
    public void navigateToRefresh(){

        methods.navigateToRefresh();
        System.out.println("Sayfa yenilendi.");
        logger.info("Sayfa yenilendi.");

    }

    @Step("<key> elementini <value> kere backspace tuşuna basarak temizle")
    public void clearElementWithBackSpace (String key, int value) {

        for ( int i = 0 ; i < value ; i++) {
            webDriver.findElement(methods.getBy(key)).click();
            webDriver.findElement(methods.getBy(key)).sendKeys(Keys.BACK_SPACE);
        }

        System.out.println("( " + key + " ) elementi ( " + value + " ) kere backspace tusu basilarak temizlendi. ");
        logger.info("( " + key + " ) elementi ( " + value + " ) kere backspace tusu basilarak temizlendi. ");

    }

    @Step("<key> elementini temizle")
    public void clearElement (String key) {

        webDriver.findElement(methods.getBy(key)).clear();
        System.out.println("Elementin text alanı temizlendi.");
        logger.info("Elementin text alanı temizlendi.");

    }

    @Step("<key> elementine js ile tıkla")
    public void clickElementJs(String key) {

        methods.clickElementJs(key);
        System.out.println("( " + key + " ) elementine js ile tiklandi.");
        logger.info("( " + key + " ) elementine js ile tiklandi.");

    }

    @Step("<key> elementine çift tıkla")
    public void doubleClickElement(String key) {

        checkVisibleElement(key);
        WebElement element = webDriver.findElement(methods.getBy(key));

        if (element == null) {
            System.out.println("!!! HATA ( Element == Null ) HATA !!!");
            logger.error("!!! HATA ( Element == Null ) HATA !!!");
            webDriver.quit();
        }

        if (methods.isDisplayedAndEnabled(element)) {
            methods.doubleClickElement(element);
            System.out.println("( " + key + " ) elementine cift tiklandi");
            logger.info("( " + key + " ) elementine cift tiklandi");
        } else {
            System.out.println("( " + key + " ) elementine cift tiklanamadi");
            logger.info("( " + key + " ) elementine cift tiklanamadi");
        }

    }

    @Step("<key> elementinin text değerini kopyala")
    public void copyElementTextValue(String key) {

        webDriver.findElement(methods.getBy(key)).sendKeys(Keys.CONTROL, "A");
        webDriver.findElement(methods.getBy(key)).sendKeys(Keys.CONTROL, "C");
        String text = webDriver.findElement(methods.getBy(key)).getText();
        System.out.println("( " + key + " ) elementinin text degeri kopyalandi.");
        System.out.println("( " + key + " ) elementinin text degeri : " + text );
        logger.info("( " + key + " ) elementinin text degeri kopyalandi.");

    }

    @Step("<url> url adresine git")
    public void navigateTo (String url) {

        webDriver.get(url);
        System.out.println("( " + url + " ) url adresine gidildi.");
        logger.info("( " + url + " ) url adresine gidildi.");

    }

    @Step("<key> hover element")
    public void hoverElementAction(String key){

        methods.hoverElement(key);
        System.out.println("( " + key + " ) elementine hover yapildi.");
        logger.info("( " + key + " ) elementine hover yapildi.");

    }

    @Step("<key> js scroll element")
    public void jsScrollElementAction(String key){

        methods.jsScrollElement(key);
        System.out.println("( " + key + " ) elementine js ile scroll yapildi.");
        logger.info("( " + key + " ) elementine js ile scroll yapildi.");

    }

    @Step("<key> elementinin text değerini <mapKey> keyinde tut")
    public void getElementTextAndSave(String key, String mapKey){

        //String text = methods.getText(methods.getBy(key));
        HashMap<String, String> map = new HashMap<>();
        String text = (webDriver.findElement(methods.getBy(key))).getText();
        //text = trim ? text.trim() : text;
        text = true ? text.trim() : text;
        logger.info(text);
        map.put(mapKey, text);
        System.out.println("( " + key + " ) elementinin text degeri " + mapKey + " keyine kaydedildi.");
        logger.info("( " + key + " ) elementinin text degeri " + mapKey + " keyine kaydedildi.");

        /*if (map.containsKey(mapKey)) {
            String str = map.get(mapKey);
            System.out.println("value for key " + mapKey + " is:- " + str);
        }*/

    }

    @Step("<key> elementinin <attribute> attribute değerini <mapKey> keyinde tut")
    public void getElementAttributeAndSave(String key, String attribute, String mapKey){

        HashMap<String, String> map = new HashMap<>();
        String value = (webDriver.findElement(methods.getBy(key))).getAttribute(attribute);
        value = true ? value.trim() : value;
        logger.info(value);
        map.put(mapKey, value);
        System.out.println("( " + key + " ) elementinin attribute degeri " + mapKey + " keyine kaydedildi.");
        System.out.println("( " + key + " ) elementinin attribute degeri : " + attribute);
        logger.info("( " + key + " ) elementinin attribute degeri " + mapKey + " keyine kaydedildi.");

        /*if (map.containsKey(mapKey)) {
            String str = map.get(mapKey);
            System.out.println("value for key " + mapKey + " is:- " + str);
        }*/

    }

    @Step("Delete All Cookies")
    public void deleteAllCookies(){

        methods.deleteAllCookies();
        System.out.println("Cerezler temizlendi.");
        logger.info("Cerezler temizlendi.");

    }

    @Step("Sekmeyi kapat")
    public void closeTab(){

        methods.close();
        System.out.println("Sekme kapatildi.");
        logger.info("Sekme kapatildi.");

    }

    @Step("<key> elementlerinden birine random tıkla")
    public void randomClick(String key) {

        checkVisibleElement(key);
        methods.randomClickElement(key);
        System.out.println("( " + key + " ) listesindeki elementlerden birine random tiklandi.");
        logger.info("( " + key + " ) listesindeki elementlerden birine random tiklandi.");

    }

}
