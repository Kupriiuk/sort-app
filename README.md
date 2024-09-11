# Sort Application

This is a simple Java Swing-based GUI application that generates random numbers and allows users to sort them using the QuickSort algorithm. It provides an interactive interface where users can specify how many numbers to display, and the application alternates between sorting the numbers in ascending and descending order with each click of the "Sort" button.

## Features
- Users can input a number between 1 and 1000 to specify how many random numbers to generate.
- At least one number in the set will always be 30 or less.
- Numbers can be sorted in ascending or descending order using the "Sort" button.
- Clicking on a number smaller than or equal to 30 generates a new set of random numbers.
- If the user clicks on a number greater than 30, an error message will be displayed.
- The "Reset" button allows the user to return to the initial screen to input a new number count.

## How to Run
1. Clone the repository:

   ```bash
   git clone https://github.com/Kupriiuk/sort-app.git
2. Navigate to the project directory: 
   ```bash
   cd sort-app
3. Compile and run the application using the following commands:
   ```bash
    javac -d out src/org/example/SortApp.java
    java -cp out org.example.SortApp

## Application Structure
The application uses a CardLayout to switch between the intro screen (where users input the number of random numbers) and the sort screen (where the generated numbers are displayed).
The SortApp class handles the GUI logic and the sorting functionality.
QuickSort is used to sort the numbers in ascending or descending order.
QuickSort Algorithm
The QuickSort algorithm is used to sort the numbers based on the user's input. Sorting alternates between ascending and descending order each time the "Sort" button is clicked.

## GUI Components
Intro Screen: Allows users to input how many random numbers to display (between 1 and 1000).
Sort Screen: Displays the generated numbers, which can be sorted or reset.
Buttons:
Sort Button: Sorts the numbers in ascending or descending order.
Reset Button: Returns to the intro screen.
Number Buttons: Clicking a number smaller than or equal to 30 regenerates new random numbers. Clicking a number greater than 30 shows an error message.
Dependencies
This project only requires Java to run. Ensure you have a Java Development Kit (JDK) installed.
