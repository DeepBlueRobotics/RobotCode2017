package team199.smartdashboard.extensions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

public class DisplayBoilerVision extends StaticWidget{
	private static ITable table = NetworkTable.getTable("SmartDashboard/Vision");
    private final int WID = 320;
    private final int HIGHT = 180;
    private final int BUTTON_HIGHT = 25;
    private final int OFF_X = WID/2;
    private final int OFF_Y = HIGHT/2;
    private final int CALIB_PT_W = 6;
    private final int CALIB_PT_H = 6;
    
    private final double CAMERA_COMPUTER_PIXEL_RATIO = 1/20; //1 cam pixel = 20 comp pixels
    
    //NOTE: -160 <= x <= 160
    //		-90 <= y <= 90
    //these coords are in terms of normal coord plane ((0,0) in middle)
    //also in comp pixels, not cam pixels, so must convert value from table (when GETting) to comp pixels
    //must convert value back to cam pixels when PUTting value in table
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private int centerX;
    private int centerY;
    
    private JPanel grid;
    private JComponent pic;
    private JButton calibrate = new JButton();
	
	@Override
	public void init(){
		try {
            table = NetworkTable.getTable("Vision");
        } catch(Exception e) {
            System.out.println("Vision not found");
            return;
        }
		
		grid = new JPanel(){
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				
				//displays the previous calibrated center point
				g.setColor(Color.RED);
		        g.fillRect(centerX + OFF_X - CALIB_PT_W/2, OFF_Y - centerY - CALIB_PT_H/2,
		        				CALIB_PT_W, CALIB_PT_H);
		        
		        //draws a new line, the middle of which will be the new calibrated center point
		        g.setColor(Color.GRAY);
		        g.drawLine(WID/2, 0, WID/2, HIGHT);
		        g.drawLine(0, HIGHT/2, WID, HIGHT/2);
		        
		        //these temp coords are in terms of the computer's coord plane (top left = (0,0))
		        int tempx1 = x1 + OFF_X;
				int tempy1 = OFF_Y - y1;
				int tempx2 = x2 + OFF_X;
				int tempy2 = OFF_Y - y2;
				g.setColor(Color.BLACK);
				g.drawLine(tempx1, tempy1, tempx2, tempy2);
			}
		};
		
		/**
		 * When Calibrate button pressed, will update variables (updatePosition()),
		 * 	repaint the red point (moveCalibCenter),
		 * 	and send the new calibrated center point to the table (sendCenterPt()).
		 * */
		calibrate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePosition();
                moveCalibCenter((x1+x2)/2, (y1+y2)/2);
                sendCenterPt();
            }
        });
		
		//When a value in the table changes, will update the variables (updatePosition()) & repaint panel
		table.addTableListener(new ITableListener(){
            @Override
            public void valueChanged(ITable itable, String key, Object value, boolean bln) {
                updatePosition();
            	grid.repaint();
            }
        });
        
		setPreferredSize(new Dimension(WID, HIGHT + BUTTON_HIGHT + 5));
		grid.setPreferredSize(new Dimension(WID, HIGHT));
		grid.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		calibrate.setPreferredSize(new Dimension(WID, BUTTON_HIGHT));
		calibrate.setText("Calibrate");
		add(grid);
		add(calibrate);
        
        setVisible(true);
	}
	
	/**
	 * Updates two coordinates' variable, getting values from table
	 * Converts from camera pixels to computer pixels (for drawing/displaying)
	 * */
	public void updatePosition(){
		x1 = (int) (table.getNumber("topBoilerCenterX", -OFF_X) * CAMERA_COMPUTER_PIXEL_RATIO);
		y1 = (int) (table.getNumber("topBoilerCenterY", OFF_Y) * CAMERA_COMPUTER_PIXEL_RATIO);
		x2 = (int) (table.getNumber("bottomBoilerCenterX", OFF_X) * CAMERA_COMPUTER_PIXEL_RATIO);
		y2 = (int) (table.getNumber("bottomBoilerCenterY", -OFF_Y) * CAMERA_COMPUTER_PIXEL_RATIO);
	}
	
	/**
	 * Updates center point coordinates in table, putting in the center x and y
	 * Converts from computer pixels to camera pixels (for robot adjustment)
	 */
	public void sendCenterPt(){
		table.putNumber("boilerCenterX", centerX/CAMERA_COMPUTER_PIXEL_RATIO);
		table.putNumber("boilerCenterY", centerY/CAMERA_COMPUTER_PIXEL_RATIO);
	}
	
	/**
	 * Repaints the calibrated center point
	 * */
	private void moveCalibCenter(int x, int y) {
        int OFFSET = 1;
        if ((centerX!=x) || (centerY!=y)) {
            grid.repaint(centerX + OFF_X - CALIB_PT_W/2, OFF_Y - centerY - CALIB_PT_H/2, CALIB_PT_W +
            				OFFSET, CALIB_PT_H + OFFSET);
            centerX = x;
            centerY = y;
            grid.repaint(centerX + OFF_X - CALIB_PT_W/2, OFF_Y - centerY - CALIB_PT_H/2, CALIB_PT_W +
            				OFFSET, CALIB_PT_H + OFFSET);
        } 
    }
	
	@Override
	public void propertyChanged(Property prprt){
	}
}
