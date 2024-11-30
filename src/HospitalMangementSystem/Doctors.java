package HospitalMangementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctors {
    private Connection connection;

    public Doctors(Connection connection) {
        this.connection = connection;
    }

    // View Doctors
    public void viewDoctors() {
        String query = "SELECT * FROM Doctors";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("Doctors");
            System.out.println("+------------+---------------------+-----------------------+");
            System.out.println("| Doctor Id  | Name                | Specialization        |");
            System.out.println("+------------+---------------------+-----------------------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String  SPECELIZATION  = resultSet.getString("SPECELIZATION");

                System.out.printf("| %-11d | %-20s | %-22s |\n", id, name, SPECELIZATION);
            }
            System.out.println("+------------+---------------------+-----------------------+");

        } catch (SQLException e) {
            System.err.println("Error retrieving doctors: " + e.getMessage());
        }
    }

    // Get Doctor By ID
    public boolean getDoctorById(int id) {
        String query = "SELECT * FROM Doctors WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("Error retrieving doctor by ID: " + e.getMessage());
        }
        return false;
    }
}
