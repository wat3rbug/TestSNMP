package testsnmp;

import java.awt.Color;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * This class contains all the elements needed for the monitoring system to use
 * for monitoring a particular host.  It includes the host name, read-only 
 * community string, the version of SNMP to use on the host, and some special
 * services.  It is not adapter or very flexible.  Bear that in mind if you have 
 * issues since it was really only designed to limited polling of memory usage
 * and CPU usage of some Raspberry Pis.
 * 
 * @author Douglas Gardiner
 */

public class SNMPHost {
    
    private ArrayList<Service> services;
    
    public String Hostname;
    public String communityString;   
    public String version;
    public String ipv4Address;
    public String ipv6Address;
    public String function;
    public int net;  
    private long prevTime;
    private long prevIfInOctet;
    private long prevIfOutOctet;
    private int iterator;
    
    public String fqdn;
    public String color;
    
    PiCheck machine;
    
    public int getNetUtil(int ifIn, int ifOut, int ifSpd) {

        long prevIfIn = prevIfInOctet;
        long prevIfOut = prevIfOutOctet;
        long ifInDelta = (ifIn > prevIfIn) ? ifIn - prevIfIn : 
                            prevIfIn - ifIn;
        long ifOutDelta = (ifOut > prevIfOut) ? ifOut - prevIfOut 
                            : prevIfOut - ifOut;
        long timestamp = System.currentTimeMillis();
        long nom = ifInDelta + ifOutDelta;
        float denom = ifSpd * 1000000;
        prevTime = timestamp;
        prevIfInOctet = ifIn;
        prevIfOutOctet = ifOut;
        if (denom == 0) {
            net = 0;
            return 0;
        } else {
            double util = (nom / denom) * 400;
            if (util > 100.0) util = (float) 100.0;
            net = (int)Math.round(util);
            return net;
        }
    }
    
    public ArrayList<Service> getServices() {
        return services;
    }
    
    public boolean getServiceStatus(Service srv) {
        boolean result = false;
        
        return result;
    }
    
    /**
     * This method gets String representation of the IPv4 Address that this host
     * uses.  It returns the first address. If there is no address then it will
     * return an "unknown" value since the host is selected via the host 
     * configuration file and not dynamically.
     * 
     * @return A string with the address stated as "x.x.x.x" 
     */
    
    public String getIPv4Address() {
        if (this.ipv4Address == null || this.ipv4Address.isEmpty()) {
            String result = null;
            try {
                StringBuffer buffer = new StringBuffer();
                InetAddress address = InetAddress.getByName(this.Hostname);
                byte[] addrRaw = address.getAddress();

                int tempResult = 0;
                for (int k = 0; k < addrRaw.length; k++) {
                    if (addrRaw[k] < 0) tempResult = 256 + (new Byte(addrRaw[k])).intValue();
                    else tempResult = new Byte(addrRaw[k]).intValue();
                    buffer.append(tempResult);
                    if (k < (addrRaw.length -1)) buffer.append(".");
                    result = buffer.toString();
                }
            } catch (UnknownHostException uhe) {
                result = "unknown";
            } 
            this.ipv4Address = result;
        }          
        return this.ipv4Address;
    }
    
    /**
     * This method gets String representation of the IPv6 Address that this host
     * uses.  It returns the first address. If there is no address then it will
     * return an "unknown" value since the host is selected via the host 
     * configuration file and not dynamically. It is possible that a link-local
     * address is returned.  Since my system blocks NDP  it is hard to say if 
     * this works correctly, since it returns the first address and not the 
     * global address for the link it received the request on.
     * 
     * @return A string with the IPv6 address. 
     */
    
    public String getIPv6Address() {
        if (this.ipv6Address == null || this.ipv6Address.isEmpty()) {
            String result = "N/A";
            try {
                InetAddress[] inet = InetAddress.getAllByName(Hostname);
                for (InetAddress address : inet) {
                    if(address instanceof Inet6Address) {
                        try {
                            if (address.isReachable(1000)) {
                                result = address.toString();
                                break;
                            }
                        } catch(IOException ioe) {
                            if (result == null) result = "unknown";
                            // do nothing since the timeout is WHY
                        }                       
                    }
                }              
            } catch (UnknownHostException uhe) {
                result = "N/A";
            }
            this.ipv6Address = result;          
        }
        return this.ipv6Address;
    }
    
    /**
     * This method removes a special service that is currently being polled for 
     * status.
     * 
     * @param srv The service that is to be removed from future polling events.
     */
    
    public void removeService(Service srv) {
        if (srv != null && services != null && services.size() != 0) {
            services.remove(srv);
        }
    } 
    
    /**
     * This method adds a special service that is not currently being polled for 
     * status.  It is reserved mostly for startup of the monitoring system, but 
     * may be additionally used for adding services. 
     * 
     * @param srv The service that is to be added.
     */
    
    public void addService(Service srv) {
        if (services == null) {
            services = new ArrayList<Service>();
        }
        if (srv != null) {
            services.add(srv);
        }
    }
    
    /**
     * This method returns the next special service that is used for polling on 
     * the host.  It starts at the first service and each subsequent request 
     * gives the next service to be polled.  If there are no more services on 
     * the host, a null value is returned.
     * 
     * @return The most recent Service as to be used for polling.
     */
    
    public Service next() {
        if (services == null || iterator == services.size()) {
            return null;
        } else {
            return services.get(iterator++);
        }
    }
    
    /**
     * This method resets the special service iterator to the first element. 
     * This may be deleted depending on whether it is used. 
     */
    
    public void reset()
    {
        iterator = 0;
    }   

    /**
     * This method returns the number of special services this host polls.  This 
     * may be deleted depending on whether it is used.
     * 
     * @return An integer value for the number of special services this host 
     * polls. 
     */
    
    public int numOfServices() {
        if (services == null) return 0;
        return services.size();
    }    
    
    /**
     * This method returns a Color object based on the string representation
     * sent to it.  It is used primarily for the display panels based on what is
     * read from the host.xml file.  For example 'gray' would return a Color 
     * object with everything necessary to draw a gray panel or button or 
     * whatever element is needed.
     * 
     * @param color The string representation of the color
     * @return A Color object with the values can used to draw that color.
     */
    
    public static Color HostColor(String color) {
		
        Color result = null;
        switch (color) {
            case "red":
                    result = Color.RED;
                    break;
            case "green":
                    result = Color.GREEN;
                    break;
            case "gray":
                    result = Color.GRAY;
                    break;
            case "yellow":
                    result = Color.YELLOW;
                    break;
            case "blue":
                    result = Color.BLUE;
                    break;
            case "orange":
                result = Color.ORANGE;
                break;
            default:
                result = Color.DARK_GRAY;
                break;
            }
        return result;
    }
}
