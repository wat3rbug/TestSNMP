package testsnmp;

import java.awt.Color;
import javax.swing.JLabel;

/**
 * This class is used for updating the JLabels using standard integer values.
 * It is assuming that the values are between 0 and 100 for things like CPU
 * utilization, memory consumption etc.
 * 
 * @author douglas
 */

public class IntWarningLevels implements IWarning{
    
    protected int off;
    protected int warning;
    protected int danger;
    
    /**
     * Constructor requires 3 integer values in its construction.
     * 
     * @param off The point at which the device is considered 'off'.
     * @param warning The percentage value as an integer when it is yellow for a 
     * warning state.
     * @param danger The percentage value as an integer when it is red for being
     * in danger and immediate action should be taken.
     */
    
    public IntWarningLevels(int off, int warning, int danger) {
    
        this.off = off;
        this.warning = warning;
        this.danger = danger;
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
        if (value <= warning && value > off) {
            label.setBackground(null);
            label.setForeground(null);
        }
        if (value == off) {
            label.setBackground(Color.blue);
            label.setForeground(Color.white);
        }
    }
}
