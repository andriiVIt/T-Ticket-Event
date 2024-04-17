package BLL;

import BE.Coordinator;
import DAL.CoordinatorDao;

import java.sql.SQLException;
import java.util.List;

public class CoordinatorLogic {

    // Field for the data access object that interacts with the database for Coordinator entities.
    CoordinatorDao coordinatorDao = new CoordinatorDao();

    // Method to create a new Coordinator in the database
    public Coordinator createCoordinator(Coordinator coordinator)throws SQLException{
        return coordinatorDao.createCoordinator(coordinator);
    }

    // Method to retrieve all Coordinators from the database
    public List<Coordinator> getAllCoordinators() throws SQLException{
        return  coordinatorDao.getAllCoordinators();
    }
    // Method to delete a Coordinator from the database
    public  void  deleteCoordinators(Coordinator coordinator)throws  SQLException{
        CoordinatorDao.deleteCoordinator(coordinator);
    }
    // Method to retrieve a Coordinator by username
    public Coordinator getCoordinatorsByUsername(String username)throws  SQLException{
        return  coordinatorDao.getCoordinatorByUsername(username);
    }
}

