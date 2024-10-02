package app.persistence;

import app.entities.Lists;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListMapper

{

    public static List<Lists> getAllListsPerUser(int userId, ConnectionPool connectionPool) throws DatabaseException
    {
        List<Lists> listList = new ArrayList<>();
        String sql = "select * from list where user_id=? order by name";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("list_id");
                String name = rs.getString("name");
                listList.add(new Lists(id, name, userId));
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Fejl!!!!", e.getMessage());
        }
        return listList;
    }

    public static Lists addList(User user, String listName, ConnectionPool connectionPool) throws DatabaseException
    {
        Lists newList = null;

        String sql = "insert into list (name, user_id) values (?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        )
        {
            ps.setString(1, listName);
            ps.setInt(2, user.getUserId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1)
            {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int newId = rs.getInt(1);
                newList = new Lists(newId, listName, user.getUserId());
            } else
            {
                throw new DatabaseException("Fejl under indsætning af liste: " + listName);
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Fejl i DB connection", e.getMessage());
        }
        return newList;
    }


//    public static void delete(int taskId, ConnectionPool connectionPool) throws DatabaseException
//    {
//        String sql = "delete from task where task_id = ?";
//
//        try (
//                Connection connection = connectionPool.getConnection();
//                PreparedStatement ps = connection.prepareStatement(sql)
//        )
//        {
//            ps.setInt(1, taskId);
//            int rowsAffected = ps.executeUpdate();
//            if (rowsAffected != 1)
//            {
//                throw new DatabaseException("Fejl i opdatering af en task");
//            }
//        }
//        catch (SQLException e)
//        {
//            throw new DatabaseException("Fejl ved sletning af en task", e.getMessage());
//        }
//    }

//    public static Task getTaskById(int taskId, ConnectionPool connectionPool) throws DatabaseException
//    {
//        Task task = null;
//
//        String sql = "select * from task where task_id = ?";
//
//        try (
//                Connection connection = connectionPool.getConnection();
//                PreparedStatement ps = connection.prepareStatement(sql)
//        )
//        {
//            ps.setInt(1, taskId);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next())
//            {
//                int id = rs.getInt("task_id");
//                String name = rs.getString("name");
//                Boolean done = rs.getBoolean("done");
//                int userId = rs.getInt("user_id");
//                task = new Task(id, name, done, userId);
//            }
//        }
//        catch (SQLException e)
//        {
//            throw new DatabaseException("Fejl ved hentning af task med id = " + taskId, e.getMessage());
//        }
//        return task;
//    }

//    public static void update(int taskId, String taskName, ConnectionPool connectionPool) throws DatabaseException
//    {
//        String sql = "update task set name = ? where task_id = ?";
//
//        try (
//                Connection connection = connectionPool.getConnection();
//                PreparedStatement ps = connection.prepareStatement(sql)
//        )
//        {
//            ps.setString(1, taskName);
//            ps.setInt(2, taskId);
//            int rowsAffected = ps.executeUpdate();
//            if (rowsAffected != 1)
//            {
//                throw new DatabaseException("Fejl i opdatering af en task");
//            }
//        }
//        catch (SQLException e)
//        {
//            throw new DatabaseException("Fejl i opdatering af en task", e.getMessage());
//        }
//    }
}