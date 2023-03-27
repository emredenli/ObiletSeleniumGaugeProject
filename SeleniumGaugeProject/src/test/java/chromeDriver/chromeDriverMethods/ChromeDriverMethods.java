package chromeDriver.chromeDriverMethods;

import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.apache.commons.io.FileUtils.forceDelete;

public class ChromeDriverMethods {

    public static String DRIVER_JSON = "src/test/java/chromeDriver/chromeDriverJson/chromeDriverVersion.json";
    public static String DRIVER_DOWNLOAD_PATH = "C:\\Users\\testinium\\Downloads\\chromedriver_win32.zip";
    public static String DRIVER_DESTDIR_PATH = "C:\\Users\\testinium\\Desktop\\ObiletSeleniumGaugeProjectGit\\SeleniumGaugeProject\\src\\test\\resources\\driver\\chrome";
    public static String PROJECT_DESTDIR_PATH = "src/test/resources/driver/chrome";
    public static String DRIVER_JSON_LONG_PATH = "C:\\Users\\testinium\\Desktop\\ObiletSeleniumGaugeProjectGit\\SeleniumGaugeProject\\src\\test\\java\\chromeDriver\\chromeDriverJson\\chromeDriverVersion.json";
    public static String DRIVER_LICENSE_PATH = "C:\\Users\\testinium\\Desktop\\ObiletSeleniumGaugeProjectGit\\SeleniumGaugeProject\\src\\test\\resources\\driver\\chrome\\LICENSE.chromedriver";

    public static String getDriverVersion(){

        String version = null;
        try {
            URL url = new URL("https://chromedriver.storage.googleapis.com/LATEST_RELEASE");
            URLConnection yc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

            String inputLine;
            if ( (inputLine = in.readLine()) != null )
                version = inputLine;
            else
                System.out.println("Driver Version = NULL");

            /*while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
                version = inputLine;
            in.close();*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;

    }

    public static void compareDriverVersion() {

        boolean bln = false;
        String newVersion = getDriverVersion();
        String version = null;
        try {
            String contents = new String((Files.readAllBytes(Paths.get(DRIVER_JSON))));
            JSONObject JsonFile = new JSONObject(contents);
            Object getValue = JsonFile.getJSONObject("ChromeVersion").getString("version");
            version = (String) getValue;
            System.out.println("version : " + version);

            if ( newVersion.equals(version)){
                bln = true;
            }
            else {
                deleteFolder();
                downloadFile(newVersion);
                setChromeVersion(newVersion);
                deleteUnusedFolder();
                bln = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadFile(String version_number) {

        try {

            //Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome https://chromedriver.storage.googleapis.com/111.0.5563.64/chromedriver_win32.zip"});
            Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome https://chromedriver.storage.googleapis.com/" + version_number + "/chromedriver_win32.zip"});
            TimeUnit.SECONDS.sleep(2);

            Path source = Paths.get(DRIVER_DOWNLOAD_PATH);
            Path target = Paths.get(DRIVER_DESTDIR_PATH);

            try {

                unzipFolder(source, target);
                System.out.println("Done");

            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void unzipFolder(Path source, Path target) throws IOException {

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source.toFile()))) {

            // list files in zip
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {

                boolean isDirectory = false;
                // example 1.1
                // some zip stored files and folders separately
                // e.g data/
                //     data/folder/
                //     data/folder/file.txt
                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }

                Path newPath = zipSlipProtect(zipEntry, target);

                if (isDirectory) {
                    Files.createDirectories(newPath);
                } else {

                    // example 1.2
                    // some zip stored file path only, need create parent directories
                    // e.g data/folder/file.txt
                    if (newPath.getParent() != null) {
                        if (Files.notExists(newPath.getParent())) {
                            Files.createDirectories(newPath.getParent());
                        }
                    }

                    // copy files, nio
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);

                    // copy files, classic
                    /*try (FileOutputStream fos = new FileOutputStream(newPath.toFile())) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }*/
                }

                zipEntry = zis.getNextEntry();

            }
            zis.closeEntry();

        }

    }

    // protect zip slip attack
    public static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir)
            throws IOException {

        // test zip slip vulnerability
        // Path targetDirResolved = targetDir.resolve("../../" + zipEntry.getName());

        Path targetDirResolved = targetDir.resolve(zipEntry.getName());

        // make sure normalized file still has targetDir as its prefix
        // else throws exception
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }

        return normalizePath;
    }

    public static void deleteFolder() {

        File file = new File(PROJECT_DESTDIR_PATH);
        try {

            IOException exception = null;
            try {
                forceDelete(file);
                TimeUnit.SECONDS.sleep(2);
                File theDir = new File(PROJECT_DESTDIR_PATH);
                if (!theDir.exists()){
                    theDir.mkdirs();
                }
            } catch (final IOException ioe) {
                exception = ioe;
            }

            if (null != exception) {
                throw exception;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static class ApplicationConfig{
        private String chromeVersion; // private = restricted access

        // Getter
        public String getChromeVersion() {
            return chromeVersion;
        }

        // Setter
        public void setChromeVersion(String chromeVersion) {
            this.chromeVersion = chromeVersion;
        }
    }

    public static void setChromeVersion(String version) {
        try {

            String str = "{\n" +
                    "  \"ChromeVersion\": {\n" +
                    "    \"version\": \"" + version + "\"\n" +
                    "  }\n" +
                    "}";

            /*ApplicationConfig config = new ApplicationConfig();
            config.setChromeVersion(str);

            JSONObject applicationConfig = new JSONObject(config);

            String jsonString = applicationConfig.toString();*/



            FileWriter writer = new FileWriter
                    (DRIVER_JSON_LONG_PATH,
                            false); //overwrites the content of file
            writer.write(str);
            writer.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteUnusedFolder() {

        File downloadDriverFolder = new File(DRIVER_DOWNLOAD_PATH);
        downloadDriverFolder.delete();

        File licenseChromeDriverFile = new File(DRIVER_LICENSE_PATH);
        licenseChromeDriverFile.delete();
    }

}
