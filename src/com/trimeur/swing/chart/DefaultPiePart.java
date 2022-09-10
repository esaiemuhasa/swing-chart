/**
 * 
 */
package com.trimeur.swing.chart;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Esaie MUHASA
 *
 */
public class DefaultPiePart extends AbstractChartData implements PiePart {
	
	protected final List<PiePartListener> listeners = new ArrayList<>();
	
	protected String label;
	protected String name;
	protected double value;
	
	/**
	 * 
	 */
	public DefaultPiePart() {
		super();
		this.label = "";
	}
	
	/**
	 * Constructeur de copie
	 * @param part
	 */
	public DefaultPiePart(PiePart part) {
		this(part.getBackgroundColor(), part.getBorderColor(), part.getValue(), part.getLabel());
		this.setData(part.getData());
		this.name = part.getName();
		this.foregroundColor = part.getForegroundColor();
	}

	/**
	 * @param backgroundColor
	 * @param value
	 * @param label
	 */
	public DefaultPiePart(Color backgroundColor, double value, String label) {
		this();
		this.backgroundColor = backgroundColor;
		this.value = value;
		this.label = label;
	}

	/**
	 * @param backgroundColor
	 * @param borderColor
	 * @param value
	 * @param label
	 */
	public DefaultPiePart(Color backgroundColor, Color borderColor, double value, String label) {
		this(backgroundColor, value, label);
		this.borderColor = borderColor;
	}

	/**
	 * @param backgroundColor
	 * @param name
	 */
	public DefaultPiePart(Color backgroundColor, String name) {
		this();
		this.backgroundColor = backgroundColor;
		this.name = name;
	}


	@Override
	public String getName() {
		return name;
	}

	@Override
	public synchronized void setName(String name) {
		this.name = name;
	}

	@Override
	public double getValue() {
		return value;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setBackgroundColor(Color backgroundColor) {
		if(this.backgroundColor.equals(backgroundColor))
			return;
		
		super.setBackgroundColor(backgroundColor);
		this.emitOnChange();
	}

	@Override
	public void setForegroundColor(Color foregroundColor) {
		if(this.foregroundColor.equals(foregroundColor)) 
			return;
		
		super.setForegroundColor(foregroundColor);
		this.emitOnChange();
	}

	@Override
	public void setBorderColor(Color borderColor) {
		if(this.borderColor.equals(borderColor))
			return;
		
		super.setBorderColor(borderColor);
		this.emitOnChange();
	}

	@Override
	public synchronized void setValue(double value) {
		if(this.value == value)
			return;
		
		this.value = value;
		this.emitOnChange();
	}

	@Override
	public synchronized void setLabel(String label) {
		if(this.label.equals(label))
			return;
		
		this.label = label;
		this.emitOnChange();		
	}

	@Override
	public synchronized void setVisible(boolean visible) {
		if(this.visible == visible)
			return;
		
		super.setVisible(visible);
		this.emitOnChange();
	}

	@Override
	public synchronized void addListener(PiePartListener listener) {	
		if(!this.listeners.contains(listener))
			this.listeners.add(listener);
	}

	@Override
	public synchronized void removeListener(PiePartListener listener) {
		this.listeners.remove(listener);
	}
	
	protected synchronized void emitOnChange () {
		for (PiePartListener listener : listeners) {
			listener.onChange(this);
		}
	}

}
