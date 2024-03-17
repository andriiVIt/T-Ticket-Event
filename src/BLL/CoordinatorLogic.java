package BLL;

import BE.Coordinator;
import DAL.CoordinatorDao;

import java.sql.SQLException;
import java.util.List;

public class CoordinatorLogic {
    CoordinatorDao coordinatorDao = new CoordinatorDao();

    public Coordinator createCoordinator(Coordinator coordinator)throws SQLException{
        return coordinatorDao.createCoordinator(coordinator);
    }
    public List<Coordinator> getAllCoordinators() throws SQLException{
        return  coordinatorDao.getAllCoordinators();
    }
    public  void  deleteCoordinators(Coordinator coordinator)throws  SQLException{
        CoordinatorDao.deleteCoordinator(coordinator);
    }
    public Coordinator getCoordinatorsByUsername(String username)throws  SQLException{
       return  coordinatorDao.getCoordinatorByUsername(username);
    }
}
