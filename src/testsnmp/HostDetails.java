package testsnmp;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;

/**
 * This class provides a means of listing the details of a particular host.  The
 * layout is fairly rudimentary as I only needed a few things more, but I wanted
 * to keep the footprint, or the view of the application, limited.  It was not 
 * included until after version 1.0
 * 
 * @author Douglas Gardiner
 */

public class HostDetails extends JFrame {
    
    private SNMPHost host;
    private JLabel ipv4;
    private JLabel ipv6;   
    private JLabel name;
    private JLabel fqdn;   
    private ArrayList<SimpleServiceLabel> srvResults;
    
    /**
     * The constructor of the class.  Since this is a sing application, the 
     * constructor does all the heavy lifting in order to display all the 
     * elements needed.  At this time it displays name, function, network 
     * utilization and if any special services are being used.
     * 
     * @param host The SNMPHost that is to be viewed.
     */
    
    public HostDetails(SNMPHost host) {
            
        // name section

        srvResults = new ArrayList<SimpleServiceLabel>();
        EmptyBorder empty = new EmptyBorder(0,10,0,10);
        this.host = host;
        this.setTitle("Host: " + host.Hostname);
        TitledBorder nameBorder = BorderFactory.createTitledBorder("Name");
        nameBorder.setTitleJustification(TitledBorder.CENTER);
        JPanel namePanel = new JPanel();
        namePanel.setBorder(nameBorder);
        namePanel.setLayout(new GridLayout(2, 2));
        JLabel nameLbl = new JLabel("Function:", SwingConstants.RIGHT);
        nameLbl.setBorder(empty);
        name = new JLabel(host.function);
        fqdn = new JLabel(host.fqdn);
        JLabel fqdnLbl = new JLabel("FQDN:", SwingConstants.RIGHT); 
        fqdnLbl.setBorder(empty);
        fqdnLbl.setAlignmentX(RIGHT_ALIGNMENT);
        namePanel.add(nameLbl);
        namePanel.add(name);
        namePanel.add(fqdnLbl);
        namePanel.add(fqdn);
        this.getContentPane().add(namePanel);
        
        // network section
        
        JPanel addresses = new JPanel();
        TitledBorder networkBorder = BorderFactory.createTitledBorder("Network");
        networkBorder.setTitleJustification(TitledBorder.CENTER);
        addresses.setBorder(networkBorder);
        addresses.setLayout(new GridLayout(3, 2));
        ipv4 = new JLabel(host.getIPv4Address());
        ipv4.setBorder(empty);
        ipv6 = new JLabel(host.getIPv6Address());
        ipv6.setBorder(empty);
        JLabel ipv4Lbl = new JLabel("IPv4", SwingConstants.RIGHT);
        ipv4Lbl.setBorder(empty);
        JLabel ipv6Lbl = new JLabel("IPv6", SwingConstants.RIGHT);  
        ipv6Lbl.setBorder(empty);
        addresses.add(ipv4Lbl);
        addresses.add(ipv4);
        addresses.add(ipv6Lbl);
        addresses.add(ipv6);
        this.getContentPane().add(addresses);
        
        // srv section
        
        JPanel servicesPanel = new JPanel();
        TitledBorder serviceBorder = BorderFactory.createTitledBorder("Services");
        serviceBorder.setTitleJustification(TitledBorder.CENTER);
        servicesPanel.setBorder(serviceBorder);
        servicesPanel.setLayout(new GridLayout(host.numOfServices() + 2, 2));
        JLabel srvNameLbl = new JLabel("Name", SwingConstants.RIGHT);
        srvNameLbl.setBorder(empty);
        JLabel srvStatusLbl = new JLabel("Status");
        srvStatusLbl.setBorder(empty);
        servicesPanel.add(srvNameLbl);
        servicesPanel.add(srvStatusLbl);
        Service temp = host.next();
        // cludgy, but need to set default if there are no special services to 
        // monitor
        if (temp == null) {
            setEmptyServicePanel(servicesPanel);
        }
        host.reset();
        while ((temp = host.next()) != null) {
            JLabel nameTemp = new JLabel(temp.serviceName, 
                    SwingConstants.RIGHT);
            SimpleServiceLabel statusTemp = new SimpleServiceLabel(temp);
            srvResults.add(statusTemp);
            nameTemp.setBorder(empty); 
            statusTemp.setBorder(empty);
            servicesPanel.add(nameTemp);
            servicesPanel.add(statusTemp);
        }
        this.getContentPane().add(servicesPanel);
        
        // the close button
        
        JButton closer = new JButton("Close");
        this.getContentPane().add(closer);
        closer.addActionListener(new CloseListener());
        this.getContentPane().setLayout(new GridLayout(4, 1));
        this.setMinimumSize(new Dimension(250, 330));
        this.pack();
    }
    
    private void setEmptyServicePanel(JPanel servicePane) {
        Border empty = BorderFactory.createEmptyBorder();
        JLabel name = new JLabel("None", SwingConstants.RIGHT);
        JLabel status = new JLabel("N/A");
        name.setBackground(null);
        name.setForeground(null);
        status.setBackground(null);
        status.setForeground(null);
        servicePane.add(name);
        servicePane.add(status);
        servicePane.setBorder(empty);
    }
    
    /**
     * Gets the host used for polling.  Not my best move, but is result of this 
     * library.  It is needed to poll the simple services.
     * 
     * @return The SNMPHost for which polling is done.  
     */
    
    public SNMPHost getHost() {
        return host;
    }
    
    /**
     * This simple class just closes the detail view for the application.
     */
    
    public class CloseListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            HostDetails.this.setVisible(false);
        }       
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
        if (index >= srvResults.size()) return null;
        else return srvResults.get(index);
    }
    
    /** 
     * This method is used from the main to update the window because the host,
     * by library design, cannot perform status updates about itself.
     */
    
    public void updateView() {

        ipv4.setText(host.getIPv4Address());
        ipv6.setText(host.getIPv6Address());
    }
    
}
