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
public abstract class AbstractChartData implements ChartData {
	
	protected Color backgroundColor;
	protected Color foregroundColor;
	protected Color borderColor;
	protected boolean visible;
	protected Object data;
	protected float borderWidth;
	
	protected final List<ChartDataRenderedListener> listListeners = new ArrayList<>();

	/**
	 * 
	 */
	public AbstractChartData() {
		this.backgroundColor = Color.WHITE;
		this.foregroundColor = Color.BLACK.brighter();
		this.borderColor = Color.DARK_GRAY.darker();
		this.visible = true;
		this.borderWidth = 2;
	}

	/**
	 * @param backgroundColor
	 */
	public AbstractChartData(Color backgroundColor) {
		super();
		this.backgroundColor = backgroundColor;
		this.borderColor = backgroundColor.darker().darker().darker().darker();
		this.foregroundColor = backgroundColor.brighter().brighter().brighter().brighter().brighter();
		this.visible = true;
		this.borderWidth = 2;
	}

	/**
	 * @param backgroundColor
	 * @param foregroundColor
	 * @param borderColor
	 */
	public AbstractChartData(Color backgroundColor, Color foregroundColor, Color borderColor) {
		super();
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
		this.borderColor = borderColor;
		this.visible = true;
		this.borderWidth = 2;
	}

	@Override
	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public void setBorderWidth(float borderWidth) {
		float old = this.borderWidth;
		if(old  == borderWidth)
			return;
		
		this.borderWidth = borderWidth;
		emitBorderWithChange(old);
	}

	@Override
	public void setBorderColor(Color borderColor) {
		Color old = this.borderColor;
		if(this.borderColor.equals(borderColor))
			return;
		
		this.borderColor = borderColor;
		emitBorderColorChange(old);
	}

	@Override
	public void setForegroundColor(Color foregroundColor) {
		Color old = this.foregroundColor;
		if(this.foregroundColor.equals(foregroundColor)) 
			return;
		
		this.foregroundColor = foregroundColor;
		emitForegroundColorChange(old);
	}

	@Override
	public void setBackgroundColor(Color backgroundColor) {
		Color old = this.backgroundColor;
		if(this.backgroundColor.equals(backgroundColor))
			return;
		
		this.backgroundColor = backgroundColor;
		emitBackgroundColorChange(old);
	}

	@Override
	public Color getForegroundColor() {
		return foregroundColor;
	}

	@Override
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	@Override
	public Color getBorderColor() {
		return borderColor;
	}

	@Override
	public float getBorderWidth() {
		return borderWidth;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		if(this.visible == visible)
			return;
		this.visible = visible;
		emitOnChange();
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public void addRenderedListener (ChartDataRenderedListener listener) {
		if(listListeners.contains(listener))
			return;
		
		listListeners.add(listener);
	}

	@Override
	public boolean removeRenderedListener(ChartDataRenderedListener listener) {
		return listListeners.remove(listener);
	}
	
	/**
	 * emission du changement de la couleur de bordure
	 * @param oldColor
	 */
	protected synchronized void emitBorderColorChange (Color oldColor) {
		for (ChartDataRenderedListener ls : listListeners)
			ls.onBorderColorChagne(this, oldColor);
	}
	
	protected synchronized void emitBorderWithChange (float oldWidth) {
		for (ChartDataRenderedListener ls : listListeners)
			ls.onBorderWidthChange(this, oldWidth);
	}
	
	/**
	 * Emission du chagnement de la couleur du premier plan
	 * @param oldColor
	 */
	protected synchronized void emitForegroundColorChange (Color oldColor) {
		for (ChartDataRenderedListener ls : listListeners)
			ls.onForegroundColorChagne(this, oldColor);
	}
	
	/**
	 * lors du chagnement de la couleur du back
	 * @param oldColor
	 */
	protected synchronized void emitBackgroundColorChange (Color oldColor) {
		for (ChartDataRenderedListener ls : listListeners)
			ls.onBackgroundColorChagne(this, oldColor);
	}
	
	/**
	 * Emission de changement d'etat
	 */
	protected synchronized void emitOnChange () {
		for (ChartDataRenderedListener ls : listListeners)
			ls.onChange(this);
	}

}
