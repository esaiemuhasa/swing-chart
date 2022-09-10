/**
 * 
 */
package com.trimeur.swing.chart.tools;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;


/**
 * @author Esaie MUHASA
 *
 */
public final class Utility {
	
	public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
	public static SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

	private Utility() {}
	
	
	/**
	 * create and return date object by last times tamps of date day
	 * in method parameter
	 * @param date
	 * @return
	 */
	public static Date toMaxTimestampOfDay (Date date){
		String date2str = DATE_FORMAT.format(date);
		Date maxDate = null;
		try {
			maxDate = DATE_TIME_FORMAT.parse(date2str+" 23:59:59");
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return maxDate;
	}
	
	/**
	 * create a date represented 12h00'00'' AM by date in method parameter
	 * @param date
	 * @return
	 */
	public static Date toMiddleTimestampOfDay (Date date){
		String date2str = DATE_FORMAT.format(date);
		Date maxDate = null;
		try {
			maxDate = DATE_TIME_FORMAT.parse(date2str+" 12:00:00");
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return maxDate;
	}
	
	/**
	 * create a new date object represented the last second time of date 
	 * in method parameter
	 * @param date
	 * @return
	 */
	public static Date toMinTimestampOfDay (Date date){
		String date2str = DATE_FORMAT.format(date);
		Date minDate = null;
		try {
			minDate = DATE_TIME_FORMAT.parse(date2str+" 00:00:00");
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return minDate;
	}
	
	/**
	 * return the image icon in icons repository
	 * @param name
	 * @return
	 */
	public static Icon getIcon (String name) {
		return new ImageIcon(name);
	}
	
	/**
	 * return the color at index, in colors dictionary
	 * @param index
	 * @return
	 */
	public static final Color getColorAt (int index) {
		return COLORS[index % (COLORS.length-1)];
	}
	
	/**
	 * return the color alpha at index in colors dictionary
	 * @param index
	 * @return
	 */
	public static final Color getColorAlphaAt (int index) {
		return COLORS_ALPHA[index % (COLORS_ALPHA.length-1)];
	}
	
	// default colors dictionaries
	private static final Color [] COLORS = new Color[] {
			new Color(0xFFCE30), new Color(0xE83845), new Color(0xE9669F),
			new Color(0x746AB0), new Color(0x2050FA), new Color(0x9A9A00), 
			new Color(0xEF5020), new Color(0xF0CF6F), new Color(0xAFAFAF),
			new Color(0x20FF76), new Color(0x077B8A), new Color(0x5C3C92),
			new Color(0xE2D810), new Color(0xD9138A), new Color(0x12A4D9),
			new Color(0x22780F), new Color(0x381A3C), new Color(0x004C56),
			new Color(0x9B571D), new Color(0xB03468), new Color(0x4E5352),
			new Color(0x0081F8), new Color(0x00AFAF), new Color(0xF0F3FF)
	};
	
	private static final Color [] COLORS_ALPHA = new Color[] {
			new Color(0x55FFCE30, true), new Color(0x55E83845, true), new Color(0x55E9889F, true),
			new Color(0x55746AB0, true), new Color(0x55288BA8, true), new Color(0x55FF88FF, true), 
			new Color(0x555F7FF0, true), new Color(0x5590C86F, true), new Color(0x550756FF, true),
			new Color(0x55A2D5C6, true), new Color(0x55077B8A, true), new Color(0x555C3C92, true),
			new Color(0x55E2D810, true), new Color(0x55D9138A, true), new Color(0x5512A4D9, true),
			new Color(0x5522780F, true), new Color(0x55381A3C, true), new Color(0x55004C56, true),
			new Color(0x559B571D, true), new Color(0x55B03468, true), new Color(0x554E5352, true)
	};
	
	// --colors

}
