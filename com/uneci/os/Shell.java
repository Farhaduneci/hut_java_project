package os;

import java.util.LinkedList;
import java.util.Queue;
import java.time.LocalDateTime;

public class Shell {
    private String username = null, currentDir = null, info = null;
    
    private Queue<PCB> history;

    public Shell() {
        this.username = System.getProperty("user.name");
        this.currentDir = System.getProperty("user.dir");
        this.info = this.username + " @ " + this.currentDir + " >> ";

        this.history = new LinkedList<PCB>();
    }

    public String getUsername() {
        return this.username;
    }

    public String getCurrentDir() {
        return this.currentDir;
    }

    public void addToHistory(String command, int id) {
        if (this.history.size() == 5) {
            this.history.remove();
        }

        String timestamps = LocalDateTime.now().toString();
        
        this.history.add(
            new PCB(command, id, timestamps)
        );
    }

    public String getHistory() {
        StringBuilder history = new StringBuilder("History: \n");
        
        for (PCB pcb : this.history) {
            history.append('\n' + pcb.toString());
        }

        return history.toString();
    }

    public String setCurrentDir(String newPath) {
        this.currentDir = newPath;
        return this.getCurrentDir();
    }

    public void updateInfo(String dir) {
        this.info = this.getUsername() + " @ " + dir + " >> ";
    }
    
    @Override
    public String toString() {
        return this.info;
    }
}
