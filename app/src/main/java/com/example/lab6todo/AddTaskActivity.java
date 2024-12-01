package com.example.lab6todo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        databaseHelper = new DatabaseHelper(this);

        // Обработчик клика на кнопку "Сохранить"
        findViewById(R.id.saveButton).setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            // Убедитесь, что введены данные
            if (title.isEmpty()) {
                Toast.makeText(AddTaskActivity.this, "Название задачи не может быть пустым", Toast.LENGTH_SHORT).show();
                return;
            }

            // Сохраняем задачу с статусом 0 (невыполнена)
            long id = databaseHelper.addTask(title, 0); // передаем статус как 0 (невыполнена)

            if (id != -1) {
                Toast.makeText(AddTaskActivity.this, "Задача добавлена", Toast.LENGTH_SHORT).show();
                finish(); // Закрываем активность после добавления задачи
            } else {
                Toast.makeText(AddTaskActivity.this, "Ошибка добавления задачи", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
