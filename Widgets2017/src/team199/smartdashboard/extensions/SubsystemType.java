package team199.smartdashboard.extensions;

import edu.wpi.first.smartdashboard.types.NamedDataType;

/**
 * A widget data type to represent a subsystem.
 *
 * @author Paul
 */
public class SubsystemType extends NamedDataType {

    public static final String LABEL = "SubSystem";

    private SubsystemType() {
        super(LABEL, OmniWidget.class);
    }

    public static NamedDataType get() {
        if (NamedDataType.get(LABEL) != null) {
            return NamedDataType.get(LABEL);
        } else {
            return new SubsystemType();
        }
    }
}
