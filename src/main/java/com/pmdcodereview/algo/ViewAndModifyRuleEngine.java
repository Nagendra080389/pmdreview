package com.pmdcodereview.algo;

import com.pmdcodereview.RuleEngine.RuleEngine;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewAndModifyRuleEngine {

    public static Map<String, String> modify(Map<String, String> mapOfRuleAndPriority) throws ParserConfigurationException, IOException, SAXException {
        Map<String, String> ruleEngine = new HashMap<>();
        File xmlFile = new File("C:\\JenkinsPOC\\Jenkins\\pmd-bin-5.8.0-SNAPSHOT\\pmd-bin-5.8.0-SNAPSHOT\\rules\\rulesets\\apex\\ruleSet.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            if(mapOfRuleAndPriority != null && !mapOfRuleAndPriority.isEmpty()) {
                for (String ref : mapOfRuleAndPriority.keySet()) {
                    updateElementValue(doc, mapOfRuleAndPriority.get(ref), ref);
                }
            }


            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("C:\\JenkinsPOC\\Jenkins\\pmd-bin-5.8.0-SNAPSHOT\\pmd-bin-5.8.0-SNAPSHOT\\rules\\rulesets\\apex\\ruleSet.xml"));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            System.out.println("XML file updated successfully");

            ruleEngine = getUpdateElemetsAndConvertToMap(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ruleEngine;
    }

    private static Map<String, String> getUpdateElemetsAndConvertToMap(Document doc) {
        Map<String, String> map = new HashMap<>();
        NodeList rules = doc.getElementsByTagName("rule");
        Element rule = null;
        for (int i = 0; i < rules.getLength(); i++) {
            rule = (Element) rules.item(i);
            String name = rule.getAttribute("ref");
            String ruleName = name.split("/")[name.split("/").length-1];
            Node priority = rule.getElementsByTagName("priority").item(0);
            String priorityValue = priority.getTextContent();
            map.put(ruleName, priorityValue);
        }
        return map;
    }

    private static void updateElementValue(Document doc, String newValue, String ref) {
        NodeList rules = doc.getElementsByTagName("rule");
        Element rule = null;
        for (int i = 0; i < rules.getLength(); i++) {
            rule = (Element) rules.item(i);
            String name = rule.getAttribute("ref");
            if(name.contains(ref)){
                Node priority = rule.getElementsByTagName("priority").item(0);
                priority.setTextContent(newValue);
            }
        }
    }
}
