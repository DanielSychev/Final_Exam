package com.example.final_exam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LastResults extends AppCompatActivity implements View.OnClickListener{
    DBHelper dbHelper;
    TextView textView;
    ListView listView;
    ArrayList <DatabaseClass> arrayList;
    ImageButton home, back;
    Button clear;
    LastResultsAdapter adapter;
    int num;
    String sub;
    SQLiteDatabase database;
    TextView sred_ball;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_results);
        sub = getIntent().getStringExtra("subject");
        dbHelper=new DBHelper(this);
        database=dbHelper.getWritableDatabase();

        long result = 0, kolvo = 0;

        arrayList=new ArrayList<>();
        Cursor cursor=database.query(DBHelper.DATABASE_NAME, null, "subject = ?", new String[]{sub}, null, null, null);
        if(cursor.moveToFirst()){
            int index=cursor.getColumnIndex(DBHelper.KEY_ID);
            int ballsIndex=cursor.getColumnIndex(DBHelper.BALLS);
            int subjectIndex=cursor.getColumnIndex(DBHelper.SUBJECT);
            int dateIndex=cursor.getColumnIndex(DBHelper.DATE);

            do{
                DatabaseClass temp=new DatabaseClass("", -1, "");
                temp.balls=cursor.getInt(ballsIndex);
                result+=temp.balls;
                kolvo+=1;
                temp.subject=cursor.getString(subjectIndex);
                temp.date=cursor.getString(dateIndex);
                if(temp.subject.equals("inf")){
                    temp.subject="Информатика";
                    num=10;
                }
                else {
                    temp.subject="Физика";
                    num=20;
                }
                arrayList.add(temp);
                //textView.setText(Integer.toString(cursor.getInt(ballsIndex)));
            }while(cursor.moveToNext());
            cursor.close();
        }

        listView=findViewById(R.id.listView);
        adapter=new LastResultsAdapter(this, arrayList);
        listView.setAdapter(adapter);

        sred_ball=findViewById(R.id.sred_ball);
        if(kolvo!=0){
            double k = result/kolvo;
            DecimalFormat REAL_FORMATTER = new DecimalFormat("0.###");
            sred_ball.setText("Средний результат:" + REAL_FORMATTER.format(k));
            Log.d("res", Double.toString(k));
        }
        else {sred_ball.setText("Пока что результатов нет!");}

        TextView subject=findViewById(R.id.subject);
        if(sub.equals("inf")){subject.setText("Информатика");}
        else {subject.setText("Физика");}

        home=findViewById(R.id.home);
        back=findViewById(R.id.go_back);
        clear=findViewById(R.id.clear);
        home.setOnClickListener(this);
        back.setOnClickListener(this);
        clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.home:
                Intent i=new Intent(LastResults.this, MainActivity.class);
                startActivity(i);
            break;
            case R.id.go_back:
                Intent k=new Intent(LastResults.this, Generate.class);
                k.putExtra("subject", sub);
                startActivity(k);
            break;
            case R.id.clear:
                database.delete(DBHelper.DATABASE_NAME, "subject = ?", new String[]{sub});
                adapter.clear();
                sred_ball.setText("Пока что результатов нет!");
            break;
        }
    }

    private class DatabaseClass{
        String date;
        int balls;
        String subject;

        public DatabaseClass(String date, int balls, String subject) {
            this.date = date;
            this.balls = balls;
            this.subject = subject;
        }
    }

    private class LastResultsAdapter extends ArrayAdapter<DatabaseClass> {

        public LastResultsAdapter(@NonNull Context context, @NonNull List<DatabaseClass> objects) {
            super(context, R.layout.last_results_adapter, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.last_results_adapter, null);
            }

            TextView ballov=convertView.findViewById(R.id.balls);
            TextView date=convertView.findViewById(R.id.date);
            TextView subject=convertView.findViewById(R.id.s);
            ballov.setText(Integer.toString(arrayList.get(position).balls) + " балл(ов)/(а) из " + Integer.toString(num));
            date.setText(arrayList.get(position).date);
            subject.setText(arrayList.get(position).subject);
            return convertView;
        }
    }
}