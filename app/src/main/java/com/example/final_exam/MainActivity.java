package com.example.final_exam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.FragmentTransitionSupport;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageButton inf, phis;
    ImageButton exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inf=findViewById(R.id.b_inf);
        phis=findViewById(R.id.b_phis);
        exit=findViewById(R.id.exit);
        inf.setOnClickListener(this);
        phis.setOnClickListener(this);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder BackAlertDialog = new AlertDialog.Builder(MainActivity.this);
                BackAlertDialog.setMessage("Вы уверены, что хотите выйти?");
                BackAlertDialog.setPositiveButton("Нет",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Cancel alert dialog box .
                                dialog.cancel();
                            }
                        });

                BackAlertDialog.setNegativeButton("Да",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Exit from activity.
                                Intent i = new Intent(Intent.ACTION_MAIN);
                                i.addCategory(Intent.CATEGORY_HOME);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }
                        });

                BackAlertDialog.show();
                return;
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(MainActivity.this, Generate.class);;
        switch (v.getId()){
            case R.id.b_phis:
                i.putExtra("subject", "phis");
            break;
            case R.id.b_inf:
                i.putExtra("subject", "inf");
            break;
        }
        startActivity(i);
    }
}