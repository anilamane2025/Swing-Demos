package org.example.studentapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class StudentDashboard extends JFrame {

    StudentDashboard(ArrayList<Student> studentList, int highlightIndex) {

        setTitle("Student Dashboard");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Column names for JTable
        String[] columnNames = {"Name", "Age", "Course"};

        // Non-editable table model so user cannot directly change cell values in JTable
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);

        // Double click on a row opens the form in edit mode with selected student data
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                if (evt.getClickCount() == 2) {

                    int selectedRow = table.getSelectedRow();

                    if (selectedRow != -1) {
                        Student selectedStudent = studentList.get(selectedRow);

                        StudentForm.editMode = true;
                        StudentForm.editIndex = selectedRow;
                        StudentForm.editStudentId = selectedStudent.getId();

                        dispose();

                        StudentForm.openForm(
                                selectedStudent.getName(),
                                selectedStudent.getAge(),
                                selectedStudent.getCourse()
                        );
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        // Load all student records from list into the table model
        for (Student s : studentList) {
            model.addRow(new Object[]{
                    s.getName(),
                    s.getAge(),
                    s.getCourse()
            });
        }

        JButton exitButton = new JButton("Exit");
        JButton backButton = new JButton("Back");
        JButton viewButton = new JButton("View");
        JButton deleteButton = new JButton("Delete");

        // Delete the selected row from both studentList and table model
        deleteButton.addActionListener(e -> {

            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete");
                return;
            }

            Student selectedStudent  = studentList.get(selectedRow);
            int studentId = selectedStudent.getId();

            System.out.println("Selected Row = "+selectedRow);
            System.out.println("Student Id = "+studentId);

            boolean deleted = StudentForm.studentService.deleteStudentById(studentId);

            if(deleted){
                studentList.remove(selectedRow);
                model.removeRow(selectedRow);
                table.clearSelection();

                JOptionPane.showMessageDialog(this, "Student deleted successfully");
            }else{
                JOptionPane.showMessageDialog(this, "Student could not be deleted");
            }
        });

        // Show details of the currently selected row
        viewButton.addActionListener(e -> {

            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row");
                return;
            }

            String name = table.getValueAt(selectedRow, 0).toString();
            String age = table.getValueAt(selectedRow, 1).toString();
            String course = table.getValueAt(selectedRow, 2).toString();

            JOptionPane.showMessageDialog(this,
                    "Name: " + name +
                            "\nAge: " + age +
                            "\nCourse: " + course);
        });

        // Exit the entire application
        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        // Go back to StudentForm
        backButton.addActionListener(e -> {
            dispose();
            StudentForm.main(null);
        });

        // Bottom button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(backButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);

        add(scrollPane, BorderLayout.CENTER);

        // Highlight the updated row when dashboard is reopened after edit/update
        if (highlightIndex != -1) {
            table.setRowSelectionInterval(highlightIndex, highlightIndex);
            table.scrollRectToVisible(table.getCellRect(highlightIndex, 0, true));
        }

        add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}