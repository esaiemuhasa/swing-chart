/**
 * 
 */
package com.trimeur.swing.chart;

import com.trimeur.swing.chart.PointCloud.CloudType;

/**
 * @author Esaie MUHASA
 *
 */
public interface CloudChartModel extends ChartData{

	/**
	 * Renvoie l'axe des X
	 * @return
	 */
	Axis getXAxis ();
	
	/**
	 * Renvoie l'axe des Y
	 * @return
	 */
	Axis getYAxis ();
	
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
	 * Ajout d'un nuage des point au model des graphique lineaire
	 * @param cloud
	 */
	void addChart (PointCloud cloud);
	
	/**
	 * Insersion du graphique
	 * @param cloud
	 * @param index
	 */
	void addChart (PointCloud cloud, int index);
	
	/**
	 * binding point cloud for models
	 * @param model
	 */
	void bindFrom (CloudChartModel model);
	void bindFrom (CloudChartModel model, int index);
	
	/**
	 * pour detacher les nuages de materialPoints du model au model encours
	 * @param model
	 */
	void unbindFrom (CloudChartModel model);
	
	/**
	 * compte le nombre de graphiques ce trouvant dans le modele
	 * @return
	 */
	int getSize ();
	
	/**
	 * y-t-il un graphique visible
	 * @return
	 */
	boolean hasVisibleChart ();
	
	/**
	 * Verifie si parmis les types de graphiques, il y a des graphique du type en parametre
	 * @param type
	 * @return
	 */
	boolean hasType (CloudType type);
	
	/**
	 * y-t-il un graphique cachee??
	 * @return
	 */
	boolean hasHiddenChart ();
	
	/**
	 * Renvoie le graphique a l'index en parametre
	 * @param index
	 * @return
	 */
	PointCloud getChartAt (int index);
	
	/**
	 * Suppression du nuage de point a l'index en parametre
	 * @param index
	 */
	void removeChartAt (int index);
	
	/**
	 * supression de tout les nuages des materialPoints dans le model
	 */
	void removeAll();
	
	/**
	 * Renvoie l'index du cloud dans le model ou -1 dans le cas où le cloud n'appartiens 
	 * pas au model
	 * @param cloud
	 * @return
	 */
	int indexOf (PointCloud cloud);
	
	/**
	 * Abonnement d'un ecouteur 
	 * @param listener
	 */
	void addListener (CloudChartModelListener listener);
	
	/**
	 * Desabonnement d'un ecouteur
	 * @param listener
	 * @return
	 */
	boolean removeListener (CloudChartModelListener listener);
}
