/**
 * 
 */
package com.trimeur.swing.chart;

/**
 * @author Esaie MUHASA
 *
 */
public interface MaterialPoint extends ChartData{
	
	/**
	 * renvoei la valeur sur l'axe des abs
	 * @return
	 */
	double getX();
	
	/**
	 * valeur sur l'axe des ord
	 * @return
	 */
	double getY();
	
	
	/**
	 * Valeur sur l'axe Z
	 * @return
	 */
	double getZ();
	
	/**
	 * transalation du point
	 * @param x
	 * @param y
	 * @param z
	 */
	void translate (double x, double y, double z);
	
	/**
	 * translation sur l'axe des X et Y
	 * @param x
	 * @param y
	 */
	default void translateXY (double x, double y) {
		translate(x, y, getZ());
	}
	
	/**
	 * Taille du point
	 * @return
	 */
	float getSize();
	
	/**
	 * Renvoie le label du point materiel sur l'axe des X
	 * @return
	 */
	String getLabelX ();
	
	/**
	 * Renvoie le label du point materiel sur l'axe des Y
	 * @return
	 */
	String getLabelY ();
	
	/**
	 * Renvoie le label du point materiel sur l'axe des Z
	 * @return
	 */
	String getlabelZ ();
	
	/**
	 * Ajout d'un ecouteur
	 * @param listener
	 */
	void addListener(PointListener listener);
	
	/**
	 * @param listener
	 * @return
	 */
	boolean removeListener(PointListener listener);
}
