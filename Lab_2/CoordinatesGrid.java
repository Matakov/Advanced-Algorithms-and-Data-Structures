import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.List;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.MouseInputListener;


public class CoordinatesGrid {
	private JLabel label;

	private static Point clickPoint;

	private Point cursorPoint;
	
	private static ArrayList<Point> clickPoints = null;

	private void buildUI(Container container) {
	    container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

	    CoordinateArea coordinateArea = new CoordinateArea(this);
	    container.add(coordinateArea);

	    label = new JLabel();
	    resetLabel();
	    container.add(label);

	    coordinateArea.setAlignmentX(Component.LEFT_ALIGNMENT);
	    label.setAlignmentX(Component.LEFT_ALIGNMENT); // redundant
	  }

	public void updateCursorLocation(int x, int y) {
	    if (x < 0 || y < 0) {
	      cursorPoint = null;
	      updateLabel();
	      return;
	    }

	    if (cursorPoint == null) {
	      cursorPoint = new Point();
	    }

	    cursorPoint.x = x;
	    cursorPoint.y = y;
	    updateLabel();
	  }

	  public void updateClickPoint(Point p) {
	    clickPoint = p;
	    //System.out.println(clickPoints);
	    //if (clickPoints == null){
	    //	clickPoints = new ArrayList<Point>();
	    //}
	    //clickPoints.add(p);
	    //System.out.println(clickPoints);
	    updateLabel();
	  }

	  public void resetLabel() {
	    cursorPoint = null;
	    updateLabel();
	  }

	  protected void updateLabel() {
	    String text = "";

	    if ((clickPoint == null) && (cursorPoint == null)) {
	      text = "Click or move the cursor within the framed area.";
	    } else {

	      if (clickPoint != null) {
	        text += "The last click was at (" + clickPoint.x + ", " + clickPoint.y + "). ";
	      }

	      if (cursorPoint != null) {
	        text += "The cursor is at (" + cursorPoint.x + ", " + cursorPoint.y + "). ";
	      }
	    }

	    label.setText(text);
	  }

	  public static void main(String[] args) {
	    JFrame frame = new JFrame("CoordinatesDemo");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    CoordinatesGrid controller = new CoordinatesGrid();
	    controller.buildUI(frame.getContentPane());

	    frame.pack();
	    frame.setVisible(true);
    frame.setFocusable(true);
    frame.requestFocusInWindow();
  }

  public static class CoordinateArea extends JComponent implements MouseInputListener,KeyListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Point point = null;
    ArrayList<Point> points = null;

    CoordinatesGrid controller;

    Dimension preferredSize = new Dimension(500, 500);

    Color gridColor;

    private class MyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                //System.out.println(points);
                GeneticAlgotihm output = new GeneticAlgotihm(points, (int)1, (float)0.5, (int)2000, 50);
                //output.printCities();
                clickPoints=output.algotihtm();
                //clickPoints=output.greedyAlgorithmCities();
                
                //System.out.println(output.minDistanceGreedy());
                repaint();
                //paintComponent(null);
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                //System.out.println("2test2");
            } else if (e.getID() == KeyEvent.KEY_TYPED) {
                //System.out.println("3test3");
            }
            return false;
        }
    }
    
    
    public CoordinateArea(CoordinatesGrid controller) {
      this.controller = controller;

      // Add a border of 1 pixels at the left and bottom,
      // and 1 pixel at the top and right.
      setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
      
      KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
      manager.addKeyEventDispatcher(new MyDispatcher());
      addMouseListener(this);
      addMouseMotionListener(this);
      addKeyListener(this);
      setBackground(Color.WHITE);
      setOpaque(true);
    }

    public Dimension getPreferredSize() {
      return preferredSize;
    }

    protected void paintComponent(Graphics g) {
      // Paint background if we're opaque.
      if (isOpaque()) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
      }

      // Paint 20x20 grid.
      g.setColor(Color.GRAY);
      drawGrid(g, 20);

      // If user has chosen a point, paint a small dot on top.
      if (point != null) {
    	  g.setColor(getForeground());
    	  g.fillRect(point.x - 3, point.y - 3, 7, 7);
      }
      if (points != null) {
    	  for (int i=0;i<points.size();i++){
    		  //System.out.println(i+1);
    		  g.setColor(getForeground());
    		  g.fillRect(points.get(i).x - 3, points.get(i).y - 3, 7, 7);  
    	  }
          
      }
      if (clickPoints != null){
    	  int j=0;
    	  for (int i=0;i<clickPoints.size();i++){
    		  if(i+1>=clickPoints.size()){
    			  j=0;
    		  }
    		  else{
    			  j=i+1;
    		  }
    		  Point X = clickPoints.get(i);
    		  Point Y = clickPoints.get(j);
    		  g.drawLine(X.x, X.y, Y.x, Y.y);
    	  }
      }
     
    }

    // Draws a 20x20 grid using the current color.
    private void drawGrid(Graphics g, int gridSpace) {
      Insets insets = getInsets();
      int firstX = insets.left;
      int firstY = insets.top;
      int lastX = getWidth() - insets.right;
      int lastY = getHeight() - insets.bottom;

      // Draw vertical lines.
      int x = firstX;
      while (x < lastX) {
        g.drawLine(x, firstY, x, lastY);
        x += gridSpace;
      }

      // Draw horizontal lines.
      int y = firstY;
      while (y < lastY) {
        g.drawLine(firstX, y, lastX, y);
        y += gridSpace;
      }
    }

    // Methods required by the MouseInputListener interface.
    public void mouseClicked(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();
      if (point == null) {
        point = new Point(x, y);
      } else {
        point.x = x;
        point.y = y;
      }
      if (points==null){
    	  points = new ArrayList<Point>();
      }
      points.add(new Point(x,y));
      //System.out.println(points);
      controller.updateClickPoint(point);
      
      repaint();
    }

    public void mouseMoved(MouseEvent e) {
      controller.updateCursorLocation(e.getX(), e.getY());
    }

    public void mouseExited(MouseEvent e) {
      controller.resetLabel();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }
    
    public void keyTyped(KeyEvent e) {
    	System.out.println(points);
    	System.exit(1);
    	
    }

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println(clickPoint);
		System.exit(1);
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	 public void actionPerformed(ActionEvent e) {
		 int id = e.getID();
	    	if (id == 101){
	    		System.out.println(points);
	    		System.exit(1);
	    	}
	 }
  }


}