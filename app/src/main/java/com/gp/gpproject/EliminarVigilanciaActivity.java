package com.gp.gpproject;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class EliminarVigilanciaActivity extends AppCompatActivity {

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_vigilancia);

        final EditText fieldID = (EditText) findViewById(R.id.fieldID);

        final DBManager manager = new DBManager(this, "", null, 2);

        ImageView confirmbtn = (ImageView) findViewById(R.id.confirmbtn);
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fieldID.getText().toString().equalsIgnoreCase("")) {
                    try {
                        manager.delete("vigilancias", fieldID.getText().toString());
                        finish();
                    } catch (Exception e) {
                        alertDialog = new AlertDialog.Builder(EliminarVigilanciaActivity.this).create();
                        alertDialog.setTitle("Erro");
                        alertDialog.setMessage("Vigilancia não existe!");
                        alertDialog.show();
                    }
                } else {
                    alertDialog = new AlertDialog.Builder(EliminarVigilanciaActivity.this).create();
                    alertDialog.setTitle("Erro");
                    alertDialog.setMessage("Insira um id válido!");
                    alertDialog.show();
                }
            }
        });

        ImageButton backbtn = (ImageButton) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
