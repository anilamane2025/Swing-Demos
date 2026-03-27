package org.example.studentapp;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class StudentForm{
    static ArrayList<Student> studentList = new ArrayList<>();
    static boolean editMode = false;
    static int editIndex = -1;

    static String tempName;
    static int tempAge;
    static String tempCourse;

    public static void openForm(String name, int age, String course){
        /*JFrame frame = new JFrame();
        frame.setTitle("Student Form");
        frame.setSize(500,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel nameLabel = new JLabel("Name: ");
        JTextField nameField = new JTextField(15);

        JLabel ageLabel =  new JLabel("Age: ");
        JTextField ageField = new JTextField(10);

        JLabel courseLabel = new JLabel("Course: ");
        String[] courses = {"Java",".NET","Python","C++"};
        JComboBox<String> courseBox = new JComboBox<>(courses);

        nameField.setText(name);
        ageField.setText(String.valueOf(age));
        courseBox.setSelectedItem(course);

        frame.setLayout(new FlowLayout());
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(ageLabel);
        frame.add(ageField);
        frame.add(courseLabel);
        frame.add(courseBox);

        frame.setVisible(true);*/

        editMode = true;

        tempName = name;
        tempAge = age;
        tempCourse = course;

        main(null);
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setTitle("Student Form");
        frame.setSize(400,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel nameLabel  = new JLabel("Name:");
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
        String[] courses = {"Java",".NET","Python"};
        JComboBox<String>  courseBox = new JComboBox<>(courses);
        JPanel coursePanel = new JPanel();
        coursePanel.add(courseLabel);
        coursePanel.add(courseBox);

        if(editMode){
            nameField.setText(tempName);
            ageField.setText(String.valueOf(tempAge));
            courseBox.setSelectedItem(tempCourse);
        }

        JLabel searchLabel = new JLabel("Search Name:");
        JTextField searchField = new JTextField(15);
        JPanel searchPanel = new JPanel();
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        //JButton submitButton = new JButton("Submit");
        JButton addButton = new JButton("Add Student");
        if(editMode){
            addButton.setText("Update Student");
        }
        JButton showButton = new JButton("Show Students");
        JButton clearAllButton = new JButton("Clear All Students");
        JButton searchButton = new JButton("Search");
        JButton deleteButton = new JButton("Delete");

        JPanel buttonPanel = new JPanel();
        //buttonPanel.add(submitButton);
        buttonPanel.add(addButton);
        buttonPanel.add(showButton);
        buttonPanel.add(clearAllButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);


        deleteButton.addActionListener(e->{
            String searchName = searchField.getText();

            if(searchName.isBlank()){
                JOptionPane.showMessageDialog(frame,"Please enter name to delete");
                return;
            }

            boolean deleted  = false;

            //chances of facing ConcurrentModificationException
            /*for(Student s : studentList){
                if(s.getName().equalsIgnoreCase(searchName)){
                    studentList.remove(s);
                    deleted = true;
                    break;
                }
            }*/

            //Safe Version using iterator
            Iterator<Student> iterator = studentList.iterator();
            while(iterator.hasNext()){
                Student s = iterator.next();

                if(s.getName().equalsIgnoreCase(searchName)){
                    iterator.remove();
                    deleted = true;
                    break;
                }

            }

            if(deleted){
                JOptionPane.showMessageDialog(frame,"deleted successfully");
                searchField.setText("");
            }else{
                JOptionPane.showMessageDialog(frame,"Student not found");
            }
        });

        searchButton.addActionListener(e->{

            String searchName = searchField.getText();
            if(searchName.isBlank()){
                JOptionPane.showMessageDialog(frame,"Please enter name to search");
                return;
            }

            //single matching name search
            /*boolean found = false;
            for(Student s : studentList){

                if(s.getName().toLowerCase().contains(searchName.toLowerCase())){
                    JOptionPane.showMessageDialog(frame,"Name: "+s.getName()
                    +"\nAge: "+s.getAge()
                    +"\nCourse: "+s.getCourse());

                    found = true;
                    break;
                }
            }
            if(!found){
                JOptionPane.showMessageDialog(frame,"Student not found");
            }*/

            //multiple matching name search
            StringBuilder result = new StringBuilder();

            for(Student s : studentList){
                if(s.getName().toLowerCase().contains(searchName.toLowerCase())){
                    result.append("Name: ").append(s.getName())
                            .append(", Age ").append(s.getAge())
                            .append(", Course ").append(s.getCourse())
                            .append("\n");
                }
            }

            if(result.length() == 0){
                JOptionPane.showMessageDialog(frame,"Student not found");
            }else{
                JOptionPane.showMessageDialog(frame,result.toString());
            }
        });

        clearAllButton.addActionListener(e->{
            studentList.clear();
            JOptionPane.showMessageDialog(frame,"All students cleared");
        });

        showButton.addActionListener(e->{

            if(studentList.isEmpty()){
                JOptionPane.showMessageDialog(frame,"No students added yet");
                return;
            }

            new StudentDashboard(studentList);
        });

        addButton.addActionListener(e->{
            String name = nameField.getText();
            String ageText = ageField.getText();
            String course = (String)courseBox.getSelectedItem();

            if(name.isBlank() || ageText.isBlank()){
                JOptionPane.showMessageDialog(frame,
                        "kindly fill all fields");
                return;
            }
            int age;
            try{
                age = Integer.parseInt(ageText);
            }catch (NumberFormatException exc){
                JOptionPane.showMessageDialog(frame,
                        "Kindly enter valid age");
                return;
            }
            if(age < 1 || age > 120){
                JOptionPane.showMessageDialog(frame,
                        "Invalid age range");
                return;
            }
            if(editMode){
                studentList.set(editIndex, new Student(name,age,course));
                editMode = false;
                editIndex = -1;
                JOptionPane.showMessageDialog(frame,"Student updated successfully");
                nameField.setText("");
                ageField.setText("");
                courseBox.setSelectedItem(0);
                nameField.requestFocus();
            }
            else{
                Student student = new Student(name,age,course);

                boolean alreadyExist = false;

                for(Student s:studentList){
                    if(s.getName().equalsIgnoreCase(name)
                    && s.getAge() == age
                    && s.getCourse().equalsIgnoreCase(course)){
                        alreadyExist = true;
                        break;
                    }
                }
                if(alreadyExist){
                    JOptionPane.showMessageDialog(frame,"Student already exist");
                    return;
                }
                studentList.add(student);
                System.out.println(studentList.size());
                /*JOptionPane.showMessageDialog(frame,
                        "Name: "+name+"\nAge: "+age+"\nCourse: "+course);*/

                /*frame.dispose();
                new StudentDashboard(studentList);*/
                nameField.setText("");
                ageField.setText("");
                courseBox.setSelectedIndex(0);
                nameField.requestFocus();

                JOptionPane.showMessageDialog(frame,"Student added successfully");
            }
        });



        frame.setLayout(new GridLayout(5,1));

        frame.add(namePanel);
        frame.add(agePanel);
        frame.add(coursePanel);
        frame.add(searchPanel);
        frame.add(buttonPanel);

        frame.setVisible(true);
    }

}