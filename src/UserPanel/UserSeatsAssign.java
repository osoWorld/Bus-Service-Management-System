package UserPanel;

import java.util.ArrayList;

public class UserSeatsAssign {
    private String tableName;
    private ArrayList<String> selectedSeats = new ArrayList<>();

    public UserSeatsAssign(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public ArrayList<String> getSelectedSeats() {
        return selectedSeats;
    }

    public void setSelectedSeats(ArrayList<String> selectedSeats) {
        this.selectedSeats = selectedSeats;
    }
}
