package View;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import View.FormQuanLyVaPhanCongDayHoc;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class database extends JFrame {
    private JTextField dbNameField, usernameField;
    private JPasswordField passwordField;
    private final JButton connectButton;
    

    public database() {
        // Thiết lập giao diện
        setTitle("Database Connection");
        setSize(400, 230);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Các trường nhập
        JLabel dbLabel = new JLabel("Database Name:");
        dbLabel.setBounds(50, 20, 100, 25);
        add(dbLabel);
        dbNameField = new JTextField();
        dbNameField.setBounds(150, 20, 150, 25);
        add(dbNameField);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 60, 100, 25);
        add(userLabel);
        usernameField = new JTextField();
        usernameField.setBounds(150, 60, 150, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 100, 25);
        add(passLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 150, 25);
        add(passwordField);

        // Nút connect
        connectButton = new JButton("Connection");
        connectButton.setBounds(150, 140, 100, 25);
        add(connectButton);

        // Xử lý sự kiện khi bấm nút connect
        connectButton.addActionListener((ActionEvent e) -> {
            String dbName = dbNameField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Kiểm tra kết nối
            if (connectToDatabase(dbName, username, password)) {
                JOptionPane.showMessageDialog(null, "Connection Successful!");
                dispose();  // Đóng form kết nối
                new LoginForm(dbName); // Mở form đăng nhập mới
            } else {
                JOptionPane.showMessageDialog(null, "Connection Failed. Please check your credentials.");
            }
        });
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Phương thức kiểm tra kết nối cơ sở dữ liệu
    private boolean connectToDatabase(String dbName, String username, String password) {
        try {
            String url = "jdbc:mysql://localhost:3306/" + dbName + "?useSSL=false&serverTimezone=UTC"; // URL cho MySQL
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection != null;  // Nếu kết nối thành công
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;  // Kết nối thất bại
        }
    }

    public static void main(String[] args) {
        new database();
       
    }

    // Lớp LoginForm với giao diện đăng nhập đơn giản
    private static class LoginForm extends JFrame {
        private JTextField userField;
        private JPasswordField passField;

        public LoginForm(String dbName) {
            setTitle("Login Form");
            setSize(300, 150);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(null);

            JLabel userLabel = new JLabel("Username:");
            userLabel.setBounds(30, 20, 80, 25);
            add(userLabel);
            userField = new JTextField();
            userField.setBounds(120, 20, 150, 25);
            add(userField);

            JLabel passLabel = new JLabel("Password:");
            passLabel.setBounds(30, 60, 80, 25);
            add(passLabel);
            passField = new JPasswordField();
            passField.setBounds(120, 60, 150, 25);
            add(passField);

            JButton loginButton = new JButton("Login");
            loginButton.setBounds(100, 100, 80, 25);
            add(loginButton);

            loginButton.addActionListener((ActionEvent e) -> {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                if (validateLogin(username, password, dbName)) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    dispose(); // Đóng form đăng nhập
                    new FormQuanLyVaPhanCongDayHoc().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.");
                }
            });
            setLocationRelativeTo(null);
            setVisible(true);
        }

        private boolean validateLogin(String username, String password, String dbName) {
            try {
                // Thay đổi thông tin kết nối nếu cần
                String url = "jdbc:mysql://localhost:3306/" + dbName + "?useSSL=false&serverTimezone=UTC";
                Connection connection = DriverManager.getConnection(url, "root", "Duycuong@123"); // Thay đổi thông tin tài khoản nếu cần

                // Tạo câu truy vấn để kiểm tra username và password
                String query = "SELECT * FROM users WHERE username = ? AND password = MD5(?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();

                // Kiểm tra xem có dòng nào được trả về không
                return resultSet.next();
            } catch (SQLException ex) {
                return false; // Kết nối thất bại
            }
        }
    }
}