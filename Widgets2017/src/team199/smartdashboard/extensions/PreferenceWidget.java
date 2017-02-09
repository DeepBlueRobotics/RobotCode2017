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
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 * A widget that provides an easy way to view and modify preferences.
 */
public class PreferenceWidget extends StaticWidget {

    public static final String NAME = "Preference Widget";
    private final JComboBox keyBox = new JComboBox();
    private final JTextField valueField = new JTextField();
    private ITable prefs = null;
    private final JButton saveButton = new JButton("Save");
    private final JButton removeButton = new JButton("Remove");

    @Override
    public void init() {
        try {
            prefs = Robot.getPreferences();
        } catch(Exception e) {
            System.out.println("Preferences not found");
            return;
        }
        setPreferredSize(new Dimension(215, 90));
        keyBox.setPreferredSize(new Dimension(200, 25));
        valueField.setPreferredSize(new Dimension(200, 25));
        saveButton.setPreferredSize(new Dimension(100, 25));
        removeButton.setPreferredSize(new Dimension(100, 25));
        keyBox.addItem("New Preference");
        add(keyBox);
        add(valueField);
        add(saveButton);
        add(removeButton);
        update();
        
        keyBox.addActionListener((ActionEvent e) -> {
            readValueOfCurrentKey();
        });
        keyBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                update();
            }
        });
        
        valueField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                writeValueOfCurrentKey();
            }
        });
        
        prefs.addTableListener((ITable itable, String key, Object value, boolean isNew) -> {
            readValueOfCurrentKey();
        }, true);
        
        saveButton.addActionListener((ActionEvent e) -> {
            prefs.putBoolean(Robot.PREF_SAVE_FIELD, true);
        });
        
        removeButton.addActionListener((ActionEvent e) -> {
            String key = keyBox.getSelectedItem() + "";
            prefs.delete(key);
            valueField.setText("");
            update();
        });
    }

    @Override
    public void propertyChanged(Property prprt) {
    }
    
    // Organizes the JComboBox to display alphabetically
    private void update() {
        String key = keyBox.getSelectedItem() + "";
        Object[] temp = prefs.getKeys().toArray();
        Arrays.sort(temp);
        keyBox.removeAllItems();
        keyBox.addItem("New Preference");
        for (Object o: temp) {
            if(!o.equals("~S A V E~")){
                keyBox.addItem(o);
            }
        }
        keyBox.setSelectedItem(key);
        repaint();
    }

    // Sets the value field to the correct value
    private void readValueOfCurrentKey() {
        String key = keyBox.getSelectedItem() + "";
        if (prefs.containsKey(key)) {
            valueField.setText(prefs.getValue(key, "") + "");
        } else {
            valueField.setText("");
        }    
    }
    
    // Changes the value of the selected key in the preferences table
    private void writeValueOfCurrentKey() {
        String key = keyBox.getSelectedItem() + "";
        String value = valueField.getText();
        // Add key
        if (key.equals("New Preference") && !value.equals("") && !prefs.containsKey(value)) {
            write(value, "0.0");
            update();
        }
        // Change value
        if(!key.equals("New Preference")) {
            write(key, value);
        }
    }
    
    // Writes a preference to the table
    private void write(String key, String value) {
        if(value.equals("true")||value.equals("false")) {
            prefs.putBoolean(key, value.equals("true"));
        } else {
            try {
                prefs.putNumber(key, Double.parseDouble(value));
            } catch(Exception e) {
                System.out.println("Error: Wrong data type");
            }
        }
    }
}