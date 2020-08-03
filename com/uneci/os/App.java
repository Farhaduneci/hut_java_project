package os;

/*
* Operating Systems Final Project
* Farhad Uneci (Mohammed Sina) | 9708253 | Monday, August 3, 2020
*/

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // New Shell Instance
        Shell shell = new Shell();

        // New Command Instance witch receives the shell to work with
        Commands commands = new Commands(shell);

        String command[] = null;

        Scanner scanner = new Scanner(System.in);

        do {            
            // Automatically will call the .toString() method of the shell instance
            System.out.print(shell);

            // Split the line into an array regarding to white spaces
            command = scanner.nextLine().split("\\s");

            commands.runCommand(command);

        } while (!command[0].equalsIgnoreCase("exit")); // first index of command array equals command name

        // Not necessary needed but not using this will cause resource leak!
        scanner.close();
    }
}