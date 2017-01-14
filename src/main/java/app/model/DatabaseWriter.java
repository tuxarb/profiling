package app.model;

import app.utils.exceptions.WrongSelectedDatabaseException;
import app.model.beans.Characteristic;
import app.model.enums.DatabaseTypes;
import app.utils.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import java.io.IOException;

class DatabaseWriter {
    private static final Logger LOG = Log.createLog(DatabaseWriter.class);
    private static SessionFactory sessionFactory;
    private DatabaseTypes databaseType;
    private String url;
    private String username;
    private String password;

    DatabaseWriter(DatabaseTypes databaseType) {
        this.databaseType = databaseType;
    }

    void write(Characteristic ch) throws Exception{
        LOG.info(Log.START_SAVING_TO_DATABASE);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(ch);
        session.getTransaction().commit();
        session.close();
        LOG.info(Log.END_SAVING_TO_DATABASE);
    }

    void setSessionFactory() throws IOException {
        sessionFactory = HibernateUtil.getSessionFactory();

        if (sessionFactory == null) {
            LOG.error(Log.SESSION_FACTORY_IS_NULL);
            throw new IOException();
        }
    }

    void initProperties() throws IOException, WrongSelectedDatabaseException {
        if (url.isEmpty()) {
            LOG.error(Log.URL_TO_DATABASE_IS_NULL);
            throw new IOException();
        }
        HibernateUtil.initProperties(url, username, password, databaseType);
    }

    void setUrl(String url) {
        this.url = url;
    }

    void setUsername(String username) {
        this.username = username;
    }

    void setPassword(String password) {
        this.password = password;
    }
}
