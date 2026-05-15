# Academic Manager - Complete Technical Documentation

## Project Overview

Academic Manager is a Java-based console application designed for managing and analyzing student academic performance.

The system supports student record management, subject-wise grading, credit-based weighted scoring, dynamic grading schemes, and detailed academic analytics such as class performance, subject statistics, and grade distribution.

---

# Core Modules

## 1. Student Management System

### Features
- Add new student records
- Store roll number and personal details
- View individual student reports
- Remove student records
- Search students by name or roll number

---

## 2. Subject Management System

### Features
- Multiple subjects per student
- Subject-wise marks entry
- Credit-based evaluation system
- Subject-level performance tracking

### Calculation Logic
Weighted Score = Marks × Credits  
Weighted Average = Total Weighted Score ÷ Total Credits

---

## 3. Grading System

### Features
- Multiple predefined grading schemes
- School Standard Scheme
- University Standard Scheme
- CBSE/Board Style Scheme
- Custom grading scheme creation

### Logic
Grades are assigned based on weighted average thresholds defined in the grading scheme.

---

## 4. Analytics & Reporting System

### Features
- Class-wide performance analysis
- Top and lowest performing students
- Pass/fail statistics
- Grade distribution report
- Subject-wise statistical analysis

---

## 5. Sorting & Search System

### Features
- Sort students by weighted average (ascending/descending)
- Sort students by name (A-Z / Z-A)
- Search students using roll number
- Search students using partial name match

---

# Technologies Used

## Language
- Java (Core Java)

## Concepts Applied
- Object-Oriented Programming (OOP)
- Collections Framework (ArrayList, Map)
- Exception Handling
- Comparator-based Sorting
- Modular Programming
- Input Handling using Scanner

---

# Key Technical Highlights

- Fully modular class-based design
- Dynamic grading system implementation
- Credit-weighted academic scoring system
- Real-time statistical computation
- Multi-level data aggregation (student → subject → class)
- Robust input validation system
- Menu-driven architecture

---

# Project Workflow

Add Student
↓
Enter Subjects & Marks
↓
Assign Grading Scheme
↓
Calculate Weighted Score
↓
Generate Student Report
↓
View Class Statistics
↓
Perform Sorting / Search Operations

---

# Future Improvements

- File-based data storage system
- GUI version using JavaFX
- Web version using Spring Boot
- Database integration (MySQL)
- REST API-based architecture
- Role-based access system (Admin/Student)

---

# Conclusion

Academic Manager is a structured Java application focused on mastering object-oriented programming, data processing, and real-world academic management logic through a modular and scalable design approach.
