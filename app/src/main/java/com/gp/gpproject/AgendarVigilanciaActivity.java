package com.gp.gpproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AgendarVigilanciaActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView mDisplayTime;
    private Spinner spinnerUC;
    private Spinner spinnerVig;
    private Spinner spinnerPontuacao;
    private EditText salatxt;
    private DBManager manager;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_vigilancia);
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mDisplayTime = (TextView) findViewById(R.id.tvTime);
        spinnerUC = (Spinner) findViewById(R.id.spinnerUc);
        spinnerVig = (Spinner) findViewById(R.id.spinnerVig);
        spinnerPontuacao = (Spinner) findViewById(R.id.spinnerPontuacao);
        salatxt = (EditText) findViewById(R.id.salatxt);
        manager = new DBManager(this, "", null, 2);

        setDate();
        setTime();
        addSpinnerUC();
        addSpinnerVigilante();
        addSpinnerPontuacao();

        ImageView confirmbtn = (ImageView) findViewById(R.id.confirmbtn);
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!salatxt.getText().toString().equalsIgnoreCase("")) {
                    if (!mDisplayDate.getText().toString().equalsIgnoreCase("")) {
                        if (!mDisplayTime.getText().toString().equalsIgnoreCase("")) {
                            manager.insert_vigilancia(salatxt.getText().toString(), mDisplayDate.getText().toString(), mDisplayTime.getText().toString(), spinnerVig.getSelectedItem().toString(), spinnerUC.getSelectedItem().toString(), spinnerPontuacao.getSelectedItem().toString());
                        //    sentNotification();
                            finish();
                        } else {
                            alertDialog = new AlertDialog.Builder(AgendarVigilanciaActivity.this).create();
                            alertDialog.setTitle("Erro");
                            alertDialog.setMessage("Insira uma hora válida!");
                            alertDialog.show();
                        }
                    } else {
                        alertDialog = new AlertDialog.Builder(AgendarVigilanciaActivity.this).create();
                        alertDialog.setTitle("Erro");
                        alertDialog.setMessage("Insira uma data válida!");
                        alertDialog.show();
                    }
                } else {
                    alertDialog = new AlertDialog.Builder(AgendarVigilanciaActivity.this).create();
                    alertDialog.setTitle("Erro");
                    alertDialog.setMessage("Insira uma sala!");
                    alertDialog.show();
                }
            }
        });

        ImageButton backbtn = (ImageButton) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(AgendarVigilanciaActivity.this).create();
                alertDialog.setTitle("Erro");
                alertDialog.setMessage(mDisplayDate.getText().toString() + mDisplayTime.getText().toString());
                alertDialog.show();

                //finish();
            }
        });
    }

    public void setTime() {
        mDisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AgendarVigilanciaActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mDisplayTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }

    public void setDate() {
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AgendarVigilanciaActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };
    }

    public void addSpinnerUC() {
        spinnerUC.setPrompt("Select an item");
        DBManager bd = new DBManager(this, "", null, 2);

        List<String> disciplinas = bd.getAllDisciplinas();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, disciplinas);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerUC.setAdapter(dataAdapter);
    }

    public void addSpinnerVigilante() {
        spinnerVig.setPrompt("Select an item");
        DBManager bd = new DBManager(this, "", null, 2);

        List<String> docentes = bd.getAllDocentes();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, docentes);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerVig.setAdapter(dataAdapter);
    }

    public void addSpinnerPontuacao() {
        ArrayList<Integer> items = new ArrayList<Integer>();
        for (int i = 1; i <= 10; i++) {
            items.add(i);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, items);
        spinnerPontuacao.setAdapter(adapter);
    }
/*
    public void sentNotification(){
        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Notificação de Vigilancia");
        intent.putExtra(Intent.EXTRA_TEXT, "Foi requisitado para uma vigilância no dia " + mDisplayDate.getText().toString() + "às " + mDisplayTime.getText().toString() + " horas." + "\n Disciplina: " + spinnerUC.getSelectedItem().toString());
        intent.setData(Uri.parse("mailto:" + spinnerVig.getSelectedItem().toString())); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);
    }
    */
}
