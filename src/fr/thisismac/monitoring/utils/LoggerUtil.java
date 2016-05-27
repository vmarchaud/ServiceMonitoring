/*******************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Valentin 'ThisIsMac' Marchaud
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
package fr.thisismac.monitoring.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

/**
 * Util used to log into a file and in the console with date and level of importance
 * @author Mac'
 *
 */
public class LoggerUtil {
	
	private static File requestLog;
	private static PrintWriter outRequest;
	private static Calendar cal;
	
	/**
	 * Constructor of this util
	 */
	public LoggerUtil() {
		requestLog = new File("lastest.log");
		
		try {
			if(!requestLog.exists()) requestLog.createNewFile();
			
			outRequest = new PrintWriter(new BufferedWriter(new FileWriter(requestLog, true)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Format the message with time and level
	 * @param log the message
	 * @param level importance of this message
	 * @return formatted string
	 */
	public static String formatWithTime(String log, Level level) {
		cal = Calendar.getInstance();
		return String.format("%02d/%02d/%d - %02d:%02d:%02d [%s] : %s", cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1,cal.get(Calendar.YEAR), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), level.toString().toUpperCase(), log);
	}
	
	/**
	 * Write into the file the log
	 * @param log
	 * @param lvl
	 */
	public static void log(String log, Level lvl) {
		outRequest.println(formatWithTime(log, lvl));
		outRequest.flush();
	}
	
	/**
	 * Close the BufferedWriter to be clear
	 */
	public static void close() {
		outRequest.close();
	}

	/**
	 * Print information to the console and into the file
	 * @param log the message
	 * @param level the importance of the message
	 */
	public static void printToConsole(String log, Level level) {
		System.out.println(formatWithTime(log, level));
		log(log, level);
	}
	
	/**
	 * Level of importance of the message
	 * @author Mac'
	 *
	 */
	public enum Level {
		INFO, ERROR;
	}
	
}
