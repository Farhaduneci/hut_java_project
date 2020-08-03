package os;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import jcolor.*;

public class Commands {
    private Shell shell;

    public Commands(Shell shell) {
        this.shell = shell;
    }

    // Will be used for the id felid of PCBs
    private int commandCounter = 0;

    String commandName = null, commandAttribute = null;

    // List of all commands available in the App every new command must be added here first, used for validation!
    String commandsList[] = {
        "pwd", "pidhis", "ls", "mkdir", "mv", "rm", "cd", "cat", "exit",

    };

    /*
        * Java Colored Debug Printer (JColor) offers an easy syntax to print messages with a
        * colored font or background on a terminal.
        * You can find the package at https://github.com/dialex/JColor
        * This package is used with the name of "Ansi" in project!
    */

    // Ansi formats for printing messages
    private AnsiFormat errorMessage = new AnsiFormat(
        Attribute.WHITE_TEXT(),
        Attribute.RED_BACK(),
        Attribute.BOLD()
    );

    private AnsiFormat infoMessage = new AnsiFormat(
        Attribute.YELLOW_BACK(),
        Attribute.BOLD()
    );

    private AnsiFormat successMessage = new AnsiFormat(
        Attribute.GREEN_BACK(),
        Attribute.BOLD()
    );

    public void runCommand(String[] command) {
        // The Command name will be available at first index
        commandName = command[0];

        // First check if the command is valid
        if (!commandValidation(commandName)) {
            System.err.println(
                Ansi.colorize(
                    "Command mismatch, try again...", errorMessage)
            );

            return;
        }

        // Second index will have the parameters for the command or --help flag!
        String commandAttribute = 
            (command.length > 1)
                ? command[1]
                : "No Attribute";

        // We check if the command has --help flag for printing the help message or not!
        if (commandAttribute.equals("--help")) {
            printHelp(commandName);

            // We skip running the switch one time
            return;
        }

        commandCounter ++;

        switch (commandName) {
            case "pwd":
                pwd();
                break;

            case "pidhis":
                pidhis();
                break;  

            case "ls":
                ls();
                break;
                
            case "mkdir":
                mkdir(commandAttribute);
                break;

            case "cd":
                cd(commandAttribute);
                break;

            case "cat":
                cat(commandAttribute);
                break;

            case "mv":
                mv(command);
                break;

            case "rm":
                rm(commandAttribute);
                break;
        
            case "exit":
            break;
        }

        // Create a PCB for the executed command if it was valid
        createPCB(commandName);
    }

    public void printHelp(String command) {
        // Not a command so we do not need to call createPCB()
        System.out.println(
            Ansi.colorize(
                Help.valueOf(command).toString(),
                infoMessage
            )
        );
    }

    private boolean commandValidation(String commandName) {
        return Arrays.stream(commandsList).anyMatch(
            command -> command.equals(commandName)
        );
    }

    private void pwd() {
        System.out.println(
            shell.getCurrentDir()
        );
    }

    private void pidhis() {
        System.out.println(
            shell.getHistory()
        );
    }

    private void cd(String newPath) {
        String newDir = shell.setCurrentDir(newPath);
        
        System.out.println(
            Ansi.colorize(
                "Working directory changed to " + newDir,
                successMessage
            )
        );

        // Update the directory path
        shell.updateInfo( newDir );
    }

    private void cat(String fileName) {
        String path = shell.getCurrentDir() + '/' + fileName;

        try (
                BufferedReader reader = new BufferedReader(
                    new FileReader(path))
            ) {

            // Each line of the file will be saved inside
            String line;

            System.out.println("File Data:");

            while ((line = reader.readLine()) != null) {
                System.out.println(
                    Ansi.colorize(line, Attribute.WHITE_BACK(), Attribute.BLACK_TEXT())
                );
            }
        } catch (Exception e) {
            System.out.println(
                Ansi.colorize(e.getMessage(), errorMessage)
            );
        }
    }

    private void ls() {
        // Creates an array in which we will store the names of files and directories
        String[] pathNames;

        // New File object
        File file = new File(shell.getCurrentDir());

        // Populates the array with names of files and directories
        pathNames = file.list();

        if (pathNames.length == 0) {
            System.err.println(
                Ansi.colorize(
                "Folder is empty", errorMessage)
            );

            return;
        }

        for (String pathname : pathNames) {
            // Print the names of files and directories
            System.out.println(pathname);
        }
    }      

    /*
        * according to
        * https://docs.oracle.com/javase/7/docs/api/java/nio/file/Files.html
        *
        * I've tried this method:
        *
        * Files.move(
        *     Paths.get(shell.getCurrentDir() + '/' + fileName),
        *     Paths.get(destination),
        *     StandardCopyOption.ATOMIC_MOVE
        * );
        *
        * This method does not MOVE a file but it only RENAMEs it!
        * 
        * Current implementation is not working properly too!
        *
        * IDK what's wrong :(
    */

    private void mv(String[] command){
        try {
            // Checking for enough attributes
            if (command.length < 3) {
                throw new Exception("Not enough attributes given!");
            }

            // Catching attributes from the command
            String fileName = command[1];
            String destination = command[2];

            // Making the file path
            String current = shell.getCurrentDir() + '/' + fileName;

            // New File object
            File file = new File(current);

            boolean success = file.renameTo(new File(destination));

            if (success) {
                System.out.println(
                    Ansi.colorize("File moved successfully", successMessage)
                );

                return;
            }

            throw new Exception("Something went wrong");

        } catch (Exception e) {
            System.out.println(
                Ansi.colorize(e.getMessage(), errorMessage)
            );
        }
    }

    private void rm(String fileName) {
        // Making the file path
        String path = shell.getCurrentDir() + '/' + fileName;

        // New File object
        File file = new File(path); 
          
        if(file.delete()) { 
            System.out.println(
                    Ansi.colorize("File deleted successfully", successMessage)
            );
        } else { 
            System.out.println(
                Ansi.colorize("Failed to delete the file", errorMessage)
            );
        } 
    }

    private void mkdir(String fileName) {
        // Path for creating a directory
        String path = shell.getCurrentDir();

        // Adding the file name to the path variable
        path = path + '/' + fileName;

        // New File object
        File file = new File(path);

        // Creating the directory
        boolean result = file.mkdir();

        if (result)
            System.out.println(
                Ansi.colorize(
                    "Directory created successfully",
                    successMessage
                )
            );
        else
            System.out.println(
                Ansi.colorize(
                    "Sorry couldn’t create specified directory",
                    errorMessage
                )
            );
    }

    private void createPCB(String command) {
        shell.addToHistory(command, commandCounter);
    }
}