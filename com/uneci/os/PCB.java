package os;

public class PCB {
    private String name, timestamps;
    private int id;

    public PCB(String name, int id, String timestamps) {
        this.name = name;
        this.id = id;
        this.timestamps = timestamps;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public String getTimestamps() {
        return this.timestamps;
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", timestamps='" + getTimestamps() + "'" +
            ", id='" + getId() + "'" +
            "}";
    }
}