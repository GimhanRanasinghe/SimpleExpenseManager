/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.SQLiteHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest {

    private static ExpenseManager expenseManager;
    private static SQLiteHelper sqLiteHelper;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        sqLiteHelper = new SQLiteHelper(context);
        expenseManager = new PersistentExpenseManager(sqLiteHelper);
        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(sqLiteHelper);
        expenseManager.setTransactionsDAO(persistentTransactionDAO);
    }

    @Test
    public void testAddAccount() {
        expenseManager.addAccount("100", "sampath", "g", 10000.0);
        List<String> accNumbers = expenseManager.getAccountNumbersList();
        assertTrue(accNumbers.contains("100"));
    }

    @Test
    public void testLogTransactionExpense() throws InvalidAccountException {
        try {
            int numberOfLogsBegin = expenseManager.getTransactionsDAO().getAllTransactionLogs().size();
            expenseManager.updateAccountBalance("100", 12, 10, 2022, ExpenseType.valueOf("EXPENSE"), "5");
            int numberOfLogsEnd = expenseManager.getTransactionsDAO().getAllTransactionLogs().size();
            assertEquals(numberOfLogsBegin + 1, numberOfLogsEnd);
        } catch (InvalidAccountException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testLogTransactionIncome() throws InvalidAccountException {
        try {
            int numberOfLogsBegin = expenseManager.getTransactionsDAO().getAllTransactionLogs().size();
            expenseManager.updateAccountBalance("100", 12, 10, 2022, ExpenseType.valueOf("INCOME"), "5");
            int numberOfLogsEnd = expenseManager.getTransactionsDAO().getAllTransactionLogs().size();
            assertEquals(numberOfLogsBegin + 1, numberOfLogsEnd);
        } catch (InvalidAccountException e) {
            e.printStackTrace();
        }


    }


}