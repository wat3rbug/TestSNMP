package testsnmp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * @author Douglas Gardiner
 */
public class SNMPHostFactory {
    
    public static ArrayList<SNMPHost> BuildHostArray(String hostfile) throws IOException, ParserConfigurationException, SAXException {
        
        ArrayList<SNMPHost> listing = new ArrayList<>();

        File hostFile = new File(hostfile);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
        Document doc = dbBuilder.parse(hostFile);
        doc.getDocumentElement().normalize();
        String root = doc.getDocumentElement().getNodeName();
        NodeList nList = doc.getElementsByTagName("host");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                SNMPHost temp = new SNMPHost();
                Element eElement = (Element)nNode;
                temp.Hostname = eElement.getAttribute("name");
                temp.fqdn = eElement.getElementsByTagName("fqdn").item(0)
                        .getTextContent();
                temp.communityString = eElement
                        .getElementsByTagName("rocommunity").item(0)
                        .getTextContent();
		temp.color = eElement.getElementsByTagName("color").item(0)
			.getTextContent();
                listing.add(temp);
            }
        }
        return listing;        
    }
}
