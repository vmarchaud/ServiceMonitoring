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
package fr.thisismac.monitoring.object;

/**
 * Classe used to implement a maintenance time between server wont send notification because they down
 * @author Mac'
 */

public class Maintenance {
	
	
	private int begin_HOUR;
	private int begin_MINUTE;
	private int end_HOUR;
	private int end_MINUTE;
	
	/**
	 * Construct a maintenance object
	 * @param begin config string of begining
	 * @param end config string of end
	 */
	public Maintenance(String begin, String end) {
		String[] temp = begin.split(":");
		this.begin_HOUR = Integer.parseInt(temp[0]);
		this.begin_MINUTE = Integer.parseInt(temp[1]);
		
		temp = end.split(":");
		this.end_HOUR = Integer.parseInt(temp[0]);
		this.end_MINUTE = Integer.parseInt(temp[1]);
	}
	
	/**
	 * Get the begin hour of this maintenance
	 * @return begin hour
	 */
	public int getBegin_HOUR() {
		return begin_HOUR;
	}
	
	/**
	 * Get the begin minute of this maintenance
	 * @return behing minute
	 */
	public int getBegin_MINUTE() {
		return begin_MINUTE;
	}
	
	/**
	 * Get the end hour of this maintenance
	 * @return end hour
	 */
	public int getEnd_HOUR() {
		return end_HOUR;
	}
	
	/**
	 * Get the end minute of this maintenance
	 * @return end minute
	 */
	public int getEnd_MINUTE() {
		return end_MINUTE;
	}
}

