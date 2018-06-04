package com.gp.gpproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class RecoverPasswordActivity extends AppCompatActivity {

    private Button recover;
    private EditText txtEmailrecover;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recover);
        txtEmailrecover = (EditText) findViewById(R.id.emailtxt);
        firebaseAuth = FirebaseAuth.getInstance();
        recover = (Button) findViewById(R.id.btn_recover);


        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail(txtEmailrecover.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RecoverPasswordActivity.this, "Email sent", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}
