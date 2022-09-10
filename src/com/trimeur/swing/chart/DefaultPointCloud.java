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
public class DefaultPointCloud extends AbstractChartData implements PointCloud {

	protected CloudType type;
	protected boolean fill = false;
	protected final List<MaterialPoint> points = new ArrayList<>();
	protected final List<PointCloudListener> listeners = new ArrayList<>();
	
	protected final PointListener pointListener = (point) -> {
		for (PointCloudListener ls : listeners) 
			ls.onPointChange(this, indexOf(point));
	};
	
	private int xMax = -1;
	private int yMax = -1;
	private int xMin = -1;
	private int yMin = -1;
	
	protected double defaultPointSize = 10;
	protected boolean pointVisibility = true;
	protected String title;
	

	public DefaultPointCloud() {
		super();
		type = CloudType.LINE_CHART;
	}

	/**
	 * @param backgroundColor
	 */
	public DefaultPointCloud(Color backgroundColor) {
		super(backgroundColor);
		type = CloudType.LINE_CHART;
	}

	/**
	 * @param title
	 */
	public DefaultPointCloud(String title) {
		super();
		this.title = title;
	}
	

	/**
	 * @param title
	 * @param backgroundColor
	 */
	public DefaultPointCloud(String title, Color backgroundColor) {
		super(backgroundColor);
		this.title = title;
		type = CloudType.LINE_CHART;
	}

	/**
	 * @param backgroundColor
	 * @param foregroundColor
	 * @param borderColor
	 */
	public DefaultPointCloud(Color backgroundColor, Color foregroundColor, Color borderColor) {
		super(backgroundColor, foregroundColor, borderColor);
		type = CloudType.LINE_CHART;
	}
	
	/**
	 * @param title
	 * @param backgroundColor
	 * @param foregroundColor
	 * @param borderColor
	 */
	public DefaultPointCloud(String title, Color backgroundColor, Color foregroundColor, Color borderColor) {
		super(backgroundColor, foregroundColor, borderColor);
		type = CloudType.LINE_CHART;
		this.title = title;
	}
	
	@Override
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		if(this.title == title)
			return;
		
		this.title = title;
	}
	
	@Override
	public boolean getPointVisibility() {
		return pointVisibility;
	}
	
	@Override
	public double getDefaultPointSize() {
		return defaultPointSize;
	}
	
	public void setPointVisibility(boolean pointVisibility) {
		if(this.pointVisibility == pointVisibility)
			return;
		
		this.pointVisibility = pointVisibility;
		emitOnChange();
	}
	
	public void setDefaultPointSize(double defaultPointSize) {
		if(defaultPointSize == this.defaultPointSize)
			return;
		
		this.defaultPointSize = defaultPointSize;
		emitOnChange();
	}
	
	/**
	 * Transforme le nuage des point en un signale carret
	 */
	public void toSquareSignal () {
		
		if(countPoints() <= 1 )
			return;

		for (int i = 0, count = (points.size() * 2) - 3; i < count; i += 2) {
			
			if (i > points.size()-3)
				break;
			
			MaterialPoint p = getPointAt(i);
			DefaultMaterialPoint copie = new DefaultMaterialPoint(getPointAt(i+1).getX(), p.getY());
			
			if (getPointAt(i+1).getY() == p.getY() && copie.getX() == getPointAt(i+2).getX()){
				i -= 1;
				continue;
			}
			
			copie.setData(getPointAt(i+1).getData());
			points.add(i+1, copie);
		}
		
		MaterialPoint last = getPointAt(points.size()-1);
		MaterialPoint beforLast = getPointAt(points.size()-2);
		
		if (last.getY() != beforLast.getY()) {
			double y = last.getY(),
					x = last.getX() + Math.abs(last.getX() - beforLast.getX());
			DefaultMaterialPoint copie = new DefaultMaterialPoint(x, y);
			points.add(copie);
		}
		
		checkMinMax();
		emitOnChange();
		
	}

	@Override
	public boolean isFill() {
		return fill;
	}

	@Override
	public synchronized void setFill (boolean fill) {
		if(this.fill == fill)
			return;
		
		this.fill = fill;
		emitOnChange();
	}
	
	@Override
	public CloudType getType() {
		return type;
	}
	
	public void setType(CloudType type) {
		if (type == this.type)
			return;
		
		this.type = type;
		if (type == CloudType.STICK_CHART && points.size() >= 2) {
			borderWidth = 1;
			defaultPointSize = (Math.abs(Math.abs(points.get(0).getX()) - Math.abs(points.get(1).getX())) / 4f);
		}
		emitOnChange();
	}

	@Override
	public void addPoint(MaterialPoint point) {
		if(points.contains(point))
			return;
			
		points.add(point);
		point.addListener(pointListener);
		emitInsertPoint(points.size()-1);
	}

	@Override
	public void addPoint(MaterialPoint point, int index) {
		if(points.contains(point))
			return;
			
		points.add(index, point);
		point.addListener(pointListener);
		emitInsertPoint(index);
	}

	@Override
	public int indexOf(MaterialPoint point) {
		if(point == null)
			return -1;
		return points.indexOf(point);
	}

	@Override
	public void removePointAt(int index) {
		MaterialPoint point = points.get(index);
		
		point.removeListener(pointListener);
		points.remove(index);		
		
		if (index == xMax){
			xMax = -1;
			checkXMax();
		} else {
			xMax += index > xMax? 0 : -1;
		}
			
		if (index == yMax){
			yMax = -1;
			checkYMax();
		} else {
			yMax += index > yMax? 0 : -1;
		}
		
		if (index == xMin){
			xMin = -1;
			checkXMin();
		} else {
			xMin += index < xMin? -1 : 0;
		}
		
		if (index == yMin){
			yMin = -1;
			checkYMin();
		} else {
			yMin += index < yMin? -1 : 0;
		}
		
		emitRemovePoint(index, point);
	}

	@Override
	public void removePoint(MaterialPoint point) {
		removePointAt(indexOf(point));
	}
	
	@Override
	public void removePoints() {
		for (MaterialPoint p : points)
			p.removeListener(pointListener);
		
		points.clear();
		checkMinMax();
		emitOnChange();
	}

	@Override
	public int countPoints() {
		return points.size();
	}

	@Override
	public MaterialPoint[] getPoints() {
		MaterialPoint [] p = new MaterialPoint[points.size()];
		for (int i = 0; i < p.length; i++)
			p[i] = points.get(i);
		return p;
	}

	@Override
	public MaterialPoint getXMax() {
		if(xMax == -1) 
			return null;
		return getPointAt(xMax);
	}

	@Override
	public MaterialPoint getYMax() {
		if(yMax == -1) 
			return null;
		
		return getPointAt(yMax);
	}

	@Override
	public MaterialPoint getXMin() {
		if(xMin == -1) 
			return null;
		return getPointAt(xMin);
	}

	@Override
	public MaterialPoint getYMin() {
		if(yMin == -1) 
			return null;
		return getPointAt(yMin);
	}

	@Override
	public MaterialPoint getPointAt(int index) {
		return points.get(index);
	}

	@Override
	public void addListener(PointCloudListener listener) {
		if(!listeners.contains(listener))
			listeners.add(listener);
	}

	@Override
	public void removeListener(PointCloudListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	protected synchronized void emitOnChange () {
		for (PointCloudListener ls : listeners)
			ls.onChange(this);
	}
	
	protected synchronized void emitRemovePoint (int index, MaterialPoint point) {
		for (PointCloudListener ls : listeners)
			ls.onRemovePoint(this, index, point);
	}
	
	/**
	 * @param index
	 */
	protected synchronized void emitInsertPoint (int index) {
		
		if (xMax == -1) {
			setXMax(0);
			setXMin(0);
			
			setYMax(0);
			setYMin(0);
		} else {			
			MaterialPoint p = points.get(index);
			
			int xMax = p.getX() > getXMax().getX()? index : this.xMax;
			int xMin = p.getX() < getXMin().getX()? index : this.xMin;
			
			int yMax = p.getY() > getYMax().getY()? index : this.yMax;
			int yMin = p.getY() < getYMin().getY()? index : this.yMin;
			
			setXMax(xMax);
			setXMin(xMin);
			
			setYMax(yMax);
			setYMin(yMin);
		}

		for (PointCloudListener ls : listeners)
			ls.onInsertPoint(this, index);
	}

	/**
	 * @param xMax the xMax to set
	 */
	protected void setXMax(int xMax) {
		if(this.xMax == xMax)
			return;
		
		this.xMax = xMax;
	}
	
	protected void checkMinMax () {
		checkXMax();
		checkXMin();
		checkYMax();
		checkYMin();
	}
	
	protected void checkXMax () {
		MaterialPoint point = points.size() != 0? points.get(0) : null;
		for (MaterialPoint p : points) {				
			if (p.getX() > point.getX())
				point = p;			
		}
		xMax = indexOf(point);
	}

	/**
	 * @param yMax the yMax to set
	 */
	protected void setYMax(int yMax) {
		if(this.yMax == yMax)
			return;
		
		this.yMax = yMax;
	}
	
	protected void checkYMax () {
		MaterialPoint point = points.size() != 0? points.get(0) : null;
		for (MaterialPoint p : points) {				
			if (p.getY() > point.getY())
				point = p;			
		}
		yMax = indexOf(point);
	}

	/**
	 * @param xMin the xMin to set
	 */
	protected void setXMin(int xMin) {
		if(this.xMin == xMin)
			return;
		
		this.xMin = xMin;
	}
	
	protected void checkXMin () {
		MaterialPoint point = points.size() != 0? points.get(0) : null;
		for (MaterialPoint p : points) {				
			if (p.getX() < point.getX())
				point = p;			
		}
		xMin = indexOf(point);
	}

	/**
	 * @param yMin the yMin to set
	 */
	protected void setYMin(int yMin) {
		this.yMin = yMin;
	}
	
	protected void checkYMin () {
		MaterialPoint point = points.size() != 0? points.get(0) : null;
		for (MaterialPoint p : points) {				
			if (p.getY() < point.getY())
				point = p;			
		}
		yMin = indexOf(point);
	}

}
