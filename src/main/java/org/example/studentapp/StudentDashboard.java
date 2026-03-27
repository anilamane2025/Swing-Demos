package org.example.studentapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class StudentDashboard extends JFrame {

    /*StudentDashboard(String name, int age, String course){

        setTitle("Student Dashboard");
        setSize(400,250);
        setLayout(new FlowLayout(FlowLayout.CENTER,20,40));

        JLabel label = new JLabel("<html>Name: "+name+"<br>Age: "+age+"<br>Course: "+course+"</html>");
        label.setFont(new Font("Arial",Font.BOLD,18));

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e->{
            System.exit(0);
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e->{
            dispose();
            StudentForm.main(null);
        });

        add(label);
        add(exitButton);
        add(backButton);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }*/
    StudentDashboard(ArrayList<Student> studentList){

        setTitle("Student Dashboard");
        setSize(700,400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        /*StringBuilder data = new StringBuilder();

        for(Student s:studentList){
            data.append("Name: ").append(s.getName())
                    .append(", Age ").append(s.getAge())
                    .append(", Course ").append(s.getCourse())
                    .append("\n");
        }*/

        /*JLabel label = new JLabel("<html>"+data.toString().replace("\n","<br>")+"</html>");

        add(label);

        label.setFont(new Font("Arial",Font.BOLD,18));*/

        String[] columnNames = {"Name","Age","Course"};

        /*String[][] data = {
                {"Ram","20","Java"},
                {"Shyam","30","Python"},
                {"Prem","40","Go"}
        };*/

        //static data loading
        /*String[][] data = new String[studentList.size()][3];

        for(int i=0; i < studentList.size(); i++){
                Student s = studentList.get(i);

                data[i][0] = s.getName();
                data[i][1] = String.valueOf(s.getAge());
                data[i][2]  = s.getCourse();
        }*/


        //JTable table = new JTable(data,columnNames);
        DefaultTableModel model = new DefaultTableModel(columnNames,0);
        JTable table =  new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        //Dynamic data loading

        for(Student s : studentList){
            model.addRow(new Object[]{
                    s.getName(),
                    s.getAge(),
                    s.getCourse()
            });
        }


        JButton exitButton = new JButton("Exit");
        JButton backButton = new JButton("Back");
        JButton rowButton = new JButton("Show selected row");
        JButton deleteButton = new JButton("Delete selected row");
        JButton editButton =  new JButton("Edit selected row");

        editButton.addActionListener(e->{
            int selectedRow = table.getSelectedRow();

            if(selectedRow == -1){
                JOptionPane.showMessageDialog(this,"please select a row to edit");
                return;
            }

            String name = model.getValueAt(selectedRow,0).toString();
            String age = model.getValueAt(selectedRow,1).toString();
            String course = model.getValueAt(selectedRow,2).toString();

            StudentForm.editMode = true;
            StudentForm.editIndex = selectedRow;

            /*JOptionPane.showMessageDialog(this,
                    "Editing:\n "+name+
                    ","+age+
                    ","+course);*/
            dispose();
            //this is for opening empty form without data
            //StudentForm.main(null);

            //this is for opening form with data

            StudentForm.openForm(name,Integer.parseInt(age),course);
        });
        deleteButton.addActionListener(e->{

            int selectedRow = table.getSelectedRow();

            if(selectedRow == -1){
                JOptionPane.showMessageDialog(this,"please select a row to delete");
                return;
            }
            studentList.remove(selectedRow);

            //this will not reload the window instead it directly remove the row from the table and makes the window stable
            model.removeRow(selectedRow);
            table.clearSelection();
            JOptionPane.showMessageDialog(this,"Student deleted successfully");
            // it will reload the window again
            /*dispose();
            new StudentDashboard(studentList);*/

        });

        rowButton.addActionListener(e->{

            int selectedRow = table.getSelectedRow();

            if(selectedRow == -1){
                JOptionPane.showMessageDialog(this,"Please select a row");
                return;
            }

            String name = table.getValueAt(selectedRow,0).toString();
            String age = table.getValueAt(selectedRow,1).toString();
            String course = table.getValueAt(selectedRow,2).toString();

            JOptionPane.showMessageDialog(this,
                    "Name: "+name+
                    "\nAge: "+age+
                    "\nCourse: "+course);

        });

        exitButton.addActionListener(e->{
            System.exit(0);
        });

        backButton.addActionListener(e->{
            dispose();
            StudentForm.main(null);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        buttonPanel.add(backButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(rowButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
