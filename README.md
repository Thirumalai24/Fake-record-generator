# Fake-record-generator

## Overview
The Fake Employee Records Generator is a Java application that generates fake employee records and inserts them into a MySQL database. It utilizes the JavaFaker library to create realistic fake data for employee names, job titles, and year of joining.

## Features
- Generates a specified number of fake employee records.
- Insert the generated records into a MySQL database using JDBC batch processing.
- Automatically creates the necessary database, table, and stored procedure for inserting data.

## Prerequisites
Before running the application, ensure you have the following:
- Java Development Kit (JDK) installed on your system.
- Java Runtime Environment (JRE) installed on your system.
- MySQL database server installed and running.
- Maven installed (for building the project).

## Usage
### 1. Clone the Repository
```
git clone https://github.com/yourusername/FakeEmployeeRecords.git
```

### 2. Build the Project
```
cd FakeEmployeeRecords
mvn clean package 
```
### 3. Run the Application
Execute the generated JAR file with the required parameters.
```
java -jar FakeEmployeeRecords-0.0.1-SNAPSHOT.jar <DBUsername> <DBPassword> <dbName> <count>
```
- Replace DBUsername, DBPassword, dbName  and count with your MySQL database credentials.

## Viewing Results

After the application completes execution, you can verify that the fake employee records have been successfully inserted into your MySQL database.

### Parameters
- `<DBUsername>`: MySQL username for database connection.
- `<DBPassword>`: MySQL password for database connection.
- `<dbName>`: Name of the database where the employee records will be inserted.

## Notes
- Make sure that your MySQL database server is running before executing the application.
- The application will automatically create the necessary database, table, and stored procedure for inserting data if they do not already exist.

## Dependencies
- **JavaFaker**: A library for generating fake data.
- **Picocli**: A command-line interface framework for Java applications.
- **Maven Shade Plugin**: A plugin for building runnable JAR files that include all dependencies, allowing for easier distribution and execution of your application.



