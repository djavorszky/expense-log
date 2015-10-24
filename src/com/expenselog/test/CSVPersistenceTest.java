package com.expenselog.test;

import com.expenselog.Transaction;
import com.expenselog.persistence.CSVPersistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by DánielJávorszky on 17/10/15.
 */
public class CSVPersistenceTest {

	@Before
	public void setUp() throws Exception {
		CSVPersistence.deleteProfile("MyTestProfile");
		CSVPersistence.deleteProfile("MyOtherTestProfile");
	}

	@After
	public void tearDown() throws Exception {
		CSVPersistence.deleteProfile("MyTestProfile");
		CSVPersistence.deleteProfile("MyOtherTestProfile");
	}

	@Test
	public void testSaveTransaction() throws Exception {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("dd/MMM/yyyy - HH:mm");

		Transaction transaction = new Transaction(date, 100.0, "MyCategory", "MyDescription", "MyTestProfile", 1);

		String transactionString = transaction.toString();

		String controlMessage = df.format(date) + ";100.0;MyCategory;MyDescription;1";

		CSVPersistence.saveTransaction(transaction);
		ArrayList<String> savedEntries = CSVPersistence.readFile("MyTestProfile");

		assertEquals(savedEntries.get(0), controlMessage);
	}

	// TODO Write this.
	@Test
	public void testReadTransaction() throws Exception {
		Date date = new Date();

		Transaction transaction = new Transaction(date, 100.0, "MyCategory", "MyDescription", "MyTestProfile", 1);
		Transaction transaction2 = new Transaction(date, 150.0, "MyCategory", "MyDescription", "MyTestProfile", 2);
		Transaction transaction3 = new Transaction(date, 200.0, "MyCategory", "MyDescription", "MyTestProfile", 3);

		CSVPersistence.saveTransaction(transaction);
		CSVPersistence.saveTransaction(transaction2);
		CSVPersistence.saveTransaction(transaction3);

		Transaction readTransaction = CSVPersistence.readTransaction(1, "MyTestProfile");
		Transaction readTransaction2 = CSVPersistence.readTransaction(2, "MyTestProfile");
		Transaction readTransaction3 = CSVPersistence.readTransaction(3, "MyTestProfile");

		assertEquals(transaction.getAmount(), readTransaction.getAmount(), 0);
		assertEquals(transaction2.getAmount(), readTransaction2.getAmount(), 0);
		assertEquals(transaction3.getAmount(), readTransaction3.getAmount(), 0);
	}

	@Test
	public void testUpdateTransaction() throws Exception {
		Date date = new Date();

		Transaction transaction = new Transaction(date, 100.0, "MyCategory", "MyDescription", "MyTestProfile", 1);
		Transaction transaction2 = new Transaction(date, 150.0, "MyCategory", "MyDescription", "MyTestProfile", 2);
		Transaction transaction3 = new Transaction(date, 200.0, "MyCategory", "MyDescription", "MyTestProfile", 3);

		CSVPersistence.saveTransaction(transaction);
		CSVPersistence.saveTransaction(transaction2);
		CSVPersistence.saveTransaction(transaction3);

		Transaction transactionToUpdate = new Transaction(date, 300.0, "MyCategory", "MyDescription", "MyTestProfile");

		CSVPersistence.updateTransaction(2, transactionToUpdate);

		Transaction updatedTransaction = CSVPersistence.readTransaction(2, "MyTestProfile");
		assertEquals(updatedTransaction.getAmount(), 300.0, 0);
	}

	// TODO Write this.
	@Test
	public void testDeleteTransaction() throws Exception {
		Date date = new Date();

		Transaction transaction = new Transaction(date, 100.0, "MyCategory", "MyDescription", "MyTestProfile", 1);
		Transaction transaction2 = new Transaction(date, 150.0, "MyCategory", "MyDescription", "MyTestProfile", 2);
		Transaction transaction3 = new Transaction(date, 200.0, "MyCategory", "MyDescription", "MyTestProfile", 3);

		CSVPersistence.saveTransaction(transaction);
		CSVPersistence.saveTransaction(transaction2);
		CSVPersistence.saveTransaction(transaction3);

		ArrayList<String> readFile = CSVPersistence.readFile("MyTestProfile");
		assertEquals(readFile.size(), 3);

		CSVPersistence.deleteTransaction(2, "MyTestProfile");

		ArrayList<String> fileReadAfterDelete = CSVPersistence.readFile("MyTestProfile");
		assertEquals(fileReadAfterDelete.size(), 2);

	}

	@Test
	public void testReadFile() throws Exception {
		// Test to see if it works with a not-yet existing file.
		ArrayList<String> emptyFile = CSVPersistence.readFile("MyTestProfile");

		File file = new File("data/MyTestProfile.csv");

		assertEquals(file.exists(), true);
		assertEquals(emptyFile.isEmpty(), true);

		// Test to see if after adding the transaction, the file is re-read.
		Transaction transaction = new Transaction(new Date(), 100.0, "MyCategory", "MyDescription", "MyTestProfile");
		CSVPersistence.saveTransaction(transaction);

		ArrayList<String> fileWithOneRecord = CSVPersistence.readFile("MyTestProfile");

		assertEquals(fileWithOneRecord.size(), 1);

		CSVPersistence.deleteProfile("MyTestProfile");

		// Test to see if a new, empty file is created.

		ArrayList<String> newEmptyFile = CSVPersistence.readFile("MyOtherTestProfile");
		assertEquals(newEmptyFile.isEmpty(), true);
	}

	@Test
	public void testDeleteProfile() throws Exception {
		ArrayList<String> emptyFile = CSVPersistence.readFile("MyTestProfile");

		// Test if file is created
		File file = new File("data/MyTestProfile.csv");

		assertEquals(file.exists(), true);

		// Test if file gets deleted
		CSVPersistence.deleteProfile("MyTestProfile");

		assertEquals(file.exists(), false);
	}
}