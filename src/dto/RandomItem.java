package dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class RandomItem {

    private String id;
    private int intVal;
    private double floatVal;
    private String strVal;

    public RandomItem() {
        this.id = UUID.randomUUID().toString();
    }

    public RandomItem(int intVal, double floatVal, String strVal) {
        this.id = UUID.randomUUID().toString();
        this.intVal = intVal;
        this.floatVal = floatVal;
        this.strVal = strVal;
    }

    @Override
    public String toString() {
        return String.format("%s %d %f %s",
                this.getId(),
                this.getIntVal(),
                this.getFloatVal(),
                this.getStrVal());
    }

    public static RandomItem fromResultSet(ResultSet rs) throws SQLException {
        RandomItem randomItem = new RandomItem();
        randomItem.setId(rs.getString("id"));
        randomItem.setIntVal(rs.getInt("int_val"));
        randomItem.setFloatVal(rs.getDouble("float_val"));
        randomItem.setStrVal(rs.getString("str_val"));
        return randomItem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIntVal() {
        return intVal;
    }

    public void setIntVal(int intVal) {
        this.intVal = intVal;
    }

    public double getFloatVal() {
        return floatVal;
    }

    public void setFloatVal(double floatVal) {
        this.floatVal = floatVal;
    }

    public String getStrVal() {
        return strVal;
    }

    public void setStrVal(String strVal) {
        this.strVal = strVal;
    }
}
