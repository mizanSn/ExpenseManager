package com.example.arcomputers.expansemanager;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportGeneratedActivity extends AppCompatActivity {
    private TextView fromDateTextView;
    private TextView toDateTextView;
    private ImageButton fromDateImageButton;
    private ImageButton toDateImageButton;
    private Button generateReportButton;
    private TextView emptyReportTextView;
    private LinearLayout reportLinearLayout;

    private Calendar calendar = Calendar.getInstance();
    private int year = calendar.get(Calendar.YEAR);
    private int month = calendar.get(Calendar.MONTH);
    private int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

    Spinner expenseCategorySpinner;

    private boolean isFromDateChanged = false;
    private boolean isToDateChanged = false;

    ExpenseDataSource expenseDataSource = new ExpenseDataSource(ReportGeneratedActivity.this);

    List<Expense> expenses = new ArrayList<>();
    ListView expenseReportListView;
    ExpenseReportAdapter expenseReportAdapter;

    String[] categories = {
            "All",
            "Grocery",
            "Utility",
            "Personal",
            "Housing",
            "Health Care",
            "Entertainment",
            "Transport",
            "Others"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_generated);

        expenses = expenseDataSource.getAllExpense();

        emptyReportTextView = findViewById(R.id.emptyReportTextView);
        expenseCategorySpinner = findViewById(R.id.expenseCategorySpinner);
        fromDateTextView = findViewById(R.id.dateFromTextView);
        toDateTextView = findViewById(R.id.dateToTextView);
        fromDateImageButton = findViewById(R.id.dateFromImageButton);
        toDateImageButton = findViewById(R.id.dateToImageButton);
        expenseReportListView = findViewById(R.id.expenseReportListView);
        generateReportButton = findViewById(R.id.reportGenerateButton);
        reportLinearLayout = findViewById(R.id.reportLinearLayout);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        categories); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        expenseCategorySpinner.setAdapter(spinnerArrayAdapter);

        expenseReportAdapter = new ExpenseReportAdapter(this, expenses);
        expenseReportListView.setAdapter(expenseReportAdapter);
    }

    public void showFromDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(ReportGeneratedActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        fromDateTextView.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                    }
                },
                year, month, dayOfMonth);
        datePickerDialog.show();
        isFromDateChanged = true;
    }

    public void showToDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(ReportGeneratedActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        toDateTextView.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                    }
                },
                year, month, dayOfMonth);
        datePickerDialog.show();
        isToDateChanged = true;
    }

    public void generateReport(View view) {
        if (expenseCategorySpinner.getSelectedItemPosition() > 0
                && isFromDateChanged
                && isToDateChanged) {
            int catIndex = expenseCategorySpinner.getSelectedItemPosition()-1;
            long unixFromDate = getUnixDate(fromDateTextView.getText().toString());
            long unixToDate = getUnixDate(toDateTextView.getText().toString());
            ArrayList<Expense> dateExpenseList = expenseDataSource.getExpenseByCategoryAndDate(catIndex,
                    unixFromDate,
                    unixToDate);
            if (dateExpenseList.size() > 0) {
                emptyReportTextView.setVisibility(View.GONE);
                reportLinearLayout.setVisibility(View.VISIBLE);
                expenseReportAdapter = new ExpenseReportAdapter(this, dateExpenseList);
                expenseReportListView.setAdapter(expenseReportAdapter);
            } else {
                emptyReportTextView.setVisibility(View.VISIBLE);
                reportLinearLayout.setVisibility(View.GONE);
            }
        }
        else if (expenseCategorySpinner.getSelectedItemPosition() > 0
                && isFromDateChanged) {
            int catIndex = expenseCategorySpinner.getSelectedItemPosition()-1;
            long unixFromDate = getUnixDate(fromDateTextView.getText().toString());
            ArrayList<Expense> dateExpenseList = expenseDataSource.getExpenseByCategoryAndDate(catIndex,
                    unixFromDate);
            if (dateExpenseList.size() > 0) {
                emptyReportTextView.setVisibility(View.GONE);
                reportLinearLayout.setVisibility(View.VISIBLE);
                expenseReportAdapter = new ExpenseReportAdapter(this, dateExpenseList);
                expenseReportListView.setAdapter(expenseReportAdapter);
            } else {
                emptyReportTextView.setVisibility(View.VISIBLE);
                reportLinearLayout.setVisibility(View.GONE);
            }
        }
        else if (expenseCategorySpinner.getSelectedItemPosition() == 0
                && isFromDateChanged
                && isToDateChanged) {
            long unixFromDate = getUnixDate(fromDateTextView.getText().toString());
            long unixToDate = getUnixDate(toDateTextView.getText().toString());
            ArrayList<Expense> dateExpenseList = expenseDataSource.getExpenseByDate(unixFromDate,
                    unixToDate);
            if (dateExpenseList.size() > 0) {
                emptyReportTextView.setVisibility(View.GONE);
                reportLinearLayout.setVisibility(View.VISIBLE);
                expenseReportAdapter = new ExpenseReportAdapter(this, dateExpenseList);
                expenseReportListView.setAdapter(expenseReportAdapter);
            } else {
                emptyReportTextView.setVisibility(View.VISIBLE);
                reportLinearLayout.setVisibility(View.GONE);
            }

        }
        else if (expenseCategorySpinner.getSelectedItemPosition() == 0
                && isFromDateChanged) {
            long unixDate = getUnixDate(fromDateTextView.getText().toString());
            ArrayList<Expense> dateExpenseList = expenseDataSource.getExpenseByDate(unixDate);

            if (dateExpenseList.size() > 0) {
                emptyReportTextView.setVisibility(View.GONE);
                reportLinearLayout.setVisibility(View.VISIBLE);
                expenseReportAdapter = new ExpenseReportAdapter(this, dateExpenseList);
                expenseReportListView.setAdapter(expenseReportAdapter);
            } else {
                emptyReportTextView.setVisibility(View.VISIBLE);
                reportLinearLayout.setVisibility(View.GONE);
            }
        }
        else if (expenseCategorySpinner.getSelectedItemPosition() == 0) {
            ArrayList<Expense> catExpenseList = (ArrayList<Expense>) expenseDataSource.getAllExpense();
            if (catExpenseList.size() > 0) {
                emptyReportTextView.setVisibility(View.GONE);
                reportLinearLayout.setVisibility(View.VISIBLE);
                expenseReportAdapter = new ExpenseReportAdapter(this, catExpenseList);
                expenseReportListView.setAdapter(expenseReportAdapter);
            } else {
                emptyReportTextView.setVisibility(View.VISIBLE);
                reportLinearLayout.setVisibility(View.GONE);
            }
        }
        else {
            int catIndex = expenseCategorySpinner.getSelectedItemPosition()-1;
            ArrayList<Expense> catExpenseList = expenseDataSource.getCategoryExpense(catIndex);

            expenseReportAdapter = new ExpenseReportAdapter(this, catExpenseList);
            expenseReportListView.setAdapter(expenseReportAdapter);
        }
    }

    private long getUnixDate(String dateStr) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        long unixDate = -1;
        try {
            Date date = formatter.parse(dateStr);
            unixDate = date.getTime();
        } catch (ParseException e) {
            Toast.makeText(this, "Cannot format Date", Toast.LENGTH_SHORT).show();
        }
        return unixDate;
    }
}
