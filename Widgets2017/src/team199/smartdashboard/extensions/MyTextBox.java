package team199.smartdashboard.extensions;

import edu.wpi.first.smartdashboard.gui.elements.bindings.AbstractValueWidget;
import javax.swing.*;
import edu.wpi.first.smartdashboard.properties.*;
import edu.wpi.first.smartdashboard.types.*;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusListener;

/**
 * Implements a simple text box UI element with a name label, but allows for a 
 * different label and field name
 */
public class MyTextBox extends AbstractValueWidget {

    public static final DataType[] TYPES = {DataType.BASIC};
    public static final String NAME = "Text Box";
    public final BooleanProperty editable = new BooleanProperty(this, "Editable", true);
    public final ColorProperty background = new ColorProperty(this, "Background");
    private JTextField valueField;
    private final String prefix;

    public MyTextBox(String prefix){
        super();
        this.prefix = prefix;
    }
    
    @Override
    public void init() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        JLabel nameLabel = new JLabel(getFieldName());
        nameLabel.setForeground(Color.WHITE);
        setFieldName(prefix+getFieldName());
        if (getType().isChildOf(DataType.BOOLEAN)) {
            valueField = new EditableBooleanValueField(getFieldName());
        } else if (getType().isChildOf(DataType.NUMBER)) {
            valueField = new EditableNumberValueField(getFieldName());
        } else if (getType().isChildOf(DataType.STRING)) {
            valueField = new EditableStringValueField(getFieldName());
        } else {
            valueField = new JTextField();
            valueField.setText("Unupported basic data type: " + getType());
            valueField.setEditable(false);
        }
        update(background, valueField.getBackground());
        valueField.setEditable(editable.getValue());
        valueField.setColumns(10);
        add(nameLabel);
        add(valueField);
    }

    @Override
    public void propertyChanged(Property property) {
        if (property == background) {
            valueField.setBackground(background.getValue());
        } else if (property == editable) {
            valueField.setEditable(editable.getValue());
        }
    }

    public String getText() {
        return valueField.getText();
    }

    public void addSpecialListener(FocusListener l) {
        valueField.addFocusListener(l);
    }
}
