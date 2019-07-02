package testsnmp;

import java.awt.Color;
import javax.swing.JLabel;

/**
 * This JLabel include information about a service that is polled via SNMP.  It
 * holds a name, and then the OID for that service.
 * 
 * @author douglas
 */

public class SimpleServiceLabel extends JLabel {
    
    private Service service;
    
    /**
     * The constructor includes a Service for which the label will use for the
     * creation of the class.
     * 
     * @param srv The Service object to be assigned to the label.
     */
    
    public SimpleServiceLabel(Service srv) {
        super(srv.serviceName);
        setText("Unknown");
        setForeground(Color.white);
        setBackground(Color.blue);
        setOpaque(true);
        service = srv;
    }
    
    /**
     * Return the the Service object used by the label.  This may be deleted 
     * depending on whether is actually got used.
     * 
     * @return Service object assigned to the label. 
     */
    
    public Service getService() {
        return service;
    }
    
    /**
     * Gets the String of the OID that was parsed and passed to the Label during
     * creation of the object.
     * 
     * @return A String of the OID used for polling the service. 
     */
    
    public String getOIDName() {
        return service.serviceOID;
    }
    
    /**
     * I may split this into another class, but I will have to research it.
     * 
     * @param value updates the label color depending on the value.  The value 
     * is an integer 1 if true and 0 if false.
     */
    
    public void updateServiceStatus(int value) {
        if (value >0) {
            this.setOpaque(true);
            this.setBackground(Color.green);
            this.setForeground(Color.black);
            this.setText("Running");
        } else {
            this.setOpaque(true);
            this.setBackground(Color.red);
            this.setForeground(Color.black);
            this.setText("Stopped");
        }
    }
}
