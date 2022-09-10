/**
 * 
 */
package com.trimeur.swing.chart;

import java.awt.Color;

/**
 * @author Esaie MUHASA
 *
 */
public interface ChartDataRenderedListener {
	
	/**
	 * lors du changement d'etat
	 * @param source
	 */
	void onChange (ChartData source);
	
	/**
	 * lors du changement de la couleur de bordure
	 * @param source
	 * @param oldColor
	 */
	void onBorderColorChagne (ChartData source, Color oldColor);
	
	/**
	 * Lors du changement de la couleur de premier plan
	 * @param source
	 * @param oldColor
	 */
	void onForegroundColorChagne (ChartData source, Color oldColor);
	
	/**
	 * Lors du changement de la couleur d'arriere plan
	 * @param source
	 * @param oldColor
	 */
	void onBackgroundColorChagne (ChartData source, Color oldColor);
	
	/**
	 * lors du changement de l'epaisseur de bordure
	 * @param source
	 * @param oldBorderWidth
	 */
	void onBorderWidthChange (ChartData source, float oldBorderWidth);
}
