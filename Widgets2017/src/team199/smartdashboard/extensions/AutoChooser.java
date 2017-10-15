package team199.smartdashboard.extensions;

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.robot.Robot;
//import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 *
 * @author NelsonYip
 */
public class AutoChooser extends StaticWidget {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String NAME = "Auto Mode Chooser";
    private final JLabel title = new JLabel("Auto Mode Chooser");
    private final JPanel mainPanel = new JPanel();
    private final JComboBox<String> autoBox = new JComboBox<String>();
    private final JCheckBox redBox = new JCheckBox();
    private final JCheckBox blueBox = new JCheckBox();
    private final JLabel blueLbl = new JLabel("Blue");
    private final JLabel redLbl = new JLabel("Red");
    private final JButton saveBtn = new JButton("Save");
    private ITable prefs;
    private boolean blue = true;
	private final Color deepblue = new Color(0, 1, 99);

    @Override
    public void init() {
    	
        prefs = Robot.getPreferences();
        
        setPreferredSize(new Dimension(310, 120));
        title.setForeground(Color.WHITE);
        title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 24));
        mainPanel.setPreferredSize(new Dimension(310,120));
        setLayout(new FlowLayout());
        mainPanel.setLayout(new FlowLayout());
        mainPanel.setBackground(deepblue);
		setBackground(deepblue);
        autoBox.setPreferredSize(new Dimension(200, 25));
//        redBox.setPreferredSize(new Dimension(30, 40));
        redLbl.setForeground(Color.white);
//        redLbl.setPreferredSize(new Dimension(30, 50));
        saveBtn.setPreferredSize(new Dimension(90, 25));
//        blueBox.setPreferredSize(new Dimension(30 , 40));
//        blueLbl.setPreferredSize(new Dimension(30 , 50));
        blueLbl.setForeground(Color.white);
        
        add(mainPanel);
        mainPanel.add(title);
        mainPanel.add(autoBox);
        mainPanel.add(saveBtn);
        mainPanel.add(blueBox);
        mainPanel.add(blueLbl);
        mainPanel.add(redBox);
        mainPanel.add(redLbl);
        update();
        
        autoBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                update();
            }
        });
        
        
        autoBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                updateTable();
            }
        });
        
        blueBox.addActionListener((ActionEvent e) -> {
        	if(blue == false)
        		redBox.setSelected(false);
        	blue = true;
        	updateTable();
        });
        
        redBox.addActionListener((ActionEvent e) -> {
        	if(blue == true)
        		blueBox.setSelected(false);
        	blue = false; 
        	updateTable();
        });
        
        saveBtn.addActionListener((ActionEvent e) -> {
            prefs.putBoolean(Robot.PREF_SAVE_FIELD, true);
        });
        
    }
    
    @Override
    public void propertyChanged(Property prprt) {
    }
    
    private void update() {
        String key = autoBox.getSelectedItem() + "";
        autoBox.removeAllItems();
        autoBox.addItem("Choose auto mode...");
        autoBox.addItem("Boiler");
        autoBox.addItem("Center");
        autoBox.addItem("Load");
        autoBox.addItem("Dead reckoning");
        autoBox.setSelectedItem(key);
        repaint();
    }
    
    private void updateTable() {
        String autoLocationValue = autoBox.getSelectedItem() + "";
//        if(autoLocationValue.equals("Boiler")){
//        	if(blue)
//        		autoLocationValue = "Left";
//        	else
//        		autoLocationValue = "Right";
//        }
//        else if(autoLocationValue.equals("Load")){
//        	if(blue)
//        		autoLocationValue = "Right";
//        	else
//        		autoLocationValue = "Left";
//        }
        prefs.putString("Auto location", autoLocationValue);
        prefs.putBoolean("Blue", blue);
    }
}

