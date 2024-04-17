package BLL;

import BE.Admin;
import DAL.AdminDao;

import java.sql.SQLException;
import java.util.List;

public class AdminLogic {
    AdminDao adminDao = new AdminDao();
    // Method to retrieve all Admin records from the database
    public List<Admin> getAllAdmins() throws SQLException {

        return adminDao.getAllAdmins();
    }
}

