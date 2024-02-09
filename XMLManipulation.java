import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLManipulation{
    public static void main(String[] args) {
        /*
         * 1- Instantiate DocumentBuilderFactory
         * 2- Get DocumentBuilder
         * 3- Parse -> Document object
         * Document, Node, Element.
         */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("ex1.xml");
            
            // Get root element
            Element root = doc.getDocumentElement();
            // Get child elements
            NodeList instructions = root.getElementsByTagName("*");

            // Map to store variable values
            Map<String, Integer> variables = new HashMap<>();
            
            // Process each statement
            for (int i = 0; i < instructions.getLength(); i++) {
                Element instruction = (Element) instructions.item(i);
                String tagName = instruction.getTagName();
                
                switch(tagName) {
                    case "var":
                        // Define a new variable
                        String varName = instruction.getAttribute("name");
                        // Check if variable is already defined
                        if (variables.containsKey(varName)) {
                            throw new IllegalArgumentException("Variable '" + varName + "' is already defined.");
                        }

                        String valueStr = instruction.getAttribute("value");
                        if (!valueStr.isEmpty()) {
                            int value = Integer.parseInt(valueStr);
                            variables.put(varName, value);
                        }
                        break;
                    case "add":
                        // Add integers
                        String n1 = instruction.getAttribute("n1");
                        String n2 = instruction.getAttribute("n2");
                        String to = instruction.getAttribute("to");
                        
                        int num1 = (Character.isLetter(n1.charAt(0))) ? getValue(n1, variables) : Integer.parseInt(n1);
                        int num2 = (Character.isLetter(n2.charAt(0))) ? getValue(n2, variables) : Integer.parseInt(n2);
                        variables.put(to, num1 + num2);
                        break;
                    case "print":
                        String n = instruction.getAttribute("n");
                        int val = getValue(n, variables);
                        System.out.println(val);
                        break;
                }
            }

            // System.out.println("Root Element:" + doc.getDocumentElement().getNodeName());

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    private static int getValue(String varName, Map<String, Integer> variables) {
        if (!variables.containsKey(varName)) {
            throw new IllegalArgumentException("Undefined variable: " + varName);
        }
        return variables.get(varName);
    }

}