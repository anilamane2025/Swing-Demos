package org.example.studentapp;

import com.sun.source.tree.BreakTree;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public class StudentService {

    static StudentService studentService = new StudentService();

    private ArrayList<Student> studentList = new ArrayList<>();

    public ArrayList<Student> getAllStudents(){
        return studentList;
    }

    public boolean addStudent(Student student){

        for(Student s : studentList){
            if(s.getName().equalsIgnoreCase(student.getName())
                    && s.getAge() == student.getAge()
                    && s.getCourse() == student.getCourse()){
                return false;
            }
        }
        studentList.add(student);
        return true;
    }

    public void updateStudent(int index,Student student){
        studentList.set(index,student);
    }

    public boolean deleteStudentByName(String name){

        Iterator<Student> iterator = studentList.iterator();

        while(iterator.hasNext()){

            Student s = iterator.next();

            if(s.getName().equalsIgnoreCase(name)){
                iterator.remove();
                return true;
            }

        }
        return false;
    }

    public String searchStudentsByName(String searchName){

        StringBuilder result = new StringBuilder();

        for(Student s : studentList){
            if(s.getName().toLowerCase().contains(searchName.toLowerCase())){
                result.append("Name: ").append(s.getName())
                        .append(", Age: ").append(s.getAge())
                        .append(", Course ").append(s.getCourse())
                        .append("\n");
            }
        }
        return result.toString();
    }

    public void clearAllStudents(){
        studentList.clear();
    }

    public void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS students("+
                "id SERIAL PRIMARY KEY,"+
                "name VARCHAR(100),"+
                "age INT,"+
                "course VARCHAR(100)" +
                ")";
        try(Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement()){
            statement.execute(sql);
            System.out.println("students table created successfully");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean addStudentToDB(Student student){
        String sql = "INSERT INTO students (name,age,course) VALUES(?,?,?)";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1,student.getName());
            ps.setInt(2,student.getAge());
            ps.setString(3,student.getCourse());

            ps.executeUpdate();
            return true;

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Student> getAllStudentsFromDB(){

        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT name,age,course FROM Students";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement  ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while (rs.next()){
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String course = rs.getString("course");
                students.add(new Student(name,age,course));
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return students;
    }

}
