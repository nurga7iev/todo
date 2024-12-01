package com.example.lab6todo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ListView taskListView;
    private TaskAdapter adapter;
    private ArrayList<String> taskList;
    private ArrayList<Integer> taskIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        taskListView = findViewById(R.id.taskListView);
        taskList = new ArrayList<>();
        taskIds = new ArrayList<>();

        // Настройка адаптера
        adapter = new TaskAdapter(this, taskList, taskIds);
        taskListView.setAdapter(adapter);

        // Обработчик клика для добавления новой задачи
        findViewById(R.id.addTaskButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        // Обработчик длинного клика для удаления задачи
        taskListView.setOnItemLongClickListener((parent, view, position, id) -> {
            int taskId = taskIds.get(position);
            if (databaseHelper.deleteTask(taskId)) {
                loadTasks(); // Перезагружаем задачи после удаления
                Toast.makeText(this, "Задача удалена", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ошибка удаления", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks(); // Загружаем задачи при возобновлении активности
    }

    private void loadTasks() {
        taskList.clear();
        taskIds.clear();
        Cursor cursor = databaseHelper.getAllTasks();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                int status = cursor.getInt(cursor.getColumnIndexOrThrow("status"));
                taskIds.add(id);
                taskList.add(title + (status == 1 ? "" : ""));
            } while (cursor.moveToNext());
            cursor.close();
        }
        adapter.notifyDataSetChanged(); // Обновляем адаптер
    }
}
