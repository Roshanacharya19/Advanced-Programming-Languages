import java.util.*;

public class EmployeeSchedulerFull {
    
    // Employee class stores the name, ranked shift preferences (per day), and days worked.
    static class Employee {
        String name;
        // Map from day -> first preference is index 0
        Map<String, List<String>> preferences;
        int daysWorked;
        
        Employee(String name, Map<String, List<String>> preferences) {
            this.name = name;
            this.preferences = preferences;
            this.daysWorked = 0;
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String[] shifts = {"morning", "afternoon", "evening"};
        
        int numEmployees;
        do {
            System.out.print("Enter the number of employees (must be at least 9): ");
            numEmployees = scanner.nextInt();
            if(numEmployees < 9) {
                System.out.println("Error: The number of employees must be at least 9. Please try again.");
            }
        } while (numEmployees < 9);
        scanner.nextLine();
        
        Map<String, Employee> employees = new HashMap<>();
        
        for (int i = 0; i < numEmployees; i++) {
            System.out.print("\nEnter name for employee " + (i + 1) + ": ");
            String name = scanner.nextLine().trim();
            
            Map<String, List<String>> preferences = new HashMap<>();
            for (String day : days) {
                System.out.print("Enter ranked shift preferences for " + name + " on " + day 
                                 + " (e.g., morning, evening, afternoon): ");
                String prefLine = scanner.nextLine().toLowerCase().trim();
                String[] parts = prefLine.split(",");
                List<String> rankedPrefs = new ArrayList<>();
                for (String part : parts) {
                    String pref = part.trim();
                    while (!pref.equals("morning") && !pref.equals("afternoon") && !pref.equals("evening")) {
                        System.out.print("Invalid input. Please enter morning, afternoon, or evening: ");
                        pref = scanner.nextLine().toLowerCase().trim();
                    }
                    rankedPrefs.add(pref);
                }
                // If the user did not provide all three preferences, add the missing ones in any order.
                for (String shift : shifts) {
                    if (!rankedPrefs.contains(shift))
                        rankedPrefs.add(shift);
                }
                preferences.put(day, rankedPrefs);
            }
            employees.put(name, new Employee(name, preferences));
        }
        
        // Final schedule of the workshift
        Map<String, Map<String, List<String>>> schedule = new HashMap<>();
        for (String day : days) {
            Map<String, List<String>> daySchedule = new HashMap<>();
            for (String shift : shifts) {
                daySchedule.put(shift, new ArrayList<>());
            }
            schedule.put(day, daySchedule);
        }
        
        Random random = new Random();
        
        // Set to track employees who worked the previous day's evening shift, to make sure they don't work the next day's morning shift.
        Set<String> previousEvening = new HashSet<>();
        
        for (String day : days) {
            Set<String> assignedToday = new HashSet<>();
            List<Employee> eligibleEmployees = new ArrayList<>();
            for (Employee e : employees.values()) {
                if (e.daysWorked < 5) {
                    eligibleEmployees.add(e);
                }
            }
            Collections.shuffle(eligibleEmployees, random);
            for (Employee e : eligibleEmployees) {
                if (assignedToday.contains(e.name))
                    continue;
                List<String> rankPrefs = e.preferences.get(day);
                boolean assigned = false;
                for (String pref : rankPrefs) {
                    if (pref.equals("morning") && previousEvening.contains(e.name)) {
                        continue;
                    }
                    if (schedule.get(day).get(pref).size() < 2) {
                        schedule.get(day).get(pref).add(e.name);
                        assignedToday.add(e.name);
                        e.daysWorked++;
                        assigned = true;
                        break;
                    }
                }
                if (!assigned) {
                    for (String shift : shifts) {
                        if (shift.equals("morning") && previousEvening.contains(e.name)) {
                            continue;
                        }
                        if (schedule.get(day).get(shift).size() < 2) {
                            schedule.get(day).get(shift).add(e.name);
                            assignedToday.add(e.name);
                            e.daysWorked++;
                            assigned = true;
                            break;
                        }
                    }
                }
            }
            for (String shift : shifts) {
                List<String> assignedList = schedule.get(day).get(shift);
                while (assignedList.size() < 2) {
                    List<Employee> notAssigned = new ArrayList<>();
                    for (Employee e : employees.values()) {
                        if (e.daysWorked < 5 && !assignedToday.contains(e.name)) {
                            if (shift.equals("morning") && previousEvening.contains(e.name)) {
                                continue;
                            }
                            notAssigned.add(e);
                        }
                    }
                    if (notAssigned.isEmpty()) {
                        System.out.println("Warning: Not enough eligible employees to fill " 
                                           + shift + " on " + day);
                        break;
                    }
                    Collections.shuffle(notAssigned, random);
                    Employee selected = notAssigned.get(0);
                    schedule.get(day).get(shift).add(selected.name);
                    assignedToday.add(selected.name);
                    selected.daysWorked++;
                }
            }
            previousEvening = new HashSet<>(schedule.get(day).get("evening"));
        }
        
        // Print the final schedule
        System.out.println("\nFinal Weekly Schedule:\n");
        for (String day : days) {
            System.out.println("--- " + day + " ---");
            for (String shift : shifts) {
                List<String> assigned = schedule.get(day).get(shift);
                String shiftLabel = shift.substring(0, 1).toUpperCase() + shift.substring(1);
                System.out.println(shiftLabel + " shift: " + String.join(", ", assigned));
            }
            System.out.println();
        }
        
        scanner.close();
    }
}
