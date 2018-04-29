package com.example.arcomputers.expansemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ExpenseDataSource {

    private ExpenseDatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private Expense expense;

    ExpenseDataSource(Context context){
        databaseHelper = new ExpenseDatabaseHelper(context);
    }

    public void openConnection(){
        database = databaseHelper.getWritableDatabase();
    }
    public void closeConnection(){
        database.close();
    }

    public boolean addExpense(Expense expense){

        openConnection();
        ContentValues values = new ContentValues();
        values.put(ExpenseDatabaseHelper.EXPENSE_COL_NAME,expense.getName());
        values.put(ExpenseDatabaseHelper.EXPENSE_COL_CATEGORY,expense.getCategory());
        values.put(ExpenseDatabaseHelper.EXPENSE_COL_AMOUNT,expense.getAmount());
        values.put(ExpenseDatabaseHelper.EXPENSE_COL_DATE,expense.getUnixDateTime());
        long savedRowID = database.insert(ExpenseDatabaseHelper.EXPENSE_TABLE,null,values);
        closeConnection();
        if (savedRowID>0){
            return true;
        }else {
            return false;
        }

    }

    public List<Expense> getAllExpense(){
        List<Expense> expenseList = new ArrayList<>();
        openConnection();
        Cursor cursor = database.query(ExpenseDatabaseHelper.EXPENSE_TABLE,null,null,null,null,null,null);
        cursor.moveToFirst();
        if(cursor!=null && cursor.getCount()>0){
            for(int i=0; i<cursor.getCount(); i++){
                int id = cursor.getInt(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_NAME));
                int category = cursor.getInt(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_CATEGORY));
                double amount = cursor.getDouble(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_AMOUNT));
                long date = cursor.getLong(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_DATE));
                Expense expense = new Expense(id,name,category,amount,date);
                expenseList.add(expense);
                cursor.moveToNext();
            }
        }
        cursor.close();
        closeConnection();
        return expenseList;
    }

    public ArrayList<Expense> getCategoryExpense(int categoryIndex) {
        ArrayList<Expense> expenseList = new ArrayList<>();
        openConnection();

        String whereClause = ExpenseDatabaseHelper.EXPENSE_COL_CATEGORY+"=?";
        String[] selectionArgs = {String.valueOf(categoryIndex)};

        Cursor cursor = database.query(ExpenseDatabaseHelper.EXPENSE_TABLE,null,whereClause,
                selectionArgs,null,null,null);
        cursor.moveToFirst();
        if(cursor!=null && cursor.getCount()>0){
            for(int i=0; i<cursor.getCount(); i++){
                int id = cursor.getInt(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_NAME));
                int category = cursor.getInt(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_CATEGORY));
                double amount = cursor.getDouble(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_AMOUNT));
                long date = cursor.getLong(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_DATE));
                Expense expense = new Expense(id,name,category,amount,date);
                expenseList.add(expense);
                cursor.moveToNext();
            }
        }
        cursor.close();
        closeConnection();
        return expenseList;
    }

    public ArrayList<Expense> getExpenseByDate(long fromDate, long toDate) {
        ArrayList<Expense> expenseList = new ArrayList<>();
        openConnection();

        String whereClause = ExpenseDatabaseHelper.EXPENSE_COL_DATE
                + ">= ?"
                + " AND " +
                ExpenseDatabaseHelper.EXPENSE_COL_DATE
                + "<= ?";
        String[] selectionArgs = {String.valueOf(fromDate), String.valueOf(toDate)};

        Cursor cursor = database.query(ExpenseDatabaseHelper.EXPENSE_TABLE,null,
                whereClause,
                selectionArgs,null,null,null);
        cursor.moveToFirst();

        // looping through all rows and adding to list
        if(cursor!=null && cursor.getCount()>0){
            for(int i=0; i<cursor.getCount(); i++){
                int id = cursor.getInt(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_NAME));
                int category = cursor.getInt(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_CATEGORY));
                double amount = cursor.getDouble(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_AMOUNT));
                long date = cursor.getLong(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_DATE));
                Expense expense = new Expense(id,name,category,amount,date);
                expenseList.add(expense);
                cursor.moveToNext();
            }
        }

        closeConnection();

        // return expense list between two dates
        return expenseList;
    }

    public ArrayList<Expense> getExpenseByDate(long fromDate) {
        ArrayList<Expense> expenseList = new ArrayList<>();
        openConnection();

        String whereClause = ExpenseDatabaseHelper.EXPENSE_COL_DATE + "> ?";
        String[] selectionArgs = {String.valueOf(fromDate)};

        Cursor cursor = database.query(ExpenseDatabaseHelper.EXPENSE_TABLE,null,
                whereClause,
                selectionArgs,null,null,null);
        cursor.moveToFirst();

        // looping through all rows and adding to list
        if(cursor!=null && cursor.getCount()>0){
            for(int i=0; i<cursor.getCount(); i++){
                int id = cursor.getInt(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_NAME));
                int category = cursor.getInt(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_CATEGORY));
                double amount = cursor.getDouble(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_AMOUNT));
                long date = cursor.getLong(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_DATE));
                Expense expense = new Expense(id,name,category,amount,date);
                expenseList.add(expense);
                cursor.moveToNext();
            }
        }

        closeConnection();

        // return expense list between two dates
        return expenseList;

    }

    public ArrayList<Expense> getExpenseByCategoryAndDate(int catIndex, long unixFromDate,
                                                          long unixToDate) {
        ArrayList<Expense> expenseList = new ArrayList<>();
        openConnection();

        String whereClause = ExpenseDatabaseHelper.EXPENSE_COL_DATE
                + ">= ?"
                + " AND " +
                ExpenseDatabaseHelper.EXPENSE_COL_DATE
                + "<= ?"
                + " AND " +
                ExpenseDatabaseHelper.EXPENSE_COL_CATEGORY
                + "= ?";
        String[] selectionArgs = {String.valueOf(unixFromDate),
                String.valueOf(unixToDate),
                String.valueOf(catIndex)};

        Cursor cursor = database.query(ExpenseDatabaseHelper.EXPENSE_TABLE,null,
                whereClause,
                selectionArgs,null,null,null);
        cursor.moveToFirst();

        // looping through all rows and adding to list
        if(cursor!=null && cursor.getCount()>0){
            for(int i=0; i<cursor.getCount(); i++){
                int id = cursor.getInt(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_NAME));
                int category = cursor.getInt(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_CATEGORY));
                double amount = cursor.getDouble(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_AMOUNT));
                long date = cursor.getLong(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_DATE));
                Expense expense = new Expense(id,name,category,amount,date);
                expenseList.add(expense);
                cursor.moveToNext();
            }
        }

        closeConnection();

        // return expense list between two dates
        return expenseList;
    }

    public ArrayList<Expense> getExpenseByCategoryAndDate(int catIndex, long unixFromDate) {
        ArrayList<Expense> expenseList = new ArrayList<>();
        openConnection();

        String whereClause = ExpenseDatabaseHelper.EXPENSE_COL_DATE
                + ">= ?"
                + " AND " +
                ExpenseDatabaseHelper.EXPENSE_COL_CATEGORY
                + "= ?";
        String[] selectionArgs = {String.valueOf(unixFromDate),
                String.valueOf(catIndex)};

        Cursor cursor = database.query(ExpenseDatabaseHelper.EXPENSE_TABLE,null,
                whereClause,
                selectionArgs,null,null,null);
        cursor.moveToFirst();

        // looping through all rows and adding to list
        if(cursor!=null && cursor.getCount()>0){
            for(int i=0; i<cursor.getCount(); i++){
                int id = cursor.getInt(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_NAME));
                int category = cursor.getInt(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_CATEGORY));
                double amount = cursor.getDouble(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_AMOUNT));
                long date = cursor.getLong(cursor.getColumnIndex(ExpenseDatabaseHelper.EXPENSE_COL_DATE));
                Expense expense = new Expense(id,name,category,amount,date);
                expenseList.add(expense);
                cursor.moveToNext();
            }
        }

        closeConnection();

        // return expense list between two dates
        return expenseList;
    }

    public boolean updateExpense(int id, Expense expense){
        openConnection();
        ContentValues values = new ContentValues();
        String whereClause = ExpenseDatabaseHelper.EXPENSE_COL_ID+"=?";
        String[] whereArgs = {String.valueOf(id)};
        values.put(ExpenseDatabaseHelper.EXPENSE_COL_NAME,expense.getName());
        values.put(ExpenseDatabaseHelper.EXPENSE_COL_CATEGORY,expense.getCategory());
        values.put(ExpenseDatabaseHelper.EXPENSE_COL_AMOUNT,expense.getAmount());
        values.put(ExpenseDatabaseHelper.EXPENSE_COL_DATE,expense.getUnixDateTime());
        int deletedRowId = database.update(ExpenseDatabaseHelper.EXPENSE_TABLE,values,whereClause,whereArgs);
        closeConnection();
        if (deletedRowId>0){
            return true;
        }else {
            return false;
        }
    }

    public boolean deleteExpense(int id){
        openConnection();
        String whereClause = ExpenseDatabaseHelper.EXPENSE_COL_ID+"=?";
        String[] whereArgs = {String.valueOf(id)};
        int deletedRowId = database.delete(ExpenseDatabaseHelper.EXPENSE_TABLE,whereClause,whereArgs);
        closeConnection();
        if (deletedRowId>0){
            return true;
        }else {
            return false;
        }
    }
}
