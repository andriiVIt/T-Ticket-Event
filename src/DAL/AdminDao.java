package DAL;

import BE.Admin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdminDao {
    private  ConnectionManager connectionManager;


    public AdminDao(){
        connectionManager = new ConnectionManager();
    }


    public List<Admin> getAllAdmins() throws SQLException {
        List<Admin> getAllAdmins = new ArrayList<>();
        try (Connection con = connectionManager.getConnection()){
            String sql ="SELECT * FROM Admin";
            Statement statement = con.createStatement();


            if(statement.execute(sql)){
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                     String password = resultSet.getString("password");
                    Admin admin = new Admin(id, username, password);
                    getAllAdmins.add(admin);
                }
            }

        }
        return getAllAdmins;
    }
}
