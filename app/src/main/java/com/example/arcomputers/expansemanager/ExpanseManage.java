package com.example.arcomputers.expansemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ExpanseManage extends AppCompatActivity {

    Button reportPageButton;
    ExpenseDataSource expenseDataSource = new ExpenseDataSource(ExpanseManage.this);
    List<Expense> expenses = new ArrayList<>();
    TextView emptyExpense;
    final static String DATAPASSKEY = "aExpense";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expanse_manage);

        reportPageButton = findViewById(R.id.reportButton);

        ListView expanseLV = findViewById(R.id.showExpanseLV);
        emptyExpense = findViewById(R.id.emptyExpenseTV);

        expenses= expenseDataSource.getAllExpense();

        if (expenses.size()==0){
            emptyExpense.setVisibility(View.VISIBLE);
        }else if(expenses.size()>0){
            ExpenseAdapter expenseAdapter = new ExpenseAdapter(ExpanseManage.this,expenses);
            expanseLV.setAdapter(expenseAdapter);
        }

        expanseLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expense expense = expenses.get(position);
                Intent intent = new Intent(ExpanseManage.this,ViewExpanse.class);
                intent.putExtra(DATAPASSKEY,expense);
                startActivity(intent);
            }
        });

        reportPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpanseManage.this, ReportGeneratedActivity.class);
                startActivity(intent);
            }
        });

    }

    public void addNewExpanse(View view) {
        Intent intent = new Intent(ExpanseManage.this,ExpanseAdd.class);
        startActivity(intent);
        // Toast.makeText(this, "going to add new expanse page", Toast.LENGTH_SHORT).show();

    }

//    public void viewExpanse(View view) {
//        Intent intent = new Intent(ExpanseManage.this,ViewExpanse.class);
//        startActivity(intent);
//        // Toast.makeText(this, "going to view expanse page", Toast.LENGTH_SHORT).show();
//    }
}
