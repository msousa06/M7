package com.gp.gpproject.Read;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gp.gpproject.Create.AgendarVigilanciaActivity;
import com.gp.gpproject.DBManager;
import com.gp.gpproject.Delete.EliminarVigilanciaActivity;
import com.gp.gpproject.R;
import com.gp.gpproject.Update.EditarVigilanciaActivity;

public class SearchVigilanciaResultActivity extends AppCompatActivity {
    private AlertDialog alertDialog;
    private DBManager manager;
    private TextView textViewResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vigilancia_result);

        textViewResult = (TextView) findViewById(R.id.searchResultTextInput);

        manager = new DBManager(this);
        Bundle extras = getIntent().getExtras();

        if(extras.containsKey("AllHistory") && extras.getBoolean("AllHistory")) {

            manager.list_historico_vigilancias(textViewResult);

        } else if (extras.containsKey("SearchID")) {

            manager.list_vigilancias_um_docente(textViewResult, extras.getString("SearchID"));

        } else if(extras.containsKey("HistorySearchID")) {

            manager.list_historico_vigilancias_um_docente(textViewResult, extras.getString("HistorySearchID"));

        } else {
                String ruc = extras.getString("ruc");
                String disciplina = extras.getString("spinnerDiscipplina");

                ruc = (ruc.contains("Select an item"))? "" : ruc;
                disciplina = (disciplina.contains("Select an item"))? "" : disciplina;

                manager.list_search_vigilancias(textViewResult,
                        extras.getString("sala"),
                        extras.getString("textViewData"),
                        extras.getString("textViewHora"),
                        ruc,
                        disciplina);
        }

        ImageButton backbtn = (ImageButton) findViewById(R.id.backBtnSearchResult);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton newBtn = (ImageButton) findViewById(R.id.newBtnSearchResult);
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchVigilanciaResultActivity.this, AgendarVigilanciaActivity.class));
                finish();
            }
        });

        ImageButton deleteBtn = (ImageButton) findViewById(R.id.deleteBtnSearchResult);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchVigilanciaResultActivity.this, EliminarVigilanciaActivity.class));
                finish();
            }
        });

        ImageButton editBtn = (ImageButton) findViewById(R.id.editBtnSearchResult);
        editBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                alertDialog = new AlertDialog.Builder(SearchVigilanciaResultActivity.this).create();
                alertDialog.setTitle("Editar Vigilância");
                alertDialog.setMessage("ID da Vigilância a Editar: ");

                final EditText input = new EditText(SearchVigilanciaResultActivity.this);
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
                                        Intent intent = new Intent(SearchVigilanciaResultActivity.this, EditarVigilanciaActivity.class);
                                        intent.putExtra("EXTRA_VIGILANCIA_ID", input.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        dialog.cancel();
                                        alertDialog = new AlertDialog.Builder(SearchVigilanciaResultActivity.this).create();
                                        alertDialog.setTitle("Erro");
                                        alertDialog.setMessage("Vigilância não existente!");
                                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        alertDialog.show();
                                    }
                                } else {
                                    dialog.cancel();
                                    alertDialog = new AlertDialog.Builder(SearchVigilanciaResultActivity.this).create();
                                    alertDialog.setTitle("Erro");
                                    alertDialog.setMessage("Insira um valor válido!");

                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                    alertDialog.show();
                                }
                            }
                        });

                alertDialog.show();
            }
        });
    }
}
