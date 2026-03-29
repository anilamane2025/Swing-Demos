package org.example.studentapp;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class StudentForm {

    // Stores all student records in memory
    static ArrayList<Student> studentList = new ArrayList<>();
    static StudentService studentService = new StudentService();
    // Used to identify whether the form is in normal add mode or edit/update mode
    static boolean editMode = false;
    static int editIndex = -1;

    // Temporary values used to prefill the form when editing a student
    static String tempName;
    static int tempAge;
    static String tempCourse;

    static int editStudentId = -1;

    // Opens the form in edit mode with existing student data prefilled
    public static void openForm(String name, int age, String course) {
        editMode = true;

        tempName = name;
        tempAge = age;
        tempCourse = course;

        main(null);
    }

    public static void main(String[] args) {
        studentService.createTable();
        try{
            System.out.println(DBConnection.getConnection());
        }catch(Exception e){
            e.printStackTrace();
        }

        JFrame frame = new JFrame();
        frame.setTitle("Student Form");

        // Change form title when user is updating an existing student
        if (editMode) {
            frame.setTitle("Update Student");
        }

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(15);
        JPanel namePanel = new JPanel();
        namePanel.add(nameLabel);
        namePanel.add(nameField);

        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField(15);
        JPanel agePanel = new JPanel();
        agePanel.add(ageLabel);
        agePanel.add(ageField);

        JLabel courseLabel = new JLabel("Course:");
        String[] courses = {"Java", ".NET", "Python"};
        JComboBox<String> courseBox = new JComboBox<>(courses);
        JPanel coursePanel = new JPanel();
        coursePanel.add(courseLabel);
        coursePanel.add(courseBox);

        // Prefill the form fields when edit mode is active
        if (editMode) {
            nameField.setText(tempName);
            ageField.setText(String.valueOf(tempAge));
            courseBox.setSelectedItem(tempCourse);
        }

        JLabel searchLabel = new JLabel("Search Name:");
        JTextField searchField = new JTextField(15);
        JPanel searchPanel = new JPanel();
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        JButton addButton = new JButton("Add Student");

        // Change button text when the same form is used for updating
        if (editMode) {
            addButton.setText("Update Student");
        }

        JButton showButton = new JButton("Show Students");
        JButton clearAllButton = new JButton("Clear All");
        JButton searchButton = new JButton("Search");
        JButton deleteButton = new JButton("Delete");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(showButton);
        buttonPanel.add(clearAllButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);

        // Delete student safely using Iterator to avoid ConcurrentModificationException
        deleteButton.addActionListener(e -> {
            String searchName = searchField.getText();

            if (searchName.isBlank()) {
                JOptionPane.showMessageDialog(frame, "Please enter name to delete");
                return;
            }

            boolean deleted = studentService.deleteStudentByName(searchName);

            if (deleted) {
                JOptionPane.showMessageDialog(frame, "deleted successfully");
                searchField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Student not found");
            }
        });

        // Search student by partial name match and show all matching results
        searchButton.addActionListener(e -> {

            String searchName = searchField.getText();
            if (searchName.isBlank()) {
                JOptionPane.showMessageDialog(frame, "Please enter name to search");
                return;
            }

            ArrayList<Student> students = studentService.searchStudentsFromDB(searchName);

            if(students.isEmpty()){
                JOptionPane.showMessageDialog(frame,"Student not found");
            }else{
                StringBuilder result = new StringBuilder();

                for(Student s : students){
                    result.append("Name: ").append(s.getName())
                            .append(", Age: ").append(s.getAge())
                            .append(", Course: ").append(s.getCourse())
                            .append("\n");
                }
                JOptionPane.showMessageDialog(frame,result.toString());
            }

        });

        // Remove all students from the list
        clearAllButton.addActionListener(e -> {

            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure want to delete all students? ",
                    "confirm",JOptionPane.YES_NO_OPTION);

            if(confirm != JOptionPane.YES_NO_OPTION){
                return;
            }
            boolean cleared = studentService.clearAllStudentsFromDB();

            if(cleared){
                JOptionPane.showMessageDialog(frame,"All students cleared successfully");
                frame.dispose();
                new StudentDashboard(studentService.getAllStudentsFromDB(),-1);
            }else{
                JOptionPane.showMessageDialog(frame,"Could not clear students");
            }
        });

        // Open dashboard only when at least one student exists
        showButton.addActionListener(e -> {

            ArrayList<Student> students = studentService.getAllStudentsFromDB();

            if(students.isEmpty()){
                JOptionPane.showMessageDialog(frame,"No students added yet");
                return;
            }

            new StudentDashboard(students,-1);

        });

        // Handles both Add Student and Update Student operations
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String ageText = ageField.getText();
            String course = (String) courseBox.getSelectedItem();

            // Basic empty field validation
            if (name.isBlank() || ageText.isBlank()) {
                JOptionPane.showMessageDialog(frame,
                        "kindly fill all fields");
                return;
            }

            int age;

            // Validate numeric age input
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException exc) {
                JOptionPane.showMessageDialog(frame,
                        "Kindly enter valid age");
                return;
            }

            // Validate age range
            if (age < 1 || age > 120) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid age range");
                return;
            }

            // Update existing student when edit mode is active
            if (editMode) {
                Student updatedStudent = new Student(name,age,course);
                boolean updated = studentService.updateStudentbyId(editStudentId,updatedStudent);
                if(updated) {
                    editMode = false;
                    editIndex = -1;
                    editStudentId = -1;

                    JOptionPane.showMessageDialog(frame, "Student updated successfully");

                    frame.dispose();
                    new StudentDashboard(studentService.getAllStudentsFromDB(), -1);

                }else{
                    JOptionPane.showMessageDialog(frame,"Student could not be updated");
                }

                }else{
                    Student student = new Student(name,age,course);
                    boolean added = studentService.addStudentToDB(student);

                    if(!added){
                        JOptionPane.showMessageDialog(frame,"Student could not be added");
                        return;
                    }

                    nameField.setText("");
                    ageField.setText("");
                    courseBox.setSelectedIndex(0);
                    nameField.requestFocus();

                    JOptionPane.showMessageDialog(frame,"Student added successfully");
                }
        });

        // Arrange form rows vertically
        frame.setLayout(new GridLayout(5, 1));

        frame.add(namePanel);
        frame.add(agePanel);
        frame.add(coursePanel);
        frame.add(searchPanel);
        frame.add(buttonPanel);

        frame.setVisible(true);
    }
}