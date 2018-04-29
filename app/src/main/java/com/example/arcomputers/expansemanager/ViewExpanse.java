package com.example.arcomputers.expansemanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.Calendar;
import java.util.Date;

public class ViewExpanse extends AppCompatActivity {

    EditText viewExpanseNameET,viewExpanseAmountET;
    Spinner viewExpanseCatagorySP;
    TextView dateTV, timeTV;
    int selectedCategory;
    int expenseId = 0;
    long selectedUnixDate;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:MM");
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    int hour = calendar.get(Calendar.HOUR);
    int min = calendar.get(Calendar.MINUTE);

    ExpenseDataSource dataSource = new ExpenseDataSource(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expanse);

        viewExpanseNameET = findViewById(R.id.expanseNameET);
        viewExpanseAmountET = findViewById(R.id.expanseAmountET);
        viewExpanseCatagorySP = findViewById(R.id.expanseCatagorySP);
        dateTV= findViewById(R.id.dateTV);
        timeTV=findViewById(R.id.timeTV);
        String[] Catagory = getResources().getStringArray(R.array.expanseCatagory);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ViewExpanse.this,R.layout.support_simple_spinner_dropdown_item,Catagory);
        viewExpanseCatagorySP.setAdapter(arrayAdapter);

        Intent i = getIntent();
        Expense expense = (Expense) i.getSerializableExtra(ExpanseManage.DATAPASSKEY);
        expenseId = expense.getId();
        String name = expense.getName();
        int category = expense.getCategory();
        double amount = expense.getAmount();
        selectedUnixDate = expense.getUnixDateTime();
        String unixDate = simpleDateFormat.format(expense.getUnixDateTime());
        String unixTime = simpleTimeFormat.format(expense.getUnixDateTime());
        viewExpanseNameET.setText(name);
        viewExpanseAmountET.setText(String.valueOf(amount));
        viewExpanseCatagorySP.setSelection(category);
        dateTV.setText(unixDate);
        //timeTV.setText(unixTime);

        viewExpanseCatagorySP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int item, long l) {
                selectedCategory = adapterView.getSelectedItemPosition();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewExpanse.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String dateStr = day+"/"+(month+1)+"/"+year;
                        dateTV.setText(dateStr);
                        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = null;
                        try {
                            date = (Date)formatter.parse(dateStr);
                            selectedUnixDate = date.getTime();
                        } catch (ParseException ex) {
                            Toast.makeText(ViewExpanse.this, "Date invalid", Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(ViewExpanse.this, "Unix date: " + date.getTime(), Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });




        timeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog= new TimePickerDialog(ViewExpanse.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        timeTV.setText(getAmPmTime(hour, min));
                    }
                }, hour, min, false);
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

    public void updateExpense(View view) {
        if(viewExpanseNameET.getText().toString().equals("")){
            viewExpanseNameET.setError("Required Fill Not Null");
        }else {

            if(viewExpanseAmountET.getText().toString().equals("")  || Double.parseDouble(viewExpanseAmountET.getText().toString())<0)
            {
                viewExpanseAmountET.setError("Required Fill Not Null");
            }
            else
            {
                String expenseName = viewExpanseNameET.getText().toString();
                double amount = Double.parseDouble(viewExpanseAmountET.getText().toString());
                Expense expense = new Expense(expenseId,expenseName,selectedCategory,amount,selectedUnixDate);
                boolean updated = dataSource.updateExpense(expenseId,expense);
                if (updated){
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ViewExpanse.this,ExpanseManage.class));
                }else {
                    Toast.makeText(this, "Not Updated", Toast.LENGTH_SHORT).show();
                }
            }

        }


    }

    public void deleteExpense(View view) {
        AlertDialog.Builder deletedAlert = new AlertDialog.Builder(ViewExpanse.this);
        deletedAlert.setTitle("Deleted Expense");
        deletedAlert.setMessage("Do You want to Delete???");
        deletedAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean deleted = dataSource.deleteExpense(expenseId);
                if (deleted){
                    Toast.makeText(ViewExpanse.this, "Deleted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ViewExpanse.this,ExpanseManage.class));
                }else {
                    Toast.makeText(ViewExpanse.this, "Not Deleted", Toast.LENGTH_SHORT).show();
                }

            }
        });
        deletedAlert.setNegativeButton("No",null);
        deletedAlert.setNeutralButton("Cancel",null);
        deletedAlert.show();

    }
}
