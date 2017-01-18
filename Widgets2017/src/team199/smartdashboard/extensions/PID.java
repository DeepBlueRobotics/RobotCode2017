package team199.smartdashboard.extensions;

import java.awt.FlowLayout;
import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.robot.Robot;
import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.gui.elements.LinePlot;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.properties.StringProperty;
import edu.wpi.first.smartdashboard.types.DataType;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Widget that helps with PID tuning.
 */

public class PID extends StaticWidget {

    public static final String NAME = "PID Tuner";

    private final JButton reset = new JButton("Reset Graphs");
    private final JButton testPID = new JButton("Start PID");
    private final JButton save = new JButton("Save");
    
    private LinePlot errorPlot = new LinePlot();
    private LinePlot outputPlot = new LinePlot();
    private final MyTextBox[] boxes = new MyTextBox[12];
    private final String[] boxNames = {"kP", "kI", "kD", "TestTarget", "SensorValue", "Input", "Error", "Target", "Output", "Interval", "Rate", "TotalError"};
    private JLabel j;
    
    private final JPanel p1 = new JPanel();
    private final JPanel p2 = new JPanel();
    
    private String name = "Default";
    public final StringProperty loopName = new StringProperty(this, "LoopName", "Default");
    private NetworkTable sd = NetworkTable.getTable("SmartDashboard/PID/Default");
    private ITable prefs;
    
    private final Color deepblue = new Color(0, 1, 99);

    @Override
    public void propertyChanged(Property prop) {
        if (prop == loopName) {
            name = loopName.getValue();
            sd = NetworkTable.getTable("SmartDashboard/PID/" + name);
            j.setText(name+"PID");
            p1.removeAll();
            
            for(int i=0; i<boxes.length; i++){
                final MyTextBox box = new MyTextBox("PID/"+name+"/");
                boxes[i] = box;
                    addWidget(box, boxNames[i], p1, DataType.NUMBER);
                if(sd.containsKey(boxNames[i])){
                    box.setValue(sd.getValue(boxNames[i], 0));
                } else {
                    sd.putNumber(boxNames[i], 0);
                    box.setValue(sd.getNumber(boxNames[i], 0));
                }
                final String prefKey = (boxNames[i]);
                if (i>3) {
                    box.editable.setValue(false);
                } else if(i<=3){
//                    if(prefs.containsKey(prefKey)){
//                        boxes[i].setValue(prefs.getValue(prefKey, ""));
//                    }
                    box.addSpecialListener(new FocusAdapter(){
                        @Override
                        public void focusLost(FocusEvent e) {
                            try {
                                sd.putNumber(prefKey, Double.parseDouble(box.getText()));
                            } catch (Exception ex) {
                                System.out.println("Incorrect input");
                            }
                        }
                        
                    });
                }
            }
            
            p1.add(testPID);
            p1.add(reset);
            p1.add(save);
            
            resetGraphs();
        }
    }

    @Override
    public void init() {
        try {
            prefs = Robot.getPreferences();
        } catch(Exception e) {
            System.out.println("Preferences not found");
        }
        setPreferredSize(new Dimension(825, 444));
        setLayout(new FlowLayout());
        GridLayout g = new GridLayout(3,5);
        g.setHgap(5);
        g.setVgap(5);
        p1.setLayout(g);
        p1.setBackground(deepblue);
        p2.setLayout(new GridLayout(1,2));
        p2.setBackground(deepblue);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGraphs();
            }
        });
        testPID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(sd.containsSubTable("Test"+name+"PID")) {
                    ITable t2 = sd.getSubTable("Test"+name+"PID");
                    t2.putBoolean("running", !t2.getBoolean("running"));
                    if(t2.getBoolean("running")){
                        testPID.setText("Cancel PID");
                    } else {
                        testPID.setText("Start PID");
                    }
                }
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = 0; i < 3; i++){
                    prefs.putString(name + boxNames[i], boxes[i].getText());
                }
                Robot.getPreferences().putBoolean(Robot.PREF_SAVE_FIELD, true);
            }
        });
        j = new JLabel(name+"PID");
        j.setForeground(Color.WHITE);
        j.setFont(new Font(j.getFont().getFontName(), Font.BOLD, 30));
        add(j);
        add(p1);
        add(p2);
        propertyChanged(loopName);
        // Listen for new SD data
        sd.addTableListener(new ITableListener(){
            @Override
            public void valueChanged(ITable itable, String key, Object value, boolean bln) {
                System.out.println(key);
                for(int i=0; i<boxes.length; i++){
                    if(key.equals(boxNames[i])){
                        boxes[i].setValue(value);
                    }
                }
                if(key.equals("Error")){
                    errorPlot.setValue(value);
                } else if(key.equals("Output")){
                    outputPlot.setValue(value);
                }
            }
        });
        //Listen for external preference modification
        prefs.addTableListener(new ITableListener() {
            @Override
            public void valueChanged(ITable itable, String string, Object o, boolean bln) {
                for(int i=0; i<4; i++){
                    if(string.equals((name+boxNames[i]).replace(" ", "_"))){
                        boxes[i].setValue(o);
                        sd.putNumber((boxNames[i]), Double.parseDouble(o+""));
                    }
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(deepblue);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    // Adds widget to dashboard
    private void addWidget(Widget w, String s, JComponent p, DataType d) {
        w.setFieldName(s);
        w.setType(d);
        w.init();
        p.add(w);
    }
    
    // Clears the line plots
    private void resetGraphs(){
        p2.removeAll();
        errorPlot = new LinePlot();
        outputPlot = new LinePlot();
        // The graphs don't need to set the field name to the proper SD path
        addWidget(errorPlot, "Error", p2, DataType.NUMBER);
        addWidget(outputPlot, "Output", p2, DataType.NUMBER);
        revalidate();
    }
}
