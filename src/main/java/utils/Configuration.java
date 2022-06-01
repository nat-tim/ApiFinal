package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

    public static String getConfVal(String key) {

        Properties property = new Properties();

        try {
            FileInputStream conf = new FileInputStream("src/test/resources/url.properties");
            property.load(conf);

        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
        return property.getProperty(key);
    }

    public static String getOtherVal(String key) {

        Properties property = new Properties();

        try {
            FileInputStream conf = new FileInputStream("src/test/resources/application.properties");
            property.load(conf);

        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
        return property.getProperty(key);
    }

}
