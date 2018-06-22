package com.gp.gpproject.Read;

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

import com.gp.gpproject.Create.AdicionarDocenteActivity;
import com.gp.gpproject.DBManager;
import com.gp.gpproject.Delete.EliminarDocenteActivity;
import com.gp.gpproject.MainActivity;
import com.gp.gpproject.R;
import com.gp.gpproject.Update.EditarDocenteActivity;


public class ListarDocentesActivity extends AppCompatActivity {

    private AlertDialog alertDialog;
    private View view;
    private TextView textView;
    private DBManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_docentes);

        manager = new DBManager(this);

        textView = (TextView) findViewById(R.id.textView);

        manager.list_all_docentes(textView);


        ImageButton backbtn = (ImageButton) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListarDocentesActivity.this, MainActivity.class));
                finish();
            }
        });

        ImageButton newBtn = (ImageButton) findViewById(R.id.newBtn);
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListarDocentesActivity.this, AdicionarDocenteActivity.class));
                finish();
            }
        });

        ImageButton deleteAllBtn = (ImageButton) findViewById(R.id.deleteAllBtn);
        deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.deleteAll("docentes");
                manager.deleteAll("funcionarios");
            }
        });

        ImageButton deleteBtn = (ImageButton) findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListarDocentesActivity.this, EliminarDocenteActivity.class));
                finish();
            }
        });

        ImageButton editBtn = (ImageButton) findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                alertDialog = new AlertDialog.Builder(ListarDocentesActivity.this).create();
                alertDialog.setTitle("Editar Docente");
                alertDialog.setMessage("ID do Docente a Editar: ");

                final EditText input = new EditText(ListarDocentesActivity.this);
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
                                    if (manager.idExists("docentes", input.getText().toString())) {
                                        dialog.cancel();
                                        Intent intent = new Intent(ListarDocentesActivity.this, EditarDocenteActivity.class);
                                        intent.putExtra("EXTRA_DOCENTE_ID", input.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        dialog.cancel();
                                        alertDialog = new AlertDialog.Builder(ListarDocentesActivity.this).create();
                                        alertDialog.setTitle("Erro");
                                        alertDialog.setMessage("Docente não existente!");
                                        alertDialog.show();
                                    }
                                } else {
                                    dialog.cancel();
                                    alertDialog = new AlertDialog.Builder(ListarDocentesActivity.this).create();
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
