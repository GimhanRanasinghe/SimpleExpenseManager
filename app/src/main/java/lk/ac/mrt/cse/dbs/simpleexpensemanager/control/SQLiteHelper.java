package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteHelper(Context context) {
        super(context, "190493U", null, 1);
    }
    private static final String COL_ACCOUNT_NO = "accountNo";
    private static final String COL_BANK_NAME = "bankName";
    private static final String COL_ACCOUNT_HOLDER_NAME = "accountHolderName";
    private static final String COL_ACCOUNT_BALANCE = "accountBalance";
    private static final String ACCOUNT_TABLE = "accountDetails";

    private static final String COL_TRANSACTION_ID = "transactionId";
    private static final String COL_TRANSACTION_ACCOUNT_NO = "accountNo";
    private static final String COL_TRANSACTION_DATE = "transactionDate";
    private static final String COL_TRANSACTION_TYPE = "transactionType";
    private static final String COL_TRANSACTION_AMOUNT= "amount";
    private static final String TRANSACTION_TABLE = "transactionDetails";

    public static final String create_account_table_query = "CREATE TABLE "+ACCOUNT_TABLE+" ("+COL_ACCOUNT_NO+" TEXT PRIMARY KEY NOT NULL, "+COL_BANK_NAME+" TEXT, "+
            COL_ACCOUNT_HOLDER_NAME+" TEXT, "+COL_ACCOUNT_BALANCE+" TEXT)";;

    public static final String create_transaction_table_query = "CREATE TABLE "+TRANSACTION_TABLE+" ("+COL_TRANSACTION_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            COL_TRANSACTION_DATE + " TIMESTAMP, "+COL_TRANSACTION_ACCOUNT_NO+" TEXT, " +COL_TRANSACTION_TYPE+" TEXT, "+COL_TRANSACTION_AMOUNT+" TEXT," +
            " FOREIGN KEY" + " (" + COL_TRANSACTION_ACCOUNT_NO + ")" + " REFERENCES " + ACCOUNT_TABLE + "(" + COL_TRANSACTION_ACCOUNT_NO + ")" + ")";;


    public static final String drop_account_table_query =
            "DROP TABLE IF EXISTS " + ACCOUNT_TABLE;
    public static final String drop_transaction_table_query =
            "DROP TABLE IF EXISTS " + TRANSACTION_TABLE;



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        sqLiteDatabase.execSQL(create_account_table_query);
        sqLiteDatabase.execSQL(create_transaction_table_query);
        System.out.println("hi");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(drop_account_table_query);
        sqLiteDatabase.execSQL(create_transaction_table_query);
        onCreate(sqLiteDatabase);
    }

    public List<String> getAccountNumbersList() {
        ArrayList<String> accountList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(ACCOUNT_TABLE, new String[]{COL_ACCOUNT_NO}, null, null, null, null, null, null);


        while (cursor.moveToNext()) {
            String accountNO = cursor.getString(cursor.getColumnIndexOrThrow(COL_ACCOUNT_NO));
            accountList.add(accountNO);
        }

        cursor.close();
        sqLiteDatabase.close();
        return accountList;
    }


    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(ACCOUNT_TABLE, null, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String accountNO = cursor.getString(cursor.getColumnIndexOrThrow(COL_ACCOUNT_NO));
                String accountHolderName = cursor.getString(cursor.getColumnIndexOrThrow(COL_ACCOUNT_HOLDER_NAME));
                String bankName = cursor.getString(cursor.getColumnIndexOrThrow(COL_BANK_NAME));
                String balance = cursor.getString(cursor.getColumnIndexOrThrow(COL_ACCOUNT_BALANCE));

                Account newAccount = new Account(accountNO, accountHolderName, bankName, Double.parseDouble(balance));
                accountList.add(newAccount);

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return accountList;
    }


    public Account getAccount(String accountNo) throws InvalidAccountException {
        String selection = COL_ACCOUNT_NO + " = ?";
        String[] selectionArgs = {accountNo};

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(ACCOUNT_TABLE, null, selection, selectionArgs, null, null, null, null);

        if (cursor.moveToFirst()) {
            String accountNO = cursor.getString(cursor.getColumnIndexOrThrow(COL_ACCOUNT_NO));
            String accountHolderName = cursor.getString(cursor.getColumnIndexOrThrow(COL_ACCOUNT_HOLDER_NAME));
            String bankName = cursor.getString(cursor.getColumnIndexOrThrow(COL_BANK_NAME));
            String balance = cursor.getString(cursor.getColumnIndexOrThrow(COL_ACCOUNT_BALANCE));
            cursor.close();
            sqLiteDatabase.close();
            return new Account(accountNO, accountHolderName, bankName, Double.parseDouble(balance));
        } else {
            cursor.close();
            sqLiteDatabase.close();
            return null;
        }

    }

    public Boolean isValidAccount(String accountNo) {

        String selection = COL_ACCOUNT_NO + " = ?"; //////
        String[] selectionArgs = {accountNo};

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(ACCOUNT_TABLE, null, selection, selectionArgs, null, null, null, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            sqLiteDatabase.close();
            return true;
        } else {
            cursor.close();
            sqLiteDatabase.close();
            return false;
        }
    }


    public void addAccount(Account account) {
        try{
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(COL_ACCOUNT_NO, account.getAccountNo());
            cv.put(COL_ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
            cv.put(COL_BANK_NAME, account.getBankName());
            cv.put(COL_ACCOUNT_BALANCE, account.getBalance());

            sqLiteDatabase.insert(ACCOUNT_TABLE, null, cv);
        }catch (Exception e){

        }

    }


    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(ACCOUNT_TABLE, COL_ACCOUNT_NO + " = ? ",new String[]{accountNo});

    }


    public void updateBalance(Account account) throws InvalidAccountException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ACCOUNT_BALANCE, account.getBalance());

        sqLiteDatabase.update(ACCOUNT_TABLE, values, COL_ACCOUNT_NO + " = ?", new String[]{account.getAccountNo()});
    }

    //TRANSACTIONS

    public void logTransaction(Transaction transaction) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_TRANSACTION_DATE, transaction.getDate().getTime());
        values.put(COL_TRANSACTION_ACCOUNT_NO, transaction.getAccountNo());
        values.put(COL_TRANSACTION_TYPE, transaction.getExpenseType().name());
        values.put(COL_TRANSACTION_AMOUNT, transaction.getAmount());
        sqLiteDatabase.insert(TRANSACTION_TABLE, null, values);
    }


    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionLogsList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TRANSACTION_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);

        while (cursor.moveToNext()) {

            long time = cursor.getLong(cursor.getColumnIndexOrThrow(COL_TRANSACTION_DATE));
            String accountNO = cursor.getString(cursor.getColumnIndexOrThrow(COL_TRANSACTION_ACCOUNT_NO));
            String expenseType = cursor.getString(cursor.getColumnIndexOrThrow(COL_TRANSACTION_TYPE));
            String amount = cursor.getString(cursor.getColumnIndexOrThrow(COL_TRANSACTION_AMOUNT));

            Transaction newtransaction = new Transaction(new Date(time), accountNO, ExpenseType.valueOf(expenseType), Double.parseDouble(amount));
            transactionLogsList.add(newtransaction);

        }
        cursor.close();
        sqLiteDatabase.close();
        return transactionLogsList;
    }


    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(TRANSACTION_TABLE, null, null, null, null, null, null, Integer.toString(limit));
        List<Transaction> transactions = new ArrayList<>();
        while (cursor.moveToNext()) {
            long time = cursor.getLong(
                    cursor.getColumnIndexOrThrow(COL_TRANSACTION_DATE));
            String accountNo = cursor.getString(
                    cursor.getColumnIndexOrThrow(COL_TRANSACTION_ACCOUNT_NO));
            String expenseType = cursor.getString(
                    cursor.getColumnIndexOrThrow(COL_TRANSACTION_TYPE));
            String amount = cursor.getString(
                    cursor.getColumnIndexOrThrow(COL_TRANSACTION_AMOUNT));
            transactions.add(new Transaction(new Date(time), accountNo, ExpenseType.valueOf(expenseType), Double.parseDouble(amount)));
        }
        cursor.close();
        sqLiteDatabase.close();
        return transactions;
    }
}
