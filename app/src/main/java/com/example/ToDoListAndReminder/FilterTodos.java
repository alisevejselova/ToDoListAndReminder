package com.example.ToDoListAndReminder;

import com.example.ToDoListAndReminder.Models.Todos;

import java.util.ArrayList;

public class FilterTodos {

    public static ArrayList<Todos> getTodos(ArrayList<Todos> alltodos, boolean completed) {

        ArrayList<Todos> completedTodos = new ArrayList<>();
        ArrayList<Todos> uncompletedTodos = new ArrayList<>();

        for (int i = 0; i < alltodos.size(); i++) {
            Boolean status = alltodos.get(i).isCompleted();
            if (status == true) {
                completedTodos.add(alltodos.get(i));
            } else {
                uncompletedTodos.add(alltodos.get(i));
            }
        }
        if (completed == true) {
            return completedTodos;
        } else {
            return uncompletedTodos;
        }

    }
}
