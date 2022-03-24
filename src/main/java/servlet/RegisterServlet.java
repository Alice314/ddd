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
@WebServlet("/user/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String salt = PasswordUtil.getSalt();
            String passwordSalt = PasswordUtil.sha256(salt,password);
            connection = DBUtil.getConnection();
            String sql = "insert into t_userSalt(username,salt,passwordSalt) values (?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,salt);
            preparedStatement.setString(3,passwordSalt);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }
        PrintWriter out = resp.getWriter();
        out.print("<h1>Register Success</h1>");
        out.print("<a href=\"/mm/index.html\">return to login!</a>");
    }
}
