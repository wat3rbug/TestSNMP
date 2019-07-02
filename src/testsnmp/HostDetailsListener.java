package testsnmp;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class provides a method of interaction for the HostDetails class.  At 
 * this time only mouse clicks are noticed.  The other methods that are used are
 * only pass-through to the original HostDetail object.
 * 
 * @author douglas
 */
public class HostDetailsListener extends MouseAdapter {
    
    private HostDetails details;
    
    public HostDetailsListener(SNMPHost host) {
        
        details = new HostDetails(host);
    }

    /**
     * Gets the label which include simple service information.  It was the best
     * strategy I had at the time to convey the OID needed for querying the 
     * service from the Pi and provides results to the label.  Positive integers
     * are necessary, and if one is passed to this method that is not in range,
     * a null value is returned.
     * 
     * @param index The positive integer for a particular service.  The number 
     * of the service is determined solely by the listing order in the xml file 
     * for the configuration of the host.
     * @return A JLabel that also includes information about the service it 
     * represents.
     */
    
    public SimpleServiceLabel getSrvLabelAt(int index) {
       // null check is done at HostDetails
       return details.getSrvLabelAt(index);
    } 


    @Override
    public void mouseClicked(MouseEvent e) {
        
        Rectangle mainOrig = e.getComponent().getBounds(); 
        Rectangle result = new Rectangle();
        Dimension dim = e.getComponent().getSize();
        result.x = mainOrig.x + dim.width + 10;
        result.y = mainOrig.y + 47;
        details.setBounds(result);
        
        // get updates
        
        details.updateView();
        details.setVisible(!details.isVisible());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // not used
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
         // not used
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //
    }
}
