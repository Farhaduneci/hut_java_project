# Java Command Line
A Bash like command line program written in Java

![Project-preview][App-screenshot]

### Compile & run the project

Make sure to install `java` on your system

> You can follow installation from [here](https://linuxize.com/post/install-java-on-ubuntu-18-04/) for Debian based linux distributions.

```sh
- git clone git@github.com:Farhaduneci/commandLine.git
- cd commandLine/com/uneci/os
- javac -cp ../ App.java
- cd ..
- java os/App
```

> You can find more about -cp used above here at [this answer](https://stackoverflow.com/a/13540223)
> at Stack Overflow.

### Project Pipeline

I tried to make a pipeline for every command to go throw for being processed in the system,

1. App starts execution from the `main` function of `App` class where every input string would be split 
   and passed to `runCommand` function of the `Commands` class instant

```java
    // Split the line into an array regarding to white spaces
    command = scanner.nextLine().split("\\s");

    commands.runCommand(command);
```

2. The `runCommand` function, validates the command, checks for the `--help` flag and if the command is valid,
   calls the coresponding functrion of the command and creates a `PCB` for the executed command.

   > PCB, theoretically must be created before execution but I did it this way, IDK why.

3. Process will be repeated for every command.

### Tools

Java Colored Debug Printer (JColor) offers an easy syntax to print messages with a colored font or
background on a terminal. You can find the package at https://github.com/dialex/JColor 
This package is used with the name of `"Ansi"` in project.

---

### Issues
according to https://docs.oracle.com/javase/7/docs/api/java/nio/file/Files.html
I've tried this method for the `mv` command:

```java
    Files.move(
        Paths.get(shell.getCurrentDir() + '/' + fileName),
        Paths.get(destination),
        StandardCopyOption.ATOMIC_MOVE
    );
```
This method does not MOVE a file but it only RENAMEs it!

I have implemented this function like this:

```java
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
```

Current implementation is not working properly too!

IDK what's wrong :(

<!-- LINKS -->
[App-screenshot]: Images/screenshot.png