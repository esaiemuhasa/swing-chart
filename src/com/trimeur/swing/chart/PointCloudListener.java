/**
 * 
 */
package com.trimeur.swing.chart;

/**
 * @author Esaie MUHASA
 *
 */
public interface PointCloudListener {
	/**
	 * Lors d'un changement mageur de la configuration du nuage des materialPoints
	 * @param cloud
	 */
	void onChange (PointCloud cloud);
	
	/**
	 * Lors du chagement des cordonnees d'un point
	 * @param cloud
	 * @param index
	 */
	void onPointChange (PointCloud cloud, int index);
	
	/**
	 * Insersion d'un nouveau point dans le nuage des materialPoints
	 * @param cloud
	 * @param index
	 */
	void onInsertPoint (PointCloud cloud, int index);
	
	/**
	 * Suppression d'un point dans le nuage des materialPoints
	 * @param cloud
	 * @param index
	 * @param materialPoint
	 */
	void onRemovePoint (PointCloud cloud, int index, MaterialPoint materialPoint);
}
