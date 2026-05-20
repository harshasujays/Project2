# KTU SGPA/CGPA Manager - Complete Technical Documentation

## Project Overview

KTU SGPA/CGPA Manager is a Java-based console application designed for managing and calculating semester-wise academic performance based on the KTU grading system.

The system supports dynamic semester scheme creation, subject-wise mark entry, internal and written examination evaluation, automatic grade assignment, SGPA/CGPA calculation, earned credit tracking, and complete student academic record management.

---

# Core Modules

## 1. Semester Scheme Management System

### Features

* Create custom semester schemes
* Add multiple subjects per semester
* Configure subject credits
* Configure written examination maximum marks
* Configure internal assessment marks
* Delete unused semester schemes
* Prevent deletion of schemes already linked to student records

### Purpose

Provides flexible semester configuration for different academic structures and regulations.

---

## 2. Student Management System

### Features

* Add new student records
* Store student name and roll number
* Maintain multiple semester records
* View complete academic history
* Delete semester records
* Search students using roll number

### Data Management

Each student record maintains:

* Personal details
* Semester-wise academic performance
* Credit statistics
* Overall CGPA information

---

## 3. Subject Result Processing System

### Features

* Subject-wise mark entry
* Written + internal mark handling
* Automatic total score calculation
* Percentage calculation
* Pass/fail validation
* Grade assignment
* Grade-point mapping

### Pass Criteria Logic

Subjects with internals require:

* Minimum 40% in written examination
* Minimum 35% in internal assessment

Subjects without internals require:

* Minimum 50% overall marks

---

## 4. SGPA Calculation System

### Features

* Semester-wise SGPA computation
* Credit-weighted grade-point calculation
* Earned credit tracking
* Failed subject exclusion from SGPA

### Calculation Logic

SGPA = Σ(Credit × Grade Point) ÷ Σ(Credits)

Only passed subjects are included in SGPA calculation.

---

## 5. CGPA Calculation System

### Features

* Multi-semester CGPA computation
* Overall academic performance analysis
* Total credit tracking
* Earned credit calculation
* Semester-wise SGPA summary

### Calculation Logic

CGPA = Σ(All Credit × Grade Point) ÷ Σ(All Earned Credits)

Failed subjects are excluded from CGPA calculation.

---

## 6. Grade Management System

### Features

* Automatic grade assignment
* Grade-point conversion
* Percentage-based evaluation
* Pass/fail determination
* Support for KTU grading structure

### Supported Grades

* S
* A+
* A
* B+
* B
* C+
* C
* D
* P
* F

---

## 7. Quick Grade Entry System

### Features

* Direct grade-based SGPA calculation
* Faster semester entry process
* Automatic mark estimation from grade points
* Instant SGPA generation

### Purpose

Allows SGPA calculation using grades without entering exact marks.

---

## 8. Input Validation & Exception Handling System

### Features

* Integer validation
* Range validation
* Yes/No input validation
* Invalid menu handling
* Exception handling using try-catch
* Prevention of invalid academic data entry

---

# Technologies Used

## Language

* Java (Core Java)

## Concepts Applied

* Object-Oriented Programming (OOP)
* Encapsulation
* Modular Programming
* Collections Framework (ArrayList)
* Exception Handling
* Dynamic Data Structures
* Console-based User Interaction
* Scanner-based Input Handling

---

# Key Technical Highlights

* Fully modular class-based design
* Dynamic semester scheme creation
* Credit-weighted SGPA/CGPA calculation
* Automatic grade and percentage evaluation
* Internal assessment handling system
* Multi-semester academic tracking
* Real-time academic computation
* Robust input validation system
* Menu-driven architecture

---

# Project Workflow

Create Semester Scheme
↓
Add Subjects & Credits
↓
Add Student Details
↓
Enter Subject Marks / Grades
↓
Calculate Grade Points
↓
Generate SGPA
↓
Store Semester Record
↓
Calculate Overall CGPA
↓
View Academic Report

---

# Future Improvements

* File-based data storage system
* Database integration using MySQL
* GUI version using JavaFX
* Web-based version using Spring Boot
* Authentication and role-based access
* Export reports as PDF
* Student performance analytics dashboard
* Cloud-based academic management system

---

# Conclusion

KTU SGPA/CGPA Manager is a structured Java application focused on academic performance management through modular object-oriented design, dynamic grading logic, semester-wise record tracking, and credit-based SGPA/CGPA computation.
