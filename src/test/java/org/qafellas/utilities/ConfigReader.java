package org.qafellas.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    public static Properties confReader() throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/java/org/qafellas/configs/config.properties");
        prop.load(fis);
        return prop;
    }
}
