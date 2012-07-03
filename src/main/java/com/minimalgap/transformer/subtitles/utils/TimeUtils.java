/**
 * Copyright (c) 2012, Minimalgap

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 **/

package com.minimalgap.transformer.subtitles.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class TimeUtils {

	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	public static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String STD_DATE_FORMAT = "dd/MM/yyyy";

	// -------- Time utilities --------

	public static String getCurrentTimeFormatted() {
		SimpleDateFormat format = new SimpleDateFormat(TimeUtils.DATE_FORMAT);
		return format.format(new Date());
	}

	public static String convertItalianToEnglishDate(String italianDate) {
		String[] englishDateVett = italianDate.split("-");
		if (englishDateVett.length == 3) {
			return englishDateVett[2] + "-" + englishDateVett[1] + "-" + englishDateVett[0];
		}
		return null;
	}

	public static String getCurrentShortData() {
		SimpleDateFormat format = new SimpleDateFormat(TimeUtils.SHORT_DATE_FORMAT);
		format.setLenient(false);
		return format.format(new Date());
	}

	public static XMLGregorianCalendar getCurrentTime() throws DatatypeConfigurationException {
		Calendar now = Calendar.getInstance();
		return toXMLGregorianCalendar(now);
	}

	public static XMLGregorianCalendar toXMLGregorianCalendar(Calendar calendar) throws DatatypeConfigurationException {
		XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		xmlCal.setDay(calendar.get(Calendar.DAY_OF_MONTH));

		xmlCal.setMonth(calendar.get(Calendar.MONTH) + 1);
		xmlCal.setYear(calendar.get(Calendar.YEAR));
		xmlCal.setTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));

		TimeZone tz = calendar.getTimeZone();
		int i = tz.getRawOffset();
		xmlCal.setTimezone(i / (60 * 1000));

		return xmlCal;
	}

	public static XMLGregorianCalendar toXMLGregorianCalendar(String time) throws DatatypeConfigurationException, ParseException {
		Date date = TimeUtils.toDate(time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return TimeUtils.toXMLGregorianCalendar(cal);
	}

	public static String toString(XMLGregorianCalendar time) throws DatatypeConfigurationException {
		SimpleDateFormat format = new SimpleDateFormat(TimeUtils.DATE_FORMAT);
		return format.format(time.toGregorianCalendar().getTime());
	}

	public static String toString(Date date) throws DatatypeConfigurationException {
		SimpleDateFormat format = new SimpleDateFormat(TimeUtils.DATE_FORMAT);
		return format.format(date);
	}

	public static String toShortString(Date date) throws DatatypeConfigurationException {
		SimpleDateFormat format = new SimpleDateFormat(TimeUtils.SHORT_DATE_FORMAT);
		return format.format(date);
	}

	public static String toStdString(Date date) throws DatatypeConfigurationException {
		SimpleDateFormat format = new SimpleDateFormat(TimeUtils.STD_DATE_FORMAT);
		return format.format(date);
	}

	public static String normalizeShortDate(String year, String month, String day) throws ParseException {
		for (int i = year.length(); i < 4; i++) {
			year = "0" + year;
		}
		if (month.length() < 2)
			month = "0" + month;
		if (day.length() < 2)
			day = "0" + day;

		return year + "-" + month + "-" + day;
	}

	/**
	 * 
	 * @param date
	 *            yyyy-MM-dd
	 * @return
	 */
	public static String getYear(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy");
		df.setLenient(false);
		return df.format(date);
	}

	/**
	 * 
	 * @param date
	 *            yyyy-MM-dd
	 * @return M if < 10 else MM
	 */
	public static String getMonth(Date date) {
		DateFormat df = new SimpleDateFormat("MM");
		df.setLenient(false);
		String month = df.format(date);
		if (month.startsWith("0"))
			return month.substring(1);
		return month;
	}

	/**
	 * 
	 * @param date
	 *            yyyy-MM-dd
	 * @return d if < 10 else dd
	 */
	public static String getDay(Date date) {
		DateFormat df = new SimpleDateFormat("dd");
		df.setLenient(false);
		String day = df.format(date);
		if (day.startsWith("0"))
			return day.substring(1);
		return day;
	}

	public static Date toShortDate(String date) throws ParseException {
		DateFormat df = new SimpleDateFormat(TimeUtils.SHORT_DATE_FORMAT);
		df.setLenient(false);
		return df.parse(date);
	}

	public static Date toShortDate(String year, String month, String day) throws ParseException {
		DateFormat df = new SimpleDateFormat(TimeUtils.SHORT_DATE_FORMAT);
		df.setLenient(false);
		return df.parse(TimeUtils.normalizeShortDate(year, month, day));
	}

	public static Date toDate(String time) throws ParseException {
		DateFormat df = new SimpleDateFormat(DATE_FORMAT);
		return df.parse(time);
	}

	/* Convert millisecond in hh:mm:ss */
	public static String convertSecondsInTime(long time) {
		String seconds = Integer.toString((int) (time % 60));
		String minutes = Integer.toString((int) ((time % 3600) / 60));
		String hours = Integer.toString((int) (time / 3600));
		for (int i = 0; i < 2; i++) {
			if (seconds.length() < 2) {
				seconds = "0" + seconds;
			}
			if (minutes.length() < 2) {
				minutes = "0" + minutes;
			}
			if (hours.length() < 2) {
				hours = "0" + hours;
			}
		}
		return hours + ":" + minutes + ":" + seconds;
	}

	/* Convert millisecond in hh:mm:ss */
	public static String convertMillisecondInTime(Integer millisecond) {
		long timeMillis = millisecond;
		return TimeUtils.convertSecondsInTime(timeMillis / 1000);
	}

	/* Convert us in hh:mm:ss */
	public static String convertMicrosecondInTime(Integer millisecond) {
		long timeMillis = millisecond;
		return TimeUtils.convertSecondsInTime(timeMillis / 1000000);
	}

}
