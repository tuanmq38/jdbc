package com.quintus.jdbc.demo;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class StudentDbUtil {
    private DataSource dataSource;

    public StudentDbUtil(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Student> getStudent() throws Exception {

        List<Student> students = new ArrayList<>();
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        try {
            // get a connection to the database
            myConn = dataSource.getConnection();

            // create sql statement
            String sql = "select * from student order by id";
            myStmt = myConn.createStatement();

            // execute query
            myRs = myStmt.executeQuery(sql);

            // process result set
            while (myRs.next()) {
                // retrieve data from result set row
                int id = myRs.getInt("id");
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");

                // create new student object

                Student tempStudent = new Student(id, firstName, lastName, email);

                // add it to the list of student
                students.add(tempStudent);
            }
            return students;
        } finally {
            close(myConn, myStmt, myRs);
        }
    }

    private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
        try {
            if (myRs != null) {
                myRs.close();
            }
            if (myStmt != null) {
                myStmt.close();
            }
            if (myConn != null) {
                myConn.close();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public void addStudent(Student theStudent) {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            // get db connection
            myConn = dataSource.getConnection();
            // create sql for inserting student
            String sql = "insert into student "
                    + "(first_name, last_name, email) "
                    + "values(?, ?, ?)";
            myStmt = myConn.prepareStatement(sql);

            // set the params values for the students
            myStmt.setString(1, theStudent.getFirstName());
            myStmt.setString(2, theStudent.getLastName());
            myStmt.setString(3, theStudent.getEmail());

            // execute sql insert
            myStmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            // clean up JDBC objects
            close(myConn, myStmt, null);
        }
    }

    public Student getStudent(String theStudentId)
            throws Exception {
        Student theStudent = null;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        int studentId;
        try {
            studentId = Integer.parseInt(theStudentId);

            myConn = dataSource.getConnection();

            String sql = "select * from student where id=?";

            myStmt = myConn.prepareStatement(sql);

            myStmt.setInt(1, studentId);

            myRs = myStmt.executeQuery();

            // retrieve data from result set row
            if (myRs.next()) {
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");

                theStudent = new Student(studentId, firstName, lastName, email);
            } else {
                throw new Exception("Could not find student id: " + studentId);
            }

            return theStudent;
        } finally {
            close(myConn, myStmt, myRs);
        }
    }

    public void updateStudent(Student theStudent)
            throws Exception {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            myConn = dataSource.getConnection();

            String sql = "update student "
                    + "set first_name=?, last_name=?, email=? "
                    + "where id=?";

            myStmt = myConn.prepareStatement(sql);

            myStmt.setString(1, theStudent.getFirstName());
            myStmt.setString(2, theStudent.getLastName());
            myStmt.setString(3, theStudent.getEmail());
            myStmt.setInt(4, theStudent.getId());

            myStmt.execute();
        } finally {
            close(myConn, myStmt, null);
        }
    }

    public void deleteStudent(String theStudentId) throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            int studentId = Integer.parseInt(theStudentId);
            myConn = dataSource.getConnection();

            String sql = "delete from student where id=? ";

            myStmt = myConn.prepareStatement(sql);

            myStmt.setInt(1, studentId);

            myStmt.execute();

        } finally {
            close(myConn, myStmt, null);
        }
    }
}