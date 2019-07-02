package testsnmp;

import javax.swing.JLabel;

/**
 * An interface for labeling warnings.  It is used to enforce a contract of 
 * behavior of its subsequent classes.  In essence, the constructor of the 
 * objects sets up parameters, and then subsequent calls to the updateWarning
 * make the colors based on those parameters . in a consistent way.
 * @author douglas
 */
public interface IWarning {
    
    /**
     * This method updates the colors of the label passed to it based on the 
     * constructor values given.  The value in the argument is the current value
     * as read from the device from an SNMP polling event.
     * 
     * @param label The JLabel to use for updating the background color.
     * @param value The integer value to use for determining the color.
     */
    
    public void updateWarning(JLabel label, int value);
}
