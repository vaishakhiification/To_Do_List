package com.vaishakhiification.todolist;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TaskListFragment extends ListFragment {

    private List<TaskDetails> taskList;
    private ArrayAdapter<TaskDetails> listAdapter;
    private DBHelper dbHelper;
    private int sortBy;
    private String searchText;
//    Button sort_id_Btn;
//    Button sort_alp_Btn;
//    Button search_Btn;
//    EditText search_txt;

    static interface Listener {
        void itemClicked(int id);
    }

    private Listener listener;

    public void refresh() {
        if (sortBy == 1) {
            taskList = dbHelper.getSortedTasks(true);
        } else if (sortBy == 2) {
            taskList = dbHelper.getSortedTasks(false);
        } else if (searchText != null && !searchText.isEmpty()) {
            taskList = dbHelper.searchTasks(searchText);
        } else {
            taskList = dbHelper.getAllTasks();
        }
        ArrayList<TaskDetails> tasks = new ArrayList<>();
        tasks.addAll(taskList);
        listAdapter = new TaskArrayAdapter(this.getContext(), tasks);
        setListAdapter(listAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return super.onCreateView(layoutInflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (Listener) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        dbHelper = new DBHelper(getContext().getApplicationContext());
        refresh();
    }

    @Override
    public void onListItemClick(ListView listView, View itemView, int position, long id) {
        if (listener != null) {
            listener.itemClicked(itemView.getId());
        }
    }

    private static class TaskViewHolder {
        private CheckBox checkBox;
        private TextView textView;

        public TaskViewHolder() {
        }

        public TaskViewHolder(TextView textView, CheckBox checkBox) {
            this.checkBox = checkBox;
            this.textView = textView;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }

    private static class TaskArrayAdapter extends ArrayAdapter<TaskDetails> {

        private LayoutInflater inflater;

        public TaskArrayAdapter(Context context, List<TaskDetails> taskList) {
            super(context, R.layout.task_row, R.id.rowTextView, taskList);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TaskDetails task = this.getItem(position);

            CheckBox checkBox;
            TextView textView;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.task_row, null);

                textView = convertView.findViewById(R.id.rowTextView);
                checkBox = convertView.findViewById(R.id.rowCheckBox);

                convertView.setTag(new TaskViewHolder(textView, checkBox));

                checkBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        View row = (View) cb.getParent();
                        TextView tv = row.findViewById(R.id.rowTextView);
                        if (cb.isChecked()) {
                            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        } else {
                            tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        }
                        TaskDetails task = (TaskDetails) cb.getTag();
                        task.setChecked(cb.isChecked());
                    }
                });
            } else {
                TaskViewHolder viewHolder = (TaskViewHolder) convertView.getTag();
                checkBox = viewHolder.getCheckBox();
                textView = viewHolder.getTextView();
            }

            checkBox.setTag(task);

            checkBox.setChecked(task.isCompleted());
            textView.setText(task.getName());
            convertView.setId(task.getId());

            if (task.isCompleted()) {
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            return convertView;
        }
    }

    public void setParameters(int sortBy, String searchTxt) {
        this.searchText = searchTxt;
        this.sortBy = sortBy;
    }

}
