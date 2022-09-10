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
public class DefaultMaterialPoint extends AbstractChartData implements MaterialPoint {
	
	protected final List<PointListener> listeners = new ArrayList<>();
	
	private double x;
	private double y;
	private float size = 1;
	private String labelX;
	private String labelY;
	
	/**
	 * @param x
	 * @param y
	 */
	public DefaultMaterialPoint(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @param x
	 * @param y
	 * @param labelX
	 * @param labelY
	 */
	public DefaultMaterialPoint(double x, double y, String labelX, String labelY) {
		super();
		this.x = x;
		this.y = y;
		this.labelX = labelX;
		this.labelY = labelY;
	}

	/**
	 * Constructeur de copie.
	 * on recopie seulement les valeurs des cordonnees sur les axes
	 * et les donnees qui cadre avec le rendue du point (couleur, taille,..)
	 * @param point
	 */
	public DefaultMaterialPoint (MaterialPoint point) {
		super(point.getBackgroundColor(), point.getForegroundColor(), point.getBorderColor());
		setBorderWidth(point.getBorderWidth());
		x = point.getX();
		y = point.getY();
		size = point.getSize();
		data = point.getData();
		labelX = point.getLabelX();
		labelY = point.getLabelY();
	}

	/**
	 * @param backgroundColor
	 * @param foregroundColor
	 * @param borderColor
	 */
	public DefaultMaterialPoint(Color backgroundColor, Color foregroundColor, Color borderColor) {
		super(backgroundColor, foregroundColor, borderColor);
	}

	/**
	 * @param backgroundColor
	 */
	public DefaultMaterialPoint(Color backgroundColor) {
		super(backgroundColor);
	}

	/**
	 * @param x
	 * @param y
	 * @param size
	 */
	public DefaultMaterialPoint(double x, double y, float size) {
		super();
		this.x = x;
		this.y = y;
		this.size = size;
	}

	/**
	 * @param x the x to set
	 */
	public synchronized void setX(double x) {
		if(this.x == x)
			return;
		
		this.x = x;
		emitOnChange();
	}

	/**
	 * @param y the y to set
	 */
	public synchronized void setY(double y) {
		if(this.x == y)
			return;
		
		this.y = y;
		emitOnChange();
	}

	@Override
	public void translate(double x, double y, double z) {
		if(this.x == x && this.y == y)
			return;
		
		this.x = x;
		this.y = y;
		emitOnChange();
	}

	/**
	 * @param size the size to set
	 */
	public synchronized void setSize(float size) {
		if(this.size == size)
			return;
		
		this.size = size;
		emitOnChange();
	}

	@Override
	public double getX () {
		return x;
	}

	@Override
	public double getY () {
		return y;
	}

	@Override
	public double getZ() {
		return 0;
	}

	@Override
	public float getSize() {
		return size;
	}

	@Override
	public String getLabelX() {
		if(labelX == null)
			return PieRender.DECIMAL_FORMAT.format(x);
		return labelX;
	}
	
	@Override
	public String getLabelY() {
		if(labelY == null)
			return PieRender.DECIMAL_FORMAT.format(y);
		return labelY;
	}
	
	@Override
	public String getlabelZ() {
		throw new RuntimeException("La 3D n'est pas pris en charge");
	}

	/**
	 * @param labelX the labelX to set
	 */
	public void setLabelX (String labelX) {
		if(labelX == this.labelX)
			return;
		
		this.labelX = labelX;
		emitOnChange();
	}
	
	public void setLabelY(String labelY) {
		if(this.labelY == labelY)
			return;
		
		this.labelY = labelY;
		emitOnChange();
	}

	@Override
	public void addListener(PointListener listener) {
		if(!listeners.contains(listener) && listener != null)
			listeners.add(listener);
	}

	@Override
	public boolean removeListener(PointListener listener) {
		return listeners.remove(listener);
	}
	
	protected synchronized void emitOnChange () {
		for (PointListener ls : listeners)
			ls.onChange(this);
	}

	@Override
	public String toString() {
		return "DefaultMaterialPoint [x=" + x + ", y=" + y + ", labelX="+labelX+", labelY="+labelY+ "]";
	}

	@Override
	public boolean equals (Object obj) {
		if(obj instanceof MaterialPoint) {
			MaterialPoint point = (MaterialPoint) obj;
			return point.getX() == x && point.getY() == y;
		}
		
		return super.equals(obj);
	}

}
