package com.pmdcodereview.algo;


import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;


public class SaveLogs {

    /*public static void savelogs() throws IOException {

        Map<String, String> propertiesMap = new HashMap<String, String>();
        FileReader fileReader = new FileReader(FILE_NAME);
        createMapOfProperties(fileReader, propertiesMap);
        final File folder = new File(propertiesMap.get("JenkinsLogs"));
        MongoClient mongoClient = null;
        mongoClient = getMongoConnection("localhost", "27017");
        MongoDatabase pmdReview = mongoClient.getDatabase("pmdReview");
        MongoCollection<Document> salesForceClass = pmdReview.getCollection("SalesForceClass");


        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isFile()) {
                List<String> xmlList = new ArrayList<>();
                Path filePath = Paths.get(fileEntry.toURI());
                if (Files.isRegularFile(filePath)) {
                    String[] split1 = filePath.toString().split("\\\\");
                    String fileName = split1[4].substring(0, split1[4].length() - 4);
                    BasicDBObject fields = new BasicDBObject("fileName", fileName);
                    Document recordPresent = salesForceClass.find(fields).first();
                    if (recordPresent == null) {
                        try {
                            if (fileName.contains("DC")) {
                                try {
                                    List<PMDStructure> duplicateError = new ArrayList<>();
                                    FileInputStream fstream = new FileInputStream(filePath.toString());
                                    try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {

                                        String codeReview;
                                        while ((codeReview = br.readLine()) != null) {
                                            if (!codeReview.equals("")) {
                                                if (!xmlList.isEmpty()) {
                                                    if (!(codeReview.contains("POST BUILD TASK : FAILURE") ||
                                                            codeReview.contains("END OF POST BUILD TASK") ||
                                                            codeReview.contains("Finished:"))) {
                                                        xmlList.add(codeReview);
                                                    }
                                                }

                                                if (codeReview.contains("<?xml version=\"1.0\" encoding=\"Cp1252\"?>")) {
                                                    xmlList.add(codeReview);
                                                }
                                            }
                                        }

                                        if (!xmlList.isEmpty()) {
                                            File tempFile = File.createTempFile(fileName, ".xml");
                                            for (String eachXmlLine : xmlList) {
                                                FileUtils.write(tempFile, eachXmlLine + "\n", true);
                                            }
                                            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                                            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                                            org.w3c.dom.Document parse = dBuilder.parse(tempFile);

                                            //optional, but recommended
                                            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
                                            parse.getDocumentElement().normalize();
                                            System.out.println("Root element :" + parse.getDocumentElement().getNodeName());

                                            NodeList fileNode = parse.getElementsByTagName("duplication");

                                            for (int temp = 0; temp < fileNode.getLength(); temp++) {
                                                Node fNode = fileNode.item(temp);
                                                if (fNode.getNodeType() == Node.ELEMENT_NODE) {
                                                    Element eElement = (Element) fNode;
                                                    Integer numberOfDuplicateLines = Integer.valueOf(eElement.getAttribute("lines"));

                                                    NodeList files = eElement.getElementsByTagName("file");
                                                    List<LineAndPath> lineAndPaths = new ArrayList<>();
                                                    for (int filestemp = 0; filestemp < files.getLength(); filestemp++) {
                                                        LineAndPath lineAndPath = new LineAndPath();
                                                        Node vNode = files.item(filestemp);
                                                        if (vNode.getNodeType() == Node.ELEMENT_NODE) {
                                                            Element eElement1 = (Element) vNode;
                                                            lineAndPath.setLine(Integer.valueOf(eElement1.getAttribute("line")));
                                                            lineAndPath.setPath(eElement1.getAttribute("path").split("\\\\")[(eElement1.getAttribute("path").split("\\\\")).length-1]);
                                                            lineAndPaths.add(lineAndPath);
                                                        }
                                                    }
                                                    String apexCode = null;
                                                    NodeList codefragmentNode = eElement.getElementsByTagName("codefragment");
                                                    if (codefragmentNode.getLength() > 0) {
                                                        Node item = codefragmentNode.item(0);
                                                        if (item.getNodeType() == Node.ELEMENT_NODE) {
                                                            eElement = (Element) item;
                                                            apexCode = eElement.getTextContent();
                                                        }
                                                    }
                                                    duplicateError = createDuplicatesList(numberOfDuplicateLines, lineAndPaths, apexCode, duplicateError,fileName);
                                                }
                                            }


                                            tempFile.deleteOnExit();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if (!duplicateError.isEmpty()) {
                                        List<Document> documentList = changeToValues(duplicateError, true);
                                        if (!documentList.isEmpty()) {
                                            salesForceClass.insertMany(documentList);
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Map<String, List<PMDStructure>> codeReviewByClass = new HashMap<>();
                                String branchName = null;
                                FileInputStream fstream = new FileInputStream(filePath.toString());
                                try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {

                                    String codeReview;
                                    while ((codeReview = br.readLine()) != null) {
                                        if (!codeReview.equals("")) {

                                            if (codeReview.contains("Checking out Revision")) {
                                                String result[] = codeReview.split("/");
                                                String s = result[result.length - 1];
                                                branchName = s.substring(0, s.length() - 1);
                                            } else {

                                            }

                                            if (!xmlList.isEmpty()) {
                                                if (!(codeReview.contains("POST BUILD TASK : FAILURE") || codeReview.contains("END OF POST BUILD TASK") ||
                                                        codeReview.contains("NoSuchFieldException") || codeReview.contains("at net.sourceforge.pmd") ||
                                                        codeReview.contains("at java.util.concurrent") || codeReview.contains("at java.lang.Thread") ||
                                                        codeReview.contains("at java.lang.Class"))) {
                                                    xmlList.add(codeReview);
                                                }
                                            }
                                            if (codeReview.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
                                                xmlList.add(codeReview);
                                            }
                                        }
                                    }

                                    if (!xmlList.isEmpty()) {
                                        File tempFile = File.createTempFile(fileName, ".xml");
                                        for (String eachXmlLine : xmlList) {
                                            FileUtils.write(tempFile, eachXmlLine + "\n", true);
                                        }

                                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                                        if (tempFile.isFile() && tempFile.canRead()) {
                                            org.w3c.dom.Document parse = dBuilder.parse(tempFile);


                                            //optional, but recommended
                                            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
                                            parse.getDocumentElement().normalize();
                                            System.out.println("Root element :" + parse.getDocumentElement().getNodeName());

                                            NodeList fileNode = parse.getElementsByTagName("file");

                                            for (int temp = 0; temp < fileNode.getLength(); temp++) {
                                                Node fNode = fileNode.item(temp);
                                                if (fNode.getNodeType() == Node.ELEMENT_NODE) {
                                                    Element eElement = (Element) fNode;
                                                    String[] nameArray = eElement.getAttribute("name").split("\\\\");
                                                    String className = nameArray[nameArray.length - 1];
                                                    NodeList violation = eElement.getElementsByTagName("violation");
                                                    for (int violationtemp = 0; violationtemp < violation.getLength(); violationtemp++) {
                                                        Node vNode = violation.item(violationtemp);
                                                        if (vNode.getNodeType() == Node.ELEMENT_NODE) {
                                                            eElement = (Element) vNode;
                                                            String severity = eElement.getAttribute("priority");
                                                            String ahref = eElement.getAttribute("externalInfoUrl");
                                                            if (className.endsWith(".page") && ahref.contains("${pmd.website.baseurl}")) {
                                                                ahref = ahref.replace("${pmd.website.baseurl}", "https://pmd.github.io/pmd-5.8.1/pmd-visualforce");
                                                            }
                                                            String ruleSet = eElement.getAttribute("ruleset");
                                                            String rule = eElement.getAttribute("rule");
                                                            String endline = eElement.getAttribute("endline");
                                                            String beginline = eElement.getAttribute("beginline");
                                                            String feedback = eElement.getFirstChild().getTextContent();
                                                            codeReviewByClass = createMapOfClassAndReview(className, severity, ahref, ruleSet, rule,
                                                                    endline, beginline, feedback, codeReviewByClass, fileName, branchName);
                                                        }
                                                    }


                                                }
                                            }


                                            tempFile.deleteOnExit();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (!codeReviewByClass.isEmpty()) {
                                    Collection<List<PMDStructure>> values = codeReviewByClass.values();
                                    for (List<PMDStructure> value : values) {
                                        List<Document> documentList = changeToValues(value, false);

                                        if (!documentList.isEmpty()) {
                                            salesForceClass.insertMany(documentList);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
    }

    private static MongoClient getMongoConnection(String hostname, String port) {
        Integer mongoPort = Integer.valueOf(port);
        return new MongoClient(hostname, mongoPort);
    }

    private static List<Document> changeToValues(List<PMDStructure> value, boolean fromDuplicateError) {
        List<Document> documentList = new ArrayList<>();
        Document document = null;
        if (!fromDuplicateError) {
            for (PMDStructure pmdStructure : value) {
                document = new Document();
                document.put("classname", pmdStructure.getClassname());
                document.put("date", pmdStructure.getDate());
                document.put("fileName", pmdStructure.getFileName());
                document.put("lineNumber", pmdStructure.getLineNumber());
                document.put("reviewFeedback", pmdStructure.getReviewFeedback());
                document.put("dsalesforceID", pmdStructure.getSalesforceID());
                document.put("severity", pmdStructure.getSeverity());
                document.put("beginLine", pmdStructure.getBeginLine());
                document.put("endLine", pmdStructure.getEndLine());
                document.put("ruleName", pmdStructure.getRuleName());
                document.put("ruleSet", pmdStructure.getRuleSet());
                document.put("helpURL", pmdStructure.getHelpURL());
                documentList.add(document);
            }
        } else {
            for (PMDStructure pmdStructure : value) {
                document = new Document();
                document.put("fileName", pmdStructure.getFileName());
                document.put("date", pmdStructure.getDate());
                document.put("numberOfDuplicates", pmdStructure.getNumberOfDuplicates());
                document.put("codeFragment", pmdStructure.getCodeFragment());
                List<BasicDBObject> fileLinePath = new ArrayList<>();
                for (LineAndPath lineAndPath : pmdStructure.getFileLineAndPath()) {
                    fileLinePath.add(new BasicDBObject("line", lineAndPath.getLine()));
                    fileLinePath.add(new BasicDBObject("path", lineAndPath.getPath()));
                }
                document.put("fileLineAndPath", fileLinePath);
                document.put("duplicationInFile", pmdStructure.getDuplicationInFile());
                documentList.add(document);
            }
        }
        return documentList;
    }

    private static List<PMDStructure> createDuplicatesList(Integer numberOfDuplicateLines, List<LineAndPath> lineAndPaths,
                                                           String apexCode, List<PMDStructure> duplicateError, String fileName) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String format = simpleDateFormat.format(date);
        PMDStructure pmdStructure = new PMDStructure();
        pmdStructure.setNumberOfDuplicates(numberOfDuplicateLines);
        if (apexCode != null) {
            pmdStructure.setCodeFragment(apexCode);
        } else {
            pmdStructure.setCodeFragment("");
        }
        List<String> duplicateFilesNames = new ArrayList<>();
        for (LineAndPath lineAndPath : lineAndPaths) {
            String duplicationInFile = lineAndPath.getPath().split("\\\\")[(lineAndPath.getPath().split("\\\\")).length - 1];
            duplicateFilesNames.add(duplicationInFile);

        }
        if(!duplicateFilesNames.isEmpty()) {
            boolean allEqual = duplicateFilesNames.stream().allMatch(duplicateFilesNames.get(0)::equals);
            if (allEqual) {
                pmdStructure.setDuplicationInFile(duplicateFilesNames.get(0));
            }else {
                pmdStructure.setDuplicationInFile("("+duplicateFilesNames.size() + " separate files)");
            }
        }
        pmdStructure.setFileLineAndPath(lineAndPaths);
        pmdStructure.setDate(format);
        pmdStructure.setFileName(fileName);
        duplicateError.add(pmdStructure);


        return duplicateError;
    }

    private static Map<String, List<PMDStructure>> createMapOfClassAndReview(String className, String severity, String ahref,
                                                                             String ruleSet, String rule, String endline, String beginline,
                                                                             String feedBack, Map<String, List<PMDStructure>> codeReviewByClass,
                                                                             String fileName, String branchName) {

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String format = simpleDateFormat.format(date);


        if (codeReviewByClass.containsKey(className)) {
            List<PMDStructure> pmdStructure = codeReviewByClass.get(className);
            PMDStructure pmdStructure1 = new PMDStructure();
            pmdStructure1.setFileName(fileName);
            pmdStructure1.setClassname(className);
            pmdStructure1.setBeginLine(Integer.valueOf(beginline));
            pmdStructure1.setHelpURL(ahref);
            pmdStructure1.setSeverity(Integer.valueOf(severity));
            pmdStructure1.setRuleName(rule);
            pmdStructure1.setRuleSet(ruleSet);
            pmdStructure1.setEndLine(Integer.valueOf(endline));
            pmdStructure1.setReviewFeedback(feedBack);
            pmdStructure1.setDate(format);
            pmdStructure1.setBranchName(branchName);
            pmdStructure.add(pmdStructure1);
        } else {
            List<PMDStructure> pmdStructureList = new ArrayList<>();
            PMDStructure pmdStructure = new PMDStructure();
            pmdStructure.setFileName(fileName);
            pmdStructure.setClassname(className);
            pmdStructure.setBeginLine(Integer.valueOf(beginline));
            pmdStructure.setHelpURL(ahref);
            pmdStructure.setSeverity(Integer.valueOf(severity));
            pmdStructure.setRuleName(rule);
            pmdStructure.setRuleSet(ruleSet);
            pmdStructure.setEndLine(Integer.valueOf(endline));
            pmdStructure.setReviewFeedback(feedBack);
            pmdStructure.setDate(format);
            pmdStructure.setBranchName(branchName);

            codeReviewByClass.put(className, pmdStructureList);
        }

        return codeReviewByClass;
    }

    private static void createMapOfProperties(FileReader fileReader, Map<String, String> propertiesMap) throws IOException {
        BufferedReader bufferedReader = null;
        String sCurrentLine;

        bufferedReader = new BufferedReader(fileReader);

        while ((sCurrentLine = bufferedReader.readLine()) != null) {
            sCurrentLine = sCurrentLine.replaceAll("\\s+", "");
            String[] split = sCurrentLine.split("=");
            propertiesMap.put(split[0], split[1]);

        }
    }*/
}
