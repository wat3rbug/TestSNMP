package testsnmp;

import java.awt.Color;
import java.util.ArrayList;

/**
 * @author Douglas Gardiner
 */
public class SNMPHost {
    
    public String Hostname;
    
    public String communityString;
    
    public String version;
    public String ipAddress;
    public double net;
    private ArrayList<Service> services;
    public int prevIfOcts;
    public int prevOfOctets;
    public long prevTime;
    public long prevIfInOctet;
    public long prevIfOutOctet;
    private int iterator;
    
    public String fqdn;
    public String color;
    
    TestSNMP machine;
    
    public Service next() {
        if (services == null || iterator == services.size()) {
            return null;
        } else {
            return services.get(iterator++);
        }
    }
    
    public void reset()
    {
        iterator = 0;
    }   

    public int numOfServices() {
        if (services == null) return 0;
        return services.size();
    }    
    
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
