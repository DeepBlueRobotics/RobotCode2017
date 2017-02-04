package team199.smartdashboard.extensions;

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.robot.Robot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
/**
 *
 * @author NelsonYip
 */
public class AutoChooser extends StaticWidget {

    public static final String NAME = "Auto Mode Chooser";
    private final JComboBox autoBox = new JComboBox();
    private final JCheckBox redBox = new JCheckBox();
    private final JCheckBox blueBox = new JCheckBox();
    private final JLabel blueLbl = new JLabel("Blue");
    private final JLabel redLbl = new JLabel("Red");
    private final JButton saveBtn = new JButton("Save");
    private final JButton rmvBtn = new JButton("Rmv");
    private ITable prefs;
    private boolean blue;

    @Override
    public void init() {
        try {
            prefs = NetworkTable.getTable("Auto Table");
        } catch(Exception e) {
            prefs = NetworkTable.getTable("Preferences");
            System.out.println("Preferences not found");
        }
        setPreferredSize(new Dimension(400, 150));
        autoBox.setPreferredSize(new Dimension(200, 25));
        redBox.setPreferredSize(new Dimension(30 , 40));
        redLbl.setPreferredSize(new Dimension(30 , 40));
        saveBtn.setPreferredSize(new Dimension(90, 25));
        rmvBtn.setPreferredSize(new Dimension(90, 25));
        blueBox.setPreferredSize(new Dimension(30 , 40));
        blueLbl.setPreferredSize(new Dimension(30 , 40));
    //  autoBox.addItem("Choose auto mode...");
        add(blueBox);
        add(blueLbl);
        add(redBox);
        add(redLbl);
        add(autoBox);
        add(saveBtn);
        add(rmvBtn);
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
            blue = true;
        });
        
        redBox.addActionListener((ActionEvent e) -> {
           blue = false; 
        });
        
        saveBtn.addActionListener((ActionEvent e) -> {
            Robot.getPreferences().putBoolean(Robot.PREF_SAVE_FIELD, true);
        });
        
        rmvBtn.addActionListener((ActionEvent e) -> {
            String key = autoBox.getSelectedItem() + "";
            prefs.delete(key);
            update();
        });
    }
    
    @Override
    public void propertyChanged(Property prprt) {
    }
    
    private void update() {
        String key = autoBox.getSelectedItem() + "";
        /**
        Object[] temp = prefs.getKeys().toArray();
        Arrays.sort(temp);
        */
        autoBox.removeAllItems();
        autoBox.addItem("Choose auto mode...");
        autoBox.addItem("Left");
        autoBox.addItem("Middle");
        autoBox.addItem("Right");
        /**
        for (Object o: temp) {
            autoBox.addItem(o);
        }
        */
        autoBox.setSelectedItem(key);
        repaint();
    }
    
    private void updateTable() {
        String key = autoBox.getSelectedItem() + "";
        prefs.delete("Left");
        prefs.delete("Middle");
        prefs.delete("Right");
        prefs.delete("Blue");
        prefs.delete("Red");
        prefs.putString("autoPost", key);
        prefs.putBoolean("Blue", blue);
    }
}

