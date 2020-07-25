package com.gtxreme.todo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final List<String> list = new ArrayList<>();
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButton = findViewById(R.id.deleteTask);
        final ListView listView = findViewById(R.id.myTaskList);
        final TextAdapter adapter = new TextAdapter();

        readInfo();

        adapter.setData(list);

        listView.setAdapter(adapter);

        final Button btnAddNewTask = findViewById(R.id.newTaskButton);
        btnAddNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText taskInput = new EditText(MainActivity.this);
                taskInput.setSingleLine();
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setTitle("Add a New Task").setMessage("What is your new Task?").
                        setView(taskInput).setPositiveButton("Add Task", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.add(taskInput.getText().toString());
                        adapter.setData(list);
                    }
                }).setNegativeButton("Cancel", null).create();

                alertDialog.show();
            }
        });

        final Button deleteAllTasks = findViewById(R.id.deleteAllTasksButton);

        deleteAllTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new  AlertDialog.Builder(MainActivity.this).setTitle("Delete All Tasks?").
                        setMessage("Do you really want to delete all tasks?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.clear();
                        adapter.setData(list);
                        saveInfo();
                    }
                }).setNegativeButton("No", null).create();

                alertDialog.show();
            }
        });
    }

    private void saveInfo() {
        try {
            File file = new File(this.getFilesDir(), "saved");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedWriter bo = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            for (int i = 0; i < list.size(); i++) {
                bo.write(list.get(i));
                bo.newLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        saveInfo();
    }

    private void readInfo() {
        File file = new File(this.getFilesDir(), "saved");
        if (file.exists()) {
            try {
                FileInputStream fi = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(fi));
                String line = br.readLine();
                while (line != null) {
                    list.add(line);
                    line = br.readLine();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class TextAdapter extends BaseAdapter {

        final List<String> list = new ArrayList<>();

        void setData(List<String> mList) {
            list.clear();
            list.addAll(mList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.task_item, parent, false);}
            TextView txtItem = convertView.findViewById(R.id.txtItem);
            ImageButton imageButton = convertView.findViewById(R.id.deleteTask);
            txtItem.setText(list.get(position));

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setTitle("Delete This Task?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            list.remove(position);
                            notifyDataSetChanged();
                        }
                    }).setNegativeButton("No", null).create();
                    alertDialog.show();
                }

            });

            return convertView;
        }
    }
}