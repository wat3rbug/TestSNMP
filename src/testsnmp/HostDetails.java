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
    private JLabel net;
    private ArrayList<ServiceStatus>serviceDictionary;
    
    
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
        name = new JLabel(host.Hostname);
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
        JLabel netLbl = new JLabel("Net util%:", SwingConstants.RIGHT);
        netLbl.setBorder(empty);
        net = new JLabel(Double.toString(host.net));
        net.setBorder(empty);
        addresses.add(ipv4Lbl);
        addresses.add(ipv4);
        addresses.add(ipv6Lbl);
        addresses.add(ipv6);
        addresses.add(netLbl);
        addresses.add(net);
        this.getContentPane().add(addresses);
        
        // srv section
        
        JPanel servicesPanel = new JPanel();
        TitledBorder serviceBorder = BorderFactory.createTitledBorder("Services");
        serviceBorder.setTitleJustification(TitledBorder.CENTER);
        servicesPanel.setBorder(serviceBorder);
        servicesPanel.setLayout(new GridLayout(host.numOfServices() + 1, 2));
        JLabel srvNameLbl = new JLabel("Name", SwingConstants.RIGHT);
        srvNameLbl.setBorder(empty);
        JLabel srvStatusLbl = new JLabel("Status");
        srvStatusLbl.setBorder(empty);
        servicesPanel.add(srvNameLbl);
        servicesPanel.add(srvStatusLbl);
        Service temp = null;
        serviceDictionary = new ArrayList<ServiceStatus>();
        while ((temp = host.next()) != null) {
            JLabel nameTemp = new JLabel(temp.serviceName,  SwingConstants.RIGHT);
            nameTemp.setBorder(empty);
            JLabel statusTemp = new JLabel(temp.serviceOID);
            statusTemp.setBorder(empty);
            servicesPanel.add(nameTemp);
            servicesPanel.add(statusTemp);
            ServiceStatus srv = new ServiceStatus(temp.serviceName, 
                    statusTemp.getText());
            serviceDictionary.add(srv);
        }
        this.getContentPane().add(servicesPanel);
        
        // the close button
        
        JButton closer = new JButton("Close");
        this.getContentPane().add(closer);
        closer.addActionListener(new CloseListener());
        this.getContentPane().setLayout(new GridLayout(4, 1));
        this.setMinimumSize(new Dimension(200, 330));
        this.pack();
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
     * This method is used from the main to update the window because the host,
     * by library design, cannot perform status updates about itself.
     */
    
    public void updateView() {
        // get info updated from host and display it
        ipv4.setText(host.getIPv4Address());
        ipv6.setText(host.getIPv6Address());
        net.setText(Double.toString(host.net));
    }
    
}
