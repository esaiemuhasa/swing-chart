/**
 * 
 */
package com.trimeur.swing.chart.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.trimeur.swing.chart.ChartPanel;
import com.trimeur.swing.chart.DateAxis;
import com.trimeur.swing.chart.DefaultAxis;
import com.trimeur.swing.chart.DefaultCloudChartModel;
import com.trimeur.swing.chart.DefaultMaterialPoint;
import com.trimeur.swing.chart.DefaultPieModel;
import com.trimeur.swing.chart.DefaultPiePart;
import com.trimeur.swing.chart.DefaultPointCloud;
import com.trimeur.swing.chart.MaterialPoint;
import com.trimeur.swing.chart.PiePanel;
import com.trimeur.swing.chart.PieRender.PieRenderType;
import com.trimeur.swing.chart.PointCloud.CloudType;

/**
 * @author Esaie MUHASA
 *
 */
public class ChartTestFrame extends JFrame {
	private static final long serialVersionUID = -8373859408934961744L;

	/**
	 * @throws HeadlessException
	 */
	public ChartTestFrame() throws HeadlessException {
		super("Graphiques");
		setSize(700, 450);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		DateAxis xAxis = new DateAxis("Abs", "X", "");
		DefaultAxis yAxis = new DefaultAxis("Ord", "Y", "");
		
		xAxis.setFormater(new SimpleDateFormat("dd/MM/yyyy\nhh:mm:ss"));
		
		List<DefaultPointCloud> listCos = new ArrayList<>();
		List<DefaultPointCloud> listSin = new ArrayList<>();
		int nombreCourbes = 2, dephasage = -30;
		for (int i = 0; i < nombreCourbes; i++) {
			DefaultPointCloud c = new DefaultPointCloud("Cos: deph = "+(dephasage*i)+"°");
			DefaultPointCloud s = new DefaultPointCloud("Sin: deph =  "+(dephasage*i)+"°");
			c.setBorderColor(Color.BLACK);
			s.setBorderColor(Color.RED);
			c.setBackgroundColor(new Color(0x55000000, true));
			s.setBackgroundColor(new Color(0x55FF0000, true));
			s.setBorderWidth(2f);
			c.setBorderWidth(2f);
			listCos.add(c);
			listSin.add(s);
		}
		listCos.get(0).setBorderWidth(0.8f);
		listSin.get(0).setBorderWidth(0.8f);
		listCos.get(0).setType(CloudType.STICK_CHART);
		listSin.get(0).setType(CloudType.STICK_CHART);
		
		double amplitude = 1000, angle, sin, cos;
		for (int i = -360; i <= 360; i+=10) {
			for (int j = 0; j < nombreCourbes; j++) {
				angle = (i+(j*dephasage)) * (Math.PI / 180);
				sin = amplitude * Math.sin(angle);
				cos = amplitude * Math.cos(angle);
				listCos.get(j).addPoint(new DefaultMaterialPoint(i, cos, 0));
				listSin.get(j).addPoint(new DefaultMaterialPoint(i, sin, 0));
			}
		}
		
		
		DefaultCloudChartModel model = new DefaultCloudChartModel(xAxis, yAxis);
		
		model.setBorderColor(Color.BLACK);
		model.setBackgroundColor(Color.BLACK);
		
		for (int j = 0; j < nombreCourbes; j++){
			model.addChart(listCos.get(j));
			model.addChart(listSin.get(j));
		}
		
		//custom
		DefaultPointCloud custom = new DefaultPointCloud("Custom 1");
		DefaultPointCloud custom2 = new DefaultPointCloud("Custom 1");
		DefaultPointCloud custom3 = new DefaultPointCloud("Custom 1");
		DefaultPointCloud custom4 = new DefaultPointCloud("Custom 1");
		custom.setBorderColor(new Color(0x990000));
		custom.setBackgroundColor(new Color(0x55990000, true));
		custom.setFill(true);
//		custom.setType(CloudType.STICK_CHART);
		
		custom2.setBorderColor(new Color(0x009900));
		custom2.setBackgroundColor(new Color(0x55009900, true));
		custom2.setFill(true);
		
		custom3.setBorderColor(new Color(0x000099));
		custom3.setBackgroundColor(new Color(0xAA000099, true));
		
		custom4.setBorderColor(new Color(0x990099));
		custom4.setBackgroundColor(new Color(0xAA990099, true));
		
		int size = 8, step = 1;
		for (double i = 0; i <= 1.1; i += 0.1) {
			double y = rand(0.01, 0.9);
			custom.addPoint(new DefaultMaterialPoint(-1 * ((step * i)), y, size));
			custom2.addPoint(new DefaultMaterialPoint((step * i), y, size));
			custom3.addPoint(new DefaultMaterialPoint(-1 * ((step * i)), -y/2, size));
			custom4.addPoint(new DefaultMaterialPoint((step * i), -y/2, size));
		}		
		custom4.setType(CloudType.STICK_CHART);
		DefaultCloudChartModel c = new DefaultCloudChartModel();
		c.addChart(custom);
		c.addChart(custom2);
		c.addChart(custom3);
		c.addChart(custom4);
		//==
		
		DefaultPieModel pie = new DefaultPieModel();
		pie.setTitle("Repartition du frick");
		pie.setSuffix("USD");
		int index = 0;
		for (double i = 0; i <= 1.1; i += 0.1) {
			double val = rand(0.01, 0.9);
			DefaultPiePart part = new DefaultPiePart(Utility.getColorAt(index), val, "Titre du part "+index);
			pie.addPart(part);
			index++;
		}
		
		pie.setRealMaxPriority(true);
		PiePanel pieDounu = new PiePanel(pie, Color.BLACK), pieDefault = new PiePanel(pie, pieDounu.getBorderColor());
		pieDounu.setBackground(getContentPane().getBackground());
		pieDefault.getRender().setType(PieRenderType.DEFAULT);
		
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pieDounu, pieDefault);
		
		ChartPanel chart = new ChartPanel(c);
		chart.getChartRender().setMouseLineColor(Color.BLACK);
		chart.getChartRender().setLineAxisColor(Color.BLACK);
		
		JTabbedPane tabbed = new JTabbedPane();
		tabbed.addTab("Pie Chart", split);
		tabbed.addTab("Custom Chart", chart);
		tabbed.addTab("Sin et Cos",	 new ChartPanel(model));
		
		getContentPane().add(tabbed, BorderLayout.CENTER);
	}
	
	public static void main (String[] args) throws Exception {
		UIManager.setLookAndFeel(new NimbusLookAndFeel());
		
//		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		ChartTestFrame f = new ChartTestFrame();
		f.setVisible(true);
	}
	
	public static MaterialPoint randPoint (double min, double max, double x) {
		double y = Math.random()*max;
		
		if(y < min)
			y += min;
		
		return new DefaultMaterialPoint(x, y);
	}
	
	public static double rand (double min, double max)  {
		double val = Math.random()*max;
		
		if(val < min)
			val += min;
		return val;
	}



}
