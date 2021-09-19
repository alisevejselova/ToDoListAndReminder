package com.example.ToDoListAndReminder.Models;

public class NumberOfUncompletedTodos {

    static int num; // se cuva momentalnata brojka na uncompleted todos

    public static int getNum() {
        return num;
    }

    public void setNum(int num) {
        NumberOfUncompletedTodos.num = num;
    }
}
