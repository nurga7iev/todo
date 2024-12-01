package com.example.lab6todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class TaskAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> taskList;
    private ArrayList<Integer> taskIds;
    private LayoutInflater inflater;
    private DatabaseHelper databaseHelper;

    public TaskAdapter(Context context, ArrayList<String> taskList, ArrayList<Integer> taskIds) {
        this.context = context;
        this.taskList = taskList;
        this.taskIds = taskIds;
        this.inflater = LayoutInflater.from(context);
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.task_item, parent, false);
        }

        TextView taskTitle = convertView.findViewById(R.id.taskTitle);
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        String task = taskList.get(position);
        taskTitle.setText(task);

        // Получаем ID задачи и её статус
        int taskId = taskIds.get(position);
        int status = databaseHelper.getTaskStatus(taskId);
        checkBox.setChecked(status == 1);

        // Обработчик изменения состояния чекбокса
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int newStatus = isChecked ? 1 : 0;
            databaseHelper.updateTaskStatus(taskId, newStatus);
        });

        // Обработчик нажатия на кнопку "Удалить"
        deleteButton.setOnClickListener(v -> {
            boolean isDeleted = databaseHelper.deleteTask(taskId);
            if (isDeleted) {
                taskList.remove(position); // Удаляем задачу из списка
                taskIds.remove(position); // Удаляем ID задачи из списка
                notifyDataSetChanged(); // Обновляем адаптер
                Toast.makeText(context, "Задача удалена", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Ошибка удаления", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
