package testsnmp;

import java.awt.Color;

/**
 * @author Douglas Gardiner
 */
public class SNMPHost {
    
    public String Hostname;
    
    public String communityString;
    
    public String version;
    public String ipAddress;
    
    public String fqdn;
	public String color;
    
    TestSNMP machine;
	
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
