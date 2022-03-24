package servlet;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DBUtil;
import utils.PasswordUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
@WebServlet("/user/login")
public class UserServlet  extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       boolean success = false;
       String username = req.getParameter("username");
       String password = req.getParameter("password");

       String contextPath = req.getContextPath();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();

            String sql = "select salt,passwordSalt from t_userSalt where username = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                String salt = resultSet.getString(1);
                String passwordSalt = resultSet.getString(2);
                if (passwordSalt.equals(PasswordUtil.sha256(salt,password))){
                    success = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }
        if (success){
            resp.sendRedirect(contextPath+"/success.html");
        }else{
            resp.sendRedirect(contextPath+"/failed.html");
        }
    }
}
