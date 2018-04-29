package com.example.arcomputers.expansemanager;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExpenseReportAdapter extends ArrayAdapter {

    private Context context;
    private List<Expense> expenseList = new ArrayList<>();
    private LayoutInflater inflater;

    String[] categories = {"Grocery",
        "Utility",
        "Personal",
        "Housing",
        "Health Care",
        "Entertainment",
        "Transport",
        "Others"
    };

    public ExpenseReportAdapter(@NonNull Context context,List<Expense> expenseList) {
        super(context, R.layout.report_listview, expenseList);
        this.context = context;
        this.expenseList = expenseList;
    }

    @Override
    public int getCount() {
        return expenseList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.report_listview,parent,false);
        }

        TextView nameTextView = convertView.findViewById(R.id.nameReportTextView);
        TextView categoryTextView = convertView.findViewById(R.id.categoryReportTextView);
        TextView amountTextView = convertView.findViewById(R.id.amountReportTextView);
        TextView dateTextView = convertView.findViewById(R.id.dateReportTextView);
        nameTextView.setText(expenseList.get(position).getName());
        categoryTextView.setText(categories[expenseList.get(position).getCategory()]);
        amountTextView.setText(expenseList.get(position).getAmount()+"");
        dateTextView.setText(String.valueOf(expenseList.get(position).getDateInString()));
        return convertView;
    }
}
