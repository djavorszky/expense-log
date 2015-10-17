package com.expenselog;

import com.sun.org.apache.xml.internal.utils.StringBufferPool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {

	public Transaction (Date date, double amount, String categoryName, String description, String profileName) {

		this.date = date;
		this.amount = amount;
		this.categoryName = categoryName;
		this.description = description;
		this.profileName = profileName;

		// TODO implement ids.
		this.transactionId = 1;
	}

	public double getAmount() {
		return amount;
	}

	public Date getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public boolean isExpense() {
		return (amount < 0);
	}

	public String getProfileName() {
		return profileName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	@Override
	public String toString() {
		return toString(false);
	}

	public String toString(boolean newLine) {
		StringBuilder sb = new StringBuilder(10);
		DateFormat df = new SimpleDateFormat("dd/MMM/yyyy - HH:mm");
		sb.append(df.format(date));
		sb.append(";");
		sb.append(amount);
		sb.append(";");
		sb.append(categoryName);
		sb.append(";");
		sb.append(description);
		sb.append(";");
		sb.append(transactionId);
		if (newLine) {
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	public static Transaction toTransaction(String representation) {
		String[] array = representation.split(";");

		return null;

	}

	//TODO Implement Comparator

	private final Date date;
	private final double amount;
	private final String categoryName;
	private final String description;
	private final String profileName;
	private final int transactionId;
}
