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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class StepImplementation extends Driver {

    Methods methods;
    Logger logger = LogManager.getLogger(Methods.class);
    HashMap<String, String> map = new HashMap<>();

    public StepImplementation (){

        this.methods = new Methods();
    }

    @Step("<seconds> saniye bekle")
    public void waitElement(long seconds) throws InterruptedException {

        Thread.sleep(seconds * 1000);
        System.out.println(seconds + " saniye beklendi");
        logger.info(seconds + " saniye beklendi");

    }

    @Step("<milliseconds> milisaniye bekle")
    public void waitByMilliSeconds(long milliseconds) {

        methods.waitByMilliSeconds(milliseconds);
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

        int counter = 0;
        boolean element = false;

        while( counter != 3 || element == false ){

            counter++;
            List<WebElement> elements = webDriver.findElements(methods.getBy(key));
            int elementSize = elements.size();

            if(elementSize > 0 && counter == 3) {
                System.out.println("( " + key + " ) elementi gorunur ");
                logger.info("( " + key + " ) elementi gorunur ");
                webDriver.quit();
            } else {
                System.out.println("( " + key + " ) elementinin sayfada gorunur olmadigi onaylandi ");
                logger.info("( " + key + " ) elementinin sayfada gorunur olmadigi onaylandi ");
                element = true; counter = 3;
            }

        }

    }

    @Step("<key> elementinin görünür olması kontrol edilir")
    public Boolean checkVisibleElement(String key) {

        Boolean bln = null;
        WebDriverWait wait = new WebDriverWait(webDriver, 3);
        WebElement element = webDriver.findElement(methods.getBy(key));
        By locator = methods.getBy(key);

        try {

            element = wait.until(visibilityOfElementLocated(locator));

            if (element != null && element.isDisplayed()){
                System.out.println("( " + key + " ) elementi sayfada goruntulendi");
                logger.info("( " + key + " ) elementi sayfada goruntulendi");
                bln = true;
            }

        } catch (Exception e) {
            System.out.println("( " + key + " ) elementi sayfada goruntulenemedi");
            logger.info("( " + key + " ) elementi sayfada goruntulenemedi");
            e.printStackTrace();
            bln = false;
        }

        waitByMilliSeconds(300);
        return bln;
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

    @Step("<key> elementinin üzerine klavyeden enter tuşuna tıkla")
    public void clickEnter(String key) {

        webDriver.findElement(methods.getBy(key)).sendKeys(Keys.ENTER);
        System.out.println("( " + key + " ) elementinin uzerine klavyeden enter tusuna tıklandi.");
        logger.info("( " + key + " ) elementinin uzerine klavyeden enter tusuna tıklandi");

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

    @Step("<key> elementinin text değeriyle <mapKey> 'deki elementin text değeri eşit mi")
    public void compareTextsElement(String key, String mapKey){

        String text = (webDriver.findElement(methods.getBy(key))).getText();
        text = true ? text.trim() : text;

        if (map.containsKey(mapKey)) {
            String mpKy = map.get(mapKey);
            if ( text.equals(mpKy)) {
                System.out.println("( " + key + " ) elementinin text degeri : " + text);
                System.out.println("( " + mapKey + " ) Key'inin text degeri : " + mpKy);
                System.out.println("( " + key + " ) elementinin text degeriyle ( " + mapKey + " ) Key'inin text degeri birbirine esit.");
                logger.info("( " + key + " ) elementinin text degeriyle ( " + mapKey + " ) Key'inin text degeri birbirine esit.");
            } else {
                System.out.println("( " + key + " ) elementinin text degeriyle ( " + mapKey + " ) elementinin text degeri birbirine esit degil!");
                logger.info("( " + key + " ) elementinin text degeriyle ( " + mapKey + " ) elementinin text degeri birbirine esit degil!");
                webDriver.quit();
            }
        } else {
            System.out.println("( " + mapKey + " ) ilgili mapKeyde element bulunamadi.");
        }

    }

    @Step("<key> elementinin <attribute> attribute değerini <mapKey> keyinde tut")
    public void getElementAttributeAndSave(String key, String attribute, String mapKey){

        String value = (webDriver.findElement(methods.getBy(key))).getAttribute(attribute);
        value = true ? value.trim() : value;
        logger.info(value);
        map.put(mapKey, value);
        System.out.println("( " + key + " ) elementinin attribute degeri ( " + mapKey + " ) keyine kaydedildi.");
        System.out.println("( " + key + " ) elementinin attribute degeri : " + attribute);
        logger.info("( " + key + " ) elementinin attribute degeri ( " + mapKey + " ) keyine kaydedildi.");

        /*if (map.containsKey(mapKey)) {
            String str = map.get(mapKey);
            System.out.println("value for key " + mapKey + " is:- " + str);
        }*/

    }

    @Step("Ekrandaki Alert Popuptaki Cancel butonuna tıkla")
    public void alertCancel(){

        methods.clickAlertCancelButton();
        System.out.println("Alert 'Cancel' butonuna tiklandi.");
        logger.info("Alert 'Cancel' butonuna tiklandi.");

    }

    @Step("Ekrandaki Alert Popuptaki OK butonuna tıkla")
    public void alertOK(){

        methods.clickAlertOkButton();
        System.out.println("Alert 'OK' butonuna tiklandi.");
        logger.info("Alert 'OK' butonuna tiklandi.");

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

    @Step("Bilet bulma ekranındaki yolculuk tarihi alanından rastgele tarih seç")
    public void selectDayFromCalendar(){

        Random rand = new Random();

        //Disabled olan günleri içine yazdırdığımız liste
        List<String> listDisabled = new ArrayList<>();
        //Enabled olan günleri içine yazdırdığımız liste
        List<String> listEnabled = new ArrayList<>();

        //Takimdeki tüm sonuçların listesi
        List<WebElement> items = webDriver.findElements(By.xpath("//table[@class='month']//tbody//button"));

        //Takvimdeki aktif hafta içi ve hafta sonu günlerinin listeleri
        List<WebElement> weekIn = webDriver.findElements(By.xpath("//table[@class='month']//tbody//button[@class='week in']"));
        List<WebElement> weekendIn = webDriver.findElements(By.xpath("//table[@class='month']//tbody//button[@class='weekend in']"));

        //Takvimdeki aktif olmayan hafta içi ve hafta sonu günlerinin listeleri
        List<WebElement> weekOut = webDriver.findElements(By.cssSelector(".week.out.out-prev"));
        List<WebElement> weekendOut = webDriver.findElements(By.cssSelector(".weekend.out.out-prev"));

        //Takvimdeki aktif olmayan tüm günlerin listesi
        List<WebElement> disabledList = webDriver.findElements(By.xpath("//table[@class='month']//tbody//button[@disabled]"));

        //Takvimde bir önceki aydan görünen günler toplamı
        int prevSize = weekOut.size() + weekendOut.size();

        //Takvimdeki bu aya ait hafta içi ve hafta sonu günlerinin birleşimi
        weekIn.addAll(weekendIn);
        int size = weekIn.size();


        for ( int i = 0 ; i < ( prevSize + size ) ; i++ ) {
            WebElement element = items.get(i);
            String elementAttribute = element.getAttribute("data-date");
            if ( disabledList.size() > i ){
                listDisabled.add(elementAttribute);
            } else {
                listEnabled.add(elementAttribute);
            }
        }

        //System.out.println("listEnabled : " + listEnabled);
        //System.out.println("listDisabled : " + listDisabled);

        String randomElement = listEnabled.get(rand.nextInt(listEnabled.size()));
        String newLocator = "//table[@class='month']//tbody//button[@data-date='" + randomElement + "']";
        WebElement clickRandomElement = webDriver.findElement(By.xpath(newLocator));
        methods.clickElement(clickRandomElement);

        System.out.println("Takvimden ( " + randomElement + " ) tarihi secildi.");
        logger.info("Takvimden ( " + randomElement + " ) tarihi secildi.");

    }

    @Step("Otobüs boş koltuk erkek seçimi yapılır")
    public void busSeatMaleSelection(){

        Boolean bln = null;
        while( bln == null || bln == false ) {

            bln = checkVisibleElement("ObiletOtobusSeferiBusLayout");
            if ( bln == true ) {
                Boolean freeSeat = checkVisibleElement("ObiletOtobusBosKoltuk");
                if ( freeSeat == true ){
                    randomClick("ObiletOtobusBosKoltuk");
                    checkVisibleElement("ObiletOtobusBosKoltukCinsiyetSecimiErkek");
                    clickElement("ObiletOtobusBosKoltukCinsiyetSecimiErkek");
                    bln = true;
                } else {
                    System.out.println("Bos koltuk yok.");
                }
            } else {
                System.out.println("Otobüs Layout goruntulenemedi. Yeni sefer seçiliyor.");
            }

        }

    }

    @Step("Sefer seçim ekranında seçilen seferdeki koltuk numarası ile ödeme ekranındaki koltuk numarası eşit mi")
    public void seatNumberCompare(){

        methods.waitByMilliSeconds(100);
        String selectSeatNumber = map.get("Secilen_Koltuklar");
        checkVisibleElement("ObiletOtobusOdemeEkraniKoltukNumarasiText");
        String paymentScreenSeatNumber
                = webDriver.findElement(methods.getBy("ObiletOtobusOdemeEkraniKoltukNumarasiText")).getText();
        paymentScreenSeatNumber = methods.textSpaceDelete(paymentScreenSeatNumber);

        if ( selectSeatNumber.equals(paymentScreenSeatNumber) ) {
            System.out.println("selectSeatNumber : " + selectSeatNumber);
            System.out.println("paymentScreenSeatNumber : " + paymentScreenSeatNumber);
            System.out.println("Sefer secim ekraninda secilen seferdeki koltuk numarasi ile odeme ekranindaki koltuk numarasi esit.");
        } else {
            System.out.println("Sefer secim ekraninda secilen seferdeki koltuk numarasi ile odeme ekranindaki koltuk numarasi esit degil!");
            webDriver.quit();
        }

    }

    @Step("Sefer seçim ekranında seçilen seferdeki fiyatı doğru formata getir ve <mapKey> keyinde sakla")
    public void priceFormatConversion(String mapKey){

        methods.waitByMilliSeconds(100);
        String amountInteger = webDriver.findElement(By.cssSelector("li.open span[class='amount-integer']")).getText();
        String amountDecimal = webDriver.findElement(By.cssSelector("li.open sup[class='amount-decimal']")).getText();
        String amountSign = webDriver.findElement(By.cssSelector("li.open span[class='amount-sign']")).getText();
        String price = amountInteger + amountDecimal + " " + amountSign;
        map.put(mapKey, price);
        System.out.println("Secilen koltugun fiyat texti : " + price + " ( " + mapKey + " ) keyinde saklandi.");
        logger.info("Secilen koltugun fiyat texti : " + price + " ( " + mapKey + " ) keyinde saklandi.");

    }

}
