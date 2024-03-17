package gui.model;

import BE.Coordinator;
import BLL.CoordinatorLogic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class CoordinatorModel {

    public CoordinatorModel() {
        try {
            coordinators.addAll(coordinatorLogic.getAllCoordinators());
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    CoordinatorLogic coordinatorLogic = new CoordinatorLogic();

    private ObservableList<Coordinator> coordinators = FXCollections.observableArrayList();

    public ObservableList<Coordinator> getCoordinators() {
        return coordinators;
    }

    public Coordinator createCoordinator(Coordinator coordinator) throws SQLException {
        Coordinator c = coordinatorLogic.createCoordinator(coordinator);
        coordinators.add(c);
        return c;
    }

    public void deleteCoordinator(Coordinator coordinator) throws SQLException {
        coordinatorLogic.deleteCoordinators(coordinator);
    }

    public boolean isValidCoordinator(String inputUsername, String inputPassword) {
        return coordinators.stream()
                .anyMatch(coordinator -> coordinator.getUsername().equals(inputUsername) && coordinator.getPassword().equals(inputPassword));
    }

    public Coordinator getCoordinatorByUsername(String username) throws SQLException {
        return coordinatorLogic.getCoordinatorsByUsername(username);
    }
}