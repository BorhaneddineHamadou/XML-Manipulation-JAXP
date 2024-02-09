import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLInverter {
    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("ex2.xml");
            
            // Get root element
            Element root = doc.getDocumentElement();

            // Find the innermost child
            Element innermostChild = findInnermostChild(root);
            
            invertElements(innermostChild, root);
        
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void invertElements(Element element, Element root) {

        if(element != root){
            // Print the opening tag
            System.out.println("<" + element.getTagName() + ">");
            
            // Print parent nodes in reverse order
            Node parent = element.getParentNode();
            invertElements((Element) parent, root);
            
            // Print the closing tag
            System.out.println("</" + element.getTagName() + ">");
        }else{
            System.out.println("<" + root.getTagName() + "/>");
        }
        
    }

    private static Element findInnermostChild(Element element) {
        NodeList children = element.getChildNodes();

        // Check if more than one child exists
        int count = 0;
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element) {
                count++;
                if (count > 1) {
                    throw new IllegalArgumentException("Invalid XML: Each node can't have more than one child.");
                }
            }
        }

        // If only one child, proceed to find innermost child

        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element) {
                Element childElement = (Element) children.item(i);
                // If the child element has no child elements, it's the innermost child
                if (!hasChildElements(childElement)) {
                    return childElement;
                } else {
                    // Recursively search for innermost child
                    return findInnermostChild(childElement);
                }
            }
        }
        return null; // No child elements found
    }
    
    private static boolean hasChildElements(Element element) {
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element) {
                return true; // Found at least one child element
            }
        }
        return false; // No child elements found
    }

}
