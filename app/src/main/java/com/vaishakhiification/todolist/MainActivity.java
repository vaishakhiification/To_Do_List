package com.vaishakhiification.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskListFragment.Listener, TaskDetailsFragment.Listener {

    static List<TaskDetails> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskList = TaskDetails.taskList;

        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void itemClicked(int id) {
        View fragmentContainer = findViewById(R.id.fragment_container);
        if (fragmentContainer != null) {
            TaskDetailsFragment details = new TaskDetailsFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            details.setTask(id);
            ft.replace(R.id.fragment_container, details);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_TASK_ID, id);
            startActivity(intent);
        }
    }


    public void addItem(View view) {
        View fragmentContainer = findViewById(R.id.fragment_container);
        if (fragmentContainer != null) {
            TaskDetailsFragment details = new TaskDetailsFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            details.setTask(0);
            ft.replace(R.id.fragment_container, details);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_TASK_ID, 0);
            startActivity(intent);
        }
    }

    public void sort_by_id(View view) {
        TaskListFragment taskList = (TaskListFragment) getSupportFragmentManager().findFragmentById(R.id.list_frag);
        taskList.setParameters(1, null);
        taskList.refresh();
    }

    public void sort_by_alp(View view) {
        TaskListFragment taskList = (TaskListFragment) getSupportFragmentManager().findFragmentById(R.id.list_frag);
        taskList.setParameters(2, null);
        taskList.refresh();
    }

    public void searchTasks(View view) {
        View parentView = (View) view.getParent();
        TaskListFragment taskList = (TaskListFragment) getSupportFragmentManager().findFragmentByTag("list_frag");
        EditText search_txt = findViewById(R.id.search_text_txt);
        String searchTxt = String.valueOf(search_txt.getText());
//        sea
        taskList.setParameters(0, searchTxt);
        taskList.refresh();
    }

    @Override
    public void goBack() {
//        FragmentManager fm = getSupportFragmentManager();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
//        fm.popBackStack();
//        TaskListFragment taskList = (TaskListFragment) getSupportFragmentManager().findFragmentById(R.id.list_frag);
//        taskList.setParameters(2, null);
//        taskList.refresh();
    }
}