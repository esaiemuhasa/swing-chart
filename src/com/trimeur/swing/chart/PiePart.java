package com.trimeur.swing.chart;

/**
 * 
 * @author Esaie MUHASA
 *
 */
public interface PiePart extends ChartData{
	
	/**
	 * renvoie la valeur exacte du part
	 * @return
	 */
	double getValue ();
	void setValue (double value);
	
	/**
	 * Renvoie le label du part
	 * @return
	 */
	String getLabel ();
	void setLabel (String label);
	
	/**
	 * Renvoi le nom du part
	 * il est preferable de ce nom sois unique dans la collection des parts dans le model
	 * @return
	 */
	String getName ();
	void setName (String name);
	
	void addListener (PiePartListener listener);
	void removeListener (PiePartListener listener);
}
