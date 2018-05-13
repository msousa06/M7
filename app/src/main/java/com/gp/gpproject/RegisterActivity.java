package com.gp.gpproject;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.SQLOutput;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText textEmail, textPassword;
    private TextView textSignIn;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        textEmail = (EditText) findViewById(R.id.textEmail);
        textPassword = (EditText) findViewById(R.id.textPassword);
        textSignIn = (TextView) findViewById(R.id.textSignin);

        buttonRegister.setOnClickListener(this);
        textSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(textSignIn)) {
            //open login activity
            Toast.makeText(this, "Sign in", Toast.LENGTH_SHORT).show();
        }
        if(v.equals(buttonRegister)) {
            registerUser();
        }
    }



    private void registerUser() {
        String email = textEmail.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User... ");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String text = "Registration ";
                        text += (task.isSuccessful())? "Complete" : "Failed! \n" + task.getException().getMessage();
                        Toast.makeText(RegisterActivity.this, text, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        //falta ir para a página inicial do utilizador
                        //já faz o registo de novo utilizador dentro do firebase
                    }
                });

    }
}
