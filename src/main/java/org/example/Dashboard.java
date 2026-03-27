package org.example;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

     Dashboard(String name, int age){

         setTitle("Dashboard");
         setSize(400,200);
         setLayout(new FlowLayout(FlowLayout.CENTER,20,60));

         JLabel label = new JLabel("<html>Welcome "+name+" <br>Age: "+age+"</html>");
         label.setFont(new Font("Arial",Font.BOLD,20));

         JButton exitButton = new JButton("Exit");
         JButton backButton = new JButton("Back");

         exitButton.addActionListener(e->{
             System.exit(0);
         });

         backButton.addActionListener(e->{
             dispose();
             Main.openForm();
         });

         add(label);
         add(exitButton);
         add(backButton);

         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setVisible(true);
     }

}
