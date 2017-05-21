package net.cliffanderson;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class CustomProperties {

    private Properties properties;
    private File propertiesFile;

    public CustomProperties(File propertiesFile) {

        this.properties = new Properties();
        this.propertiesFile = propertiesFile;

        // Check that file exists
        if(! this.propertiesFile.exists()) {
            try {
                if(! this.propertiesFile.createNewFile()) {
                    System.err.println("ERROR: Could not create new file: " + this.propertiesFile.getAbsolutePath());
                    return;
                }

            } catch (IOException e) {
                System.err.println("Error creating properties file: " + this.propertiesFile.getAbsolutePath());
                e.printStackTrace();
            }
        }

        // Load properties
        InputStream in = null;
        try {
            in = new FileInputStream(this.propertiesFile);
        } catch (FileNotFoundException e) {
            System.err.println("Error creating input stream for file: " + this.propertiesFile.getAbsolutePath());
            e.printStackTrace();
            return;
        }

        try {
            this.properties.load(in);
        } catch (IOException e) {
            System.err.println("Error loading properties from file " + this.propertiesFile.getAbsolutePath());
        }
    }

    public void addDefault(String key, Object value) {
        if(! this.properties.containsKey(key)) {
            this.properties.put(key, value);
            this.save();
        }
    }

    public String get(String key) {
        return this.properties.getProperty(key);
    }

    public void set(String key, String value) {
        this.properties.setProperty(key, value);
        this.save();
    }

    public void save() {
        OutputStream out = null;
        try {
            out = new FileOutputStream(this.propertiesFile);
        } catch (FileNotFoundException e) {
            System.err.println("Error creating output stream for file " + this.propertiesFile.getAbsolutePath());
            return;
        }

        try {
            this.properties.store(out, "CustomProperties at " + new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z").format(new Date()));
        } catch (IOException e) {
            System.err.println("Error: could not store properties to file " + this.propertiesFile.getAbsolutePath());
        }
    }
}
