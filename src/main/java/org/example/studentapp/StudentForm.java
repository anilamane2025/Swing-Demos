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

            /*boolean deleted = false;

            Iterator<Student> iterator = studentList.iterator();
            while (iterator.hasNext()) {
                Student s = iterator.next();

                if (s.getName().equalsIgnoreCase(searchName)) {
                    iterator.remove();
                    deleted = true;
                    break;
                }
            }*/

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

            /*StringBuilder result = new StringBuilder();

            for (Student s : studentList) {
                if (s.getName().toLowerCase().contains(searchName.toLowerCase())) {
                    result.append("Name: ").append(s.getName())
                            .append(", Age ").append(s.getAge())
                            .append(", Course ").append(s.getCourse())
                            .append("\n");
                }
            }*/

            String result  = studentService.searchStudentsByName(searchName);

            if (result.isBlank()) {
                JOptionPane.showMessageDialog(frame, "Student not found");
            } else {
                JOptionPane.showMessageDialog(frame, result);
            }
        });

        // Remove all students from the list
        clearAllButton.addActionListener(e -> {

            if(studentService.getAllStudents().isEmpty()){
                JOptionPane.showMessageDialog(frame,"No students to clear");
                return;
            }

            //studentList.clear();
            studentService.clearAllStudents();
            JOptionPane.showMessageDialog(frame, "All students cleared");
        });

        // Open dashboard only when at least one student exists
        showButton.addActionListener(e -> {

            /*if (studentService.getAllStudents().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No students added yet");
                return;
            }

            //new StudentDashboard(studentList, -1);
            new StudentDashboard(studentService.getAllStudents(), -1);*/

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
                studentService.updateStudent(editIndex,updatedStudent);
                //studentList.set(editIndex, new Student(name, age, course));
                editMode = false;
                editIndex = -1;

                JOptionPane.showMessageDialog(frame, "Student updated successfully");

                frame.dispose();
                //new StudentDashboard(studentList, -1);
                new StudentDashboard(studentService.getAllStudents(), -1);
            } else {
                /*Student student = new Student(name, age, course);

                // Prevent duplicate student entries with same name, age and course
                boolean alreadyExist = false;

                for (Student s : studentList) {
                    if (s.getName().equalsIgnoreCase(name)
                            && s.getAge() == age
                            && s.getCourse().equalsIgnoreCase(course)) {
                        alreadyExist = true;
                        break;
                    }
                }

                if (alreadyExist) {
                    JOptionPane.showMessageDialog(frame, "Student already exist");
                    return;
                }

                // Add new student and reset form fields
                studentList.add(student);*/

                Student student = new Student(name,age,course);

                //boolean added = studentService.addStudent(student);
                boolean added = studentService.addStudentToDB(student);

                if(!added){
                    JOptionPane.showMessageDialog(frame,"Student already exist");
                    return;
                }

                nameField.setText("");
                ageField.setText("");
                courseBox.setSelectedIndex(0);
                nameField.requestFocus();

                JOptionPane.showMessageDialog(frame, "Student added successfully");
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