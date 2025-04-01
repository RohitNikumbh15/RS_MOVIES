package com.rs.movies;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/Register")
public class Register extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Register() {
        super();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String name = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        Connection con = null;
        PreparedStatement checkEmailStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish Database Connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/rsmovies", "root", "rohit");

            // Check if Email Already Exists
            String checkEmailQuery = "SELECT * FROM users WHERE email = ?";
            checkEmailStmt = con.prepareStatement(checkEmailQuery);
            checkEmailStmt.setString(1, email);
            rs = checkEmailStmt.executeQuery();

            if (rs.next()) {
                // Email already exists, redirect to login
                res.setContentType("text/html");
                System.out.println("User already registered with this email.");
                RequestDispatcher rd = req.getRequestDispatcher("/login.html");
                rd.include(req, res);
            } else {
                // Insert New User
                String insertQuery = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
                insertStmt = con.prepareStatement(insertQuery);
                insertStmt.setString(1, name);
                insertStmt.setString(2, email);
                insertStmt.setString(3, password);

                int count = insertStmt.executeUpdate();
                if (count > 0) {
                    res.setContentType("text/html");
                    System.out.println("User registration successful.");
                    RequestDispatcher rd = req.getRequestDispatcher("/home.html");
                    rd.include(req, res);
                } else {
                    res.setContentType("text/html");
                    System.out.println("User registration failed.");
                    RequestDispatcher rd = req.getRequestDispatcher("/register.html");
                    rd.include(req, res);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close Resources
            try {
                if (rs != null) rs.close();
                if (checkEmailStmt != null) checkEmailStmt.close();
                if (insertStmt != null) insertStmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
