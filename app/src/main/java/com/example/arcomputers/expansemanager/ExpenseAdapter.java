package com.example.arcomputers.expansemanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mobile App Develop on 4/27/2018.
 */

public class ExpenseAdapter extends ArrayAdapter {

    private List<Expense> expenseList = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    SimpleDateFormat format  = new SimpleDateFormat("dd/MM/yyyy");


    public ExpenseAdapter(@NonNull Context context,List<Expense> expenseList) {
        super(context, R.layout.custome_expanse_layout, expenseList);
        this.context = context;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null)
        {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custome_expanse_layout,parent,false);
        }

        TextView expanseNameTV= convertView.findViewById(R.id.expanseNameTV);
        TextView expanseDateTV = convertView.findViewById(R.id.dateTV);
        TextView expanseAmountTV= convertView.findViewById(R.id.expanseAmountTv);

        Expense currentExpanse = expenseList.get(position);

        expanseNameTV.setText(currentExpanse.getName());
        long unixDateTime = currentExpanse.getUnixDateTime();
        String date = format.format(unixDateTime);
        expanseDateTV.setText(date);
        expanseAmountTV.setText("$ "+String.valueOf(currentExpanse.getAmount()));

        return convertView;
    }
}
