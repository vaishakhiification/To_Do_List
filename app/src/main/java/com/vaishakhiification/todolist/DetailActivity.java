package com.vaishakhiification.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity implements TaskDetailsFragment.Listener {
    public static final String EXTRA_TASK_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TaskDetailsFragment frag = (TaskDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.detail_frag);
        int taskId = (int) getIntent().getExtras().get(EXTRA_TASK_ID);
        frag.setTask(taskId);
    }

    @Override
    public void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
