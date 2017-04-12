package app.model;

import app.utils.Log;
import app.utils.exceptions.TheFileIsNotPropertiesException;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class PropertyRepository {
    private File propertyFile;
    private static PropertyRepository instance;
    private static final Logger LOG = Log.createLog(PropertyRepository.class);
    private static final Properties STORE = new Properties() {{
        put("program_path", "");
        put("script_file_path", "");
        put("url", "");
        put("user", "");
        put("password", "");
        put("program_name", "");
        put("result_file_path", "");
        put("number_tests", "");
        put("connection.driver_class", "");
        put("hibernate.dialect", "");
    }};

    private PropertyRepository() {
    }

    static PropertyRepository getInstance() {
        if (instance == null) {
            instance = new PropertyRepository();
        }
        return instance;
    }

    void setPropertyFile(File propertyFile) throws Exception {
        if (!isPropertiesFile(propertyFile.getAbsolutePath())) {
            this.propertyFile = null;
            throw new TheFileIsNotPropertiesException();
        }
        this.propertyFile = propertyFile;
        saveProperties();
    }

    private void saveProperties() throws Exception {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(propertyFile));
            STORE.replaceAll((k, v) -> "");
            for (Object key : properties.keySet()) {
                STORE.computeIfPresent(((String)key).toLowerCase(), (k, v) -> properties.get(key));
            }
            LOG.info(Log.PROPERTY_FILE_READ + getPathToPropertyFile());
        } catch (Exception e) {
            LOG.error(Log.PROPERTY_READ_ERROR + getPathToPropertyFile());
            if (e.getMessage() != null && !e.getMessage().isEmpty()) {
                LOG.error(e.getMessage());
            }
            throw e;
        }
    }

    private boolean isPropertiesFile(String path) {
        return path.toLowerCase().trim().endsWith(".properties");
    }

    private String getPathToPropertyFile() {
        return "[ " + propertyFile.getAbsolutePath().trim() + " ]";
    }

    Properties getProperties() {
        return STORE;
    }

    public File getPropertyFile() {
        return propertyFile;
    }
}