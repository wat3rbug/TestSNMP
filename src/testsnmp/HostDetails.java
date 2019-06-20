/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testsnmp;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author douglas
 */
public class HostDetails extends JFrame {
    
    private JPanel namePanel;
    private JPanel addresses;
    private JLabel ipv4;
    private JLabel ipv6;
    
    public HostDetails(SNMPHost host) {
        
        EmptyBorder empty = new EmptyBorder(0,10,0,10);
        // name section
        
        this.setTitle("Host: " + host.Hostname);
        TitledBorder nameBorder = BorderFactory.createTitledBorder("Name");
        nameBorder.setTitleJustification(TitledBorder.CENTER);
        namePanel = new JPanel();
        namePanel.setBorder(nameBorder);
        namePanel.setLayout(new GridLayout(2, 2));
        JLabel nameLbl = new JLabel("Function:", SwingConstants.RIGHT);
        nameLbl.setBorder(empty);
        JLabel name = new JLabel(host.Hostname);
        JLabel fqdn = new JLabel(host.fqdn);
        JLabel fqdnLbl = new JLabel("FQDN:", SwingConstants.RIGHT); 
        fqdnLbl.setBorder(empty);
        fqdnLbl.setAlignmentX(RIGHT_ALIGNMENT);
        namePanel.add(nameLbl);
        namePanel.add(name);
        namePanel.add(fqdnLbl);
        namePanel.add(fqdn);
        this.getContentPane().add(namePanel);
        
        // network section
        
        addresses = new JPanel();
        TitledBorder networkBorder = BorderFactory.createTitledBorder("Network");
        networkBorder.setTitleJustification(TitledBorder.CENTER);
        addresses.setBorder(networkBorder);
        addresses.setLayout(new GridLayout(3, 2));
        ipv4 = new JLabel();
        ipv4.setBorder(empty);
        ipv6 = new JLabel();
        ipv6.setBorder(empty);
        JLabel ipv4Lbl = new JLabel("IPv4", SwingConstants.RIGHT);
        ipv4Lbl.setBorder(empty);
        JLabel ipv6Lbl = new JLabel("IPv6", SwingConstants.RIGHT);  
        ipv6Lbl.setBorder(empty);
        JLabel netLbl = new JLabel("Net util%:", SwingConstants.RIGHT);
        netLbl.setBorder(empty);
        JLabel net = new JLabel(new Double(host.net).toString());
        net.setBorder(empty);
        addresses.add(ipv4Lbl);
        addresses.add(ipv4);
        addresses.add(ipv6Lbl);
        addresses.add(ipv6);
        addresses.add(netLbl);
        addresses.add(net);
        this.getContentPane().add(addresses);
        
        // service section
        
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
        while ((temp = host.next()) != null) {
            JLabel nameTemp = new JLabel(temp.serviceName);
            nameTemp.setBorder(empty);
            JLabel statusTemp = new JLabel(temp.serviecOID);
            statusTemp.setBorder(empty);
            servicesPanel.add(nameTemp);
            servicesPanel.add(statusTemp);
        }
        this.getContentPane().add(servicesPanel);
        this.getContentPane().setLayout(new GridLayout(3, 1));
        this.setMinimumSize(new Dimension(200, 300));
        this.pack();
    }
    
}
