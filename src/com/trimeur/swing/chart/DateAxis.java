package com.trimeur.swing.chart;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.trimeur.swing.chart.tools.Utility;

/**
 * @author Esaie MUHASA
 * 
 */
public class DateAxis extends DefaultAxis{
	
	public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private DateFormat formater;

	public DateAxis() {
		super();
		formater = new SimpleDateFormat("dd/MM/yyyy");
	}

	/**
	 * @param backgroundColor
	 * @param foregroundColor
	 * @param borderColor
	 */
	public DateAxis(Color backgroundColor, Color foregroundColor, Color borderColor) {
		super(backgroundColor, foregroundColor, borderColor);
		formater = new SimpleDateFormat("dd/MM/yyyy");
	}

	/**
	 * @param backgroundColor
	 */
	public DateAxis(Color backgroundColor) {
		super(backgroundColor);
		formater = new SimpleDateFormat("dd/MM/yyyy");
	}

	/**
	 * @param formater
	 */
	public DateAxis(DateFormat formater) {
		super();
		this.formater = formater;
	}

	/**
	 * @param name
	 * @param shortName
	 * @param measureUnit
	 */
	public DateAxis(String name, String shortName, String measureUnit) {
		super(name, shortName, measureUnit);
		formater = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public DateAxis(DateFormat formater, String name, String shortName, String measureUnit) {
		super(name, shortName, measureUnit);
		this.formater = formater;
	}

	public void setFormater(DateFormat formater) {
		if(this.formater == formater)
			return;
		
		this.formater = formater;
		emitOnChange();
	}
	
	@Override
	public String getLabelOf (double value) {
		long millis = System.currentTimeMillis() + (((long) value) * 1000l * 60l * 60l * 24l );
		Date date = new Date(millis);
		Date max = Utility.toMiddleTimestampOfDay(date);
		return formater.format(max);
	}

	/**
	 * @return the formater
	 */
	public DateFormat getFormater() {
		return formater;
	}
}
