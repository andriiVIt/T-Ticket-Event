package DAL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;


public class ConnectionManager {
    private final SQLServerDataSource ds;

    public ConnectionManager(){
        ds = new SQLServerDataSource();

        ds.setDatabaseName("T-Ticket Event1");
        ds.setUser("CSe2023b_e_5");
        ds.setPassword("CSe2023bE5#23 ");
        ds.setServerName("EASV-DB4");
        ds.setTrustServerCertificate(true);

    }
    public Connection getConnection() throws SQLServerException{
        return ds.getConnection();
    }
}
