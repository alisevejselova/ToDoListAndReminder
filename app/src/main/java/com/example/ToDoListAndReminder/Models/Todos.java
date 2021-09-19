package com.example.ToDoListAndReminder.Models;

public class Todos {
    private int id;
    private boolean completed;
    private String title;
    private int userId;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return
                "Todos{" +
                        "id = '" + id + '\'' +
                        ",completed = '" + completed + '\'' +
                        ",title = '" + title + '\'' +
                        ",userId = '" + userId + '\'' +
                        "}";
    }
}
