package com.example.final_exam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class Phis extends AppCompatActivity  {
    private int num;
    MyAdapter adapter;
    ImageButton home;
    boolean firstTime = true;
    String subject;

    private void scrollToSecondPosition() {
        // do the scroll
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phis);

        subject = getIntent().getStringExtra("subject");
        if(subject.equals("inf")){
            num=10;
        }
        else {
            num=20;
        }
        Example[]task=new Example[num];

        RecyclerView recyclerView=findViewById(R.id.listView);
        for(int i=0;i<=task.length-1;i++) {
            task[i] = new Example("", "");
        }
        ViewTreeObserver vto = recyclerView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (recyclerView.getChildCount() > 0 && firstTime){
                    firstTime = false;
                    scrollToSecondPosition();
                }
            }
        });
        adapter=new MyAdapter(this, task, subject);
        recyclerView.setAdapter(adapter);
        home=findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Phis.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}