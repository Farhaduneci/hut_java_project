package os;

public enum Help {
    pwd("This command print's out the current working directory"),
    ls("This command will print all file and folder names inside the current working directory"),
    mkdir("This command creates a new directory inside the current working directory"),
    cd("This command changes the working directory"),
    mv("This command moves a file to the specified directory"),
    rm("This command removes a file from current directory"),
    cat("This command prints a file data");

    private final String helpMessage;

    Help(String helpMessage) {
        this.helpMessage = helpMessage;
    }

    @Override
    public String toString() {
        return helpMessage;
    }
}