import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

class SubjectTemplate {
    String name;
    int credit;
    int writtenMax;
    int internalMax;

    SubjectTemplate(String name, int credit, int writtenMax, int internalMax) {
        this.name = name;
        this.credit = credit;
        this.writtenMax = writtenMax;
        this.internalMax = internalMax;
    }
}

class SubjectResult {
    String name;
    int credit;
    int writtenObtained;
    int writtenMax;
    int internalObtained;
    int internalMax;

    SubjectResult(String name, int credit, int writtenObtained, int writtenMax,
                  int internalObtained, int internalMax) {
        this.name = name;
        this.credit = credit;
        this.writtenObtained = writtenObtained;
        this.writtenMax = writtenMax;
        this.internalObtained = internalObtained;
        this.internalMax = internalMax;
    }

    int getTotalObtained() {
        return writtenObtained + internalObtained;
    }

    int getTotalMax() {
        return writtenMax + internalMax;
    }

    boolean hasPassed() {
        // If subject has internals, need both thresholds (40% of written, 35% of internal)
        if (internalMax > 0) {
            double writtenPercent = writtenMax > 0 ? (100.0 * writtenObtained) / writtenMax : 0;
            double internalPercent = internalMax > 0 ? (100.0 * internalObtained) / internalMax : 0;
            if (writtenPercent < 40) {
                return false;
            }
            if (internalPercent < 35) {
                return false;
            }
            return true;
        }
        // If no internals, use percentage threshold (50% = P grade)
        double percent = getPercentage();
        return percent >= 50;
    }

    double getPercentage() {
        return getTotalMax() > 0 ? (100.0 * getTotalObtained()) / getTotalMax() : 0;
    }

    double getGradePoint() {
        if (!hasPassed()) {
            return 0.0;
        }
        double percent = getPercentage();
        if (percent >= 90) return 10.0;
        if (percent >= 85) return 9.0;
        if (percent >= 80) return 8.5;
        if (percent >= 75) return 8.0;
        if (percent >= 70) return 7.5;
        if (percent >= 65) return 7.0;
        if (percent >= 60) return 6.5;
        if (percent >= 55) return 6.0;
        if (percent >= 50) return 5.5;
        return 0.0;
    }

    String getGradeLabel() {
        if (!hasPassed()) {
            return "F";
        }
        double percent = getPercentage();
        if (percent >= 90) return "S";
        if (percent >= 85) return "A+";
        if (percent >= 80) return "A";
        if (percent >= 75) return "B+";
        if (percent >= 70) return "B";
        if (percent >= 65) return "C+";
        if (percent >= 60) return "C";
        if (percent >= 55) return "D";
        if (percent >= 50) return "P";
        return "F";
    }

    @Override
    public String toString() {
        return String.format("%s (%dcr): written %d/%d, internal %d/%d, total %d/%d, %%=%.2f, GP=%.1f, grade=%s",
                name, credit,
                writtenObtained, writtenMax,
                internalObtained, internalMax,
                getTotalObtained(), getTotalMax(),
                getPercentage(), getGradePoint(), getGradeLabel());
    }
}



class SemesterScheme {
    String name;
    List<SubjectTemplate> subjects;

    SemesterScheme(String name, List<SubjectTemplate> subjects) {
        this.name = name;
        this.subjects = subjects;
    }
}

class SemesterRecord {
    SemesterScheme scheme;
    List<SubjectResult> results;

    SemesterRecord(SemesterScheme scheme, List<SubjectResult> results) {
        this.scheme = scheme;
        this.results = results;
    }

    double calculateSGPA() {
        double totalCredits = 0;
        double weightedPoints = 0;
        for (SubjectResult result : results) {
            // Only include subjects with credit and that have passed
            if (result.credit > 0 && result.hasPassed()) {
                totalCredits += result.credit;
                weightedPoints += result.credit * result.getGradePoint();
            }
        }
        return totalCredits > 0 ? weightedPoints / totalCredits : 0;
    }

    int getTotalSemesterCredits() {
        int total = 0;
        for (SubjectResult result : results) {
            total += result.credit;
        }
        return total;
    }

    int getEarnedCredits() {
        int earned = 0;
        for (SubjectResult result : results) {
            if (result.credit > 0 && result.hasPassed()) {
                earned += result.credit;
            }
        }
        return earned;
    }
}

class Student {
    String name;
    int rollNo;
    List<SemesterRecord> semesters = new ArrayList<>();

    Student(String name, int rollNo) {
        this.name = name;
        this.rollNo = rollNo;
    }

    void addSemesterRecord(SemesterRecord record) {
        semesters.add(record);
    }

    SemesterRecord findSemester(String semesterName) {
        for (SemesterRecord record : semesters) {
            if (record.scheme.name.equalsIgnoreCase(semesterName)) {
                return record;
            }
        }
        return null;
    }

    double calculateCGPA() {
        double totalCredits = 0;
        double totalWeightedPoints = 0;
        for (SemesterRecord record : semesters) {
            for (SubjectResult result : record.results) {
                // Only include subjects with credit and that have passed
                if (result.credit > 0 && result.hasPassed()) {
                    totalCredits += result.credit;
                    totalWeightedPoints += result.credit * result.getGradePoint();
                }
            }
        }
        return totalCredits > 0 ? totalWeightedPoints / totalCredits : 0;
    }

    int getTotalCredits() {
        int total = 0;
        for (SemesterRecord record : semesters) {
            total += record.getTotalSemesterCredits();
        }
        return total;
    }

    int getTotalEarnedCredits() {
        int total = 0;
        for (SemesterRecord record : semesters) {
            total += record.getEarnedCredits();
        }
        return total;
    }
}

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<SemesterScheme> schemes = new ArrayList<>();
    private static final List<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        printBanner("KTU SEMESTER SGPA/CGPA MANAGER");
        while (true) {
            printMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1 -> createNewScheme();
                case 2 -> deleteScheme();
                case 3 -> addStudentDetail();
                case 4 -> addStudentGrades();
                case 5 -> viewStudentRecord();
                case 6 -> deleteStudentRecord();
                case 7 -> findSGPA();
                case 8 -> findCGPA();
                case 0 -> {
                    println("Exiting. Run again to manage another student.");
                    scanner.close();
                    return;
                }
                default -> println("Invalid option. Choose 0-8.");
            }
        }
    }

    private static void printBanner(String title) {
        println("============================================");
        println("        " + title);
        println("============================================");
    }

    private static void printMenu() {
        println("\nMAIN MENU");
        println("1. Create new semester scheme");
        println("2. Delete existing semester scheme");
        println("3. Add student detail (with marks)");
        println("4. Add student grades (quick SGPA calculation)");
        println("5. View student record");
        println("6. Delete a student's semester record");
        println("7. Find SGPA");
        println("8. Find CGPA");
        println("0. Exit");
    }

    private static void createNewScheme() {
        println("\n--- Create New Semester Scheme ---");
        String semesterName = readString("Scheme name / semester name: ");
        if (findSchemeByName(semesterName) != null) {
            println("A scheme with this name already exists.");
            return;
        }
        int subjectCount = readIntRange("Number of subjects in this semester: ", 1, 25);
        List<SubjectTemplate> subjects = new ArrayList<>();
        for (int i = 1; i <= subjectCount; i++) {
            println("\nSubject " + i + " details:");
            String subjectName = readString("  Subject name: ");
            int credit = readIntRange("  Credit value (0-10, 0 for pass-only subjects): ", 0, 10);
            int writtenMax = readIntRange("  Written exam maximum marks (usually 100): ", 1, 200);
            int internalMax = 0;
            if (readYesNo("  Does this subject have internal marks? (y/n): ")) {
                internalMax = readIntRange("  Internal maximum marks (usually 50): ", 1, 100);
            }
            subjects.add(new SubjectTemplate(subjectName, credit, writtenMax, internalMax));
        }
        schemes.add(new SemesterScheme(semesterName, subjects));
        println("Semester scheme '" + semesterName + "' created successfully.");
    }

    private static void deleteScheme() {
        if (schemes.isEmpty()) {
            println("No semester schemes available to delete.");
            return;
        }
        println("\n--- Delete Semester Scheme ---");
        for (int i = 0; i < schemes.size(); i++) {
            println("  " + (i + 1) + ". " + schemes.get(i).name);
        }
        int choice = readIntRange("Choose a scheme to delete: ", 1, schemes.size()) - 1;
        SemesterScheme selected = schemes.get(choice);
        boolean inUse = false;
        for (Student student : students) {
            for (SemesterRecord record : student.semesters) {
                if (record.scheme.name.equalsIgnoreCase(selected.name)) {
                    inUse = true;
                    break;
                }
            }
            if (inUse) {
                break;
            }
        }
        if (inUse) {
            println("Cannot delete '" + selected.name + "' because it is already used in student records.");
            return;
        }
        schemes.remove(choice);
        println("Deleted semester scheme '" + selected.name + "'.");
    }

    private static void addStudentDetail() {
        if (schemes.isEmpty()) {
            println("No semester schemes available. Create a scheme first.");
            return;
        }
        println("\n--- Add Student Detail ---");
        String name = readString("Student name: ");
        int roll = readInt("Roll number: ");
        Student student = findStudentByRoll(roll);
        if (student == null) {
            student = new Student(name, roll);
            students.add(student);
        } else if (!student.name.equalsIgnoreCase(name)) {
            println("Warning: roll number exists with name '" + student.name + "'. Using existing student record.");
        }
        SemesterScheme scheme = chooseScheme();
        if (scheme == null) {
            return;
        }
        if (student.findSemester(scheme.name) != null) {
            println("This student already has records for " + scheme.name + ". Use a different scheme or delete the old record.");
            return;
        }
        List<SubjectResult> results = new ArrayList<>();
        for (SubjectTemplate template : scheme.subjects) {
            println("\nEnter marks for subject: " + template.name);
            int written = readIntRange("  Written obtained (0-" + template.writtenMax + "): ", 0, template.writtenMax);
            int internal = 0;
            if (template.internalMax > 0) {
                internal = readIntRange("  Internal obtained (0-" + template.internalMax + "): ", 0, template.internalMax);
            } else {
                println("  No internal marks for this subject.");
            }
            results.add(new SubjectResult(template.name, template.credit, written, template.writtenMax, internal, template.internalMax));
        }
        student.addSemesterRecord(new SemesterRecord(scheme, results));
        println("Student details added for " + scheme.name + " successfully.");
    }

    private static void addStudentGrades() {
        if (schemes.isEmpty()) {
            println("No semester schemes available. Create a scheme first.");
            return;
        }
        println("\n--- Add Student Grades (Quick SGPA) ---");
        String name = readString("Student name: ");
        int roll = readInt("Roll number: ");
        Student student = findStudentByRoll(roll);
        if (student == null) {
            student = new Student(name, roll);
            students.add(student);
        } else if (!student.name.equalsIgnoreCase(name)) {
            println("Warning: roll number exists with name '" + student.name + "'. Using existing student record.");
        }
        SemesterScheme scheme = chooseScheme();
        if (scheme == null) {
            return;
        }
        if (student.findSemester(scheme.name) != null) {
            println("This student already has records for " + scheme.name + ". Use a different scheme or delete the old record.");
            return;
        }
        List<SubjectResult> results = new ArrayList<>();
        println("Valid grades: S, A+, A, B+, B, C+, C, D, P, F");
        for (SubjectTemplate template : scheme.subjects) {
            String gradeInput = readString("\nEnter grade for " + template.name + " (" + template.credit + " credits): ");
            double gp = gradeToGradePoint(gradeInput.toUpperCase());
            // Create SubjectResult with marks derived from grade point
            int marks = gradePointToMarks(gp, template.writtenMax + template.internalMax);
            results.add(new SubjectResult(template.name, template.credit, marks, 100, 0, 0));
        }
        SemesterRecord record = new SemesterRecord(scheme, results);
        student.addSemesterRecord(record);
        double sgpa = record.calculateSGPA();
        println("\n========== SGPA CALCULATION ==========");
        println("Semester: " + scheme.name);
        println("Student: " + student.name + " (Roll " + student.rollNo + ")");
        println("Total credits: " + record.getTotalSemesterCredits());
        println("Earned credits: " + record.getEarnedCredits());
        println("SGPA: " + String.format("%.2f", sgpa));
        println("Subject details:");
        for (SubjectResult result : results) {
            println("  - " + result);
        }
        println("====================================");
    }

    private static double gradeToGradePoint(String grade) {
        return switch (grade) {
            case "S" -> 10.0;
            case "A+" -> 9.0;
            case "A" -> 8.5;
            case "B+" -> 8.0;
            case "B" -> 7.5;
            case "C+" -> 7.0;
            case "C" -> 6.5;
            case "D" -> 6.0;
            case "P" -> 5.5;
            case "F" -> 0.0;
            default -> 0.0;
        };
    }

    private static int gradePointToMarks(double gp, int totalMax) {
        if (gp == 0.0) return 0;
        if (gp == 10.0) return (int)(totalMax * 0.95);
        if (gp == 9.0) return (int)(totalMax * 0.87);
        if (gp == 8.5) return (int)(totalMax * 0.80);
        if (gp == 8.0) return (int)(totalMax * 0.75);
        if (gp == 7.5) return (int)(totalMax * 0.70);
        if (gp == 7.0) return (int)(totalMax * 0.65);
        if (gp == 6.5) return (int)(totalMax * 0.60);
        if (gp == 6.0) return (int)(totalMax * 0.55);
        if (gp == 5.5) return (int)(totalMax * 0.50);
        return 0;
    }

    private static void findSGPA() {
        if (students.isEmpty()) {
            println("No student records found.");
            return;
        }
        int roll = readInt("Enter student roll number: ");
        Student student = findStudentByRoll(roll);
        if (student == null) {
            println("Student not found.");
            return;
        }
        if (student.semesters.isEmpty()) {
            println("No semester records found for this student.");
            return;
        }
        System.out.println("Select semester to calculate SGPA:");
        for (int i = 0; i < student.semesters.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + student.semesters.get(i).scheme.name);
        }
        int choice = readIntRange("Semester number: ", 1, student.semesters.size()) - 1;
        SemesterRecord record = student.semesters.get(choice);
        double sgpa = record.calculateSGPA();
        println("\nSGPA for " + student.name + " (" + record.scheme.name + ") = " + String.format("%.2f", sgpa));
        println("Total semester credits: " + record.getTotalSemesterCredits());
        println("Earned credits: " + record.getEarnedCredits());
        println("Subject details:");
        for (SubjectResult result : record.results) {
            println("  - " + result);
        }
    }

    private static void findCGPA() {
        if (students.isEmpty()) {
            println("No student records found.");
            return;
        }
        int roll = readInt("Enter student roll number: ");
        Student student = findStudentByRoll(roll);
        if (student == null) {
            println("Student not found.");
            return;
        }
        if (student.semesters.isEmpty()) {
            println("No semester records found for this student.");
            return;
        }
        double cgpa = student.calculateCGPA();
        println("\nCGPA for " + student.name + " across " + student.semesters.size() + " semester(s) = " + String.format("%.2f", cgpa));
        println("Total semester credits: " + student.getTotalCredits());
        println("Total earned credits: " + student.getTotalEarnedCredits());
        for (SemesterRecord record : student.semesters) {
            println("  - " + record.scheme.name + ": SGPA=" + String.format("%.2f", record.calculateSGPA()) + ", credits=" + record.getTotalSemesterCredits() + ", earned=" + record.getEarnedCredits());
        }
    }

    private static void viewStudentRecord() {
        if (students.isEmpty()) {
            println("No student records found.");
            return;
        }
        int roll = readInt("Enter student roll number: ");
        Student student = findStudentByRoll(roll);
        if (student == null) {
            println("Student not found.");
            return;
        }
        println("\nStudent: " + student.name + " (Roll " + student.rollNo + ")");
        if (student.semesters.isEmpty()) {
            println("No semester records available.");
            return;
        }
        println("Total credits: " + student.getTotalCredits());
        println("Total earned credits: " + student.getTotalEarnedCredits());
        println("Overall CGPA: " + String.format("%.2f", student.calculateCGPA()));
        for (SemesterRecord record : student.semesters) {
            println("\nSemester: " + record.scheme.name);
            println("  Credits: " + record.getTotalSemesterCredits() + ", Earned: " + record.getEarnedCredits() + ", SGPA: " + String.format("%.2f", record.calculateSGPA()));
            println("  Subjects:");
            for (SubjectResult result : record.results) {
                println("    - " + result);
            }
        }
    }

    private static void deleteStudentRecord() {
        if (students.isEmpty()) {
            println("No student records available.");
            return;
        }
        int roll = readInt("Enter student roll number: ");
        Student student = findStudentByRoll(roll);
        if (student == null) {
            println("Student not found.");
            return;
        }
        if (student.semesters.isEmpty()) {
            println("This student has no semester records to delete.");
            return;
        }
        println("\nSelect the semester record to delete for " + student.name + ":");
        for (int i = 0; i < student.semesters.size(); i++) {
            println("  " + (i + 1) + ". " + student.semesters.get(i).scheme.name);
        }
        int choice = readIntRange("Semester number: ", 1, student.semesters.size()) - 1;
        SemesterRecord removed = student.semesters.remove(choice);
        println("Deleted record for semester '" + removed.scheme.name + "' from student " + student.name + ".");
    }

    private static SemesterScheme chooseScheme() {
        println("Available semester schemes:");
        for (int i = 0; i < schemes.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + schemes.get(i).name);
        }
        int choice = readIntRange("Choose a scheme: ", 1, schemes.size()) - 1;
        return schemes.get(choice);
    }

    private static SemesterScheme findSchemeByName(String name) {
        for (SemesterScheme scheme : schemes) {
            if (scheme.name.equalsIgnoreCase(name)) {
                return scheme;
            }
        }
        return null;
    }

    private static Student findStudentByRoll(int rollNo) {
        for (Student student : students) {
            if (student.rollNo == rollNo) {
                return student;
            }
        }
        return null;
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int v = scanner.nextInt();
                scanner.nextLine();
                return v;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid integer.");
                scanner.nextLine();
            }
        }
    }

    private static boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.next().trim().toLowerCase();
            scanner.nextLine();
            if (line.equals("y") || line.equals("yes")) {
                return true;
            }
            if (line.equals("n") || line.equals("no")) {
                return false;
            }
            System.out.println("Please answer 'y' or 'n'.");
        }
    }

    private static int readIntRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value < min || value > max) {
                println("Please enter a number between " + min + " and " + max + ".");
            } else {
                return value;
            }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static void println(String message) {
        System.out.println(message);
    }
}
