package team199.smartdashboard.extensions;

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ManualControl extends StaticWidget {

    private static final JComboBox dropBox = new JComboBox();
    private NetworkTable sd = NetworkTable.getTable("SmartDashboard/ManualControl");
    private  final String[] options = {"Intake", "Feeder", "Climber", "Shooter", "Turret", "Hood"};
    private static final JButton runButton = new JButton("Start");
    private JPanel p = new JPanel();
    private JLabel title = new JLabel("<html><font color='white'>Mech Manual Control</font></html>", SwingConstants.CENTER);

    private final Color deepblue = new Color(0, 1, 99);
    
    @Override
    public void init(){
        
        //fill drop box
        for(String system: options){
            dropBox.addItem(system);
        }
        
        //can also use dropBox.addActionListener((ActionEvent e) -> {dropBox.shopPopup()});
        dropBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                dropBox.showPopup();
            }
            @Override
            public void focusLost(FocusEvent e){
                sd.putValue("Manual Control Mech", dropBox.getSelectedItem().toString());
            }
        });
        
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ITable t2 = sd.getSubTable("Command");
                t2.putBoolean("running", !t2.getBoolean("running", false));
                if(t2.getBoolean("running", false)){
                    runButton.setText("Stop");
                } else {
                    runButton.setText("Start");
                }
            }
        });
        
        setPreferredSize(new Dimension(215, 105));
        dropBox.setPreferredSize(new Dimension(200, 25));
        runButton.setPreferredSize(new Dimension(90, 25));
        
        GridLayout g = new GridLayout(3,1, 5, 5);
        p.setLayout(g);
        p.setBackground(deepblue);
        p.add(title);
        p.add(dropBox);
        p.add(runButton);
        add(p);
    }
    
    @Override
    public void propertyChanged(Property prprt) {
    }
    
}

