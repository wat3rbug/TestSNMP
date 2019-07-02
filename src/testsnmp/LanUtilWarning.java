package testsnmp;

import java.awt.Color;
import javax.swing.JLabel;

/**
 * This class is used for updating the JLabels using standard integer values.
 * This class is particular to Raspberry Pis as they do not emit traffic on a 
 * regular basis due to their design and construction. So 0% does not constitute
 * and 'off' condition, but an inactivity.  It became necessary to make a 
 * special case to handle it.
 * 
 * @author douglas
 */

public class LanUtilWarning extends IntWarningLevels {
    
    /**
     * Constructor requires 3 integer values in its construction.
     * 
     * @param off The point at which the device is considered at baseline 0.
     * @param warning The percentage value as an integer when it is yellow for a 
     * warning state.
     * @param danger The percentage value as an integer when it is red for being
     * in danger and immediate action should be taken.
     */
    
    public LanUtilWarning(int off, int warning, int danger) {
        super(off, warning, danger);
    }
    
    /**
     * This method updates the colors of the label passed to it based on the 
     * constructor values given.  The value in the argument is the current value
     * as read from the device from an SNMP polling event.
     * 
     * @param label The JLabel to use for updating the background color.
     * @param value The integer value to use for determining the color.
     */
    
    @Override
    public void updateWarning(JLabel label, int value) {
        if (value >= danger) {
            label.setBackground(Color.red);
            label.setForeground(Color.black);
        }
        if (value > warning && value <= danger) {
            label.setBackground(Color.yellow);
            label.setForeground(Color.black);
        }
        if (value <= warning && value >= off) {
            label.setBackground(null);
            label.setForeground(null);
        }
    }
}
