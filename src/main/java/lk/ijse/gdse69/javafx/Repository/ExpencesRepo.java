package lk.ijse.gdse69.javafx.Repository;

import lk.ijse.gdse69.javafx.Model.Expences;
import lk.ijse.gdse69.javafx.db.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpencesRepo {
    public static boolean save(Expences expences) throws SQLException {

        String query = "INSERT INTO Expences VALUES(?,?,?,?,?)";

        Connection connection = DbConnection.getInstance().getConnection();
        if (connection != null && !connection.isClosed()) {
            System.out.println("Connection is active.");
        } else {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/New_Magazine_Prison", "root", "Ijse@123");
        }
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setObject(1, expences.getExpenceId());
        pstm.setObject(2, expences.getSectionId());
        pstm.setObject(3, expences.getMonth());
        pstm.setObject(4, expences.getType());
        pstm.setObject(5, expences.getCost());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String expenceId) throws SQLException {
        String query = "DELETE FROM Expences WHERE expencesId = ?";

        Connection connection = DbConnection.getInstance().getConnection();

        if (connection != null && !connection.isClosed()) {
            System.out.println("Connection is active.");
        } else {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/New_Magazine_Prison", "root", "Ijse@123");
        }
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setObject(1, expenceId);

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Expences expences) throws SQLException {
        String query = "UPDATE Expences SET sectionId = ?, month = ?, type = ?, cost = ? WHERE expencesId = ?";

        Connection connection = DbConnection.getInstance().getConnection();

        if (connection != null && !connection.isClosed()) {
            System.out.println("Connection is active.");
        } else {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/New_Magazine_Prison", "root", "Ijse@123");
        }
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setObject(1, expences.getSectionId());
        pstm.setObject(2, expences.getMonth());
        pstm.setObject(3, expences.getType());
        pstm.setObject(4, expences.getCost());
        pstm.setObject(5, expences.getExpenceId());

        return pstm.executeUpdate() > 0;
    }

    public static Expences search(String expenceId) throws SQLException {
        String query = "SELECT * FROM Expences WHERE expencesId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        if (connection != null && !connection.isClosed()) {
            System.out.println("Connection is active.");
        } else {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/New_Magazine_Prison", "root", "Ijse@123");
        }
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setObject(1, expenceId);

        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            String id = resultSet.getString(1);
            String sectionId = resultSet.getString(2);
            String month = resultSet.getString(3);
            String type = resultSet.getString(4);
            double cost = resultSet.getDouble(5);

            Expences expences = new Expences(id, sectionId, month, type, cost);

            return expences;
        }
        return null;
    }

    public static List<Expences> getAllExpenses() {
        List<Expences> expenses = new ArrayList<>();
        try {
            String query = "SELECT * FROM Expences";

            Connection connection = DbConnection.getInstance().getConnection();
            if (connection != null && !connection.isClosed()) {
                System.out.println("Connection is active.");
            } else {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/New_Magazine_Prison", "root", "Ijse@123");
            }
            PreparedStatement pstm = connection.prepareStatement(query);

            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String sectionId = resultSet.getString(2);
                String month = resultSet.getString(3);
                String type = resultSet.getString(4);
                double cost = resultSet.getDouble(5);

                Expences expences = new Expences(id, sectionId, month, type, cost);
                expenses.add(expences);
            }
            return expenses;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Expences> getExpensesByType(String inType) {
        List<Expences> expenses = new ArrayList<>();
        try {
            String query = "SELECT * FROM Expences WHERE type = ?";

            Connection connection = DbConnection.getInstance().getConnection();
            if (connection != null && !connection.isClosed()) {
                System.out.println("Connection is active.");
            } else {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/New_Magazine_Prison", "root", "Ijse@123");
            }
            PreparedStatement pstm = connection.prepareStatement(query);

            pstm.setObject(1, inType);
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String sectionId = resultSet.getString(2);
                String month = resultSet.getString(3);
                String type = resultSet.getString(4);
                double cost = resultSet.getDouble(5);

                Expences expences = new Expences(id, sectionId, month, type, cost);
                expenses.add(expences);
            }
            return expenses;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Double> getTotalCostByType(){
        String yearAndMonth = "2024/6"; // Example: "YYYY/MM"

        // Parse the year and month
        String[] parts = yearAndMonth.split("/");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        // Subtract one from the year to get the previous year
        int previousYear = year - 1;

        // Construct a new string for the previous year
        String previousYearAndMonth = previousYear + "/" + month;

        // Query the database for expenses from the previous year
        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT type, SUM(cost) AS total_cost FROM Expences WHERE month LIKE ? GROUP BY type")) {
            statement.setString(1, previousYear + "/%");
            ResultSet resultSet = statement.executeQuery();

            // Map to store total expenses for each expense type
            Map<String, Double> expensesByType = new HashMap<>();

            // Process the result set
            while (resultSet.next()) {
                // Retrieve expense type and total cost
                String expenseType = resultSet.getString("type");
                double totalCost = resultSet.getDouble("total_cost");

                // Store total cost for the expense type
                expensesByType.put(expenseType, totalCost);
            }

            // Print total expenses for each expense type
            for (Map.Entry<String, Double> entry : expensesByType.entrySet()) {
                System.out.println("Expense Type: " + entry.getKey() + ", Total Cost: " + entry.getValue());
            }
            return expensesByType;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
