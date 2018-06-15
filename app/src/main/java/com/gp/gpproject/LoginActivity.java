package com.gp.gpproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private EditText txtEmailLogin;
    private EditText txtPwd;
    private FirebaseAuth firebaseAuth;
    private Button btnlogin;
    private Button resetpass;
    private Button createUser;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        txtEmailLogin = (EditText) findViewById(R.id.emailtxt);
        txtPwd = (EditText) findViewById(R.id.passwordtxt);
        firebaseAuth = FirebaseAuth.getInstance();
        btnlogin = (Button) findViewById(R.id.btn_login);
        resetpass = (Button) findViewById(R.id.link_resetPass);
        createUser = (Button) findViewById(R.id.link_signup);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.createUserWithEmailAndPassword(txtEmailLogin.getText().toString(), txtPwd.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                                    String idLogin = "";
                                    String nameLogin = "";
                                    Bundle bundleLogin = new Bundle();
                                    bundleLogin.putString(FirebaseAnalytics.Param.ITEM_ID, idLogin);
                                    bundleLogin.putString(FirebaseAnalytics.Param.ITEM_NAME, nameLogin);
                                    bundleLogin.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleLogin);
                                    startActivity(i);
                                } else {
                                    Log.e("ERROR", task.getException().toString());
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });


        resetpass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(LoginActivity.this,RecoverPasswordActivity.class));
            }
        });

        createUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String idRegis = "";
                String nameRegis = "";
                Bundle bundleRegis = new Bundle();
                bundleRegis.putString(FirebaseAnalytics.Param.ITEM_ID, idRegis);
                bundleRegis.putString(FirebaseAnalytics.Param.ITEM_NAME, nameRegis);
                bundleRegis.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleRegis);
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });
    }
}