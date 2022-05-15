package com.example.final_exam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class SomeTasks extends AppCompatActivity implements View.OnClickListener {
    private int k=20;
    String subject;
    ImageButton home, go_back;
    ArrayAdapter<Integer> adapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_some_tasks);

        subject = getIntent().getStringExtra("subject");
        if(subject.equals("inf")){
            k=10;
        }
        else {
            k=20;
        }
        Integer[]array=new Integer[k];
        for(int i=0;i<k;i++){
            array[i]=i;
        }
        listView=findViewById(R.id.listView);
        adapter=new SomeTasksAdapter(this, array);
        listView.setAdapter(adapter);
        home=findViewById(R.id.home);
        go_back=findViewById(R.id.go_back);
        home.setOnClickListener(this);
        go_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i=new Intent();
        switch(v.getId()){
            case R.id.home:
                i=new Intent(SomeTasks.this, MainActivity.class);
            break;
            case R.id.go_back:
                i=new Intent(SomeTasks.this, Generate.class);
                i.putExtra("subject", subject);
            break;
        }
        startActivity(i);
    }

    public class SomeTasksAdapter extends ArrayAdapter<Integer> implements View.OnTouchListener {

        public SomeTasksAdapter(@NonNull Context context, @NonNull Integer[] objects) {
            super(context, R.layout.some_tasks_adapter, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.some_tasks_adapter, null);
            }
            TextView num= convertView.findViewById(R.id.task_number);
            num.setText("№"+Integer.toString(position+1));
            num.setOnTouchListener( this);

            return convertView;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TextView num=v.findViewById(R.id.task_number);
            int i = Integer.valueOf(num.getText().toString().substring(1));
            Intent intent =new Intent(SomeTasks.this, OneTask.class);
            intent.putExtra("num", i);
            intent.putExtra("subject", subject);
            startActivity(intent);
            return true;
        }
    }

}