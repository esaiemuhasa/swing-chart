/**
 * 
 */
package com.trimeur.swing.chart;

import java.awt.Color;

/**
 * @author Esaie MUHASA
 *
 */
public interface ChartData {
	
	/**
	 * Conservation d'une reference d'un object dans un chardata
	 * @param data
	 */
	void setData(Object data);
	
	/**
	 * Modification de l'epaisseur du bordure
	 * @param borderWidth
	 */
	void setBorderWidth (float borderWidth);
	
	/**
	 * modification de la coulteur de bordure
	 * @param borderColor
	 */
	void setBorderColor(Color borderColor);
	
	/**
	 * modification de la coulteur de premier plan (couleur de texte)
	 * @param foregroundColor
	 */
	void setForegroundColor (Color foregroundColor);
	
	/**
	 * Modification de la couleur d'arriere plan
	 * @param backgroundColor
	 */
	void setBackgroundColor (Color backgroundColor);
	
	/**
	 * Renvoie la couleur du premier plan (couleur de texte)
	 * @return
	 */
	Color getForegroundColor ();
	
	/**
	 * Renvoie la couleur d'arriere plan
	 * @return
	 */
	Color getBackgroundColor ();
	
	/**
	 * Renvoie la couleur de bordure
	 * @return
	 */
	Color getBorderColor ();
	
	/**
	 * Renvoie l'epaisseur du bordure
	 * @return
	 */
	float getBorderWidth();

	/**
	 * Es-ce que le part est visible???
	 * @return
	 */
	boolean isVisible ();
	
	/**
	 * Modification de la visibilite du chartdata
	 * @param visible
	 */
	void setVisible(boolean visible);
	
	/**
	 * renvoie la reference d'un object conserver dans le chartdata
	 * @return
	 */
	Object getData ();
	
	/**
	 * ajout d'un listener
	 * @param listener
	 */
	void addRenderedListener (ChartDataRenderedListener listener);
	
	/**
	 * supression d'un listener
	 * @param listener
	 * @return
	 */
	boolean removeRenderedListener (ChartDataRenderedListener listener);
}
