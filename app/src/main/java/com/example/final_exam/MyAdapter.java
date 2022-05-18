package com.example.final_exam;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener{
    Context myContext;
    private int num=20;
    String sub;
    String []answers=new String[num];
    String []correct_answers=new String[num];
    int []rnd=new int[num];
    int []balls=new int[num];
    Boolean edit=true;
    Button end;
    TextView timer;
    public int counter=0;
    Boolean cod=false;
    private final LayoutInflater inflater;
    Example[]objects;
    TextView result;
    DBHelper dbHelper;

    public MyAdapter(Context context, Example[] objects, String subject) {
        sub=subject;
        fill();
        if(subject.equals("inf")){
            num=10;
        }
        else {
            num=20;
        }

        dbHelper=new DBHelper(context);

        myContext=context;
        this.objects=objects;
        this.inflater = LayoutInflater.from(context);

        Button end=((AppCompatActivity) myContext).findViewById(R.id.end);
        end.setOnClickListener(this);

        result=((AppCompatActivity) myContext).findViewById(R.id.bally);
        timer=((AppCompatActivity) myContext).findViewById(R.id.timer);

        int seconds=12600;
        new CountDownTimer(seconds*1000, 1000){
            public void onTick(long millisUntilFinished){
                int ch=(seconds-counter)/3600;
                int min=((seconds-counter)%3600)/60;
                int sec=(seconds-counter)%60;
                timer.setText(String.valueOf(ch)+":"+String.valueOf(min/10)+String.valueOf(min%10)+":"+String.valueOf(sec/10)+String.valueOf(sec%10));
                counter++;
            }
            public  void onFinish(){
                cod=true;
                end.callOnClick();
                timer.setVisibility(View.INVISIBLE);
            }
        }.start();

    }

    public void fill(){
        for (int i=0;i<num;i++){
            if(sub.equals("phis")){
                rnd[i]=(int) ((Math.random() * 2) + 1);
            }
            else{
                rnd[i]=1;
            }
            answers[i]="";
            correct_answers[i]="";
            balls[i]=0;
        }
    }

    public int sum(){
        int summa=0;
        for(int i=0;i<num;i++){
            summa+=balls[i];
        }
        return summa;
    }

    public void databasesql(int k){
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        //dbHelper.onUpgrade(database, DBHelper.DATABASE_VERSION, DBHelper.DATABASE_VERSION);
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.BALLS, k);
        contentValues.put(DBHelper.SUBJECT, sub);
        contentValues.put(DBHelper.DATE, new SimpleDateFormat("dd.MM.yyyy,HH:mm:ss").format(new Date()));
        database.insert(DBHelper.DATABASE_NAME, null, contentValues);
    }


    @Override
    public void onClick(View v) {
        if (cod == false) {

            timer.setVisibility(View.INVISIBLE);
            Dialog dialog;
            dialog = new Dialog(myContext);
            dialog.setContentView(R.layout.dialog);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.show();

            Button yes = dialog.findViewById(R.id.yes);
            Button no = dialog.findViewById(R.id.no);

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edit = false;
                    int k=sum();
                    result.setText("Вы набрали "+Integer.toString(k)+" балл(ов)/(а) из " + Integer.toString(num));
                    end = v.findViewById(R.id.end);
                    end.setVisibility(View.INVISIBLE);
                    dialog.dismiss();
                    MyAdapter.this.notifyDataSetChanged();
                    databasesql(k);
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    timer.setVisibility(View.VISIBLE);
                }
            });

        }
        else{
            edit = false;
            end = v.findViewById(R.id.end);
            end.setVisibility(View.INVISIBLE);
            result.setText(Integer.toString(sum()));
            MyAdapter.this.notifyDataSetChanged();
            int k=sum();
            databasesql(k);
        }

    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //Получение текста
        holder.task_number.setText("№" + Integer.toString(position + 1));
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://finalexam-1e5ff-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference reference = db.getReference().child("questions").child(sub).child("task" + Integer.toString(position + 1)).child(Integer.toString(rnd[position]));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Example example = snapshot.getValue(Example.class);
                holder.task_text.setText(example.getText().replace("n", "\n"));
                correct_answers[position] = example.getAnswer();
                /*if(example.getAnswer()%1==0){
                    correct_answers[position]=Integer.toString((int)example.getAnswer());
                }
                else{
                    correct_answers[position]=Double.toString(example.getAnswer());
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Получение картинки
        StorageReference image_reference = FirebaseStorage.getInstance("gs://finalexam-1e5ff.appspot.com").getReference(sub+"/" + Integer.toString(position + 1) + "." + Integer.toString(rnd[position]) + ".JPG");
        try {
            File localfile = File.createTempFile("tempfile", ".jpeg");
            image_reference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            holder.imageView.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.imageView.setImageDrawable(null);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        holder.save.setFocusable(edit);

        holder.task_answer.setFocusable(edit);
        holder.task_answer.setEnabled(edit);
        holder.task_answer.setCursorVisible(edit);
        holder.task_answer.setText(answers[position]);

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answers[position]=holder.task_answer.getText().toString();
                if(correct_answers[position].equals(answers[position])){
                    balls[position]=1;
                }

                InputMethodManager mgr = (InputMethodManager) myContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(holder.task_answer.getWindowToken(), 0);

            }
        });

        if(edit==false){
            holder.task_answer.setHint("");
            holder.otvet.setText("Ваш ответ:");
            holder.save.setVisibility(View.GONE);
            holder.prav.setVisibility(View.VISIBLE);

            if((answers[position].equals(correct_answers[position])) && !(answers[position].equals(""))){
                holder.prav.setTextColor(myContext.getResources().getColor(R.color.green));
                holder.prav.setText("Верно");
                holder.verno.setVisibility(View.GONE);
            }

            else {
                holder.verno.setVisibility(View.VISIBLE);
                holder.verno.setText("Правильный ответ:" + correct_answers[position]);
                holder.prav.setTextColor(myContext.getResources().getColor(R.color.red2));
                holder.prav.setText("Неверно");
            }

        }
        else {
            holder.prav.setVisibility(View.GONE);
            holder.verno.setVisibility(View.GONE);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView prav;
        TextView verno;
        TextView task_text;
        TextView task_number;
        EditText task_answer;
        Button save;
        TextView otvet;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prav=itemView.findViewById(R.id.prav);
            verno=itemView.findViewById(R.id.verno);
            task_text = itemView.findViewById(R.id.task_text);
            task_number = itemView.findViewById(R.id.task_number);
            task_answer = itemView.findViewById(R.id.task_answer);
            save = itemView.findViewById(R.id.save);
            otvet=itemView.findViewById(R.id.otvet);
            imageView = itemView.findViewById(R.id.photo);
        }
    }
    @Override
    public int getItemCount() {
        return num;
    }
}
