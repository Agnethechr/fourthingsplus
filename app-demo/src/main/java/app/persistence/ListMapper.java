package app.persistence;

import app.entities.Lists;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListMapper {

    public static List<Lists> getAllListsPerUser(int userId, ConnectionPool connectionPool) throws DatabaseException {
        List<Lists> listList = new ArrayList<>();
        String sql = "select * from list where user_id=? order by listname";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("list_id");
                String listName = rs.getString("listname");
                listList.add(new Lists(id, listName, userId));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl!!!!", e.getMessage());
        }
        return listList;
    }

    public static Lists addList(User user, String listName, ConnectionPool connectionPool) throws DatabaseException {
        Lists newList = null;

        String sql = "insert into list (listname, user_id) values (?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, listName);
            ps.setInt(2, user.getUserId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int newId = rs.getInt(1);
                newList = new Lists(newId, listName, user.getUserId());
            } else {
                throw new DatabaseException("Fejl under indsætning af liste: " + listName);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl i DB connection", e.getMessage());
        }
        return newList;
    }


    public static void delete(int listId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "delete from list where list_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, listId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl i opdatering af en liste");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved sletning af en liste", e.getMessage());
        }
    }

    public static Lists getListById(int listId, ConnectionPool connectionPool) throws DatabaseException {
        Lists list = null;

        String sql = "select * from list where list_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, listId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("list_id");
                String name = rs.getString("name");
                int userId = rs.getInt("user_id");
                list = new Lists(id, name, userId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved hentning af liste med id = " + listId, e.getMessage());
        }
        return list;
    }

    public static void update(int listId, String taskName, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "update list set name = ? where list_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, taskName);
            ps.setInt(2, listId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl i opdatering af en liste");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl i opdatering af en liste", e.getMessage());
        }
    }
}