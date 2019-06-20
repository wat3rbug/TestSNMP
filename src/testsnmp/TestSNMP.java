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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private static final int HOST_COL = 0;
    private static final int FQDN_COL = 1;
    private static final int HOST_IP_COL = 2;
    private static final int CPU_COL = 5;
    private static final int MEM_COL = 4;
    private static final int LAN_UTIL_COL = 3;
    private static final int NUM_OF_COLS = 6;
    
    static String communityString = "public";
    static String cpuIdle = ".1.3.6.1.4.1.2021.11.11.0";  
    static String totalMem = ".1.3.6.1.4.1.2021.4.5.0";
    static String freedMem = ".1.3.6.1.4.1.2021.4.6.0";
    static String ifInOctet = ".1.3.6.1.2.1.2.2.1.10.2";
    static String ifOutOctet = ".1.3.6.1.2.1.2.2.1.16.2";
    static String ifSpeed = ".1.3.6.1.2.1.2.2.1.5.2";
    
    static Snmp snmp = null;
    static JLabel hostTitle = null;
    static JLabel cpuTitle = null;
    static JLabel memUsedTitle = null;
    static JLabel ipTitle = null;
    static JLabel fqdnTitle = null;
    static JLabel netUtilTitle = null;
    
    
    String address = null;
    
    public TestSNMP(String address) {
        
        this.address = "udp:/" + address + "/161";
    }
    
    public static void main(String[] args) {
          
        JFrame frame = new JFrame("Pi Status");
        hostTitle = new JLabel("Host");
        fqdnTitle = new JLabel("FQDN");
        ipTitle = new JLabel("IPv4");
        netUtilTitle = new JLabel("LAN Util %");
        cpuTitle = new JLabel("CPU Idle");
        memUsedTitle = new JLabel("Memory In Use");
        ArrayList<SNMPHost> hostlisting = null;
        try {
            if (args.length == 0) {
                hostlisting = SNMPHostFactory.BuildHostArray("hosts.xml");
            } else {
                for (int i = 0; i < args.length; i++) {
                    if (hostlisting == null) hostlisting = SNMPHostFactory
                            .BuildHostArray(args[i]);
                    else {
                        ArrayList<SNMPHost> temp = SNMPHostFactory
                                .BuildHostArray(args[i]);
                        for (int j = 0; j< temp.size(); j++) hostlisting
                                .add(temp.get(j));
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
        JLabel[][] labels = new JLabel[hostlisting.size()][NUM_OF_COLS];
        
        // build frame
        
        frame.getContentPane().add(hostTitle);
        frame.getContentPane().add(fqdnTitle);
        frame.getContentPane().add(ipTitle);
        frame.getContentPane().add(netUtilTitle);
        frame.getContentPane().add(memUsedTitle);
        frame.getContentPane().add(cpuTitle);
        hostTitle.setBorder(new EmptyBorder(INSET,INSET,INSET,INSET));
        memUsedTitle.setBorder(new EmptyBorder(INSET,INSET,INSET,INSET));
        cpuTitle.setBorder(new EmptyBorder(INSET,INSET,INSET,INSET));
        fqdnTitle.setBorder(new EmptyBorder(INSET,INSET,INSET,INSET));
        ipTitle.setBorder(new EmptyBorder(INSET,INSET,INSET,INSET));
        netUtilTitle.setBorder(new EmptyBorder(INSET,INSET,INSET,INSET));
        hostTitle.setOpaque(true);
        fqdnTitle.setOpaque(true);
        cpuTitle.setOpaque(true);
        ipTitle.setOpaque(true);
        memUsedTitle.setOpaque(true);
        netUtilTitle.setOpaque(true);
        
        // build the table
        
        int maxCols = labels[0].length;
        int maxRows = labels.length;
        for (int row = 0; row < maxRows; row++) {
            for (int col = 0; col < maxCols; col++) {
                SNMPHost host = hostlisting.get(row);
                if (col == HOST_COL) {
                    HostDetailsListener popup = new HostDetailsListener(host);
                    labels[row][col] = new JLabel(host.Hostname);
                    labels[row][col].addMouseListener(popup);
                    Color hostColor = SNMPHost.HostColor(host.color);
                    labels[row][col].setBackground(hostColor); 
                    if (hostColor == Color.BLUE) labels[row][col].setForeground(Color.white);
                    labels[row][col].setOpaque(true);
                } 
                if (col == FQDN_COL) {
                    labels[row][col] = new JLabel(host.fqdn);
                }
                if (col == HOST_IP_COL) {
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
                    }                                     
                }
                if (col > 2) {
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
                    // get #s from machine
                    
                    int ifIn = hosts[i].getAsInt(new OID(ifInOctet));
                    int ifOut = hosts[i].getAsInt(new OID(ifOutOctet));
                    int ifSpd = hosts[i].getAsInt(new OID(ifSpeed));
                    
                    // get time and If deltas
                    
                    long prevIfIn = hostlisting.get(i).prevIfInOctet;
                    long prevIfOut = hostlisting.get(i).prevIfOutOctet;
                    long ifInDelta = (ifIn > prevIfIn) ? ifIn - prevIfIn : 
                            prevIfIn - ifIn;
                    long ifOutDelta = (ifOut > prevIfOut) ? ifOut - prevIfOut 
                            : prevIfOut - ifOut;

                    long timestamp = System.currentTimeMillis();
                    long nom = ifInDelta + ifOutDelta;
                    float denom = ifSpd * 1000000;
                    hostlisting.get(i).prevTime = timestamp;
                    hostlisting.get(i).prevIfInOctet = ifIn;
                    hostlisting.get(i).prevIfOutOctet = ifOut;
                    if (denom == 0) {
                        labels[i][LAN_UTIL_COL].setText("Unknown");
                    } else {
                        double util = (nom / denom) * 400;
                        if (util > 100.0) util = (float) 100.0;
                        setUtilColor(labels[i][LAN_UTIL_COL], (float)util);
                    }
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                    System.out.println("cant get net utilization");
                }
                try {
                    float usedMemFloat = 0;
                    int cpu = 0;
                    cpu = hosts[i].getAsInt(new OID(cpuIdle)); 
                    usedMemFloat = getUsedMem(hosts[i]);             
                    setCPUColor(labels[i][CPU_COL], cpu);
                    setPanelColor(labels[i][MEM_COL], usedMemFloat);

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
        
        cpuTitle.setBackground(Color.green);
        hostTitle.setBackground(Color.green);
        memUsedTitle.setBackground(Color.green);
        fqdnTitle.setBackground(Color.green);
        ipTitle.setBackground(Color.green);
        netUtilTitle.setBackground(Color.green);
        try {
            Thread.sleep(SEC / 10);
        } catch (InterruptedException ie) {
            
        }
        cpuTitle.setBackground(null);
        hostTitle.setBackground(null);
        memUsedTitle.setBackground(null);
        fqdnTitle.setBackground(null);
        ipTitle.setBackground(null);
        netUtilTitle.setBackground(null);
    }
    private static float getUsedMem(TestSNMP machine) {
        
        float result = 0;
        try {
            int totalMemInt = machine.getAsInt(new OID(totalMem));
            int freeMemInt = machine.getAsInt(new OID(freedMem));
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
    private static void setPanelColor(JLabel label, float value) {
        
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
    
    private static void setUtilColor(JLabel label, float value) {
        
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
        String result = String.format("%2.1f", value);
        label.setText(result + "%");
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
            // do nothing
            //System.out.println("GET timed out");
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
