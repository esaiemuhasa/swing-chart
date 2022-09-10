/**
 * 
 */
package com.trimeur.swing.chart;

/**
 * @author Esaie MUHASA
 *
 */
public interface PointCloud extends ChartData{
	
	/**
	 * Type de nuage des points
	 * @author Esaie MUHASA
	 */
	public enum CloudType {
		LINE_CHART, //pour un nuage des points qui decrits, un graphique de type multiligne
		STICK_CHART, // pour un nuage des points qui decrit un graphique de type bar
		STICK_DISPERSION// pour le grahique de dispersion
	}
	
	/**
	 * renvoie le max sur l'axe des X
	 * @return
	 */
	MaterialPoint getXMax ();
	
	/**
	 * Renvoie le max sur l'axe des Y
	 * @return
	 */
	MaterialPoint getYMax ();
	
	/**
	 * renvoie le point le plus bas sur l'axe des 
	 * @return
	 */
	MaterialPoint getXMin ();
	
	/**
	 * renvoie le point le plus bas sur l'axe des Y
	 * @return
	 */
	MaterialPoint getYMin ();
	
	/**
	 * Renvoie le type de graphique decrit par le nuage de point
	 * @return
	 */
	CloudType getType ();
	
	/**
	 * Renvoie le titre du graphique
	 * @return
	 */
	String getTitle ();
	
	/**
	 * Faut il replir la surface limiter par la courbe et l'ax des X
	 * @return
	 */
	boolean isFill ();
	
	/**
	 * Modification de l'etat de remplissage d'un nuage des points
	 * @param fill
	 */
	void setFill(boolean fill);
	
	/**
	 * ajout d'un point au nuage des points
	 * @param point
	 */
	void addPoint (MaterialPoint point);
	
	/**
	 * Insersion d'un point au nuage des points
	 * @param point
	 * @param index
	 */
	void addPoint(MaterialPoint point, int index);
	
	/**
	 * Renvoie l'index d'un point dans le model
	 * @param point
	 */
	int indexOf (MaterialPoint point);
	
	/**
	 * renvoie le point a l'index en parametre
	 * @param index
	 * @return
	 */
	MaterialPoint getPointAt (int index);
	
	/**
	 * suprime le point a l'index en parametre dans le nuage des points
	 * @param index
	 */
	void removePointAt(int index);
	
	/**
	 * Suppression de tout les point du model
	 */
	void removePoints ();
	
	/**
	 * suppression d'un point dans le nuage
	 * @param point
	 */
	void removePoint (MaterialPoint point);
	
	/**
	 * comptage des points dans le nuage
	 * @return
	 */
	int countPoints ();
	
	/**
	 * Renvoie la collection des point dans le nuage
	 * @return
	 */
	MaterialPoint[] getPoints ();
	
	/**
	 * renvoie la taille par defaut d'un point materiel
	 * @return
	 */
	double getDefaultPointSize ();
	
	/**
	 * les points doit-elle etre visible?
	 * @return
	 */
	boolean getPointVisibility();
	
	/**
	 * Ajout d'un ecouteur du cloud
	 * @param listener
	 */
	void addListener (PointCloudListener listener);
	
	/**
	 * supression d'un ecouteur du cloud
	 * @param listener
	 */
	void removeListener (PointCloudListener listener);
}
