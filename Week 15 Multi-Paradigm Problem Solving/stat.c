#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

void sort(int arr[], int n) {
    for (int i = 0; i < n-1; i++) {
        for (int j = 0; j < n-i-1; j++) {
            if (arr[j] > arr[j+1]) {
                int temp = arr[j];
                arr[j] = arr[j+1];
                arr[j+1] = temp;
            }
        }
    }
}

float mean(int arr[], int n) {
    int sum = 0;
    for (int i = 0; i < n; i++) {
        sum += arr[i];
    }
    return (float)sum / n;
}

float median(int arr[], int n) {
    // Create a copy of the array to avoid modifying the original
    int* temp_arr = (int*)malloc(n * sizeof(int));
    if (temp_arr == NULL) {
        printf("Memory allocation failed\n");
        exit(1);
    }
    
    for (int i = 0; i < n; i++) {
        temp_arr[i] = arr[i];
    }
    
    // Sort the copy
    sort(temp_arr, n);
    
    float result;
    if (n % 2 == 0) {
        result = (temp_arr[n/2 - 1] + temp_arr[n/2]) / 2.0;
    } else {
        result = temp_arr[n/2];
    }
    
    free(temp_arr);
    return result;
}

void mode(int arr[], int n) {
    if (n <= 0) {
        printf("Empty array, no mode\n");
        return;
    }

    // Create a copy of the array
    int* temp_arr = (int*)malloc(n * sizeof(int));
    if (temp_arr == NULL) {
        printf("Memory allocation failed\n");
        exit(1);
    }
    
    for (int i = 0; i < n; i++) {
        temp_arr[i] = arr[i];
    }

    // Find the maximum frequency
    int maxCount = 0;
    for (int i = 0; i < n; i++) {
        // Skip if this value has been marked
        if (temp_arr[i] == INT_MIN) continue;
        
        int count = 1;
        
        // Count duplicates and mark them
        for (int j = i + 1; j < n; j++) {
            if (temp_arr[j] == temp_arr[i]) {
                count++;
                // Mark as counted
                temp_arr[j] = INT_MIN;
            }
        }
        
        if (count > maxCount) {
            maxCount = count;
        }
    }
    
    // Reset the array
    for (int i = 0; i < n; i++) {
        temp_arr[i] = arr[i];
    }
    
    printf("Mode(s): ");
    int modeFound = 0;
    
    for (int i = 0; i < n; i++) {
        // Skip if this value has been processed
        if (temp_arr[i] == INT_MIN) continue;
        
        int count = 1;
        
        // Count frequency
        for (int j = i + 1; j < n; j++) {
            if (temp_arr[j] == temp_arr[i]) {
                count++;
                // Mark as counted
                temp_arr[j] = INT_MIN;
            }
        }
        
        // If this is a mode, print it
        if (count == maxCount) {
            printf("%d ", temp_arr[i]);
            modeFound = 1;
        }
    }
    
    if (!modeFound) {
        printf("No mode found");
    }
    
    printf("\n");
    free(temp_arr);
}

int main() {
    int n, i;
    
    // Get the number of elements
    printf("Enter the number of elements: ");
    if (scanf("%d", &n) != 1 || n <= 0) {
        printf("Invalid input. Please enter a positive integer.\n");
        return 1;
    }
    
    // Allocate memory for the array
    int* arr = (int*)malloc(n * sizeof(int));
    if (arr == NULL) {
        printf("Memory allocation failed\n");
        return 1;
    }
    
    // Get the elements
    printf("Enter %d integers:\n", n);
    for (i = 0; i < n; i++) {
        if (scanf("%d", &arr[i]) != 1) {
            printf("Invalid input. Please enter integers only.\n");
            free(arr);
            return 1;
        }
    }
    
    // Calculate and display statistics
    printf("\nStatistics:\n");
    printf("Mean: %.2f\n", mean(arr, n));
    printf("Median: %.2f\n", median(arr, n));
    mode(arr, n);
    
    // Free allocated memory
    free(arr);
    
    return 0;
}