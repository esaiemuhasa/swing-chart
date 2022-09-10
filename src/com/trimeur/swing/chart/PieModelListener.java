/**
 * 
 */
package com.trimeur.swing.chart;

/**
 * @author Esaie MUHASA
 *
 */
public interface PieModelListener {
	
	/**
	 * method utilitaire appeler pour refrechier la vue
	 * @param model
	 */
	void refresh (PieModel model);
	
	/**
	 * pour demander a la vue de redessiner le part en parametre
	 * @param model
	 * @param partIndex
	 */
	void repaintPart (PieModel model, int partIndex);
	
	/**
	 * Lors du changement de l'index selectionner
	 * @param model
	 * @param oldIndex
	 * @param newIndex
	 */
	void onSelectedIndex (PieModel model, int oldIndex, int newIndex);
	
	/**
	 * lors du changement du titre
	 * @param model
	 * @param title
	 */
	default void onTitleChange (PieModel  model, String title) {}

}
