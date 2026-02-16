package com.example.managertask;

import java.io.Serializable;

public class Task implements Serializable {
    private int id;
    private String title;
    private String description;
    private String assignee;
    private String deadline;
    private String priority;
    private boolean completed;
    private String project;
    private String tags;

    public Task(int id, String title, String description, String assignee,
                String deadline, String priority, boolean completed, String project, String tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.deadline = deadline;
        this.priority = priority;
        this.completed = completed;
        this.project = project;
        this.tags = tags;
    }

    // Геттеры
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getAssignee() { return assignee; }
    public String getDeadline() { return deadline; }
    public String getPriority() { return priority; }
    public boolean isCompleted() { return completed; }
    public String getProject() { return project; }
    public String getTags() { return tags; }

    // Сеттеры
    public void setCompleted(boolean completed) { this.completed = completed; }
}