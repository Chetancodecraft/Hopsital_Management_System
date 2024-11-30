package HospitalMangementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_managament_system";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1505";

    public static void main(String[] args) {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            Patient patient = new Patient(connection, sc);
            Doctors doctors = new Doctors(connection);

            while (true) {
                System.out.println("Hospital Management System");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.print("Enter Your Choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        doctors.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        bookAppointment(patient, doctors, connection, sc);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("Exiting the system...");
                        return;
                    default:
                        System.out.println("Enter a valid choice!");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient, Doctors doctor, Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter Patient ID: ");
        int patientId = sc.nextInt();
        System.out.print("Enter Doctor ID: ");
        int doctorId = sc.nextInt();
        System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
        String appointment_Date = sc.next();

        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
            if (checkDoctorAvailability(doctorId, appointment_Date, connection)) {
                String query = "INSERT INTO appointments(patient_id, doctor_id, Appointment_datec) VALUES (?, ?, ?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointment_Date);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Appointment booked successfully!");
                    } else {
                        System.out.println("Failed to book appointment!");
                    }
                }
            } else {
                System.out.println("Doctor is not available on this date.");
            }
        } else {
            System.out.println("Either the doctor or patient does not exist!");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND Appointment_datec = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0; // Return true if no appointments exist
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}