package com.expenselog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class is responsible for parsing Strings and returning Dates.
 *
 * Created by Dániel Jávorszky on 23/10/15.
 */
public class DateUtil {


	// TODO this is not working as expected. :-(
	private static ArrayList<String> knownFormats;
	private static ArrayList<Character> separators;
	private static ArrayList<SimpleDateFormat> dateFormats;

	private static final char SEPARATOR_SLASH = '/';
	private static final char SEPARATOR_DASH = '-';

	private static final String DF_NORMAL = "d_M_y";
	private static final String DF_REVERSED = "y_M_d";

	private static final String DF_NORMAL_LONG_MONTH = "d_MMM_y";
	private static final String DF_REVERSED_LONG_MONTH = "y_MMM_d";

	private static final String DF_HRS_ADDON = " - HH:mm";

	/**
	 * The static initialization block does the following:
	 * 1 - Instantiates the ArrayLists that will be used to store the separators, Strings, and DateFormats
	 * 2 - Adds the above final variables to their respective ArrayLists
	 * 3 - Replaces the wildcard ("_") in the date formats with each separator, and adds them to the
	 *     dateFormats ArrayList as SimpleDateFormats
	 * 4 - Does the same again, but adds the "DF_HRS_ADDON" to it.
	 */

	static {
		separators = new ArrayList<Character>();
		separators.add(SEPARATOR_SLASH);
		separators.add(SEPARATOR_DASH);

		knownFormats = new ArrayList<String>();
		knownFormats.add(DF_NORMAL);
		knownFormats.add(DF_REVERSED);
		knownFormats.add(DF_NORMAL_LONG_MONTH);
		knownFormats.add(DF_REVERSED_LONG_MONTH);

		dateFormats = new ArrayList<SimpleDateFormat>();

		for (char separator : separators) {
			for (String format : knownFormats) {
				String finalFormat = format.replace('_', separator);
				dateFormats.add(new SimpleDateFormat(finalFormat.concat(DF_HRS_ADDON)));
				dateFormats.add(new SimpleDateFormat(finalFormat));
			}
		}
	}

	/**
	 * This function will try to parse a string representation of a date based on multiple patterns and return the
	 * corresponding Date object if successful, or null otherwise.
	 *
	 * The only issue with this is that it can (and will) mix up 04/05/06, as it can produce both 2004/May/06 and
	 * 2006/May/04.
	 *
	 * @param dateString A string representation of a date.
	 * @return Date object based on the provided String, or null if parse wasn't successful.
	 */
	public static Date parseDate(String dateString) {
		if (dateString == null) {
			return new Date();
		}

		for (SimpleDateFormat format : dateFormats) {
			try {
				Date date = format.parse(dateString);

				// 946684800 = January 1, 2000
				if (date.getTime() < 946684800) {
					System.out.println("Date would be before January 1, 2000. Skipping.");
					continue;
				}

				return date;
			}
			catch (ParseException pe) {
				// Fail gracefully.
			}
		}
		System.out.println("Could not parse date.");

		return null;
	}
}
