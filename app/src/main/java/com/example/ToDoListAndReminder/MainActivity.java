package com.example.ToDoListAndReminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.ToDoListAndReminder.Adapters.TodosAdapter;
import com.example.ToDoListAndReminder.Models.NumberOfUncompletedTodos;
import com.example.ToDoListAndReminder.Models.Todos;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    AlarmManager alarmManager;
    ListView listView;
    ArrayList<Todos> arrayList;
    ArrayList<Todos> uncompletedTodos;
    ArrayList<Todos> completedTodos;
    TodosAdapter adapter;
    Switch aSwitch;
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listviewid);
        aSwitch = findViewById(R.id.switchtodos);
        arrayList = new ArrayList<>();

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager.getInstance(this).initLoader(0, null, this).forceLoad();
           //vo zavisnost od sostojbata na Switch'ot , se prikazuvaat soodvetnite Todos
            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                       ArrayList<Todos> uncompletedTodos = new ArrayList<>();
                        uncompletedTodos = FilterTodos.getTodos(arrayList, true);
                        TodosAdapter adapter = new TodosAdapter(uncompletedTodos, MainActivity.this);
                        listView.setAdapter(adapter);
                    } else {
                        ArrayList<Todos> completedTodos = new ArrayList<>();
                        completedTodos = FilterTodos.getTodos(arrayList, false);
                        TodosAdapter adapter = new TodosAdapter(completedTodos, MainActivity.this);
                        listView.setAdapter(adapter);
                    }
                }
            });
        } else {
            // uredot ne e povrzan na internet
            ShowDialog.show(MainActivity.this);
        }

       // fakjane na Local Broadcast message koj se ispraka pri promena na sostojbata na checkbox vo listview item
        LocalBroadcastManager.getInstance(this).registerReceiver(itemChangeReceiver, new IntentFilter("complete checkbox change"));

       // Aktivirane na alarm koj zakazuva isprakanje na notifikacija sekoj den vo 16:30 h
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setAction("notify me receiver");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR, 4 ); //HOUR_OF_DAY 16
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        createNotificationChannel();

    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        // povlekuvanje na  podatocite od fake RestApi preku AsyncTaskLoader
        return new DataLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        arrayList = JsonParser.dataParsing(data);
        // primenite podatoci se vo json format
        ArrayList<Todos> uncompletedTodos = new ArrayList<>();
        uncompletedTodos = FilterTodos.getTodos(arrayList, false); // zemanje na uncompleted todos
        TodosAdapter adapter = new TodosAdapter(uncompletedTodos, MainActivity.this);
        listView.setAdapter(adapter);

        NumberOfUncompletedTodos num = new NumberOfUncompletedTodos();
        num.setNum(uncompletedTodos.size());
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
    // fakjane na Local Broadcast message koj se ispraka pri promena na sostojbata na checkbox vo listview item
    public BroadcastReceiver itemChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int itemID = intent.getIntExtra("itemID", 0);
            int todoId = intent.getIntExtra("todoId", 0);
            boolean complete = intent.getBooleanExtra("change", true);
            //  fake RestApi https://jsonplaceholder.typicode.com/todos , ovozmozuva samo http GET
            // ne postoi http PUT request na https://jsonplaceholder.typicode.com/todos/1
            //new DataPutRequestLoader().execute(String.valueOf(todoId),String.valueOf(complete));  //azuriranje na podatocite
            //new DataLoader(MainActivity.this);  //povtorno zemanje na podatci


            // zatoa  lokalno se  azurira completedTodos i uncompletedTodos listView
            Todos todo;
            todo = (Todos) listView.getAdapter().getItem(itemID);
            completedTodos = FilterTodos.getTodos(arrayList, true);
            uncompletedTodos = FilterTodos.getTodos(arrayList, false);
            NumberOfUncompletedTodos num = new NumberOfUncompletedTodos();
            if (!aSwitch.isChecked()) //uncompleted todos
            {
                todo.setCompleted(complete);
                completedTodos.add(todo);
                uncompletedTodos.remove(todo);
                num.setNum(uncompletedTodos.size());
                adapter = new TodosAdapter(uncompletedTodos, MainActivity.this);
            } else
                {
                todo.setCompleted(complete);
                uncompletedTodos.add(todo);
                completedTodos.remove(todo);
                num.setNum(uncompletedTodos.size());
                adapter = new TodosAdapter(completedTodos, MainActivity.this);
            }

            listView.setAdapter(adapter);
        }

    };
    @Override
    protected void onDestroy() {
        unregisterReceiver(itemChangeReceiver);
        super.onDestroy();
    }


    public void createNotificationChannel() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, "Todos notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifies every day at 12:30pm");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }


}