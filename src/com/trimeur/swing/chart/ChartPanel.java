/**
 * 
 */
package com.trimeur.swing.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import com.trimeur.swing.chart.tools.Utility;

import jnafilechooser.api.JnaFileChooser;

/**
 * @author Esaie MUHASA
 *
 */
public class ChartPanel extends JPanel {
	private static final long serialVersionUID = -4145881145979100410L;
	static final JnaFileChooser FILE_CHOOSER = new JnaFileChooser();
	private Frame owner;
	static {		
		FILE_CHOOSER.setMultiSelectionEnabled(false);
		FILE_CHOOSER.addFilter("Images", "png", "jpg", "jpeg");
	}
	
	private CloudChartModel model;
	private CloudChartRender chartRender;
	private ModelAdapter modelAdapter = new ModelAdapter();
	
	private final JCheckBox xLine = new JCheckBox("Grilles-X", true);
	private final JCheckBox yLine = new JCheckBox("Grilles-Y", true);
	private final JCheckBox mouseLine = new JCheckBox("Traces", true);
	
	private final Box checkBox = Box.createHorizontalBox();
	private final JPanel checkChart = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	private final JPanel chartContainer = new JPanel(new BorderLayout());
	
	private final JButton btnPrint = new JButton("Imprimer", Utility.getIcon("print"));
	private final JButton btnImg = new JButton("Exporter", Utility.getIcon("saveimg"));
	
	private final List<JCheckBox> chartItems = new ArrayList<>();
	private final ChangeListener chartItemListener = event -> {
		JCheckBox box = (JCheckBox) event.getSource();
		int index = chartItems.indexOf(box);
		model.getChartAt(index).setVisible(box.isSelected());
	};
	
	private final ChangeListener xLineListener = event -> {
		chartRender.setGridXvisible(xLine.isSelected());
	};
	
	private final ChangeListener yLineListener = event -> {
		chartRender.setGridYvisible(yLine.isSelected());
	};
	
	private final ChangeListener mouseLineListener = event -> {
		chartRender.setMouseLineVisible(mouseLine.isSelected());
	};
	
	private final ActionListener btnPrintListener = event -> {
		PrinterJob job = PrinterJob.getPrinterJob();
		
		if(job.printDialog() && job.pageDialog(job.defaultPage()) != null) {
			Color color = chartRender.getLineAxisColor();
			
			chartRender.setVisible(false);
			chartRender.setLineAxisColor(Color.BLACK);
			job.setPrintable(chartRender);
			
			try {
				job.print();
			} catch (PrinterException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur d'impression", JOptionPane.ERROR_MESSAGE);
			}
			
			chartRender.setLineAxisColor(color);
			chartRender.setVisible(true);
		}
	};
	
	private final ActionListener btnImgListener = event -> {
		
		boolean status = FILE_CHOOSER.showSaveDialog(getOwner());
		if(status) {
			String fileName = FILE_CHOOSER.getSelectedFile().getAbsolutePath();
			if(!fileName.matches("^(.+)(\\.)(png|jpeg|jpg)$"))
				fileName += ".png";
			
			String type = fileName.substring(fileName.lastIndexOf(".")+1);
			
			Toolkit tool = Toolkit.getDefaultToolkit();
			int width = (int)(tool.getScreenSize().getWidth() * 1.5f);
			int height = (int)(tool.getScreenSize().getHeight() * 1.5f);
			
			BufferedImage buffer = new BufferedImage(width, height, type.equals("png")? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D) buffer.getGraphics();
			Color color = chartRender.getLineAxisColor();
			Color bkg = type.equals("png")? null : Color.WHITE;
			chartRender.setLineAxisColor(Color.BLACK);
			chartRender.paint(g, bkg, width, height);
			chartRender.setLineAxisColor(color);
			
			File file = new File(fileName);
			try {
				ImageIO.write(buffer, type, file);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur d'exportation", JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	/**
	 * 
	 */
	public ChartPanel() {
		super(new BorderLayout());
		chartRender = new CloudChartRender();
		initCheckBox();
	}
	
	/**
	 * constructeur d'initialisation du model des donnees
	 * @param model
	 */
	public ChartPanel (CloudChartModel model) {
		super(new BorderLayout());
		this.model = model;
		chartRender = new CloudChartRender(model);
		model.addListener(modelAdapter);
		initCheckBox();
		initCheckChart();
		init();
	}
	
	/**
	 * @return the model
	 */
	public CloudChartModel getModel() {
		return model;
	}

	/**
	 * @return the owner
	 */
	public Frame getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Frame owner) {
		this.owner = owner;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(CloudChartModel model) {
		if(this.model == model)
			return;
		if(this.model != null)
			this.model.removeListener(modelAdapter);
		
		this.model = model;
		initCheckChart();
		if(model != null)
			model.addListener(modelAdapter);
	}

	/**
	 * @return the chartRender
	 */
	public CloudChartRender getChartRender() {
		return chartRender;
	}

	/**
	 * initalisation des composants graphique
	 */
	private void init () {
		final JPanel tool = new JPanel(new BorderLayout());
		tool.add(checkBox, BorderLayout.WEST);
		tool.add(checkChart, BorderLayout.CENTER);
		tool.setBackground(getBackground().darker());
		chartContainer.add(chartRender, BorderLayout.CENTER);
		chartRender.setLineAxisColor(Color.BLACK);
		checkChart.setOpaque(false);
		
		add(tool, BorderLayout.SOUTH);
		add(chartContainer, BorderLayout.CENTER);
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
	}
	
	/**
	 * initialisation du panel contenant les checks box qui permet d'activer 
	 * ou de desactiver un graphique
	 */
	private void initCheckChart() {
		for (JCheckBox box : chartItems) {
			box.removeChangeListener(chartItemListener);
			checkChart.remove(box);
		}
		
		chartItems.clear();
		if(model == null)
			return;
		
		for (int i = 0; i < model.getSize(); i++) {
			JCheckBox box = new JCheckBox(model.getChartAt(i).getTitle(), model.getChartAt(i).isVisible());
			box.setForeground(model.getChartAt(i).getBorderColor());
			box.addChangeListener(chartItemListener);
			checkChart.add(box);
			chartItems.add(box);
		}
		
		revalidate();
	}
	
	/**
	 * initialisation des composants faciltant la configuration de l'apparence du rendu graphique
	 */
	private void initCheckBox() {
		checkBox.add(xLine);
		checkBox.add(Box.createHorizontalStrut(10));
		checkBox.add(yLine);
		checkBox.add(Box.createHorizontalStrut(10));
		checkBox.add(mouseLine);
		checkBox.add(Box.createHorizontalStrut(20));
		checkBox.add(btnPrint);
		checkBox.add(btnImg);
		
		btnPrint.addActionListener(btnPrintListener);
		btnImg.addActionListener(btnImgListener);
		
		xLine.addChangeListener(xLineListener);
		yLine.addChangeListener(yLineListener);
		mouseLine.addChangeListener(mouseLineListener);
	}
	
	private class ModelAdapter implements CloudChartModelListener {

		@Override
		public void onChange(CloudChartModel model) {}

		@Override
		public void onChartChange(CloudChartModel model, int index) {
			JCheckBox box = chartItems.get(index);
			box.setSelected(model.getChartAt(index).isVisible());
			box.setText(model.getChartAt(index).getTitle());
		}

		@Override
		public void onPointChange(CloudChartModel model, int chartIndex, int pointIndex) {}

		@Override
		public void onInsertChart(CloudChartModel model, int chartIndex) {
			JCheckBox box = new JCheckBox(model.getChartAt(chartIndex).getTitle(), model.getChartAt(chartIndex).isVisible());
			box.setForeground(model.getChartAt(chartIndex).getBorderColor());
			box.addChangeListener(chartItemListener);
			checkChart.add(box);
			chartItems.add(box);
		}

		@Override
		public void onRemoveChart(CloudChartModel model, int chartIndex) {
			JCheckBox box = chartItems.get(chartIndex);
			box.removeChangeListener(chartItemListener);
			checkChart.remove(box);
		}

		@Override
		public void onInsertPoint(CloudChartModel model, int chartIndex, int pointIndex) {}

		@Override
		public void onRemovePoint(CloudChartModel model, int chartIndex, int pointIndex, MaterialPoint materialPoint) {}
		
	}
	
	
	/**
	 * @author Esaie MUHASA
	 * Filtrage lors de la selection d'une image
	 */
	static class ImageExportFilter extends FileFilter {
		public static final String [] EXT = {"png", "jpg", "jpeg"};
		@Override
		public boolean accept(File f) {
			if(f.isDirectory())
				return true;
			
			for (String e : EXT)
				if(f.getAbsoluteFile().getName().toLowerCase().matches(".+\\."+e))
					return true;
			
			return false;
		}

		@Override
		public String getDescription() {
			return "Enregistrer l'image au format (.png, .jpg et .jpeg)";
		}
		
	}

}
