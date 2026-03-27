package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        openForm();
    }


    public static void openForm(){
        JFrame frame = new JFrame();
        //frame.setLayout(new FlowLayout());
        frame.setLayout(null);

        JLabel nameLabel = new JLabel("Enter name:");
        nameLabel.setBounds(30,50,100,30);

        JTextField nameTextField = new JTextField(15);
        nameTextField.setBounds(120,50,150,30);

        JLabel ageLabel = new JLabel("Enter age:");
        ageLabel.setBounds(30,100,100,30);

        JTextField ageTextField = new JTextField(15);
        ageTextField.setBounds(120,100,150,30);

        JButton button = new JButton("Submit");
        button.setBounds(120,150,100,30);

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(230,150,100,30);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                String ageText = ageTextField.getText();
                if(name.isBlank() || ageText.isBlank()){
                    JOptionPane.showMessageDialog(frame,"Please enter all fields..");
                    return;
                }
                int age;
                try{
                    age = Integer.parseInt(ageText);
                }catch (NumberFormatException exc){
                    JOptionPane.showMessageDialog(frame,"Please enter valid age..");
                    return;
                }
                if(age<1 || age>120){
                    JOptionPane.showMessageDialog(frame,"Invalid age range");
                    return;
                }
                if(name.equalsIgnoreCase("admin")){
                    JOptionPane.showMessageDialog(frame,"Welcome Admin, Age "+age);
                    return;
                }
                //JOptionPane.showMessageDialog(frame,"Name "+name+",Age "+age);
                /*frame.dispose();
                JFrame newFrame = new JFrame();

                newFrame.setSize(300,200);
                newFrame.setLayout(new FlowLayout());

                JLabel msg = new JLabel("Name "+name+",Age "+age);
                newFrame.add(msg);

                newFrame.setVisible(true);*/
                frame.dispose();
                new Dashboard(name,age);
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameTextField.setText("");
                ageTextField.setText("");
                nameTextField.requestFocus();
                JOptionPane.showMessageDialog(frame,"Form cleared");
            }
        });

        frame.add(nameLabel);
        frame.add(ageLabel);
        frame.add(nameTextField);
        frame.add(ageTextField);
        frame.add(button);
        frame.add(clearButton);
        frame.setSize(400,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}