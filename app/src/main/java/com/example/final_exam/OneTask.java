package com.example.final_exam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class OneTask extends AppCompatActivity implements View.OnClickListener {
    private int k;
    ImageButton home, go_back;
    String subject;
    OneTaskAdapter adapter;
    ListView listView;
    int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_task);

        Bundle arguments = getIntent().getExtras();
        number = (int) arguments.get("num");
        subject = getIntent().getStringExtra("subject");
        if(subject.equals("phis")){
            k=2;
        }
        else{
            k=1;
        }

        Example[] array = new Example[k];
        for(int i=0;i<k;i++) {
            array[i] = new Example("", "");
        }

        listView = findViewById(R.id.listView);
        adapter = new OneTaskAdapter(this, array);
        listView.setAdapter(adapter);

        home = findViewById(R.id.home);
        go_back = findViewById(R.id.go_back);
        home.setOnClickListener(this);
        go_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        switch (v.getId()) {
            case R.id.home:
                i = new Intent(OneTask.this, MainActivity.class);
                break;
            case R.id.go_back:
                i = new Intent(OneTask.this, SomeTasks.class);
                i.putExtra("subject", subject);
                break;
        }
        startActivity(i);
    }
    public class OneTaskAdapter extends ArrayAdapter<Example>{

        public OneTaskAdapter(@NonNull Context context, @NonNull Example[] objects) {
            super(context, R.layout.one_task_adapter, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.one_task_adapter, null);
            ImageView imageView = convertView.findViewById(R.id.photo);
            //Получение текста
            TextView task_text = convertView.findViewById(R.id.task_text);
            TextView task_number = convertView.findViewById(R.id.task_number);
            TextView task_answer = convertView.findViewById(R.id.task_answer);
            task_number.setText("№" + Integer.toString(number)+ "." + Integer.toString(position + 1));
            FirebaseDatabase db = FirebaseDatabase.getInstance("https://finalexam-1e5ff-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference reference = db.getReference().child("questions").child(subject).child("task" + Integer.toString(number)).child(Integer.toString(position+1));
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Example example = dataSnapshot.getValue(Example.class);
                    task_text.setText(example.getText().replace("n", "\n"));
                    task_answer.setText(example.getAnswer());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //Получение картинки
            StorageReference image_reference = FirebaseStorage.getInstance("gs://finalexam-1e5ff.appspot.com").getReference(subject+"/" + Integer.toString(number) + "." + Integer.toString(position+1) + ".JPG");
            try {
                File localfile = File.createTempFile("tempfile", ".jpeg");
                image_reference.getFile(localfile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                imageView.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        imageView.setImageDrawable(null);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            return convertView;
        }
    }
}