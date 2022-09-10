/**
 * 
 */
package com.trimeur.swing.chart;

/**
 * @author Esaie MUHASA
 *
 */
public interface Axis extends ChartData{
	
	/**
	 * Renvoie le nom de l'axe
	 * @return
	 */
	String getName ();
	
	/**
	 * renvoie le nom court de l'axe
	 * @return
	 */
	String getShortName ();
	
	/**
	 * renvoie le symbole de l'unite de mesure
	 * @return
	 */
	String getMeasureUnit();
	
	/**
	 * Renvoie le label de la valeur en parametre
	 * @param value
	 * @return
	 */
	String getLabelOf(final double value);
}
