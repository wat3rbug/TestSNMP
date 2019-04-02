/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testsnmp;

/**
 *
 * @author Your Name <Douglas Gardiner>
 */

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.MessageException;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;


public class TestSNMP {

    /**
     * @param args the command line arguments
     */

    static final int SEC = 1000;
    static final int NUM_OF_SECS = 5;
    static final int INSET = 5;
    static final int TIMEOUT_SEC = 5;
    String address = null;
    static Snmp snmp = null;
    static JLabel hostTitle = null;
    static JLabel cpuTitle = null;
    static JLabel memUsedTitle = null;
    static JLabel ipTitle = null;
    static String communityString = "public";
    static String cpuIdle = ".1.3.6.1.4.1.2021.11.11.0";
    
    static String totalMem = ".1.3.6.1.4.1.2021.4.5.0";
    static String freedMem = ".1.3.6.1.4.1.2021.4.6.0";
    
    public TestSNMP(String address) {
        
        this.address = "udp:/" + address + "/161";
    }
    
    public static void main(String[] args) {
          
        JFrame frame = new JFrame("Pi Status");
        hostTitle = new JLabel("Host");
        ipTitle = new JLabel("IPv4");
        cpuTitle = new JLabel("CPU Idle");
        memUsedTitle = new JLabel("Memory In Use");
        ArrayList<SNMPHost> hostlisting = null;
        try {
            if (args.length == 0) {
                hostlisting = SNMPHostFactory.BuildHostArray("hosts.xml");
            } else {
                for (int i = 0; i < args.length; i++) {
                    if (hostlisting == null) hostlisting = SNMPHostFactory.BuildHostArray(args[i]);
                    else {
                        ArrayList<SNMPHost> temp = SNMPHostFactory.BuildHostArray(args[i]);
                        for (int j = 0; j< temp.size(); j++) hostlisting.add(temp.get(j));
                    }
                }
            }
        } catch (SAXException saxe) {
            System.out.println("File is not readable\n" + saxe.getMessage());
            System.exit(0);
        } catch (ParserConfigurationException pce) {
            System.out.println("File is not readable\n" + pce.getMessage());
            System.exit(0);
        } catch (IOException ioe) {
            System.out.println("File is not readable\n" + ioe.getMessage());
            System.exit(0);
        }
        JLabel[][] labels = new JLabel[hostlisting.size()][4];
        
        // build frame
        
        frame.getContentPane().add(hostTitle);
        frame.getContentPane().add(ipTitle);
        frame.getContentPane().add(memUsedTitle);
        frame.getContentPane().add(cpuTitle);
        hostTitle.setBorder(new EmptyBorder(INSET,INSET,INSET,INSET));
        memUsedTitle.setBorder(new EmptyBorder(INSET,INSET,INSET,INSET));
        cpuTitle.setBorder(new EmptyBorder(INSET,INSET,INSET,INSET));
        hostTitle.setOpaque(true);
        cpuTitle.setOpaque(true);
        ipTitle.setOpaque(true);
        memUsedTitle.setOpaque(true);
        int maxCols = labels[0].length;
        int maxRows = labels.length;
        for (int row = 0; row < maxRows; row++) {
            for (int col = 0; col < maxCols; col++) {
                SNMPHost host = hostlisting.get(row);
                if (col == 0) {                    
                    labels[row][col] = new JLabel(host.Hostname);
                    labels[row][col].setBackground(SNMPHost.HostColor(host.color));                  
                } 
                if (col == 1) {
                    try {
                        InetAddress addr = InetAddress.getByName(host.Hostname);
                        byte[] addrRaw = addr.getAddress();
                        int result;
                        StringBuffer buffer = new StringBuffer();
                        for(int k = 0; k < addrRaw.length; k++) {
                            if (addrRaw[k] < 0) result = 256 + (new Byte(addrRaw[k])).intValue();
                            else result = new Byte(addrRaw[k]).intValue();
                            buffer.append(result);
                            if (k < (addrRaw.length -1)) buffer.append(".");
                        }
                        labels[row][col] = new JLabel(buffer.toString());
                    } catch (UnknownHostException uhe) {
                        labels[row][col] = new JLabel("Unknown");
                        // nothing to do here, cause it would be caught earlier
                        // result of bad design before things were learned
                    }                                     
                }
                if (col > 1) {
                    labels[row][col] = new JLabel("0%");
                          
                labels[row][col].setOpaque(true);
                labels[row][col].setBorder(new EmptyBorder(INSET,INSET,INSET,INSET));
                }
                frame.getContentPane().add(labels[row][col]);
            }
            
        }        
        // display
        
        frame.getContentPane().setLayout(new GridLayout(hostlisting.size() + 1,4));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        TestSNMP[] hosts = new TestSNMP[labels.length];
        for (int i = 0; i < labels.length; i++) {
            hosts[i] = new TestSNMP(hostlisting.get(i).fqdn);
        }

        
        // start worker thread to poll

        boolean keepRunning = true;
        while (keepRunning) {
            flashTitle();
            try {
                for (int i = 0; i < hosts.length; i++) {
                    hosts[i].start();
                }
            } catch (IOException ioe) {
                keepRunning = false;
                System.out.println("Unable to check machines");
            }
            for (int i = 0; i < labels.length; i++) {
                try {
                    float usedMemFloat = 0;
                    int cpu = 0;
                    cpu = hosts[i].getAsInt(new OID(cpuIdle)); 
                    System.out.println("CPU idle " + cpu);
                    usedMemFloat = getUsedMem(hosts[i]);             
                    setCPUColor(labels[i][3], cpu);
                    setMemColor(labels[i][2], usedMemFloat);

                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            try {
                Thread.sleep(SEC * NUM_OF_SECS);
            } catch (InterruptedException ie) {
                // what do do?
            }
        }
    }
    private static void flashTitle() {
        
        cpuTitle.setBackground(Color.GREEN);
        hostTitle.setBackground(Color.GREEN);
        memUsedTitle.setBackground(Color.GREEN);
        try {
            Thread.sleep(SEC / 10);
        } catch (InterruptedException ie) {
            
        }
        cpuTitle.setBackground(null);
        hostTitle.setBackground(null);
        memUsedTitle.setBackground(null);
    }
    private static float getUsedMem(TestSNMP machine) {
        
        float result = 0;
        try {
            int totalMemInt = machine.getAsInt(new OID(totalMem));
            System.out.println(machine.address + ": total mem:" + totalMemInt);
            int freeMemInt = machine.getAsInt(new OID(freedMem));
            System.out.println(machine.address + ": free mem:" + freeMemInt);
            if (totalMemInt != 0) {
                result = (100 * (totalMemInt - freeMemInt)/ totalMemInt);
            } 
        } catch (IOException ioe) {
        }                  
        return result;
    }
    
    private static void setCPUColor(JLabel label, int value) {
        
        if (value > 100) {
            value = 100;
        }
        if (value == 0) {
            label.setBackground(Color.blue);
            label.setForeground(Color.white);
        }
        if (value >= 25 && value < 50) {
            label.setBackground(Color.yellow);
            label.setForeground(Color.black);
        }
        if (value < 25 && value != 0) {
            label.setBackground(Color.red);
            label.setForeground(Color.black);
        }
        if (value >= 50) {
            label.setBackground(null);
            label.setForeground(null);
        }
        label.setText(String.valueOf(value) + "%");
    }
    private static void setMemColor(JLabel label, float value) {
        
        if (value == 0.0) {
            label.setBackground(Color.blue);
            label.setForeground(Color.white);
        }
        if (value >= 50.0 && value < 75.0) {
            label.setBackground(Color.yellow);
            label.setForeground(Color.black);
        }
        if (value > 75.0) {
            label.setBackground(Color.red);
            label.setForeground(Color.black);
        }
        if (value < 50.0 && value != 0.0) {
            label.setBackground(null);
            label.setForeground(null);
        }
        String result = String.format("%.1f", value);
        label.setText(String.valueOf(value) + "%");
    }
    
    private void start() throws IOException {
    
        TransportMapping transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }
    
    public String getAsString(OID oid) throws IOException {

        ResponseEvent event = get(new OID[] { oid });
        return event.getResponse().get(0).getVariable().toString();
    }
    
    public int getAsInt(OID oid) throws IOException {
        
        ResponseEvent event = null;
        try {
            event = get(new OID[] { oid});
        } catch (RuntimeException re) {
            System.out.println("GET timed out");
        }
        if ( event == null) {
            return 0;
        }
        PDU response = event.getResponse();
        if (response == null) {
            return 0;
        } else {
            return response.get(0).getVariable().toInt();
        }
    }

    public ResponseEvent get(OID oids[]) throws IOException {

        PDU pdu = new PDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(PDU.GET);
        ResponseEvent event = null;
        try {
            event = snmp.send(pdu, getTarget(), null);
        } catch (MessageException me) {
            System.out.println(me.getMessage());
        }
        if(event != null) {
            return event;
        }
        throw new RuntimeException("GET timed out");
    }
    
    private Target getTarget() {

        Address targetAddress = GenericAddress.parse(address);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(communityString));
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(TIMEOUT_SEC * SEC);
        target.setVersion(SnmpConstants.version2c);
        return target;
    }
}
