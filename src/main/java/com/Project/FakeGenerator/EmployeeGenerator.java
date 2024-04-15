package com.Project.FakeGenerator;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.ArrayList;
import java.util.List;

import com.github.javafaker.Faker;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

//class-level annotation
@Command(name = "employee-generator", mixinStandardHelpOptions = true, version = "1.0",   
        description = "Generates fake employee records and inserts them into the database")

public class EmployeeGenerator implements Callable<Void> {

    private int count;

    @Parameters(index = "0", description = "MySQL  username")
    private String DBUsername;

    @Parameters(index = "1", description = "MySQL password")
    private String DBPassword;

    @Parameters(index = "2", description = "Database name")
    private String dbName;

    @Parameters(index = "3", description = "Number of employee records to generate")
    private void setCount(int count) {
        this.count = count;
    }

    @Override
    public Void call() throws Exception {
        // Create the database, employee table, and stored procedure
        createDatabaseAndTable();

        // Generate fake employee records
        List<String[]> employees = generateFakeEmployeeRecords();

        // Insert records into the database using JDBC batch processing
        insertRecordsIntoDatabase(employees);

        return null;
    }

    // Method to create database and employee table
    private void createDatabaseAndTable() {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/", DBUsername, DBPassword)) {
        	
            // Create the database if not exists
            String createDbSQL = "CREATE DATABASE IF NOT EXISTS " + dbName;
            try (CallableStatement cs = connection.prepareCall(createDbSQL)) {
                cs.executeUpdate();
            }

            // Use the database
            String useDbSQL = "USE " + dbName;
            try (CallableStatement cs = connection.prepareCall(useDbSQL)) {
                cs.executeUpdate();
            }

            // Create the employee table if not exists
            String createTableSQL = "CREATE TABLE IF NOT EXISTS employee ("
                    + "id INT PRIMARY KEY,"
                    + "name VARCHAR(255),"
                    + "designation VARCHAR(255),"
                    + "year_of_joining INT)";
            try (CallableStatement cs = connection.prepareCall(createTableSQL)) {
                cs.executeUpdate();
            }

            // Create the stored procedure for inserting data
            String createProcedureSQL = "CREATE PROCEDURE insert_employee("
                    + "IN emp_id INT,"
                    + "IN emp_name VARCHAR(255),"
                    + "IN emp_designation VARCHAR(255),"
                    + "IN emp_year_of_joining INT)"
                    + "BEGIN"
                    + " INSERT INTO employee (id, name, designation, year_of_joining)"
                    + " VALUES (emp_id, emp_name, emp_designation, emp_year_of_joining);"
                    + "END";
            try (CallableStatement cs = connection.prepareCall(createProcedureSQL)) {
                cs.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to generate fake employee records
    private List<String[]> generateFakeEmployeeRecords() {
        Faker faker = new Faker();
        List<String[]> employees = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String[] employeeDetails = {
                String.valueOf(i + 1),
                faker.name().fullName(),
                faker.job().title(),
                String.valueOf(faker.number().numberBetween(1980, 2023))
            };
            employees.add(employeeDetails);
        }
        return employees;
    }

    // Method to insert records into the database using JDBC batch processing
    private void insertRecordsIntoDatabase(List<String[]> employees) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/" + dbName, DBUsername, DBPassword)) {
            String sql = "CALL insert_employee(?, ?, ?, ?)";
            try (CallableStatement cs = connection.prepareCall(sql)) {
                for (String[] emp : employees) {
                    cs.setString(1, emp[0]);
                    cs.setString(2, emp[1]);
                    cs.setString(3, emp[2]);
                    cs.setInt(4, Integer.parseInt(emp[3]));
                    cs.addBatch();
                }
                // Execute batch insertion
                cs.executeBatch();
                System.out.println("Values inserted successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) {
        CommandLine commandLine = new CommandLine(new EmployeeGenerator());
        int exitCode = commandLine.execute(args);
        System.exit(exitCode);
    }
}
