/**
 * 
 */
package com.trimeur.swing.chart;

import java.awt.Color;

/**
 * @author Esaie MUHASA
 *
 */
public abstract class ChartDataRenderedAdapter implements ChartDataRenderedListener {

	@Override
	public void onChange(ChartData source) {}

	@Override
	public void onBorderColorChagne(ChartData source, Color oldColor) {}

	@Override
	public void onForegroundColorChagne(ChartData source, Color oldColor) {}

	@Override
	public void onBackgroundColorChagne(ChartData source, Color oldColor) {}

	@Override
	public void onBorderWidthChange(ChartData source, float oldBorderWidth) {}

}
