package BE;

import java.time.LocalDate;
import java.util.Arrays;


public class Event {

    private int id;
    private String name;
    private String location;
    private LocalDate date;
    private double time;


    public Event(String name, String location, LocalDate date, double time, String note, byte[] imageData) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.time = time;
    }

    public Event(int id, String name, String location, LocalDate date, double time, String note, byte[] imageData){
        this(name, location, date, time, note, imageData);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return name;
    }
}
