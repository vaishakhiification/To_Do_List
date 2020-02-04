package com.vaishakhiification.todolist;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class TaskDetailsFragment extends Fragment {
    private int taskId;
    private Listener listener;
    private static List<TaskDetails> taskList;
    private EditText taskNameTxt;
    private EditText descriptionTxt;
    private Spinner taskStatusSpinner;
    private TextView taskIdTxt;
    private DBHelper dbHelper;

    static interface Listener {
        void goBack();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            taskId = savedInstanceState.getInt("taskId");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (Listener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_details, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        dbHelper = new DBHelper(getContext().getApplicationContext());
        taskList = dbHelper.getAllTasks();
        View view = getView();
        if (view != null) {
            TaskDetails task = null;
            if (taskId == 0) {
                task = new TaskDetails(0);
            } else {
                for (TaskDetails obj : taskList) {
                    if (obj.getId() == taskId) {
                        task = obj;
                    }
                }
            }
            descriptionTxt = view.findViewById(R.id.textDescription);
            descriptionTxt.setText(task.getDescription());
            taskNameTxt = view.findViewById(R.id.taskName_Txt);
            taskNameTxt.setText(task.getName());
            taskStatusSpinner = view.findViewById(R.id.taskStatus_ddl);
            taskIdTxt = view.findViewById(R.id.taskId);
            taskStatusSpinner.setSelection(task.getStatusId());

            Button addButton = view.findViewById(R.id.save_Btn);
            addButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    boolean isUpdate = true;
                    String taskName = String.valueOf(taskNameTxt.getText());
                    String description = String.valueOf(descriptionTxt.getText());
                    String status = String.valueOf(taskStatusSpinner.getSelectedItem());
                    int statusId = getTaskStatusId(status);

                    if (taskId == 0) {
                        taskId = MainActivity.taskList.size() + 1;
                        isUpdate = false;
                    }
                    TaskDetails task = new TaskDetails(taskId, taskName, description, false, statusId);
                    if (!isUpdate) {
                        int newId = dbHelper.insertTask(task);
                        task.setId(newId);
                        TaskDetails.taskList.add(task);
                    } else {
                        int index = getIndex(taskId);
                        dbHelper.updateTask(task);
                        TaskDetails.taskList.set(index, task);
                    }
                    Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
                    listener.goBack();
                }
            });

            Button deleteButton = view.findViewById(R.id.delete_Btn);
            deleteButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (taskId > 0) {
                        int index = getIndex(taskId);
                        dbHelper.deletetask(taskId);
                        TaskDetails.taskList.remove(index);
                    }
                    listener.goBack();
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong("taskId", taskId);
    }

    public void setTask(int id) {
        this.taskId = id;
    }

    private int getIndex(int taskId) {
        int index = 0;
        for (int i = 0; i < taskList.size(); i++) {
            if (taskId == taskList.get(i).getId()) {
                return i;
            }
        }
        return index;
    }

    private int getTaskStatusId(String status) {
        int index = -1;
        for (int i = 0; i < TaskDetails.taskStatusList.size(); i++) {
            if (TaskDetails.taskStatusList.get(i).equals(status)) {
                return i;
            }
        }
        return index;
    }
}
