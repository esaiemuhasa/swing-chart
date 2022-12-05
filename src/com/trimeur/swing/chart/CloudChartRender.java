/**
 * 
 */
package com.trimeur.swing.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;

import com.trimeur.swing.chart.PointCloud.CloudType;

/**
 * @author Esaie MUHASA
 *
 */
public class CloudChartRender extends JComponent implements Printable{
	private static final long serialVersionUID = -8580414807783085550L;
	
	private static final EmptyBorder DEFAULT_PADDING = new EmptyBorder(10, 10, 10, 10);
	private final ModelListener modelListener = new  ModelListener();
	private final MouseListener mouseListener = new  MouseListener();
	private final List<ChartRenderTranslationListener> translationListener = new ArrayList<>();
	private CloudChartModel model;
	
	private BasicStroke borderStroke = new BasicStroke(1.5f);
	
	//render data
	private final List<PreparedPointCloud> chartMetadatas = new ArrayList<>();
	private int hoverPoint = -1;
	private int hoverChart = -1;
	
	private double xRation;
	private double yRation;
	private double widthRender;
	private double heightRender;
	private float padding = 15;
	private float paddingLeft = 60f;
	private float paddingBottom = 60f;
	
	private double translateX;
	private double translateY;
	//==
	
	//grid
	private BasicStroke gridStroke = new BasicStroke(0.8f);
	private Color gridColor = new Color(0x559F6666, true);
	private double gridXstep = 75;
	private double gridYstep = 35;
	private boolean gridXvisible = true;
	private boolean gridYvisible = true;
	private Line2D xlineAxis;
	private Line2D ylineAxis;
	private Color lineAxisColor = new Color(0xCC505050, true);
	private int [][] fleshXlineAxis = new int [2][3];
	private int [][] fleshYlineAxis = new int [2][3];
	private Rectangle2D borderRect = new Rectangle2D.Double();//rectagle du bordure
	//
	
	//hover
	private Point2D mouseLocation;
	private double mouseXvalue;
	private double mouseYvalue;
	private final RoundRectangle2D recMouseXvalue = new RoundRectangle2D.Double();
	private final RoundRectangle2D recMouseYvalue = new RoundRectangle2D.Double();
	private Color mouseLineColor = new Color(0xBB550055, true);
	private BasicStroke mouseLineWidth = new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private boolean mouseLineVisible = true;
	//==
	
	//translatable
	private boolean horizontalTranslate = true;
	private boolean verticalTranslate = false;
	//==

	public CloudChartRender() {
		init();
	}
	
	/**
	 * constructeur d'initilisation du model des donnees du graphique
	 * @param model
	 */
	public CloudChartRender(CloudChartModel model) {
		super();
		this.model = model;
		init();
	}

	/**
	 * initalisation des composants graphiques
	 * ecoute des evenements bas-niveau
	 */
	private void init () {
		setBorder(DEFAULT_PADDING);
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		setOpaque(false);
		setFont(new Font("Arial", Font.PLAIN, 12));
		if (model != null) 
			model.addListener(modelListener);
	}
	
	/**
	 * mis en jour de la refference du model des donnees
	 * @param model
	 */
	public void setModel (CloudChartModel model) {
		if(this.model == model)
			return;
		
		if (this.model != null)
			this.model.removeListener(modelListener);
		
		this.model = model;
		
		if(model != null)
			model.addListener(modelListener);
		
		prepareRender(getWidth(), getHeight());
		repaint();
	}
	
	/**
	 * @return the verticalTranslate
	 */
	public boolean isVerticalTranslate() {
		return verticalTranslate;
	}

	/**
	 * @param verticalTranslate the verticalTranslate to set
	 */
	public void setVerticalTranslate (boolean verticalTranslate) {
		this.verticalTranslate = verticalTranslate;
	}

	/**
	 * @return the horizontalTranslate
	 */
	public boolean isHorizontalTranslate() {
		return horizontalTranslate;
	}

	/**
	 * @param horizontalTranslate the horizontalTranslate to set
	 */
	public void setHorizontalTranslate(boolean horizontalTranslate) {
		this.horizontalTranslate = horizontalTranslate;
	}
	
	/**
	 * abonnement d'un ecouteur des translation
	 * @param listener
	 */
	public void addTranslationListener (ChartRenderTranslationListener listener) {
		if(!translationListener.contains(listener))
			translationListener.add(listener);
	}
	
	/**
	 * desabonnement d'un ecouteur de translation
	 * @param listener
	 */
	public void removeTranslationListener (ChartRenderTranslationListener listener) {
		translationListener.remove(listener);
	}

	/**
	 * @return the mouseLineColor
	 */
	public Color getMouseLineColor() {
		return mouseLineColor;
	}

	/**
	 * @param mouseLineColor the mouseLineColor to set
	 */
	public void setMouseLineColor(Color mouseLineColor) {
		if(mouseLineColor.getRGB() == this.mouseLineColor.getRGB())
			return;
		
		this.mouseLineColor = mouseLineColor;
		repaint();
	}
	
	public void setLineAxisColor (Color lineAxisColor) {
		if(this.lineAxisColor.getRGB() == lineAxisColor.getRGB())
			return;
		
		this.lineAxisColor = lineAxisColor;
		repaint();
	}

	/**
	 * @return the lineAxisColor
	 */
	public Color getLineAxisColor() {
		return lineAxisColor;
	}

	/**
	 * @return the mouseLineVisible
	 */
	public boolean isMouseLineVisible() {
		return mouseLineVisible;
	}

	/**
	 * @param mouseLineVisible the mouseLineVisible to set
	 */
	public void setMouseLineVisible(boolean mouseLineVisible) {
		this.mouseLineVisible = mouseLineVisible;
	}

	@Override
	public void doLayout() {
		super.doLayout();
		prepareRender(getWidth(), getHeight());
	}
	
	/**
	 * @return the gridColor
	 */
	public Color getGridColor() {
		return gridColor;
	}

	/**
	 * @param gridColor the gridColor to set
	 */
	public void setGridColor (Color gridColor) {
		if(this.gridColor.getRGB() == gridColor.getRGB())
			return;
		
		this.gridColor = gridColor;
		repaint();
	}

	/**
	 * @return the gridXstep
	 */
	public double getGridXstep() {
		return gridXstep;
	}

	/**
	 * @param gridXstep the gridXstep to set
	 */
	public void setGridXstep(double gridXstep) {
		if(gridXstep == this.gridXstep)
			return;
		
		this.gridXstep = gridXstep;
		repaint();
	}

	/**
	 * @return the gridYstep
	 */
	public double getGridYstep() {
		return gridYstep;
	}

	/**
	 * @param gridYstep the gridYstep to set
	 */
	public void setGridYstep(double gridYstep) {
		if(gridYstep == this.gridYstep)
			return;
		
		this.gridYstep = gridYstep;
		repaint();
	}

	/**
	 * @return the gridXvisible
	 */
	public boolean isGridXvisible() {
		return gridXvisible;
	}

	/**
	 * @param gridXvisible the gridXvisible to set
	 */
	public void setGridXvisible(boolean gridXvisible) {
		if(this.gridXvisible == gridXvisible)
			return;
		
		this.gridXvisible = gridXvisible;
		repaint();
	}

	/**
	 * @return the gridYvisible
	 */
	public boolean isGridYvisible() {
		return gridYvisible;
	}

	/**
	 * @param gridYvisible the gridYvisible to set
	 */
	public void setGridYvisible(boolean gridYvisible) {
		if(this.gridYvisible == gridYvisible)
			return;
		
		this.gridYvisible = gridYvisible;
		repaint();
	}

	/**
	 * @return the xRation
	 */
	public double getxRation() {
		return xRation;
	}

	/**
	 * @return the yRation
	 */
	public double getyRation() {
		return yRation;
	}

	/**
	 * @return the widthRender
	 */
	public double getWidthRender() {
		return widthRender;
	}

	/**
	 * @return the heightRender
	 */
	public double getHeightRender() {
		return heightRender;
	}

	@Override
	protected void paintBorder(Graphics g) {
		super.paintBorder(g);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		
		if (model != null && model.getBorderColor() != null && borderRect != null) {
			g2.setStroke(borderStroke);
			g2.setColor(model.getBorderColor());
			g2.draw(borderRect);
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		
		doPaint(g2, getWidth(), getHeight());
		
		if (mouseLocation != null ) {
			if (mouseLineVisible) {
				g2.setStroke(mouseLineWidth);
				g2.setColor(mouseLineColor);
				int x = (int) mouseLocation.getX(), y = (int) mouseLocation.getY();
				g2.drawLine(0, y, getWidth(), y);
				g2.drawLine(x, 0, x, getHeight());
				showAxisLabel(mouseLocation, g2);
			}
			
			if (hoverChart != -1 && hoverPoint != -1) {
				PreparedPointCloud c = chartMetadatas.get(hoverChart);
				PreparedMaterialPoint p = chartMetadatas.get(hoverChart).getPoints()[hoverPoint];
				g2.setColor(c.getCloud().getBorderColor());
				g2.fill(p.getShape());
				drawPopupLabel(c, p, mouseLocation, g2);
			}
		}
	}
	
	/** 
	 * utilitaire qui dessine les graphiques
	 * @param g2
	 * @param width
	 * @param height
	 */
	private void doPaint(Graphics2D g2, double width, double height) {
		for (int i = 0, count = chartMetadatas.size(); i < count; i++) {
			PreparedPointCloud  cloud = chartMetadatas.get(i);
			PointCloud chart = cloud.getCloud();
			
			if(cloud.getCloud().isFill()){
				g2.setColor(cloud.getCloud().getBackgroundColor());
				g2.fill(cloud.getArea());
			}
			
			g2.setStroke(mouseLineWidth);
			if (chart.getType() == CloudType.STICK_CHART) {
				for (int j = 0; j < cloud.getCloud().countPoints(); j++) {
					if(cloud.getPoints()[j].getPoint().getY() == 0)
						continue;
					g2.setColor(chart.getBackgroundColor());
					g2.fill(cloud.getPoints()[j].getShape());
					
					g2.setColor(cloud.getCloud().getPointAt(j).getBorderColor());
					g2.setColor(chart.getBorderColor());
					g2.draw(cloud.getPoints()[j].getShape());
				}
			} else {
				
				for (int j = 0; j < cloud.getCloud().countPoints(); j++) {
					g2.setColor(cloud.getCloud().getPointAt(j).getBackgroundColor());
					g2.fill(cloud.getPoints()[j].getShape());
					
					g2.setColor(cloud.getCloud().getPointAt(j).getBorderColor());
					g2.draw(cloud.getPoints()[j].getShape());
				}
			}
			
			if (cloud.getCloud().getType() == CloudType.STICK_CHART)
				continue;
			
			g2.setStroke(cloud.getStroke());
			g2.setColor(cloud.getCloud().getBorderColor());
			g2.drawPolyline(cloud.getX(), cloud.getY(), cloud.getCloud().countPoints());
		}
		paintGrid(g2, width, height);
		
		if(xlineAxis != null || ylineAxis != null)
			g2.setColor(lineAxisColor);
		
		if(xlineAxis  != null){
			g2.draw(xlineAxis);
			g2.fillPolygon(fleshXlineAxis[0], fleshXlineAxis[1], 3);
		}
		
		if(ylineAxis  != null){
			g2.draw(ylineAxis);
			g2.fillPolygon(fleshYlineAxis[0], fleshYlineAxis[1], 3);
		}
	}
	
	/** 
	 * utilitaire qui dessine le graphique dans un canvas expterne
	 * @param g
	 * @param bkg, la couleur d'arriere plan (est nullable)
	 * @param width
	 * @param height
	 */
	public void paint (Graphics2D g, Color bkg, int width, int height) {
		setVisible(false);
		
		prepareRender(width, height);
		if(bkg != null){
			g.setColor(bkg);
			g.fillRect(0, 0, width, height);
		}
		
		doPaint(g, width, height);
		//border
		g.setStroke(borderStroke);
		g.setColor(model.getBorderColor());
		g.draw(borderRect);
		g.drawRect(0, 0, width-1, height-1);
		//==
		
		prepareRender(getWidth(), getHeight());
		setVisible(true);
	}
	
	@Override
	public int print (Graphics graphics, PageFormat page, int pageIndex) throws PrinterException {
		if (pageIndex > 0)
			return NO_SUCH_PAGE;
		
		setVisible(false);
		Graphics2D g2 = (Graphics2D) graphics;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.translate(page.getImageableX(), page.getImageableY());
		
		double width = page.getImageableWidth();
		double height = page.getImageableHeight();
		
		if(page.getOrientation() == PageFormat.PORTRAIT)
			height /= 2;
		
		prepareRender(width, height);
		doPaint(g2, width, height);//paint charts
		
		//border
		g2.setStroke(borderStroke);
		g2.setColor(model.getBorderColor());
		g2.draw(borderRect);
		//==
		
		prepareRender(getWidth(), getHeight());
		setVisible(true);
		return PAGE_EXISTS;
	}
	
	/**
	 * visialisation des label sur les axes
	 * @param point
	 * @param g2
	 */
	private void showAxisLabel (Point2D point, Graphics2D g2) {
		
		FontMetrics metrics = g2.getFontMetrics();
		String xTxt = model.getXAxis().getLabelOf(mouseXvalue);
		String yTxt = model.getYAxis().getLabelOf(mouseYvalue);
		
		xTxt = xTxt.replace("\n", " ");
		
		float 
			p = 4,//padding
			h = metrics.getHeight() + p * 2;//height
		
		double 
			recXw = metrics.stringWidth(xTxt) + p * 2,
			recYw = metrics.stringWidth(yTxt) + p * 2;
		
		float 
			xRecX = (float) (point.getX() - recXw/2), 
			yRecX = getHeight() - paddingBottom + metrics.getHeight()/2f;
		
		float 
			xRecY = 0, 
			yRecY = (float) (point.getY() - metrics.getHeight());
		
		recMouseXvalue.setRoundRect(xRecX, yRecX, recXw, h, p * 2, p * 2);
		recMouseYvalue.setRoundRect(xRecY, yRecY, recYw, h, p * 2, p * 2);
		
		g2.setColor(mouseLineColor);
		g2.fill(recMouseXvalue);
		g2.fill(recMouseYvalue);
		
		g2.setColor(Color.WHITE);
		g2.draw(recMouseXvalue);
		g2.draw(recMouseYvalue);
		
		g2.drawString(yTxt, xRecY + p, yRecY + p + metrics.getAscent());//Y axis
		g2.drawString(xTxt, xRecX + p, yRecX + p + metrics.getAscent());//X axis
	}
	
	/**
	 * rendu du label de survole sur un point
	 * @param data
	 * @param g2
	 */
    private void drawPopupLabel(PreparedPointCloud chart, PreparedMaterialPoint data, Point2D point, Graphics2D g2) {

		float x = (float) point.getX() + 10f;//x par defaut
		float y = (float) point.getY();//y par defaut
		
    	final float labelPadding = 4f;

        //h1: titre de niveau 1
        //h2: titre de niveau 2
        String h2 = chart.getCloud().getTitle();
        String h1 = data.getPoint().getLabelX()+" ("+data.getPoint().getLabelY()+")";
       
        FontMetrics fmH1 = g2.getFontMetrics(getFont().deriveFont(Font.PLAIN, g2.getFont().getSize2D()));
        FontMetrics fmH2 = g2.getFontMetrics(getFont().deriveFont(Font.BOLD, g2.getFont().getSize2D()));
        Rectangle2D r1 = fmH1.getStringBounds(h1, g2);
        Rectangle2D r2 = fmH1.getStringBounds(h2, g2);
        
        double widthH2 = r2.getWidth() + (labelPadding * 2),
        		widthH1 = r1.getWidth() + (labelPadding * 2);
        double width = Math.max(widthH1, widthH2);
        
        double height = r1.getHeight() + r2.getHeight() + labelPadding;
        double recY = (y + height) <= (getHeight() - paddingBottom)?  y : y - height;
        double recX = (x + width) <= (getWidth() - padding)?  x : x - width;
        
        
        Color c = new Color(0x99000000 & getBackground().getRGB(), true);
        g2.setColor(c);
        RoundRectangle2D rec = new RoundRectangle2D.Double(recX, recY, width, height, 5, 5);
        
        g2.fill(rec);
        g2.setColor(Color.WHITE);
        g2.draw(rec);
        g2.setFont(getFont().deriveFont(Font.PLAIN, g2.getFont().getSize2D()));
        g2.drawString(h2, (float)recX + labelPadding, (float) (recY + fmH1.getAscent() + labelPadding));
        g2.setFont(getFont().deriveFont(Font.BOLD, g2.getFont().getSize2D()));
        g2.drawString(h1, (float)recX + labelPadding, (float) (recY + height - r2.getHeight() + fmH2.getAscent() - + labelPadding/2f));
    }
	
    /**
     * dessinage de la grille
     * @param g
     * @param width : largeur max disponible
     * @param height : hauteur max disponible
     */
	private void paintGrid (Graphics2D g, double width, double height) {
		
		double xMax = model.hasVisibleChart()? model.getXMax().getX() : 10;
		double yMax = model.hasVisibleChart()? model.getYMax().getY() : 10;
		
		double xMin = model.hasVisibleChart()? model.getXMin().getX() : - xMax;
		double yMin = model.hasVisibleChart()? model.getYMin().getY() : - yMax;
		
		if(yMax == 0 && yMin == 0) {
			yMax = 5;
			yMin = -yMax;
		} else if (yMax == yMin) {
			yMin = yMax / 4f;
		}
		
		if(xMax == 0 && xMin == 0) {
			xMax = 5;
			xMin = -xMax;
		} else if (xMax == xMin) {
			xMin = xMax / 4f;
		}
		
		g.setStroke(gridStroke);
		g.setColor(gridColor);
		Font font = g.getFont();
		FontMetrics metrics = g.getFontMetrics(font);
		
		if(xlineAxis != null) {//pour le cas où l'axe est visible
			int y = 0;
			final double percentAxis = (100f / heightRender) * (xlineAxis.getY1() - padding);//pourcentage de la position de l'axe
			for (double i = xlineAxis.getY1(); i > 0; i -= gridYstep) {
				y = (int) i;
				double percent = (100f / heightRender) * (i - padding);
				double real = Math.abs(percent - percentAxis) * (yMax / percentAxis);
				if(real == 0 || y < padding)
					continue;
				
				String txt = model.getYAxis().getLabelOf(real);
				if(gridYvisible) {			
					g.setColor(gridColor);
					g.drawLine((int)(paddingLeft - padding/2), y, (int)(width - (padding/2)), y);
				}
				g.setColor(lineAxisColor);
				g.drawString(txt, 0f, y + metrics.getHeight() / 4);
			}
			
			y = 0;
			//das le cas ou Y max != zero, pour eviter d'avoir valeur / zero = infinity
			//cas où le max possible sur y = 0
			final double ration = (yMax != 0) ? yMax / percentAxis : Math.abs(yMin) / 100f;
			for (double i = xlineAxis.getY1(); y <= height; i += gridYstep) {

				y =  (int) i;
				double percent = (100f / heightRender) * (i - padding);
				double real = -Math.abs(percent - percentAxis) * ration;
				if(real == 0f || y > (height - paddingBottom))
					continue;
				
				String txt = model.getYAxis().getLabelOf(real);
				if(gridYvisible) {			
					g.setColor(gridColor);
					g.drawLine((int)(paddingLeft - padding/2), y, (int)(width - (padding/2)), y);
				}
				g.setColor(lineAxisColor);
				g.drawString(txt, 0f, y + metrics.getHeight() / 4);
			}
			
			if (mouseLocation != null) {//lors du survol de la sourie
				double percent = (100f / heightRender) * (mouseLocation.getY() - padding);
				mouseYvalue = Math.abs(percent - percentAxis) * ration;
				if(mouseLocation.getY() > xlineAxis.getY1()) {
					mouseYvalue *= -1;
				} 
			}
		} else {//dans le cas où l'axe n'est pas visible
			final double ration = (yMax - yMin) / 100f;
			for (double i = heightRender + padding; i >= 0; i -= gridYstep) {
				int y = (int)(i);
				
				if (y < padding)
					continue;
				
				double percent = (100f / heightRender) * (i - padding);
				double real = Math.abs(percent - 100f) * ration;
				real += yMin;
				
				String txt = model.getYAxis().getLabelOf(real);
				if(gridYvisible) {	
					g.setColor(gridColor);
					g.drawLine((int)(paddingLeft - padding/2), y, (int)(width - (padding/2)), y);
				}
				g.setColor(lineAxisColor);
				g.drawString(txt, 0f, y + metrics.getHeight() / 4);
			}
			
			if (mouseLocation != null) {//lors du survol de la sourie
				double percent = (100f / heightRender) * (mouseLocation.getY() - padding);
				mouseYvalue = Math.abs(percent - 100f) * ((yMax - yMin) / 100f);
				mouseYvalue += model.getYMin().getY();
			}
		}
		
		if(ylineAxis != null) {	
			double percentAxis = (100f / widthRender) * (ylineAxis.getX1() - paddingLeft + padding);
			double ration = Math.abs(xMin) / percentAxis;
			for (double i = ylineAxis.getX1(); i > 0; i -= gridXstep) {
				int x = (int)i;
				double percent = (100f / widthRender) * (i - paddingLeft);
				double real = -Math.abs(percent - percentAxis) * ration;
				
				if(real == 0 || x < paddingLeft)
					continue;
				String txt = model.getXAxis().getLabelOf(real);
				String [] txts = txt.split("\n");
				if (gridXvisible) {		
					g.setColor(gridColor);
					g.drawLine(x, (int)padding/2, x, (int) (height - paddingBottom + padding/2));
				}
				g.setColor(lineAxisColor);
				if (txts.length == 2) {
					g.drawString(txts[0], 2 + x - metrics.stringWidth(txts[0]) / 2, (float)(height - padding * 2));
					g.drawString(txts[1], 2 + x - metrics.stringWidth(txts[1]) / 2, (float)(height - padding/2f));
				} else {					
					g.drawString(txt, 2 + x - metrics.stringWidth(txt) / 2, (float)(height - padding/2f));
				}
			}
			
			ration = Math.abs(xMax) / percentAxis;
			for (double i = ylineAxis.getX1(); i < width; i += gridXstep) {
				int x = (int)i;
				double percent = (100f / widthRender) * (i - paddingLeft);
				double real = Math.abs(percent - percentAxis) * ration;
				
				if(real == 0 || x > (width - padding))
					continue;
				
				String txt = model.getXAxis().getLabelOf(real);
				String[] txts = txt.split("\n");
				if(gridXvisible) {			
					g.setColor(gridColor);
					g.drawLine(x, (int)padding/2, x, (int) (height - paddingBottom + padding/2));
				}
				g.setColor(lineAxisColor);
				if (txts.length == 2) {
					g.drawString(txts[0], 2 + x - metrics.stringWidth(txts[0]) / 2, (float)(height - padding * 2));
					g.drawString(txts[1], 2 + x - metrics.stringWidth(txts[1]) / 2, (float)(height - padding/2f));
				} else {					
					g.drawString(txt, 2 + x - metrics.stringWidth(txt) / 2, (float)(height - padding/2f));
				}
			}
			
			if (mouseLocation != null) {//lors du survol de la sourie
				ration = mouseLocation.getX() > ylineAxis.getX1()? Math.abs(xMax) / percentAxis : Math.abs(xMin) / percentAxis;
				double percent = (100f / widthRender) * (mouseLocation.getX() - paddingLeft);
				mouseXvalue = Math.abs(percent - percentAxis) * ration;
				if(mouseLocation.getX() < ylineAxis.getX1()) {
					mouseXvalue *= -1;
				} 
			}
		} else {
			for (double i = widthRender + paddingLeft; i >= 0 ; i -= gridXstep) {
				int x = (int)i;
				
				if (x < paddingLeft)
					continue;
				
				double percent = (100f / widthRender) * (i - paddingLeft);
				double real = Math.abs(percent) * ((xMax - xMin) / 100f);
				real += xMin;
				
				String txt = model.getXAxis().getLabelOf(real);
				String[] txts = txt.split("\n");
				if(gridXvisible) {			
					g.setColor(gridColor);
					g.drawLine(x, (int)padding/2, x, (int) (height - paddingBottom + padding/2));
				}
				g.setColor(lineAxisColor);
				if (txts.length == 2) {
					g.drawString(txts[0], 2 + x - metrics.stringWidth(txts[0]) / 2, (float)(height - padding * 2));
					g.drawString(txts[1], 2 + x - metrics.stringWidth(txts[1]) / 2, (float)(height - padding/2f));
				} else {					
					g.drawString(txt, 2 + x - metrics.stringWidth(txt) / 2, (float)(height - padding/2f));
				}
			}
			
			if (mouseLocation != null) {//lors du survol de la sourie
				double percent = (100f / widthRender) * (mouseLocation.getX() - paddingLeft);
				mouseXvalue = Math.abs(percent) * ((xMax - xMin) / 100f);
				mouseXvalue += xMin;
			}
		}
		
	}
	
	/**
	 * utilitaire de preparation du rendu du graphique (Soit pour l'impression, soit pour dessiner la vue)
	 * @param width : largeur max disponible
	 * @param height : hauteur max disponile
	 */
	private synchronized void prepareRender (double width, double height) {
		chartMetadatas.clear();
		
		double xMax = model.hasVisibleChart()? model.getXMax().getX() : 10;
		double yMax = model.hasVisibleChart()? model.getYMax().getY() : 10;
		
		double xMin = model.hasVisibleChart()? model.getXMin().getX() : - xMax;
		double yMin = model.hasVisibleChart()? model.getYMin().getY() : - yMax;
		
		if(yMax == 0 && yMin == 0) {
			yMax = 5;
			yMin = -5;
		} else if (yMax == yMin) {
			yMin = yMax / 4f;
		}
		
		if(xMax == 0 && xMin == 0) {
			xMax = 5;
			xMin = -5;
		} else if (xMax == xMin) {
			xMin = xMax / 4f;
		}
		
		final double absXmax = Math.abs(xMax);
		final double absYmax = Math.abs(yMax);
		final double absXmin = Math.abs(xMin);
		final double absYmin = Math.abs(yMin);
		
		final String labelYMax = model.getYAxis().getLabelOf(yMax+0.01f).length() > model.getYAxis().getLabelOf(yMin-0.01f).length()? model.getYAxis().getLabelOf(yMin+0.01f) : model.getYAxis().getLabelOf(yMin-0.01f);
		FontMetrics metrics = getFontMetrics(getFont());
		paddingLeft = metrics.stringWidth(labelYMax) + padding * 2f;
		
		widthRender = width - (padding + paddingLeft);
		heightRender = height - (padding + paddingBottom);
		
		xRation = widthRender / Math.max(absXmin, absXmax);
		yRation = heightRender / Math.max(absYmin, absYmax);
		
		double yXline = -1;// le y de l'axe des X
		double xYline = -1;// le x de l'axe des Y
		double maxX = 0;
		double maxY = 0;
		
		//recherche de la valeur a translater sur X
		if (xMax < 0) {//tout les X sont dans R-
			translateX = absXmin;
			maxX =Math.max(xMin + translateX, xMax + translateX);
		} else if (xMin > 0) {//tout les X sont dans R+
			translateX = -absXmin;
			maxX = Math.max(xMin + translateX, xMax + translateX);
		} else {//pour R*
			translateX = absXmin;
			maxX = absXmin + absXmax;
			xYline = (widthRender / maxX) * translateX + paddingLeft; // xRation...
		}
		xRation = widthRender / maxX;
		//==
		
		//recherche de la valeur a translater sur Y
		if (yMax < 0) {//tous les Y sont dans R-
			translateY = absYmin ;
			maxY = Math.max(yMin + translateY, yMax + translateY);
		} else if (yMin > 0) {//tous les Y sont dans R+
			translateY = -absYmin;
			maxY = Math.max(yMin + translateY, yMax + translateY);
		} else {//pour R*
			translateY = absYmin;
			maxY = absYmax + absYmin;
			yXline = (-(heightRender / maxY) * translateY) + heightRender + padding;//-yRation ...
		}
		yRation = heightRender / maxY;
		//
		
		if (yXline != -1) {//l'axe des X est visible
			xlineAxis = new Line2D.Double(0, yXline, width, yXline);
			int h = (int) (padding/4f), w = (int) (padding/2f);
			fleshXlineAxis[0][0] = (int)width - w;
			fleshXlineAxis[0][1] = (int)width;
			fleshXlineAxis[0][2] = (int)width - w;
			fleshXlineAxis[1][0] = (int)(xlineAxis.getP1().getY() - h);
			fleshXlineAxis[1][1] = (int)(xlineAxis.getP1().getY());
			fleshXlineAxis[1][2] = (int)(xlineAxis.getP1().getY() + h);
		} else xlineAxis = null;
		
		if (xYline != -1) {//l'axe des Y est visible
			ylineAxis = new Line2D.Double(xYline, 0, xYline, height);
			int w = (int) (padding/4f), h = (int) (padding/2f);
			fleshYlineAxis[0][0] = (int)(ylineAxis.getP1().getX() - w);
			fleshYlineAxis[0][1] = (int)(ylineAxis.getP1().getX());
			fleshYlineAxis[0][2] = (int)(ylineAxis.getP1().getX() + w);
			fleshYlineAxis[1][0] = h;
			fleshYlineAxis[1][1] = 0;
			fleshYlineAxis[1][2] = h;
		} else ylineAxis = null;
		
		for (int i = 0, count = model.getSize(); i < count; i++) {
			if (!model.getChartAt(i).isVisible() || model.getChartAt(i).countPoints() == 0)
				continue;
			
			PreparedPointCloud  cloud = null;
			if (model.getChartAt(i).getType() == CloudType.STICK_CHART)
				cloud = createPreparedStickChart(model.getChartAt(i));
			else 
				cloud = createPreparedLineChart(model.getChartAt(i));
			
			chartMetadatas.add(cloud);
		}
		
		//determination du bordure
		borderRect.setRect(paddingLeft - padding/2, padding/2, widthRender + padding, heightRender + padding);
	}
	
	/**
	 * normalisation d'un point.
	 * vue que le repere de l'ecran d'un ordinateur ne sont pas identique a ceux utiliser courament en Math
	 * (en math Y+ -> vers le haut, alors que pour l'ecran d'un PC Y+ -> vers le bas)
	 * pour Y on cherche la valeur negative des coordonnee d'un point sur Y, puis on fait un translation de la hauteur disponible.
	 * En plus pour eviter des  surprise (Ex: pour des graphique qui contiens des valeur tres elevee ou dea valeurs tres inferieur a 0)
	 * on prefere translater le graphique, de sorte que la plus petite valeur sur les axes soit = a zero
	 * @param point
	 * @return
	 */
	protected MaterialPoint normalize (MaterialPoint point) {
		MaterialPoint p = new DefaultMaterialPoint(point);
		double x = (xRation * (point.getX() + translateX)) + paddingLeft;
		double y = (-yRation * (point.getY() + translateY)) + heightRender + padding;
		p.translateXY(x, y);
		return p;
	}
	
	/**
	 * transpose un point appartement au plan du rendu dans un plan le l'espace reel 
	 * des donnees du graphique.
	 * En realite les cordonnées  du point en parametre sont translater
	 * @param point
	 * @return
	 */
	protected Point2D transpose (Point2D point) {
		double x = 0, y = 0;
		
		if(model.getXMin() == null || model.getXMin() == null) {
			point.setLocation(x, y);
			return point;
		}
		
		//transposing of Y
		if (xlineAxis != null) {//pour le cas où l'axe est visible
			final double percentAxis = (100f / heightRender) * (xlineAxis.getY1() - padding);//pourcentage de la position de l'axe
			final double ration = (model.getYMax().getY() != 0) ? model.getYMax().getY() / percentAxis : Math.abs(model.getYMin().getY()) / 100f;
			final double percent = (100f / heightRender) * (point.getY() - padding);
			y = Math.abs(percent - percentAxis) * ration;
			if(point.getY() > xlineAxis.getY1())
				y *= -1;
		} else {
			final double percent = (100f / heightRender) * (point.getY() - padding);
			y = Math.abs(percent - 100f) * ((model.getYMax().getY() - model.getYMin().getY()) / 100f);
			y += model.getYMin().getY();
		}
		//==
		
		//transposing of X
		if(ylineAxis != null) {
			final double percentAxis = (100f / widthRender) * (ylineAxis.getX1() - padding);
			final double ration = point.getX() > ylineAxis.getX1()? Math.abs(model.getXMax().getX()) / percentAxis : Math.abs(model.getXMin().getX()) / percentAxis;
			final double percent = (100f / widthRender) * (point.getX() - padding);
			x = Math.abs(percent - percentAxis) * ration;
			if (point.getX() < ylineAxis.getX1())
				x *= -1;
		} else {
			final double percent = (100f / widthRender) * (point.getX() - padding);
			x = Math.abs(percent) * ((model.getXMax().getX() - model.getXMin().getX()) / 100f);
			x += model.getXMin().getX();
		}
		//==
		
		point.setLocation(x, y);
		return point;
	}
	
	/**
	 * @param chart
	 * @return
	 */
	private PreparedPointCloud createPreparedLineChart (PointCloud chart) {
		
		PreparedMaterialPoint [] list = new PreparedMaterialPoint[chart.countPoints()];
		MaterialPoint []  points = chart.getPoints();
		int length = points.length+2;
		int [] xs = new int[length];
		int [] ys = new int[length];
		
		final float space = chart.getBorderWidth() / 2.0f;
		
		for (int  i = 0; i < points.length; i++) {
			MaterialPoint point = normalize(points[i]);
			
			xs[i] = (int) (point.getX() - space);
			ys[i] = (int) (point.getY() - space);
			
			double eX = xs[i] - points[i].getSize() / 2.0f;
			double eY = ys[i] - points[i].getSize() / 2.0f;
			
			Ellipse2D e = new Ellipse2D.Double(eX, eY, points[i].getSize(), points[i].getSize());
			list[i] = new PreparedMaterialPoint(e, point);
		}
		
		Polygon poly  = null;
		
		if (chart.isFill()) {
			xs[length-2] = xs[length-3];
			xs[length-1] = xs[0];
			
			int y = (int) (xlineAxis != null? (xlineAxis.getP1().getY()) : (chart.getYMax().getY() > 0 ? heightRender : 0) + padding );
			ys[length-2] = y;
			ys[length-1] = y;
			
			poly = new Polygon(xs, ys, length);
		} 

		return new PreparedPointCloud(poly, chart, list, xs, ys);
	}
	
	private PreparedPointCloud createPreparedStickChart (PointCloud chart) {
		PreparedMaterialPoint [] list = new PreparedMaterialPoint[chart.countPoints()];
		MaterialPoint []  points = chart.getPoints();
		int length = points.length+2;
		int [] xs = new int[length];
		int [] ys = new int[length];
		
		double size = xRation * chart.getDefaultPointSize();
		double limit = xlineAxis == null? (model.getYMin().getY() > 0? 0 : heightRender) : xlineAxis.getY1();
		
		for (int  i = 0; i < points.length; i++) {
			MaterialPoint point = normalize(points[i]);
			
			xs[i] = (int) point.getX();
			ys[i] = (int) point.getY();
			
			double h = 0;
			if(ys[i] <= limit)
				h = (heightRender - (translateY * yRation)) - ys[i] + padding;
			else 
				h =  ys[i] - (heightRender - (translateY * yRation)) - padding;
			
			Rectangle2D rect = new Rectangle2D.Double(xs[i] - (size/2), ys[i] > limit? limit : ys[i], size, h);
			
			Area area = new Area(rect);			
			list[i] = new PreparedMaterialPoint(area, point);
		}
		
		Polygon poly  = null;
		
		if (chart.isFill()) {
			xs[length-2] = xs[length-3];
			xs[length-1] = xs[0];
			
			int y = (int) (xlineAxis != null? (xlineAxis.getP1().getY()) : (chart.getYMin().getY() < chart.getYMax().getY()? heightRender : 0) + padding );
			ys[length-2] = y;
			ys[length-1] = y;
			
			poly = new Polygon(xs, ys, length);
		} 

		return new PreparedPointCloud(poly, chart, list, xs, ys);
	}

	/**
	 * 
	 * @author Esaie MUHASA
	 *
	 */
	protected static class PreparedMaterialPoint {
		
		private Shape shape;
		private MaterialPoint point;
		
		
		/**
		 * @param shape
		 * @param point
		 */
		public PreparedMaterialPoint(Shape shape, MaterialPoint point) {
			super();
			this.shape = shape;
			this.point = point;
		}


		/**
		 * verification de l'appartenance du point Geometrique au point materiel
		 * @param M
		 * @return
		 */
		public boolean match (Point M) {
			return shape.contains(M);
		}


		/**
		 * @return the shape
		 */
		public Shape getShape() {
			return shape;
		}


		/**
		 * @return the point
		 */
		public MaterialPoint getPoint() {
			return point;
		}
	}
	
	/**
	 * 
	 * @author Esaie MUHASA
	 *
	 */
	protected static class PreparedPointCloud {
		
		private final Shape area;
		private final PointCloud cloud;
		private final PreparedMaterialPoint [] points;
		private final BasicStroke stroke;
		
		private final int [] x;
		private final int [] y;
		
		/**
		 * @param area
		 * @param cloud
		 * @param points
		 * @param x
		 * @param y
		 */
		public PreparedPointCloud(Shape area, PointCloud cloud, PreparedMaterialPoint[] points, int[] x, int[] y) {
			super();
			this.area = area;
			this.cloud = cloud;
			this.points = points;
			this.x = x;
			this.y = y;
			stroke = new BasicStroke(cloud.getBorderWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		}


		/**
		 * Verifie si la ligne apartiens a la ligne de bordure du nuage de point
		 * @param M
		 * @return
		 */
		public boolean match (Point M) {
			if(area == null)
				return false;
			
			return area.contains(M);
		}


		/**
		 * @return the points
		 */
		public PreparedMaterialPoint[] getPoints() {
			return points;
		}


		/**
		 * @return the area
		 */
		public Shape getArea() {
			return area;
		}


		/**
		 * @return the cloud
		 */
		public PointCloud getCloud() {
			return cloud;
		}


		/**
		 * @return the x
		 */
		public int[] getX() {
			return x;
		}


		/**
		 * @return the y
		 */
		public int[] getY() {
			return y;
		}


		/**
		 * @return the stroke
		 */
		public BasicStroke getStroke() {
			return stroke;
		}
		
	}
	
	/**
	 * Ecouteur des changement du model
	 * @author Esaie MUHASA
	 *
	 */
	protected class ModelListener implements CloudChartModelListener {

		@Override
		public void onChange(CloudChartModel model) {
			prepareRender(getWidth(), getHeight());
			repaint();
		}

		@Override
		public void onChartChange(CloudChartModel model, int index) {
			onChange(model);
		}

		@Override
		public void onPointChange(CloudChartModel model, int chartIndex, int pointIndex) {
			onChange(model);
		}

		@Override
		public void onInsertChart(CloudChartModel model, int chartIndex) {
			onChange(model);
		}

		@Override
		public void onRemoveChart(CloudChartModel model, int chartIndex) {
			onChange(model);
		}

		@Override
		public void onInsertPoint(CloudChartModel model, int chartIndex, int pointIndex) {
			onChange(model);
		}

		@Override
		public void onRemovePoint(CloudChartModel model, int chartIndex, int pointIndex, MaterialPoint materialPoint) {
			onChange(model);
		}
		
		
	}
	
	protected class MouseListener extends MouseAdapter {
		
		private Point2D A;//debut du drag de la souri
		private Point2D B;//fin du drag de la souri 
		
		@Override
		public void mouseExited(MouseEvent e) {
			mouseLocation = null;
			A = null;
			B = null;
			repaint();
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		
		@Override
		public void mouseMoved (MouseEvent e) {
			if (borderRect.contains(e.getPoint()) && model.getXMax() != null) {				
				mouseLocation = e.getPoint();
				boolean match = false;
				hoverChart = -1;
				hoverPoint = -1;
				
				for (int i = 0; i < chartMetadatas.size(); i++) {
					PreparedPointCloud c = chartMetadatas.get(i);
					
					for (int j = 0; j < c.getPoints().length; j++) {
						if(c.getPoints()[j].getShape().contains(e.getPoint())) {
							hoverChart = i;
							hoverPoint = j;
							match = true;
							break;
						}
					}
					
					if (match)
						break;
				}
				
				repaint();
			} else 
				mouseExited(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if(!verticalTranslate && !horizontalTranslate)
				return;
			
			setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			A = e.getPoint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(!verticalTranslate && !horizontalTranslate)
				return;
			
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			B = e.getPoint();
			translate();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			mouseMoved(e);
			B = e.getPoint();
		}
		
		/**
		 * traitement de la valeur a translater
		 * Nous avons 3 points: A, B et C. trois points forment un triangle dont l'angle au moint C est de 90°
		 * la distance d(AC) est la variation sur l'axe des X
		 * la distance d(BC) est la variation sur l'axe des Y
		 */
		private void translate () {
			if(A != null && B != null && (verticalTranslate || horizontalTranslate)) {
				
				A = transpose(A);
				B = transpose(B);
				final double dAC = A.distance(B.getX(), A.getY());
				final double dBC = B.distance(B.getX(), A.getY());
				
				final boolean translateToX = dAC != 0 && horizontalTranslate;
				final boolean translateToY = dBC != 0 && verticalTranslate;

				double stepsX = (A.getX() < B.getX()? -1f : 1f) * dAC;
				double stepsY = (A.getY() > B.getY()? -1f : 1f) * dBC;
				
				if (translateToX && translateToY) {//translation sur les deux axe
					Interval xInterval = new Interval(model.getXMin().getX() + stepsX, model.getXMax().getX() + stepsX);
					Interval yInterval = new Interval(model.getYMin().getY() + stepsY, model.getYMax().getY() + stepsY);

					for (ChartRenderTranslationListener ls : translationListener)
						ls.onRequireTranslation(CloudChartRender.this, xInterval, yInterval);

				} else if (translateToX) {//translation du sur l'axe des X
					Interval interval = new Interval(model.getXMin().getX() + stepsX, model.getXMax().getX() + stepsX);
					
					for (ChartRenderTranslationListener ls : translationListener)
						ls.onRequireTranslation(CloudChartRender.this, model.getXAxis(), interval);
				} else if (translateToY) {//triansaltion sur l'axe des Y
					Interval interval = new Interval(model.getYMin().getY() + stepsY, model.getYMax().getY() + stepsY);
					
					for (ChartRenderTranslationListener ls : translationListener)
						ls.onRequireTranslation(CloudChartRender.this, model.getYAxis(), interval);
				}
			}
			
			A = null;
			B = null;
		}

	}
	
	/**
	 * intervale de translation sur l'une des Axes
	 * @author Esaie MUHASA
	 */
	public static class Interval {
		private double min;//la plus petite valeur de l'intervale
		private double max;//la plus grande valeur de l'intervale

		/**
		 * @param min
		 * @param max
		 */
		public Interval(double min, double max) {
			super();
			this.min = min;
			this.max = max;
		}

		/**
		 * @return the min
		 */
		public double getMin() {
			return min;
		}

		/**
		 * @return the max
		 */
		public double getMax() {
			return max;
		}
		
		/**
		 * midification de l'intervale
		 * @param min
		 * @param max
		 */
		public void setInterval (double min, double max) {
			this.min = min;
			this.max = max;
		}
		
		/**
		 * Modification de l'intervale
		 * @param interval
		 */
		public void setInterval (Interval interval) {
			min = interval.min;
			max = interval.max;
		}

		@Override
		public boolean equals (Object obj) {
			if (obj instanceof Interval) {
				Interval i = (Interval) obj;
				return (i.min == min && i.max == max);
			}
			return super.equals(obj);
		}

		@Override
		public String toString() {
			return "Interval [min=" + min + ", max=" + max + "]";
		}
	}
	
	/**
	 * evenement de demande de transaltion
	 * @author Esaie MUHASA
	 */
	public static interface ChartRenderTranslationListener {

		/**
		 * demande de translation sur l'axe en deuxieme parametre
		 * @param source la source de l'evenement
		 * @param axis
		 * @param interval
		 */
		void onRequireTranslation (CloudChartRender source, Axis axis, Interval interval);

		/**
		 * require translation on all axis
		 * @param source
		 * @param xInterval
		 * @param yInterval
		 */
		void onRequireTranslation (CloudChartRender source, Interval xInterval, Interval yInterval);
	}

}
