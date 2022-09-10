/**
 * 
 */
package com.trimeur.swing.chart;

/**
 * @author Esaie MUHASA
 *
 */
public interface PieModel {
	
	void setTitle (String title);
	String getTitle ();	
	
	//adding parts
	void addPart (PiePart part);
	void addPart (int index, PiePart part);
	void addParts (PiePart ...parts);
	// --
	
	/**
	 * Renvoie la valeur max. l'equivalent de 360°
	 * @return
	 */
	double getMax ();
	void setMax (double max);
	
	/**
	 * La priorite du du real max est plus eleve que celui du max fixer par l'utilisateur
	 * @return
	 */
	boolean isRealMaxPriority();
	
	double getRealMax();
	String getSuffix ();
	
	/**
	 * @param index
	 * @return
	 */
	PiePart getPartAt (int index);
	
	/**
	 * Renvoie l'index du part en parametre
	 * renvoie -1 dans le cas où le part n'appartien pas au model du pie
	 * @param part
	 * @return
	 */
	int indexOf (PiePart part);
	
	/**
	 * modification de l'index selectionner
	 * @param index
	 */
	void setSelectedIndex (int index) throws IndexOutOfBoundsException;
	
	/**
	 * Renvoie l'index selectionnner ou -1 si aucune index n'est selectionner
	 * @return
	 */
	int getSelectedIndex ();
	
	/**
	 * Renvoei les pourcentages de l'indem a l'index
	 * @param index
	 * @return
	 */
	double getPercentOf (int index);
	
	/**
	 * Renvoie les pourcentage du part en parametre, si cel-ci ce trouve dans le model
	 * @param part
	 * @return
	 */
	double getPercentOf (PiePart part);
	
	/**
	 * Renvoie la somme des pourcents de parts
	 * @return
	 */
	double getSumPercent ();
	
	/**
	 * renvoie l'object le part qui a une reference vers l'object en parametre
	 * @param data
	 * @return
	 */
	PiePart findByData (Object data);
	
	/**
	 * Renvoie le nombre des parts
	 * @return
	 */
	int getCountPart();
	
	/**
	 * recuperation du part dont le nom est en parametre
	 * @param name
	 * @return
	 */
	PiePart getPartByName (String name);
	
	/**
	 * @param index
	 */
	void removePartAt (int index);
	/**
	 * Depamande de supression d'un part dont la reference est en parametre
	 * @param part
	 * @return
	 */
	boolean removePart (PiePart part);
	
	/**
	 * Supression du part dont la donnee est en parametre
	 * @param data
	 * @return
	 */
	boolean removeByData (Object data);
	
	void removeAll ();
	PiePart [] getParts ();
	
	/**
	 * recuperation du contenue d'un model en un tableau des double
	 * @param model
	 * @return
	 */
	public static Double [] toValuesArray (PieModel model) {
		Double [] values = new Double[model.getCountPart()];
		for (int i = 0; i < values.length; i++)
			values[i] = model.getPartAt(i).getValue();
		return values;
	}
	
	/**
	 * Renvoie le tableau des labels d'un model
	 * @param model
	 * @return
	 */
	public static String [] toLabelsArray (PieModel model) {
		String [] labels = new String[model.getCountPart()]; 
		for (int i = 0; i < labels.length; i++)
			labels[i] = model.getPartAt(i).getLabel();
		return labels;
	}
	
	void addListener (PieModelListener listener);
	void removeListener (PieModelListener listener);
}
