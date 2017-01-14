package app.model;

import app.utils.Log;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class PropertyRepository {
    private File propertyFile;
    private static PropertyRepository instance;
    private static final Logger LOG = Log.createLog(PropertyRepository.class);
    private static Properties store = new Properties() {{
        put("program_path", "");
        put("url", "");
        put("user", "");
        put("password", "");
        put("program_name", "");
        put("result_file_path", "");
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
        this.propertyFile = propertyFile;
        saveProperties();
    }

    private void saveProperties() throws Exception{
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(propertyFile));
            store.replaceAll((k, v) -> "");
            for (Object key : properties.keySet()) {
                store.computeIfPresent(key, (k, v) -> properties.get(k));
            }
            LOG.info(Log.PROPERTY_READ);
        }
        catch (Exception e) {
            LOG.error(Log.PROPERTY_READ_ERROR);
            e.printStackTrace();
            throw e;
        }
    }

    Properties getProperties() {
        return store;
    }

    public File getPropertyFile() {
        return propertyFile;
    }
}