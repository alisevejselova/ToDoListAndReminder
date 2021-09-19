package com.example.ToDoListAndReminder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.ToDoListAndReminder.Models.Todos;
import com.example.ToDoListAndReminder.R;

import java.util.List;

public class TodosAdapter extends BaseAdapter {

    List<Todos> todosList;
    Context context;

    public TodosAdapter(List<Todos> list, Context context) {
        this.todosList = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return todosList.size();
    }

    @Override
    public Object getItem(int position) {
        return todosList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.listviewitem_layout, parent, false);

        TextView Id = convertView.findViewById(R.id.id);
        TextView userId = convertView.findViewById(R.id.userid);
        TextView title = convertView.findViewById(R.id.title);
        CheckBox completedCheckBox = convertView.findViewById(R.id.completed);


        Id.setText("ToDo: " + todosList.get(position).getId());
        userId.setText("User id: " + todosList.get(position).getUserId());
        title.setText("Title: " + todosList.get(position).getTitle());
        Boolean status = todosList.get(position).isCompleted();
        completedCheckBox.setChecked(status == true);

        completedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final boolean isChecked = completedCheckBox.isChecked();

                Intent intent = new Intent("complete checkbox change");
                intent.putExtra("itemID", position);
                intent.putExtra("todoId", todosList.get(position).getId());
                intent.putExtra("change", isChecked);
                // pri check/uncheck na Complete checkbox , se isprakja lokalna Broadcast poraka
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
        return convertView;
    }


}
