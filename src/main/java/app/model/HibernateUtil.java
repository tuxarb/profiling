package app.model;

import app.model.enums.DatabaseTypes;
import app.utils.Log;
import app.utils.Utils;
import app.utils.exceptions.WrongSelectedDatabaseException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.slf4j.Logger;

import java.util.Locale;
import java.util.Properties;

class HibernateUtil {
    private static final Logger LOG = Log.createLog(HibernateUtil.class);
    private static Properties properties;
    private static final PropertyRepository PROPERTY_REPOSITORY = PropertyRepository.getInstance();

    private static SessionFactory buildSessionFactory() {
        try {
            return new AnnotationConfiguration()
                    .setProperties(properties)
                    .configure()
                    .buildSessionFactory();
        } catch (Exception ex) {
            LOG.error(Log.CREATING_SESSION_FACTORY_ERROR + "\n" + ex.getMessage());
        }
        return null;
    }

    static SessionFactory getSessionFactory() {
        return buildSessionFactory();
    }

    static void initProperties(String url, String username, String password, DatabaseTypes type) throws WrongSelectedDatabaseException {
        LOG.info(Log.START_INIT_PROPERTIES);
        Locale.setDefault(Locale.ENGLISH);
        properties = new Properties();
        properties.setProperty("hibernate.connection.url", url);
        properties.setProperty("hibernate.connection.username", username);
        properties.setProperty("hibernate.connection.password", password);

        if (type.name().equalsIgnoreCase(Utils.POSTGRESQL)) {
            validateDatabaseUser(url, Utils.POSTGRESQL);
            setDriverAndDialect("org.postgresql.Driver", "org.hibernate.dialect.PostgreSQLDialect");
        } else if (type.name().equalsIgnoreCase(Utils.MYSQL)) {
            validateDatabaseUser(url, Utils.MYSQL);
            setDriverAndDialect("com.mysql.jdbc.Driver", "org.hibernate.dialect.MySQLDialect");
        } else if (type.name().equalsIgnoreCase(Utils.ORACLE)) {
            validateDatabaseUser(url, Utils.ORACLE);
            setDriverAndDialect("oracle.jdbc.OracleDriver", "org.hibernate.dialect.OracleDialect");
        } else if (type.name().equalsIgnoreCase(Utils.SQL_SERVER)) {
            validateDatabaseUser(url, Utils.SQL_SERVER);
            setDriverAndDialect("net.sourceforge.jtds.jdbc.Driver", "org.hibernate.dialect.SQLServerDialect");
        } else if (type.name().equalsIgnoreCase(Utils.OTHER_DBMS)) {
            setDriverAndDialect(getDriverFromProperty(), getDialectFromProperty());
        }
        LOG.info(Log.END_INIT_PROPERTIES);
    }

    private static String getDriverFromProperty() {
        return PROPERTY_REPOSITORY.getProperties().getProperty("connection.driver_class");
    }

    private static String getDialectFromProperty() {
        return PROPERTY_REPOSITORY.getProperties().getProperty("hibernate.dialect");
    }

    private static void setDriverAndDialect(String driver, String dialect) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            LOG.error(Log.DRIVER_CLASS_NOT_FOUND_ERROR);
        }
        properties.setProperty("connection.driver_class", driver);
        properties.setProperty("hibernate.dialect", dialect);
    }

    private static void validateDatabaseUser(String url, String database) throws WrongSelectedDatabaseException {
        if (!url.toLowerCase().contains(database.toLowerCase())) {
            LOG.error(Log.WRONG_DATABASE_URL + " [ " + database + " ]");
            throw new WrongSelectedDatabaseException();
        }
    }
}