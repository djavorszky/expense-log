package com.expenselog.test;

import com.expenselog.DateUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Dániel Jávorszky on 23/10/15.
 */
public class DateUtilTest {

	@Test
	public void testParseDate() throws Exception {

		ArrayList<Character> separators = new ArrayList<Character>();
		separators.add('/');
		separators.add('-');

		String hrsAddon = " - 18:23";

		// Note: The "_" wildcard will be replaced to any of the above two.
		// Note: The below formats will be tested both with and without the above "hrsAddon" String.
		ArrayList<String> dateStrings = new ArrayList<String>();
		// DF_NORMAL - 2009 February 1;
		dateStrings.add("01_02_09");
		dateStrings.add("01_02_2009");

		// DF_REVERSED - 2015 April 17
		dateStrings.add("15_4_17");
		dateStrings.add("15_04_17");
		dateStrings.add("2015_04_17");

		// DF_NORMAL_LONG_MONTH - 2013 July 7
		dateStrings.add("7_Jul_13");
		dateStrings.add("07_Jul_13");
		dateStrings.add("07_Jul_2013");

		// DF_REVERSED_LONG_MONTH - 2012 December 6
		dateStrings.add("12_Dec_6");
		dateStrings.add("12_Dec_06");
		dateStrings.add("2012_Dec_06");

		for (char separator : separators) {
			for (String format : dateStrings) {
				String finalFormat = format.replace('_', separator);

				Date date = DateUtil.parseDate(finalFormat);
				assertNotNull(date);

				Date date2 = DateUtil.parseDate(finalFormat.concat(hrsAddon));
				assertNotNull(date2);
			}
		}
	}
}