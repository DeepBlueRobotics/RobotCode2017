package team199.smartdashboard.extensions;

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;

/**
 *
 * @author lauramcgann
 */
public class LaurasWidget extends StaticWidget {

    /**
     * @param args the command line arguments
     */
    
    private static ITable table = NetworkTable.getTable("Move values");
    private final int SIZE = 480;//translates to real world in inches
    private final int robotW = 20;
    private final int robotH = 20;
    
    //robot and prev X's and Y's are without built-in offsets
    private int robotX = SIZE/2;
    private int robotY = SIZE/2;
    private int prevX = robotX;
    private int prevY = robotY;
    private double prevBearing = 0;

    private int drawRobotX = robotX - robotW/2;
    private int drawRobotY = robotY - robotH/2;
    
    @Override
    public void init(){
        try {
            table = NetworkTable.getTable("Move values");
        } catch(Exception e) {
            System.out.println("Move values not found");
            return;
        }
        setPreferredSize(new Dimension(SIZE, SIZE));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setVisible(true);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                moveSquare(e.getX(),e.getY());
                calcToTable();
            }
        });

        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                moveSquare(e.getX(),e.getY());
                calcToTable();
            }
        });
    }
    
    /*
    Visually moves the "robot" (blue square) to where the mouse was clicked.
    Updates stored "robot" x and y coordinates
    def: computer positioning: (0,0) is top left; x increases positively right;
        y increases positively down; no negatives
    @param x the new x coordinate in terms of computer positioning
    @param y the new y coordinate in terms of computer positioning
    */
    private void moveSquare(int x, int y) {
        int OFFSET = 1;
        if ((robotX!=x) || (robotY!=y)) {
            repaint(drawRobotX, drawRobotY, robotW + OFFSET, robotH + OFFSET);
            robotX = x;
            robotY = y;
            setDraws();
            repaint(drawRobotX, drawRobotY, robotW + OFFSET, robotH + OFFSET);
        } 
    }
    
    /*
    Updates the x and y coordinates used to draw the "robot" (blue square)
    centered on the position of the mouse click
    */
    private void setDraws(){
        drawRobotX = robotX - robotW/2;
        drawRobotY = robotY - robotH/2;
    }
    
    /*
    Calculates the forward distance the robot needs to travel and the angle
    the robot needs to turn from its current position in order for that
    distance to be correct.
    Sends distance and angle to a NetworkTable, later accessible by the robot.
    */
    private void calcToTable(){
        double dist = findDist(robotX - prevX, (-1)*(robotY - prevY));
        
        double angle = Math.toDegrees(Math.atan2((-1)*(robotY - prevY), robotX - prevX));
        double bearing = 90 - angle;
        if(bearing > 180)
            bearing = bearing - 360;
        double gyroAngle = bearing - prevBearing;

        double oppAngle = calcOppositeAngle(gyroAngle);
        
        if(Math.abs(gyroAngle) > Math.abs(oppAngle))
            gyroAngle = oppAngle;
        
        table.putNumber("distance", dist);
        table.putNumber("gyroAngle", gyroAngle);
        
        prevBearing = bearing;
        prevX = robotX;
        prevY = robotY;
    }
    
    /*
    Finds the hypotenuse of the triangle whose legs are the parameters.
    @param xChange the horizontal component traveled and horizontal leg
    @param yChange the vertical component traveled and vertical leg
    @return the hypotenuse and actual distance traveled
    */
    private double findDist(int xChange, int yChange){
        return Math.sqrt(xChange*xChange + yChange*yChange);
    }
    
    /*
    Calculates the opposite angle with the same terminal side
    @param gyroAngle the current angle the robot will be told to turn
    @return the opposite angle (of the parameter) with the same terminal side
    */
    private double calcOppositeAngle(double gyroAngle){
        double oppAngle = 0;
        if (gyroAngle > 0)
            oppAngle = gyroAngle - 360;
        else oppAngle = gyroAngle + 360;
        return oppAngle;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);       
        g.setColor(Color.BLUE);
        g.fillRect(drawRobotX,drawRobotY,robotW,robotH);
        g.setColor(Color.BLACK);
        g.drawRect(drawRobotX,drawRobotY,robotW,robotH);
        g.drawLine(SIZE/2, 0, SIZE/2, SIZE);
        g.drawLine(0, SIZE/2, SIZE, SIZE/2);
    }
    
    /*
    just in case we need it later (to tell which end of blue box is front)
    needs modification for our case
    supposed to draw a circle with center at params x and y and radius of param r
    */
    private void drawCenteredCircle(Graphics2D g, int x, int y, int r) {
        x = x-(r/2);
        y = y-(r/2);
        g.fillOval(x,y,r,r);
    }
    
    @Override
    public void propertyChanged(Property prprt) {
    }
    
}
