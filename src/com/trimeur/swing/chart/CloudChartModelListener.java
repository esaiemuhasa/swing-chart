/**
 * 
 */
package com.trimeur.swing.chart;

/**
 * @author Esaie MUHASA
 *
 */
public interface CloudChartModelListener{
	
	/**
	 * changement total de la configuration du model
	 * @param model
	 */
	void onChange (CloudChartModel model);
	
	/**
	 * changement total de la configuration d'un cloud du model
	 * @param model
	 * @param index
	 */
	void onChartChange (CloudChartModel model, int index);
	
	/**
	 * Changement de la configuration d'un point
	 * @param model
	 * @param chartIndex
	 * @param pointIndex
	 */
	void onPointChange (CloudChartModel model, int chartIndex, int pointIndex);
	
	/**
	 * insrsion d'un nouveau cloud dans le model
	 * @param model
	 * @param chartIndex
	 */
	void onInsertChart (CloudChartModel model, int chartIndex);
	
	/**
	 * suppression d'un cloud dans le model des donnees
	 * @param model
	 * @param chartIndex
	 */
	void onRemoveChart (CloudChartModel model, int chartIndex);
	
	/**
	 * insersion d'un point dans un cloud du model
	 * @param model
	 * @param chartIndex
	 * @param pointIndex
	 */
	void onInsertPoint (CloudChartModel model, int chartIndex, int pointIndex);
	
	/**
	 * suppression d'un point dansun cloud du model
	 * @param model
	 * @param chartIndex
	 * @param pointIndex
	 * @param materialPoint
	 */
	void onRemovePoint (CloudChartModel model, int chartIndex, int pointIndex, MaterialPoint materialPoint);
}
