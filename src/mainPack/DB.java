package mainPack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import props.Admin;
import utils.MD5;

public class DB {

    private final String driver = "org.sqlite.JDBC";
    private final String url = "jdbc:sqlite:database/database.db";

    private Connection conn = null;
    private PreparedStatement pre = null;

    public static Admin admin = new Admin();

    public DB() {

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url);
            System.out.println("Connection Success");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Connection Error : " + e);
        }

    }

    public boolean adminLogin(String email, String password) {
        boolean status = false;

        try {
            String sql = " SELECT * FROM admins WHERE password = ? and email = ? ";
            pre = conn.prepareStatement(sql);
            pre.setString(1, MD5.createMD5Password(password, 3));
            pre.setString(2, email);
            ResultSet rs = pre.executeQuery();
            status = rs.next();
            if (status) {
                admin.setId(rs.getInt("id"));
                admin.setEmail(rs.getString("email"));
                admin.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Login Error : " + e);
        }

        return status;
    }

    public TableModel listCustomers() {

        DefaultTableModel tableModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        tableModel.addColumn("id");
        tableModel.addColumn("İsim");
        tableModel.addColumn("Soyisim");
        tableModel.addColumn("Telefon");
        tableModel.addColumn("Adres");

        try {
            String sql = "SELECT * FROM customers";
            pre = conn.prepareStatement(sql);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String surName = rs.getString("surname");
                String phoneNumber = rs.getString("phone_number");
                String address = rs.getString("address");

                Object[] row = {id, name, surName, phoneNumber, address};
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            System.err.println("listCustomers Error : " + e);

        }

        return tableModel;
    }

    public int addCustomer(String name, String surName, String phoneNumber, String address) {
        int status = 0;

        try {
            String sql = " INSERT INTO customers VALUES ( null, ?, ?, ?, ? ) ";
            pre = conn.prepareStatement(sql);
            pre.setString(1, name);
            pre.setString(2, surName);
            pre.setString(3, phoneNumber);
            pre.setString(4, address);
            status = pre.executeUpdate();

        } catch (SQLException e) {
            System.err.println("productInsert Error : " + e);
            if (e.toString().contains("SQLITE_CONSTRAINT_UNIQUE")) {
                status = -1;
            }
        }

        return status;
    }

    public int updateCustomer(int id, String name, String surName, String phoneNumber, String address) {
        int status = 0;

        try {
            String sql = " UPDATE customers SET name = ?, surname = ?, phone_number = ?, address = ? WHERE id = ? ";
            pre = conn.prepareStatement(sql);
            pre.setString(1, name);
            pre.setString(2, surName);
            pre.setString(3, phoneNumber);
            pre.setString(4, address);
            pre.setInt(5, id);

            status = pre.executeUpdate();

        } catch (SQLException e) {
            System.err.println("customerUpdate Error : " + e);
            if (e.toString().contains("SQLITE_CONSTRAINT_UNIQUE")) {
                status = -1;
            }
        }

        return status;
    }

    public int deleteCustomer(int id) {

        int status = 0;

        try {
            String sql = "DELETE FROM customers WHERE id = ?";
            pre = conn.prepareStatement(sql);
            pre.setInt(1, id);
            status = pre.executeUpdate();
        } catch (SQLException e) {
            System.err.println("customerDelete Error : " + e);
        }

        return status;
    }

    public TableModel listOrders() {

        DefaultTableModel tableModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        tableModel.addColumn("id");
        tableModel.addColumn("Adı");
        tableModel.addColumn("Soyadı");
        tableModel.addColumn("Durum");
        tableModel.addColumn("Adres");
        tableModel.addColumn("Tutar (TL)");

        try {
            String sql = "SELECT * FROM orders";
            pre = conn.prepareStatement(sql);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String customerName = rs.getString("customer_name");
                String customerSurName = rs.getString("customer_surname");
                String orderStatus = rs.getString("status");
                String address = rs.getString("address");
                double price = rs.getDouble("price");

                Object[] row = {id, customerName, customerSurName, orderStatus, address, price};
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            System.err.println("listOrders Error : " + e);

        }

        return tableModel;

    }

    public int addOrder(String customerName, String customerSurName, String orderStatus, String address, double price) {
        int status = 0;

        try {
            String sql = " INSERT INTO orders VALUES ( null, ?, ?, ?, ?, ? ) ";
            pre = conn.prepareStatement(sql);
            pre.setString(1, customerName);
            pre.setString(2, customerSurName);
            pre.setString(3, orderStatus);
            pre.setString(4, address);
            pre.setDouble(5, price);
            status = pre.executeUpdate();

        } catch (SQLException e) {
            System.err.println("productInsert Error : " + e);
            status = -1;
        }

        return status;
    }

    public int updateOrderStatus(int id, String orderStatus) {
        int status = 0;

        try {
            String sql = " UPDATE orders SET status = ? WHERE id = ? ";
            pre = conn.prepareStatement(sql);
            pre.setString(1, orderStatus);
            pre.setInt(2, id);
            status = pre.executeUpdate();

        } catch (SQLException e) {
            System.err.println("orderUpdate Error : " + e);
            status = -1;
        }

        return status;
    }

    public int deleteOrder(int id) {
        int status = 0;

        try {
            String sql = "DELETE FROM orders WHERE id = ?";
            pre = conn.prepareStatement(sql);
            pre.setInt(1, id);
            status = pre.executeUpdate();
        } catch (SQLException e) {
            System.err.println("orderDelete Error : " + e);
        }

        return status;
    }

    public int deleteAllOrders() {
        int status = 0;

        try {
            String sql = "DELETE FROM orders";
            pre = conn.prepareStatement(sql);
            status = pre.executeUpdate();

        } catch (SQLException e) {
            System.err.println("orderDeleteAll Error : " + e);

        }

        return status;
    }

    public void close() {

        try {

            if (conn != null && !conn.isClosed()) {
                conn.close();
            }

            if (pre != null) {
                pre.close();
            }

        } catch (SQLException e) {
            System.err.println("Close Error : " + e);
        }

    }
}
