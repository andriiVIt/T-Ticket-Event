package DAL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;


public class ConnectionManager {
    // Field to store the SQLServerDataSource instance
    private final SQLServerDataSource ds;

    // Constructor to initialize the SQLServerDataSource
    public ConnectionManager() {
        ds = new SQLServerDataSource(); // Create a new instance of SQLServerDataSource

        // Setting properties for the data source
        ds.setDatabaseName("T-Ticket Event1"); // Name of the database to connect to
        ds.setUser("CSe2023b_e_5"); // Username for database login
        ds.setPassword("CSe2023bE5#23 "); // Password for database login
        ds.setServerName("EASV-DB4"); // Server name where the database is hosted
        ds.setTrustServerCertificate(true); // Trust the SSL certificate without validating (for development use)
    }

    // Method to get a database connection from the data source
    public Connection getConnection() throws SQLServerException {
        return ds.getConnection(); // Retrieves a connection from the configured data source
    }
}
