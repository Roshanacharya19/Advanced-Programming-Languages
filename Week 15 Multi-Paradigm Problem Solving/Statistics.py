class Statistics:
    """
    A class that calculates various statistics on a collection of integers.
    This implements an object-oriented approach to statistical calculations.
    """
    
    def __init__(self):
        """Initialize an empty Statistics object."""
        self.data = []
        
    def input_data(self):
        """Get input data from the user."""
        try:
            user_input = input("Enter integers separated by spaces: ")
            self.data = list(map(int, user_input.split()))
            if not self.data:
                print("No numbers provided!")
                return False
            return True
        except ValueError:
            print("Invalid input! Please enter integers only.")
            return False
    
    def calculate_mean(self):
        """
        Calculate the mean (average) of the data.
        
        Returns:
            float: The mean of the data.
        """
        if not self.data:
            raise ValueError("Cannot calculate mean of empty dataset")
        return sum(self.data) / len(self.data)
    
    def calculate_median(self):
        """
        Calculate the median (middle value) of the data.
        
        Returns:
            float: The median of the data.
        """
        if not self.data:
            raise ValueError("Cannot calculate median of empty dataset")
            
        sorted_data = sorted(self.data)
        n = len(sorted_data)
        
        if n % 2 == 0:
            # Even number of elements
            return (sorted_data[n//2 - 1] + sorted_data[n//2]) / 2
        else:
            # Odd number of elements
            return sorted_data[n//2]
    
    def calculate_mode(self):
        """
        Calculate the mode (most frequent value(s)) of the data.
        
        Returns:
            list: A list of the mode(s) of the data.
        """
        if not self.data:
            raise ValueError("Cannot calculate mode of empty dataset")
            
        # Count occurrences of each value
        frequency = {}
        for num in self.data:
            if num in frequency:
                frequency[num] += 1
            else:
                frequency[num] = 1
                
        # Find the maximum frequency
        max_frequency = max(frequency.values())
        
        # Get all values with the maximum frequency
        modes = [num for num, freq in frequency.items() if freq == max_frequency]
        
        return modes
    
    def display_statistics(self):
        """Display all calculated statistics."""
        try:
            mean_value = self.calculate_mean()
            median_value = self.calculate_median()
            mode_values = self.calculate_mode()
            
            print("\nStatistics:")
            print(f"Mean: {mean_value:.2f}")
            print(f"Median: {median_value:.2f}")
            print("Mode(s):", " ".join(map(str, mode_values)))
            
        except ValueError as e:
            print(f"Error: {e}")


class StatisticsApplication:
    """
    Application class that manages the statistics program.
    Demonstrates separation of concerns in OOP.
    """
    
    def __init__(self):
        """Initialize the application with a Statistics object."""
        self.stats = Statistics()
    
    def run(self):
        """Run the statistics application."""
        print("Statistical Calculator")
        print("=====================")
        
        if self.stats.input_data():
            self.stats.display_statistics()


# Main entry point
if __name__ == "__main__":
    app = StatisticsApplication()
    app.run()