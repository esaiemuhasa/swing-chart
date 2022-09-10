/**
 * 
 */
package com.trimeur.swing.chart;

import java.awt.Color;
import java.text.DecimalFormat;

/**
 * @author Esaie MUHASA
 *
 */
public class DefaultAxis extends AbstractChartData implements Axis {
	
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.##");
	protected String name;
	protected String shortName;
	protected String measureUnit = "";

	/**
	 * 
	 */
	public DefaultAxis() {
		super();
	}

	/**
	 * @param name
	 * @param shortName
	 * @param measureUnit
	 */
	public DefaultAxis(String name, String shortName, String measureUnit) {
		super();
		this.name = name;
		this.shortName = shortName;
		this.measureUnit = measureUnit;
	}

	/**
	 * @param backgroundColor
	 */
	public DefaultAxis(Color backgroundColor) {
		super(backgroundColor);
	}

	/**
	 * @param backgroundColor
	 * @param foregroundColor
	 * @param borderColor
	 */
	public DefaultAxis(Color backgroundColor, Color foregroundColor, Color borderColor) {
		super(backgroundColor, foregroundColor, borderColor);
	}
	
	/**
	 * @return the measureUnit
	 */
	@Override
	public String getMeasureUnit() {
		return measureUnit;
	}

	/**
	 * @param measureUnit the measureUnit to set
	 */
	public void setMeasureUnit(String measureUnit) {
		if(measureUnit == this.measureUnit)
			return;
		
		this.measureUnit = measureUnit;
		emitOnChange();
	}
	
	@Override
	public String getLabelOf (final double value) {
		if(Double.isNaN(value) || value == Double.NaN)
			return "";
		return DECIMAL_FORMAT.format(value)+" "+getMeasureUnit();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getShortName() {
		return shortName;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		if(this.name == name)
			return;
		
		this.name = name;
		emitOnChange();
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		if(this.shortName == shortName)
			return;
		
		this.shortName = shortName;
		emitOnChange();
	}

}
