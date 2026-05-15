import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Subject {
    String name;
    int marks;
    int credit;

    Subject(String name, int marks, int credit) {
        this.name = name;
        this.marks = marks;
        this.credit = credit;
    }

    @Override
    public String toString() {
        return String.format("%s (%dcr): %d", name, credit, marks);
    }
}

class GradeThreshold {
    String label;
    double minScore;

    GradeThreshold(String label, double minScore) {
        this.label = label;
        this.minScore = minScore;
    }
}

class GradingScheme {
    String name;
    List<GradeThreshold> thresholds;

    GradingScheme(String name, List<GradeThreshold> thresholds) {
        this.name = name;
        this.thresholds = new ArrayList<>(thresholds);
        this.thresholds.sort((a, b) -> Double.compare(b.minScore, a.minScore));
    }

    String getGrade(double score) {
        for (GradeThreshold threshold : thresholds) {
            if (score >= threshold.minScore) {
                return threshold.label;
            }
        }
        return "Fail";
    }

    void displayScheme() {
        System.out.println("Scheme: " + name);
        for (GradeThreshold threshold : thresholds) {
            System.out.printf("  %s >= %.0f%n", threshold.label, threshold.minScore);
        }
    }
}

class Student {
    String name;
    int rollNo;
    List<Subject> subjects;
    GradingScheme scheme;
    int totalWeightedScore;
    int totalCredits;
    double weightedAverage;
    double rawAverage;
    String grade;

    Student(String name, int rollNo, List<Subject> subjects, GradingScheme scheme) {
        this.name = name;
        this.rollNo = rollNo;
        this.subjects = subjects;
        this.scheme = scheme;
        calculate();
    }

    void calculate() {
        totalWeightedScore = 0;
        totalCredits = 0;
        int rawTotal = 0;
        for (Subject subject : subjects) {
            totalWeightedScore += subject.marks * subject.credit;
            totalCredits += subject.credit;
            rawTotal += subject.marks;
        }
        weightedAverage = totalCredits > 0 ? (double) totalWeightedScore / totalCredits : 0;
        rawAverage = subjects.size() > 0 ? (double) rawTotal / subjects.size() : 0;
        grade = scheme.getGrade(weightedAverage);
    }

    void updateDetails(List<Subject> newSubjects, GradingScheme newScheme) {
        this.subjects = newSubjects;
        this.scheme = newScheme;
        calculate();
    }

    void displaySummary() {
        System.out.printf("%5d | %-18s | %10.2f | %-6s | %-16s%n",
                rollNo, name, weightedAverage, grade, scheme.name);
    }

    void displayDetailed() {
        System.out.println("\n========== STUDENT REPORT ==========");
        System.out.println("Name           : " + name);
        System.out.println("Roll Number    : " + rollNo);
        System.out.println("Grading Scheme : " + scheme.name);
        System.out.println("Subjects       : " + subjects.size());
        System.out.println("Subject details:");
        for (Subject subject : subjects) {
            System.out.println("  - " + subject);
        }
        System.out.println("Total credits  : " + totalCredits);
        System.out.println("Weighted score : " + totalWeightedScore);
        System.out.printf("Weighted avg   : %.2f%n", weightedAverage);
        System.out.printf("Raw average    : %.2f%n", rawAverage);
        System.out.println("Final grade    : " + grade);
        System.out.println("====================================");
    }

    String formatSubjectList() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < subjects.size(); i++) {
            builder.append(subjects.get(i).toString());
            if (i < subjects.size() - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }
}

class SubjectSummary {
    int totalMarks;
    int totalCredits;
    int count;
    int highest = Integer.MIN_VALUE;
    int lowest = Integer.MAX_VALUE;
    String bestStudent = "-";
    String worstStudent = "-";
}

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Student> students = new ArrayList<>();
    private static final List<GradingScheme> schemePresets = new ArrayList<>();

    public static void main(String[] args) {
        loadDefaultSchemes();
        printBanner("EDUCATION REPORT MANAGER");
        while (true) {
            printMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewAllStudents();
                case 3 -> viewStudentReport();
                case 4 -> searchStudent();
                case 5 -> updateStudentMarks();
                case 6 -> showClassStatistics();
                case 7 -> showSubjectStatistics();
                case 8 -> showGradeDistribution();
                case 9 -> sortByTotal();
                case 10 -> removeStudent();
                case 11 -> manageGradingSchemes();
                case 0 -> {
                    println("Goodbye. Your reports are safe until the next run.");
                    scanner.close();
                    return;
                }
                default -> println("Invalid selection. Please choose a valid option.");
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
        println("1. Add student record");
        println("2. View all student summaries");
        println("3. View specific student report");
        println("4. Search student by roll or name");
        println("5. Update student marks / credits");
        println("6. Show class statistics");
        println("7. Show subject-wise statistics");
        println("8. View grade distribution");
        println("9. Sort student list");
        println("10. Remove a student record");
        println("11. Grading scheme manager");
        println("0. Exit");
    }

    private static void loadDefaultSchemes() {
        schemePresets.add(new GradingScheme("School Standard", List.of(
                new GradeThreshold("A", 90),
                new GradeThreshold("B", 75),
                new GradeThreshold("C", 50),
                new GradeThreshold("D", 40),
                new GradeThreshold("E", 33),
                new GradeThreshold("F", 0)
        )));
        schemePresets.add(new GradingScheme("University Standard", List.of(
                new GradeThreshold("A", 85),
                new GradeThreshold("B", 70),
                new GradeThreshold("C", 55),
                new GradeThreshold("D", 45),
                new GradeThreshold("F", 0)
        )));
        schemePresets.add(new GradingScheme("CBSE / Board Style", List.of(
                new GradeThreshold("A1", 91),
                new GradeThreshold("A2", 81),
                new GradeThreshold("B1", 71),
                new GradeThreshold("B2", 61),
                new GradeThreshold("C1", 51),
                new GradeThreshold("C2", 41),
                new GradeThreshold("D", 33),
                new GradeThreshold("E1", 21),
                new GradeThreshold("E2", 0)
        )));
    }

    private static GradingScheme chooseGradingScheme() {
        println("\nChoose the grading criteria:");
        for (int i = 0; i < schemePresets.size(); i++) {
            println((i + 1) + ". " + schemePresets.get(i).name);
        }
        println((schemePresets.size() + 1) + ". Create a custom scheme");
        int choice = readIntRange("Select a scheme: ", 1, schemePresets.size() + 1);
        if (choice <= schemePresets.size()) {
            return schemePresets.get(choice - 1);
        }
        return createCustomScheme();
    }

    private static GradingScheme createCustomScheme() {
        println("\n--- Custom Grading Scheme ---");
        String name = readString("Scheme name: ");
        int thresholdCount = readIntRange("How many grade levels? ", 2, 10);
        List<GradeThreshold> thresholds = new ArrayList<>();
        double lastMin = 100;
        for (int i = 1; i <= thresholdCount; i++) {
            String label = readString("Label for level " + i + " (for example A, B or Distinction): ");
            int minScore = readIntRange("Minimum score for " + label + ": ", 0, (int) lastMin);
            thresholds.add(new GradeThreshold(label, minScore));
            lastMin = minScore - 1;
            if (lastMin < 0) {
                break;
            }
        }
        if (thresholds.isEmpty()) {
            println("Using default school scheme because no valid custom levels were entered.");
            return schemePresets.get(0);
        }
        return new GradingScheme(name, thresholds);
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

    private static void viewStudentReport() {
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
        student.displayDetailed();
    }

    private static void addStudent() {
        println("\n--- Add New Student ---");
        String name = readString("Enter student name: ");
        int roll = readInt("Enter roll number: ");
        if (findStudentByRoll(roll) != null) {
            println("A student with this roll number already exists.");
            return;
        }
        GradingScheme scheme = chooseGradingScheme();
        int subjectCount = readIntRange("Number of subjects: ", 1, 20);
        List<Subject> subjects = readSubjectList(subjectCount);
        students.add(new Student(name, roll, subjects, scheme));
        println("Student record saved successfully.");
    }

    private static int[] readMarks(int count) {
        int[] marks = new int[count];
        for (int i = 0; i < count; i++) {
            marks[i] = readInt("Enter marks for subject " + (i + 1) + ": ");
        }
        return marks;
    }

    private static List<Subject> readSubjectList(int count) {
        List<Subject> subjects = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            println("\nSubject " + i + " details:");
            String subjectName = readString("  Name: ");
            int credit = readIntRange("  Credit value (1-10): ", 1, 10);
            int marks = readIntRange("  Marks obtained (0-100): ", 0, 100);
            subjects.add(new Subject(subjectName, marks, credit));
        }
        return subjects;
    }

    private static void viewAllStudents() {
        if (students.isEmpty()) {
            println("No student records available.");
            return;
        }
        println("\n===== STUDENT SUMMARY =====");
        println("Roll  | Name                 | Total Marks | Average | GRADE");
        println("-----------------------------------------------------------");
        for (Student s : students) {
            s.displaySummary();
        }
    }

    private static void searchStudent() {
        if (students.isEmpty()) {
            println("No student records available.");
            return;
        }
        String query = readString("Enter roll number or name to search: ");
        List<Student> found = new ArrayList<>();
        try {
            int rollQuery = Integer.parseInt(query);
            for (Student s : students) {
                if (s.rollNo == rollQuery) {
                    found.add(s);
                }
            }
        } catch (NumberFormatException ignored) {
            String lower = query.toLowerCase();
            for (Student s : students) {
                if (s.name.toLowerCase().contains(lower)) {
                    found.add(s);
                }
            }
        }
        if (found.isEmpty()) {
            println("No matching student records found.");
            return;
        }
        for (Student s : found) {
            s.displayDetailed();
        }
    }

    private static void updateStudentMarks() {
        if (students.isEmpty()) {
            println("No student records available.");
            return;
        }
        int roll = readInt("Enter roll number of the student to update: ");
        Student student = findStudentByRoll(roll);
        if (student == null) {
            println("Student not found.");
            return;
        }
        println("Current subjects: " + student.formatSubjectList());
        println("Current grading scheme: " + student.scheme.name);
        println("1. Replace all subjects");
        println("2. Change grading scheme only");
        int option = readIntRange("Choose an update option: ", 1, 2);
        if (option == 1) {
            int subjectCount = readIntRange("New number of subjects: ", 1, 20);
            List<Subject> subjects = readSubjectList(subjectCount);
            GradingScheme scheme = chooseGradingScheme();
            student.updateDetails(subjects, scheme);
        } else {
            GradingScheme scheme = chooseGradingScheme();
            student.updateDetails(student.subjects, scheme);
        }
        println("Student record updated successfully.");
    }

    private static void showClassStatistics() {
        if (students.isEmpty()) {
            println("No student records available.");
            return;
        }
        int totalStudents = students.size();
        double sumWeightedAverage = 0;
        Student top = null;
        Student bottom = null;
        int passed = 0;
        int failed = 0;
        Map<String, Integer> schemeCount = new LinkedHashMap<>();

        for (Student student : students) {
            sumWeightedAverage += student.weightedAverage;
            if (top == null || student.weightedAverage > top.weightedAverage) {
                top = student;
            }
            if (bottom == null || student.weightedAverage < bottom.weightedAverage) {
                bottom = student;
            }
            if (!student.grade.equalsIgnoreCase("F") && !student.grade.equalsIgnoreCase("Fail")) {
                passed++;
            } else {
                failed++;
            }
            schemeCount.put(student.scheme.name, schemeCount.getOrDefault(student.scheme.name, 0) + 1);
        }

        println("\n--- Class Statistics ---");
        println("Number of students     : " + totalStudents);
        println(String.format("Average weighted score: %.2f", sumWeightedAverage / totalStudents));
        if (top != null) {
            println("Top student            : " + top.name + " (" + top.rollNo + ") with " + String.format("%.2f", top.weightedAverage));
        }
        if (bottom != null) {
            println("Lowest student         : " + bottom.name + " (" + bottom.rollNo + ") with " + String.format("%.2f", bottom.weightedAverage));
        }
        println("Passed students        : " + passed);
        println("Failed students        : " + failed);
        println("Schemes used:");
        for (Map.Entry<String, Integer> entry : schemeCount.entrySet()) {
            println("  - " + entry.getKey() + ": " + entry.getValue());
        }
    }

    private static void showSubjectStatistics() {
        if (students.isEmpty()) {
            println("No student records available.");
            return;
        }
        Map<String, SubjectSummary> stats = new LinkedHashMap<>();
        for (Student student : students) {
            for (Subject subject : student.subjects) {
                SubjectSummary summary = stats.computeIfAbsent(subject.name, k -> new SubjectSummary());
                summary.totalMarks += subject.marks;
                summary.totalCredits += subject.credit;
                summary.count++;
                if (subject.marks > summary.highest) {
                    summary.highest = subject.marks;
                    summary.bestStudent = student.name;
                }
                if (subject.marks < summary.lowest) {
                    summary.lowest = subject.marks;
                    summary.worstStudent = student.name;
                }
            }
        }

        println("\n--- Subject-wise Statistics ---");
        for (Map.Entry<String, SubjectSummary> entry : stats.entrySet()) {
            String subjectName = entry.getKey();
            SubjectSummary summary = entry.getValue();
            double averageMark = (double) summary.totalMarks / summary.count;
            println(String.format("%s -> Avg: %.2f | High: %d (%s) | Low: %d (%s) | Avg credits: %.2f",
                    subjectName,
                    averageMark,
                    summary.highest,
                    summary.bestStudent,
                    summary.lowest,
                    summary.worstStudent,
                    (double) summary.totalCredits / summary.count));
        }
    }

    private static void showGradeDistribution() {
        if (students.isEmpty()) {
            println("No student records available.");
            return;
        }
        Map<String, Integer> gradeCounts = new LinkedHashMap<>();
        for (Student student : students) {
            gradeCounts.put(student.grade, gradeCounts.getOrDefault(student.grade, 0) + 1);
        }
        println("\n--- Grade Distribution ---");
        for (Map.Entry<String, Integer> entry : gradeCounts.entrySet()) {
            println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static void manageGradingSchemes() {
        println("\n--- Grading Scheme Manager ---");
        while (true) {
            println("1. View available schemes");
            println("2. Create a new custom scheme");
            println("3. Remove a custom scheme");
            println("0. Return to main menu");
            int choice = readIntRange("Choose an option: ", 0, 3);
            if (choice == 0) {
                return;
            }
            if (choice == 1) {
                println("\nAvailable grading schemes:");
                for (int i = 0; i < schemePresets.size(); i++) {
                    println((i + 1) + ". " + schemePresets.get(i).name);
                }
            } else if (choice == 2) {
                GradingScheme custom = createCustomScheme();
                schemePresets.add(custom);
                println("Custom grading scheme added: " + custom.name);
            } else if (choice == 3) {
                if (schemePresets.size() <= 1) {
                    println("Cannot remove scheme when only one scheme exists.");
                    continue;
                }
                println("\nSelect scheme to remove:");
                for (int i = 0; i < schemePresets.size(); i++) {
                    println((i + 1) + ". " + schemePresets.get(i).name);
                }
                int removeIndex = readIntRange("Scheme number: ", 1, schemePresets.size()) - 1;
                GradingScheme removed = schemePresets.remove(removeIndex);
                println("Removed grading scheme: " + removed.name);
            }
        }
    }

    private static void sortByTotal() {
        if (students.isEmpty()) {
            println("No student records available.");
            return;
        }
        println("\nSort Options:");
        println("1. By weighted average descending");
        println("2. By weighted average ascending");
        println("3. By name A-Z");
        println("4. By name Z-A");
        int option = readIntRange("Choose sort option: ", 1, 4);
        switch (option) {
            case 1 -> students.sort(Comparator.comparingDouble((Student s) -> s.weightedAverage).reversed());
            case 2 -> students.sort(Comparator.comparingDouble(s -> s.weightedAverage));
            case 3 -> students.sort(Comparator.comparing(s -> s.name.toLowerCase()));
            case 4 -> students.sort(Comparator.comparing((Student s) -> s.name.toLowerCase()).reversed());
        }
        println("Students sorted successfully.");
        viewAllStudents();
    }

    private static void removeStudent() {
        if (students.isEmpty()) {
            println("No student records available.");
            return;
        }
        int roll = readInt("Enter roll number of the student to remove: ");
        Student student = findStudentByRoll(roll);
        if (student == null) {
            println("Student not found.");
            return;
        }
        students.remove(student);
        println("Student removed successfully.");
    }

    private static Student findStudentByRoll(int rollNo) {
        for (Student s : students) {
            if (s.rollNo == rollNo) {
                return s;
            }
        }
        return null;
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid integer.");
                scanner.nextLine();
            }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        scanner.nextLine();
        return scanner.nextLine().trim();
    }

    private static void println(String value) {
        System.out.println(value);
    }
}
