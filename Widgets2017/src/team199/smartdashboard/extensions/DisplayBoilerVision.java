package team199.smartdashboard.extensions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

public class DisplayBoilerVision extends StaticWidget{
	private static ITable table = NetworkTable.getTable("SmartDashboard/Vision");
	private final String[] ipList = { "172.22.11.2", "10.1.99.2", "10.1.99.0", "roboRIO-199-FRC.local" };
    private final int WID = 320;
    private final int HIGHT = 180;
    private final int BUTTON_HIGHT = 25;
    private final int OFF_X = WID/2;
    private final int OFF_Y = HIGHT/2;
    private final int DOT_W = 6;
    private final int DOT_H = 6;
    
    //black dot (current data; moves w/ each table change) starts in center of screen
    private int x = OFF_X;
    private int y = OFF_Y;
    
    //red dot (calibrated center; doesn't move until button pressed) starts off screen
    private int centerX = -DOT_W;
    private int centerY = -DOT_H;
    
    private JPanel grid;
    private JButton calibrate = new JButton();
    private JLabel title = new JLabel("<html><font color='black'>Shooter Vision</font></html>", SwingConstants.CENTER);
	
	@Override
	public void init(){
		NetworkTable.setClientMode();
		NetworkTable.setTeam(199);
		NetworkTable.setIPAddress(ipList);
		NetworkTable.initialize();
		try {
            table = NetworkTable.getTable("SmartDashboard/Vision");
        } catch(Exception e) {
            System.out.println("Vision not found");
            return;
        }
		
		//method called when grid.repaint() is called
		grid = new JPanel(){
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				
				//displays the previous calibrated center point
				g.setColor(Color.RED);
		        g.fillRect(centerX - DOT_W/2, centerY - DOT_H/2, DOT_W, DOT_H);
		        
		        //displays the current center point (from table)
		        g.setColor(Color.BLACK);
		        g.fillRect(x - DOT_W/2, y - DOT_H/2, DOT_W, DOT_H);
			}
		};
		
		/**
		 * When Calibrate button pressed, will update variables (updatePosition())
		 * 	& repaint the red point (moveCalibCenter)
		 * */
		calibrate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePosition();
                moveCalibCenter(x, y);
            }
        });
		
		/**
		 * When a value in the table changes, will update the variables (updatePosition())
		 * 	& repaint panel
		 * */
		table.addTableListener(new ITableListener(){
            @Override
            public void valueChanged(ITable itable, String key, Object value, boolean bln) {
                updatePosition();
            	grid.repaint();
            }
        });
        
		setPreferredSize(new Dimension(WID, HIGHT + 2 * BUTTON_HIGHT + 10));
		title.setPreferredSize(new Dimension(WID, BUTTON_HIGHT));
		title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 20));
		grid.setPreferredSize(new Dimension(WID, HIGHT));
		grid.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		calibrate.setPreferredSize(new Dimension(WID, BUTTON_HIGHT));
		calibrate.setText("Calibrate");
		add(title);
		add(grid);
		add(calibrate);
        
        setVisible(true);
	}
	
	/**
	 * Updates coordinates, getting values from table
	 * */
	public void updatePosition(){
		x = (int) (table.getNumber("boilerX", OFF_X));
		y = (int) (table.getNumber("boilerY", OFF_Y));
	}
	
	/**
	 * Repaints the calibrated center point
	 * */
	private void moveCalibCenter(int x, int y) {
        int OFFSET = 1;
        if ((centerX!=x) || (centerY!=y)) {
            grid.repaint(centerX - DOT_W/2, centerY - DOT_H/2, DOT_W + OFFSET, DOT_H + OFFSET);
            centerX = x;
            centerY = y;
            grid.repaint(centerX - DOT_W/2, centerY - DOT_H/2, DOT_W + OFFSET, DOT_H + OFFSET);
        } 
    }
	
	@Override
	public void propertyChanged(Property prprt){
	}
}
