package HospitalMangementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner sc;

    public Patient(Connection connection, Scanner sc) {
        this.connection = connection;
        this.sc = sc;
    }

    // Add Patient
    public void addPatient() {
        System.out.println("Enter Your Name:");
        String name = sc.next();
        System.out.println("Enter your Age:");
        int age = sc.nextInt();
        System.out.println("Enter Your Gender:");
        String gender = sc.next();

        String query = "INSERT INTO Patients(name, age, gender) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Patient Added Successfully");
            } else {
                System.out.println("Failed to Add Patient!");
            }
        } catch (SQLException e) {
            System.err.println("Error adding patient: " + e.getMessage());
        }
    }

    // View Patients
    public void viewPatients() {
        String query = "SELECT * FROM Patients";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("Patients");
            System.out.println("+------------+---------------------+-----------+----------+");
            System.out.println("| Patient Id | Name                | Age       | Gender   |");
            System.out.println("+------------+---------------------+-----------+----------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");

                System.out.printf("| %-11d | %-20s | %-9d | %-8s |\n", id, name, age, gender);
            }
            System.out.println("+------------+---------------------+-----------+----------+");

        } catch (SQLException e) {
            System.err.println("Error retrieving patients: " + e.getMessage());
        }
    }

    // Get Patient By ID
    public boolean getPatientById(int id) {
        String query = "SELECT * FROM Patients WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("Error retrieving patient by ID: " + e.getMessage());
        }
        return false;
    }
}
