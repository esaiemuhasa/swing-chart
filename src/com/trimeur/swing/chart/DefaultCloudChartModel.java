/**
 * 
 */
package com.trimeur.swing.chart;

import java.util.ArrayList;
import java.util.List;

import com.trimeur.swing.chart.PointCloud.CloudType;

/**
 * @author Esaie MUHASA
 *
 */
public class DefaultCloudChartModel extends AbstractChartData implements CloudChartModel{
	
	protected final List<PointCloud> clouds = new ArrayList<>();
	protected final List<CloudChartModelListener> listeners = new  ArrayList<>();
	
	protected Axis xAxis;
	protected Axis yAxis;
	
	private MaterialPoint xMax;
	private MaterialPoint yMax;
	private MaterialPoint xMin;
	private MaterialPoint yMin;

	protected final PointCloudListener cloudListener = new PointCloudListener() {
		
		@Override
		public synchronized void onRemovePoint (PointCloud cloud, int index, MaterialPoint materialPoint) {
			checkMinMax();
			for (CloudChartModelListener ls : listeners) 
				ls.onRemovePoint(DefaultCloudChartModel.this, indexOf(cloud), index, materialPoint);
		}
		
		@Override
		public synchronized void onInsertPoint (PointCloud cloud, int index) {
			checkMinMax();
			for (CloudChartModelListener ls : listeners) 
				ls.onInsertPoint(DefaultCloudChartModel.this, indexOf(cloud), index);
		}
		
		@Override
		public synchronized void onChange(PointCloud cloud) {
			checkMinMax();
			for (CloudChartModelListener ls : listeners) 
				ls.onChartChange(DefaultCloudChartModel.this, indexOf(cloud));
		}

		@Override
		public void onPointChange(PointCloud cloud, int index) {
			checkMinMax();
			for (CloudChartModelListener ls : listeners) 
				ls.onPointChange(DefaultCloudChartModel.this, indexOf(cloud), index);
			
		}
	};
	
	/**
	 * 
	 */
	public DefaultCloudChartModel() {
		super();
		xAxis = new DefaultAxis();
		yAxis = new DefaultAxis();
	}

	/**
	 * @param xAxis
	 * @param yAxis
	 */
	public DefaultCloudChartModel(Axis xAxis, Axis yAxis) {
		super();
		this.xAxis = xAxis;
		this.yAxis = yAxis;
	}

	@Override
	public synchronized void bindFrom (CloudChartModel model) {
		boolean update = false;
		for (int i = 0, count = model.getSize(); i < count; i++) {
			if(!clouds.contains(model.getChartAt(i))){
				clouds.add(model.getChartAt(i));
				model.getChartAt(i).addListener(cloudListener);
				update = true;
			}
		}
		
		if (update) {			
			checkMinMax();
			emitOnChange();
		}
	}
	
	@Override
	public boolean hasType (CloudType type) {
		for (int i = 0, count = getSize(); i < count; i++) 
			if(clouds.get(i).getType() == type)
				return true;
		return false;
	}

	@Override
	public void bindFrom(CloudChartModel model, int index) {
		boolean update = false;
		for (int i = 0, count = model.getSize(); i < count; i++) {
			if(!clouds.contains(model.getChartAt(i))){
				clouds.add(index+i, model.getChartAt(i));
				model.getChartAt(i).addListener(cloudListener);
				update = true;
			}
		}
		
		if (update) {			
			checkMinMax();
			emitOnChange();
		}
	}

	@Override
	public void unbindFrom(CloudChartModel model) {
		boolean update = false;
		for (int i = 0, count = model.getSize(); i < count; i++) {
			int index = indexOf(model.getChartAt(i));
			if(index != -1){
				clouds.get(index).removeListener(cloudListener);
				clouds.remove(index);
				update = true;
			}
		}
		
		if (update) {			
			checkMinMax();
			emitOnChange();
		}
	}

	@Override
	public synchronized void addChart(PointCloud cloud) {
		if(clouds.contains(cloud))
			return;
		
		clouds.add(cloud);
		cloud.addListener(cloudListener);
		emitOnInserted(clouds.size()-1);
	}
	
	@Override
	public boolean hasVisibleChart() {
		for (PointCloud cl : clouds) {
			if(cl.isVisible() && cl.countPoints() != 0)
				return true;
		}
		return false;
	}

	@Override
	public boolean hasHiddenChart() {
		for (PointCloud cl : clouds) {
			if(!cl.isVisible())
				return true;
		}
		return false;
	}

	@Override
	public int indexOf(PointCloud cloud) {
		return clouds.indexOf(cloud);
	}

	@Override
	public synchronized void addChart(PointCloud cloud, int index) {
		if(clouds.contains(cloud))
			return;
			
		clouds.add(index, cloud);
		cloud.addListener(cloudListener);
		emitOnInserted(index);
	}
	
	@Override
	public void setVisible(boolean visible) {
		if(visible != this.isVisible()){
			super.setVisible(visible);
			for (PointCloud cl : clouds) {
				cl.setVisible(visible);
			}
			
			emitOnChange();
		}
	}

	@Override
	public int getSize() {
		return clouds.size();
	}

	@Override
	public PointCloud getChartAt(int index) {
		return clouds.get(index);
	}

	@Override
	public synchronized void removeChartAt(int index) {
		clouds.get(index).removeListener(cloudListener);
		clouds.remove(index);
		emitOnRemove(index);
	}
	
	@Override
	public void removeAll() {
		for (PointCloud cloud : clouds)
			cloud.removeListener(cloudListener);
		
		clouds.clear();
	}

	@Override
	public void addListener(CloudChartModelListener listener) {
		if(!listeners.contains(listener))
			listeners.add(listener);
	}

	@Override
	public boolean removeListener(CloudChartModelListener listener) {
		return listeners.remove(listener);
	}

	@Override
	public Axis getXAxis() {
		return xAxis;
	}

	@Override
	public Axis getYAxis() {
		return yAxis;
	}

	@Override
	public MaterialPoint getXMax() {
		return xMax;
	}
	
	protected void checkXMax () {
		MaterialPoint materialPoint = null;
		
		for (PointCloud c : clouds)
			if(c.isVisible() && c.countPoints() != 0) {
				materialPoint = c.getXMax();
				break;
			}
		
		if(materialPoint == null)
			xMax = null;
		
		for (PointCloud c : clouds) {
			if(!c.isVisible() || c.getXMax() == null)
				continue;
			
			if (c.getXMax().getX() > materialPoint.getX())
				materialPoint = c.getXMax();
		}
		xMax = materialPoint;		
	}

	@Override
	public MaterialPoint getYMax() {
		return yMax;
	}
	
	protected void checkYMax () {
		MaterialPoint materialPoint = null;
		
		for (PointCloud c : clouds)
			if(c.isVisible() && c.countPoints() != 0) {
				materialPoint = c.getYMax();
				break;
			}
		
		if(materialPoint == null)
			yMax = null;
		
		for (PointCloud c : clouds) {
			if(!c.isVisible() || c.getYMax() == null)
				continue;
			
			if (c.getYMax().getY() > materialPoint.getY())
				materialPoint = c.getYMax();
		}
		yMax = materialPoint;		
	}

	@Override
	public MaterialPoint getXMin() {
		return xMin;
	}
	
	protected void checkXMin () {
		MaterialPoint materialPoint = null;
		for (PointCloud c : clouds)
			if(c.isVisible() && c.countPoints() != 0) {
				materialPoint = c.getXMin();
				break;
			}
		
		if(materialPoint == null)
			xMin = null;
		
		for (PointCloud c : clouds) {
			if(!c.isVisible() || c.getXMin() == null)
				continue;
			
			if (c.getXMin().getX() < materialPoint.getX())
				materialPoint = c.getXMin();
		}
		
		xMin = materialPoint;		
	}

	@Override
	public MaterialPoint getYMin() {
		return yMin;
	}
	
	protected void checkYMin () {
		MaterialPoint materialPoint = null;
		for (PointCloud c : clouds)
			if(c.isVisible() && c.countPoints() != 0) {
				materialPoint = c.getYMin();
				break;
			}
		
		if(materialPoint == null)
			yMin = null;
		
		for (PointCloud c : clouds) {
			if(!c.isVisible() || c.getYMin() == null)
				continue;
			
			if (c.getYMin().getY() < materialPoint.getY())
				materialPoint = c.getYMin();
		}
		yMin = materialPoint;
	}
	
	/**
	 * Recherche du min et du max
	 */
	protected synchronized void checkMinMax () {
		checkXMax();
		checkXMin();
		checkYMax();
		checkYMin();
	}
	
	@Override
	protected synchronized void emitOnChange () {
		for (CloudChartModelListener ls : listeners) {
			ls.onChange(this);
		}
	}
	
	protected synchronized void emitOnInserted (int index) {
		checkMinMax();
		for (CloudChartModelListener ls : listeners) {
			ls.onInsertChart(this, index);
		}
	}
	
	protected synchronized void emitOnRemove (int index) {
		checkMinMax();
		for (CloudChartModelListener ls : listeners) {
			ls.onRemoveChart(this, index);
		}
	}

}
