package com.quintus.jdbc.demo;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "StudentControllerServlet", urlPatterns = {"/list-students", "/StudentControllerServlet"})
public class StudentControllerServlet extends HttpServlet {

    private StudentDbUtil studentDbUtil;
    @Resource(name="jdbc/web_student_tracker")
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        super.init();

        try {
            studentDbUtil = new StudentDbUtil(dataSource);
        }
        catch (Exception exc) {
            throw  new ServletException(exc);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // read the "command" parameter
            String theCommand = request.getParameter("command");
            // default as listing students
            if(theCommand == null) {
                theCommand = "LIST";
            }
            // route to the method
            switch(theCommand) {
                case "LIST":
                    listStudent(request, response);
                    break;
                case "ADD":
                    addStudent(request, response);
                    break;
                case "LOAD":
                    loadStudent(request, response);
                    break;
                case "UPDATE":
                    updateStudent(request, response);
                    break;
                case "DELETE":
                    deleteStudent(request, response);
                    break;
                default:
                    listStudent(request, response);
            }
            listStudent(request, response);
        }
        catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String theStudentId = request.getParameter("studentId");

        studentDbUtil.deleteStudent(theStudentId);

        listStudent(request, response);
    }

    private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = Integer.parseInt(request.getParameter("studentId"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        Student theStudent = new Student(id, firstName, lastName, email);

        studentDbUtil.updateStudent(theStudent);

        listStudent(request, response);
    }

    private void loadStudent(HttpServletRequest request, HttpServletResponse response)
    throws Exception {
        String theStudentId = request.getParameter("studentId");

        Student theStudent = studentDbUtil.getStudent(theStudentId);

        request.setAttribute("THE_STUDENT", theStudent);

        RequestDispatcher dispatcher =
                request.getRequestDispatcher("/update-student-form.jsp");
        dispatcher.forward(request,response);
    }

    private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // read student info from data
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        // create a new student object
        Student theStudent = new Student(firstName, lastName, email);

        // add the student to the database
        studentDbUtil.addStudent(theStudent);

        // send back to main page (the student list)
        listStudent(request, response);

    }

    private void listStudent(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        List<Student> students = studentDbUtil.getStudent();

        request.setAttribute("student_list", students);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
        dispatcher.forward(request, response);
    }
}
