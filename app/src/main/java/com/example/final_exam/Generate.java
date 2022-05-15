package com.example.final_exam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Generate extends AppCompatActivity implements View.OnClickListener {
    Button create, see;
    ImageButton home;
    String subject;
    TextView sub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subject = getIntent().getStringExtra("subject");
        setContentView(R.layout.activity_generate);
        create=findViewById(R.id.create);
        see=findViewById(R.id.see);
        sub=findViewById(R.id.subject);
        if(subject.equals("inf")){
            sub.setText("Информатика");
        }
        else{
            sub.setText("Физика");
        }
        home=findViewById(R.id.home);
        create.setOnClickListener(this);
        see.setOnClickListener(this);
        home.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        switch (v.getId()){
            case R.id.create:
                i=new Intent(Generate.this, Phis.class);
            break;
            case R.id.see:
                i=new Intent(Generate.this, SomeTasks.class);
            break;
            case R.id.home:
                i=new Intent(Generate.this, MainActivity.class);
            break;
        }
        i.putExtra("subject", subject);
        startActivity(i);
    }
}