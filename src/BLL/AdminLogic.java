package BLL;

import BE.Admin;
import DAL.AdminDao;

import java.sql.SQLException;
import java.util.List;

public class AdminLogic {
    AdminDao adminDao = new AdminDao();
    public List<Admin> getAllAdmins() throws SQLException {

        return adminDao.getAllAdmins();
    }
}
