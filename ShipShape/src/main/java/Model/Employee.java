package Model;

import java.sql.Time;


public class Employee {
    private int id;
    private String name;
    private String role;
    private Time startTime;
    private Time endTime;

    public Employee(String name, String role, Time startTime, Time endTime) {
        this.name = name;
        this.role = role;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Employee(int id, String name, String role, Time startTime, Time endTime) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
}

