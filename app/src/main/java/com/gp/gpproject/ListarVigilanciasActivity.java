package com.gp.gpproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ListarVigilanciasActivity extends AppCompatActivity {

    private AlertDialog alertDialog;
    private View view;
    private TextView textView;
    private DBManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_vigilancias);

        manager = new DBManager(this, "", null, 2);

        textView = (TextView) findViewById(R.id.textView);

        manager.list_all_Vigilancias(textView);


        ImageButton backbtn = (ImageButton) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton newBtn = (ImageButton) findViewById(R.id.newBtn);
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListarVigilanciasActivity.this, AgendarVigilanciaActivity.class));
                finish();
            }
        });


        ImageButton deleteAllBtn = (ImageButton) findViewById(R.id.deleteALLBtn);
        deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.deleteAll("vigilancias");
            }
        });

        ImageButton deleteBtn = (ImageButton) findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListarVigilanciasActivity.this, EliminarVigilanciaActivity.class));
                finish();
            }
        });

        ImageButton editBtn = (ImageButton) findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                alertDialog = new AlertDialog.Builder(ListarVigilanciasActivity.this).create();
                alertDialog.setTitle("Editar Vigilância");
                alertDialog.setMessage("ID da Vigilância a Editar: ");

                final EditText input = new EditText(ListarVigilanciasActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!input.getText().toString().equalsIgnoreCase("") && Integer.parseInt(input.getText().toString()) >= 0) {
                                    if (manager.idExists("vigilancias", input.getText().toString())) {
                                        dialog.cancel();
                                        Intent intent = new Intent(ListarVigilanciasActivity.this, EditarVigilanciaActivity.class);
                                        intent.putExtra("EXTRA_VIGILANCIA_ID", input.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        dialog.cancel();
                                        alertDialog = new AlertDialog.Builder(ListarVigilanciasActivity.this).create();
                                        alertDialog.setTitle("Erro");
                                        alertDialog.setMessage("Vigilância não existente!");
                                        alertDialog.show();
                                    }
                                } else {
                                    dialog.cancel();
                                    alertDialog = new AlertDialog.Builder(ListarVigilanciasActivity.this).create();
                                    alertDialog.setTitle("Erro");
                                    alertDialog.setMessage("Insira um valor válido!");
                                    alertDialog.show();
                                }
                            }
                        });

                alertDialog.show();
            }
        });
    }
}
