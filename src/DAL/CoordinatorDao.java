package DAL;

import BE.Coordinator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorDao {
    private static ConnectionManager connectionManager;

    public  CoordinatorDao(){
        connectionManager = new ConnectionManager();
    }
    public List<Coordinator> getAllCoordinators() throws SQLException {
        List<Coordinator> allCoordinators = new ArrayList<>();
        try (Connection con = connectionManager.getConnection()) {
            String sql = "SELECT * FROM Coordinator;";
            Statement statement = con.createStatement();

            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");

                    Coordinator coordinator = new Coordinator(id, username, password);
                    allCoordinators.add(coordinator);
                }
            }
        } return allCoordinators;
    }
    public Coordinator createCoordinator(Coordinator coordinator) throws SQLException {
        try (Connection con = connectionManager.getConnection()) {
            PreparedStatement pst = con.prepareStatement("INSERT INTO Coordinator (username, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, coordinator.getUsername());
            pst.setString(2, coordinator.getPassword());
            pst.execute();

            if (pst.getGeneratedKeys().next()) {
                int id = pst.getGeneratedKeys().getInt(1);
                coordinator.setId(id);
                return coordinator;
            }
        }
        throw new RuntimeException("Id not set");
    }
    public static void deleteCoordinator(Coordinator coordinator) throws SQLException {
        try (Connection con = connectionManager.getConnection()) {
            PreparedStatement statement = con.prepareStatement("DELETE FROM Coordinator WHERE id = ?;");
            statement.setInt(1, coordinator.getId());
            statement.executeUpdate();
        }
    }
    public Coordinator getCoordinatorByUsername(String username) throws SQLException {
        Coordinator coordinator = null;
        try (Connection con = connectionManager.getConnection()) {
            String sql = "SELECT * FROM Coordinator WHERE username = ?;";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);

            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String password = resultSet.getString("password");
                coordinator = new Coordinator(id, username, password);
            }
        }
        return coordinator;
    }
}
