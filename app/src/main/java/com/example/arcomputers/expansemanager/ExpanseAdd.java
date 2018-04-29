package com.example.arcomputers.expansemanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExpanseAdd extends AppCompatActivity {

    EditText expanseNameET, expanseAmountET;
    Spinner expanseCatSP;
    TextView dateTV, timeTV;
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
    int currentMin = calendar.get(Calendar.MINUTE);
    private List<Expense> expenseList = new ArrayList<>();
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    Date date = null;
    ExpenseDataSource expenseDataSource = new ExpenseDataSource(ExpanseAdd.this);
    boolean added;

    String currentDate = day + "/" + month + "/" +year;
    String currentTime = currentHour + ":" + currentMin;
    private long unixDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanse_add);

        expanseNameET = findViewById(R.id.expanseNameET);
        expanseAmountET = findViewById(R.id.expanseAmountET);
        expanseCatSP = findViewById(R.id.expanseCatSP);
        dateTV = findViewById(R.id.dateTV);
        timeTV = findViewById(R.id.timeTV);

        dateTV.setText(currentDate);
        timeTV.setText(getAmPmTime(currentHour,currentMin));

        try {
            date = formatter.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        unixDate = date.getTime();

        String[] Catagory = getResources().getStringArray(R.array.expanseCatagory);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ExpanseAdd.this,R.layout.support_simple_spinner_dropdown_item,Catagory);
        expanseCatSP.setAdapter(arrayAdapter);

        expanseCatSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int item, long l) {
                String expaseCat = (String) adapterView.getItemAtPosition(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        expenseList = expenseDataSource.getAllExpense();


        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ExpanseAdd.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String dateStr = day+"/"+(month+1)+"/"+year;
                        dateTV.setText(dateStr);
                        formatter = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            date = (Date)formatter.parse(dateStr);
                            unixDate = date.getTime();
                        } catch (ParseException ex) {
                            Toast.makeText(ExpanseAdd.this, "Date invalid", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });




        timeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog= new TimePickerDialog(ExpanseAdd.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        timeTV.setText(getAmPmTime(hour, min));
                    }
                }, currentHour, currentMin, false);
                timePickerDialog.show();
            }
        });

    }

    private String getAmPmTime(int hours, int minutes) {
        if (hours > 12) {
            hours -= 12;
            return hours+":"+minutes+"PM";
        } else {
            return hours+":"+minutes+"AM";
        }
    }

    public void saveExpanse(View view) {

        if(expanseNameET.getText().toString().equals("")){
            expanseNameET.setError("Required Fill Not Null");
        }else {

            if(expanseAmountET.getText().toString().equals("")  || Double.parseDouble(expanseAmountET.getText().toString())<0)
            {
                expanseAmountET.setError("Required Fill Not Null");
            }
            else
            {
                saveData();
            }

        }
        /*if(expanseNameET.getText().toString().equals("")){
            expanseNameET.setError("Required Fill Not Null");
        }else if(expanseAmountET.getText().toString().equals("")  || Double.parseDouble(expanseAmountET.getText().toString())<0)
            {
                expanseAmountET.setError("Required Fill Not Null");
            }
            else
            {
                saveData();
            }*/





    }

    private void saveData(){
        String expanseName= expanseNameET.getText().toString();
        int expanseCatagory = expanseCatSP.getSelectedItemPosition();
        double expanseAmount = Double.parseDouble(expanseAmountET.getText().toString());
        long date = unixDate;
        Expense expense = new Expense(expanseName,expanseCatagory,date,expanseAmount);
        added = expenseDataSource.addExpense(expense);
        if (added)
        {
            Intent intent = new Intent(ExpanseAdd.this,ExpanseManage.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
