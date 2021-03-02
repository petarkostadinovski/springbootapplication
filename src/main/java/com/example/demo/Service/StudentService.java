package com.example.demo.Service;

import com.example.demo.Model.Student;
import com.example.demo.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentByEmailOptional = studentRepository.findStudentByEmail(student.getEmail());

        if (studentByEmailOptional.isPresent()){
            throw new IllegalStateException("email exists");
        }

        studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        boolean exists = studentRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("student with id: " + id + " does not exist.");
        }
        studentRepository.deleteById(id);
    }

    @Transactional
    public void updateStudent(long id, String name, String email) {

        Student student = studentRepository.findById(id).orElseThrow(() -> new IllegalStateException("student with id: " + id +" does not exist."));

        if (name != null && name.length() > 0 && !student.getName().equals(name)) {
            student.setName(name);
        }

        if (email != null && email.length() > 0 && !student.getEmail().equals(email)){
            Optional<Student> studentByEmail = studentRepository.findStudentByEmail(email);
            if (studentByEmail.isPresent())
                throw new IllegalStateException("email already exists");
            student.setEmail(email);
        }

    }
}
