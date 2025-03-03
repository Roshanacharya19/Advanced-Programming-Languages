import random

class Employee:
    def __init__(self, name, preferences):
        self.name = name
        self.preferences = preferences  # dict: day -> list of ranked preferences
        self.days_worked = 0

days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
shifts = ["morning", "afternoon", "evening"]

# Input: enforce at least 9 employees.
while True:
    try:
        num_employees = int(input("Enter the number of employees (must be at least 9): "))
        if num_employees < 9:
            print("Error: Must be at least 9. Try again.")
        else:
            break
    except ValueError:
        print("Invalid input. Enter an integer.")

employees = {}
for i in range(num_employees):
    name = input(f"\nEnter name for employee {i+1}: ").strip()
    preferences = {}
    for day in days:
        pref_line = input(f"Enter ranked shift preferences for {name} on {day} (e.g., morning, evening, afternoon): ").lower().strip()
        parts = [p.strip() for p in pref_line.split(",")]
        ranked_prefs = []
        for part in parts:
            while part not in shifts:
                part = input("Invalid input. Enter morning, afternoon, or evening: ").lower().strip()
            ranked_prefs.append(part)
        for shift in shifts:
            if shift not in ranked_prefs:
                ranked_prefs.append(shift)
        preferences[day] = ranked_prefs
    employees[name] = Employee(name, preferences)

# Initialize schedule: day -> (shift -> list of employee names)
schedule = {day: {shift: [] for shift in shifts} for day in days}
previous_evening = set()

for day in days:
    assigned_today = set()
    eligible_employees = [e for e in employees.values() if e.days_worked < 5]
    random.shuffle(eligible_employees)
    
    # First pass: assign based on ranked preferences.
    for e in eligible_employees:
        if e.name in assigned_today:
            continue
        for pref in e.preferences[day]:
            if pref == "morning" and e.name in previous_evening:
                continue
            if len(schedule[day][pref]) < 2:
                schedule[day][pref].append(e.name)
                assigned_today.add(e.name)
                e.days_worked += 1
                break
        else:
            # If not assigned by ranking, try any available shift.
            for shift in shifts:
                if shift == "morning" and e.name in previous_evening:
                    continue
                if len(schedule[day][shift]) < 2:
                    schedule[day][shift].append(e.name)
                    assigned_today.add(e.name)
                    e.days_worked += 1
                    break

    # Second pass: fill shifts with fewer than 2 employees.
    for shift in shifts:
        while len(schedule[day][shift]) < 2:
            not_assigned = []
            for e in employees.values():
                if e.days_worked < 5 and e.name not in assigned_today:
                    if shift == "morning" and e.name in previous_evening:
                        continue
                    not_assigned.append(e)
            if not not_assigned:
                print(f"Warning: Not enough eligible employees to fill {shift} on {day}")
                break
            random.shuffle(not_assigned)
            selected = not_assigned[0]
            schedule[day][shift].append(selected.name)
            assigned_today.add(selected.name)
            selected.days_worked += 1

    previous_evening = set(schedule[day]["evening"])

# Output final schedule.
print("\nFinal Weekly Schedule:\n")
for day in days:
    print(f"--- {day} ---")
    for shift in shifts:
        print(f"{shift.capitalize()} shift: {', '.join(schedule[day][shift])}")
    print()
