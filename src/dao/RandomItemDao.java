package dao;


import dto.RandomItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RandomItemDao {
    private final Connection connection;

    public RandomItemDao(Connection connection) {
        this.connection = connection;
    }

    public List<RandomItem> getAll() {
        List<RandomItem> res = new ArrayList<>();
        String sql = "SELECT r.`id`, r.`int_val`, r.`float_val`, r.`str_val` "
                + "FROM `random_items` r";
        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()) {
                res.add(RandomItem.fromResultSet(rs));
            }
        }
        catch(SQLException ex) {
            System.err.println("RandomItemDao::getAll " + ex.getMessage());
        }
        return res;
    }

    public void add(RandomItem randomItem)
    {

        String sql = "INSERT INTO `random_items`(`id`, `int_val`, `float_val`, `str_val`)"
                + "VALUES(UUID(), ?, ?, ?)";
        ;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, randomItem.getId());
            statement.setInt(2, randomItem.getIntVal());
            statement.setDouble(3, randomItem.getFloatVal());
            statement.setString(4, randomItem.getStrVal());

            statement.executeUpdate();
            //System.out.println("INSERT OK");
        } catch (SQLException ex) {
            System.err.println("RandomItemDao::add " + ex.getMessage());
        }

    }
    public boolean delete(String id) {
        String sql = "DELETE FROM `random_items` WHERE `id` = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            int affected = statement.executeUpdate();
            return affected > 0;
        } catch (SQLException ex) {
            System.err.println("RandomItemDao::delete " + ex.getMessage());
            return false;
        }
    }

}
