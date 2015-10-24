package com.expenselog.persistence;


import com.expenselog.Transaction;
import com.expenselog.exception.NoSuchTransactionException;
import sun.swing.FilePane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class handles reading from and writing to a CSV file. The name of the file is always the name of the profile.
 * In order to minimize file operations, the class also saves read values in an ArrayList, as well as in a HashMap.
 * The first one is primarily used for iterating through all of the elements, whereas the latter one is for identifying
 * transactions as per their transactionIds.
 *
 * Created by Dániel Jávorszky on 17/10/15.
 */

public class CSVPersistence {

	private static String currentProfileName;
	private static ArrayList<String> fileArray;
	private static HashMap<Integer, Integer> transactionMap;
	private static boolean needUpdate = false;
	private static int currentMaxId = 0;

	/**
	 * This method saves an individual Transaction to the CSV file, and sets the "needUpdate" boolean flag to true.
	 * @param transaction an object which should be saved
	 * @return true if save is successful, false otherwise.
	 */

	public static boolean saveTransaction(Transaction transaction) {

		if (fileArray == null || !currentProfileName.equals(transaction.getProfileName()) || needUpdate) {
			readFile(transaction.getProfileName());
		}

		try {
			Files.write(Paths.get("data/" + transaction.getProfileName() + ".csv"),
					transaction.toString(true).getBytes(), StandardOpenOption.APPEND);
			needUpdate = true;
			return true;
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return false;
	}

	// TODO Write this.
	public static Transaction readTransaction(int id, String profileName) {
		readFile(profileName);

		int transactionLocation = transactionMap.get(id);
		String transactionString = fileArray.get(transactionLocation);
		return Transaction.toTransaction(transactionString, profileName);
	}

	public static boolean updateTransaction(int id, Transaction transaction) throws NoSuchTransactionException {
		readFile(transaction.getProfileName());

		if (!transactionMap.containsKey(id)) {
			throw new NoSuchTransactionException();
		}

		int location = transactionMap.get(id);
		fileArray.add(location, transaction.toString());

		_updateProfile(transaction.getProfileName());

		return true;
	}

	public static boolean deleteTransaction(int id, String profileName) throws NoSuchTransactionException {
		readFile(profileName);

		if (!transactionMap.containsKey(id)) {
			throw new NoSuchTransactionException();
		}

		int location = transactionMap.get(id);
		fileArray.remove(location);

		File originalFile = new File("data/" + profileName + ".csv");
		File tempFile = new File("data/" + profileName + ".csv.tmp");


		try {
			tempFile.createNewFile();

			BufferedReader reader = new BufferedReader(new FileReader(originalFile));
			String line = null;

			int index = 0;
			while ((line = reader.readLine()) != null) {
				if (index++ == location) {
					continue;
				}

				Files.write(tempFile.toPath(), line.concat(System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
			}

			originalFile.delete();
			Files.copy(tempFile.toPath(), originalFile.toPath());
			tempFile.delete();

			readFile(profileName);

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * This function is responsible for reading the profile's CSV file and initializing and popoulating the
	 * ArrayList and HashMap.
	 *
	 * If the file doesn't exist, then it creates a new one and sets the ArrayList and HashMap objects to be new, empty
	 * objects.
	 *
	 * The file's name will be the value of the profileName variable.
	 *
	 * If a profile's file has been read already, the method will return its ArrayList already in memory, unless the
	 * needUpdate variable is set to true, in which case it will re-read and update the internal variables.
	 *
	 * @param profileName The name of the profile, which is also the name for the file.
	 * @return returns an ArrayList containing the file, one line per index.
	 */
	public static ArrayList<String> readFile(String profileName) {
		if (profileName.equals(currentProfileName) && !needUpdate) {
			return fileArray;
		}

		transactionMap = new HashMap<Integer, Integer>();
		currentProfileName = profileName;
		fileArray = new ArrayList<String>();

		File file = new File("data/" + profileName + ".csv");

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			else {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = null;
				while ((line = reader.readLine()) != null) {
					fileArray.add(line);

					String[] lineArray = line.split(";");
					int currentId = Integer.valueOf(lineArray[4]);
					transactionMap.put(currentId, fileArray.size() - 1);

					if (currentId > currentMaxId) {
						currentMaxId = currentId;
					}
				}
				reader.close();
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return fileArray;
	}

	/**
	 * Delete a profile and unset all in-memory objects.
	 * @param profileName The name of the profile that should be deleted.
	 * @return True if successful, false if error.
	 */
	public static boolean deleteProfile(String profileName) {
		try {
			Files.deleteIfExists(Paths.get("data/" + profileName + ".csv"));
			currentProfileName = null;
			fileArray = null;
			transactionMap = null;
			currentMaxId = 0;
			return true;
		}
		catch (IOException ioe) {
			System.out.println("Couldn't delete file for some reason.");
			return false;
		}
	}

	/**
	 * Returns the next Id for a transaction object. If the corresponding .csv file has not been read yet, reads it.
	 * @param profileName Name of the profile.
	 * @return TransactionId to be used for transactions.
	 */
	public static synchronized int increment(String profileName) {
		if (currentMaxId == 0) {
			readFile(profileName);
		}

		return ++currentMaxId;
	}


	private static void _updateProfile(String profileName) {
		File originalFile = new File("data/" + profileName + ".csv");

		File backupFile = new File("data/" + profileName + ".csv.bak");

		try {
			if (backupFile.exists()) {
				Files.delete(backupFile.toPath());
			}
			Files.copy(originalFile.toPath(), backupFile.toPath());
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		ArrayList<String> localFileArray = new ArrayList<String>();
		localFileArray.addAll(fileArray);

		deleteProfile(profileName);

		try {
			originalFile.createNewFile();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		for (String line : localFileArray) {
			saveTransaction(Transaction.toTransaction(line, profileName));
		}

		try {
			Files.delete(backupFile.toPath());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
