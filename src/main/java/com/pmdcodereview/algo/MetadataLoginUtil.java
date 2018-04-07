package com.pmdcodereview.algo;

import com.pmdcodereview.model.PMDStructure;
import com.pmdcodereview.pmd.PmdReviewService;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.tooling.ToolingConnection;
import com.sforce.ws.ConnectorConfig;
import net.sourceforge.pmd.*;
import net.sourceforge.pmd.util.ResourceLoader;
import org.apache.commons.io.IOUtils;
import com.sforce.soap.partner.*;
import com.sforce.soap.partner.sobject.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.io.*;
import java.util.*;

@Service
public class MetadataLoginUtil {

    static PartnerConnection partnerConnection;
    static List<String> classList = new ArrayList<>();
    static List<String> triggerList = new ArrayList<>();
    static List<String> pageList = new ArrayList<>();
    static List<String> allObjectsList = new ArrayList<>();

    public List<PMDStructure> startReviewer(String partnerURL, String toolingURL, Cookie[] cookies) throws Exception {

        String instanceUrl = null;
        String accessToken = null;
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("ACCESS_TOKEN")){
                accessToken = cookie.getValue();
            }
            if(cookie.getName().equals("INSTANCE_URL")){
                instanceUrl = cookie.getValue();
                instanceUrl = instanceUrl + partnerURL;
            }
        }

        ConnectorConfig config = new ConnectorConfig();
        config.setSessionId(accessToken);
        config.setServiceEndpoint(instanceUrl);
        try {
            try {
                partnerConnection = Connector.newConnection(config);
            }catch (Exception e){
                throw new com.sforce.ws.ConnectionException("Cannot connect to Org");
            }
            String apexClass = "SELECT NAME, BODY FROM APEXCLASS WHERE NamespacePrefix = NULL";
            String apexTrigger = "SELECT NAME, BODY FROM APEXTRIGGER WHERE NamespacePrefix = NULL";
            String apexPage = "SELECT NAME, markup FROM APEXPAGE WHERE NamespacePrefix = NULL";

            List<SObject> apexClasses = queryRecords(apexClass, partnerConnection, null, true);
            List<SObject> apexTriggers = queryRecords(apexTrigger, partnerConnection, null, true);
            List<SObject> apexPages = queryRecords(apexPage, partnerConnection, null, true);

            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream resourceAsStream = classLoader.getResourceAsStream("xml/ruleSet.xml");
            String ruleSetFilePath = "";
            if(resourceAsStream != null){
                File file = null;
                try {
                    file = stream2file(resourceAsStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    resourceAsStream.close();
                }
                ruleSetFilePath = file != null ? file.getPath() : null;

            }

            List<PMDStructure> pmdStructures = new ArrayList<>();
            for (SObject aClass : apexClasses) {
                if(aClass.getChild("Body") != null){
                    String body = (String) aClass.getChild("Body").getValue();
                    String name = (String) aClass.getChild("Name").getValue();
                    List<RuleViolation> ruleViolations = reviewResult(body, name, ".cls",ruleSetFilePath);

                    createViolations(pmdStructures, name, ruleViolations,".cls");
                }

            }
            for (SObject aClass : apexTriggers) {
                if(aClass.getChild("Body") != null){
                    String body = (String) aClass.getChild("Body").getValue();
                    String name = (String) aClass.getChild("Name").getValue();
                    List<RuleViolation> ruleViolations = reviewResult(body, name, ".trigger",ruleSetFilePath);

                    createViolations(pmdStructures, name, ruleViolations, ".trigger");
                }
            }
            for (SObject aClass : apexPages) {
                if(aClass.getChild("Markup") != null){
                    String body = (String) aClass.getChild("Markup").getValue();
                    String name = (String) aClass.getChild("Name").getValue();
                    List<RuleViolation> ruleViolations = reviewResult(body, name, ".page",ruleSetFilePath);

                    createViolations(pmdStructures, name, ruleViolations, ".page");
                }
            }


            return pmdStructures;


        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private void createViolations(List<PMDStructure> pmdStructures, String name, List<RuleViolation> ruleViolations, String extension) {
        PMDStructure pmdStructure = null;
        ;
        for (RuleViolation ruleViolation : ruleViolations) {
            pmdStructure = new PMDStructure();
            pmdStructure.setReviewFeedback(ruleViolation.getDescription());
            pmdStructure.setLineNumber(ruleViolation.getBeginLine());
            pmdStructure.setName(name+extension);
            pmdStructure.setRuleName(ruleViolation.getRule().getName());
            pmdStructure.setRuleUrl(ruleViolation.getRule().getExternalInfoUrl());
            pmdStructure.setRulePriority(ruleViolation.getRule().getPriority().getPriority());
            pmdStructures.add(pmdStructure);
        }


    }

    public static <T> List<T> queryRecords(String query, PartnerConnection partnerConnection, ToolingConnection toolingConnection, boolean usePartner)
            throws com.sforce.ws.ConnectionException {
        if (usePartner) {
            List<T> sObjectList = new ArrayList<>();
            QueryResult qResult;
            qResult = partnerConnection.query(query);
            boolean done = false;
            if (qResult.getSize() > 0) {
                System.out.println("Logged-in user can see a total of "
                        + qResult.getSize() + " contact records.");
                while (!done) {
                    com.sforce.soap.partner.sobject.SObject[] records = qResult.getRecords();
                    for (com.sforce.soap.partner.sobject.SObject record : records) {
                        sObjectList.add((T) record);
                    }

                    if (qResult.isDone()) {
                        done = true;
                    } else {
                        qResult = partnerConnection.queryMore(qResult.getQueryLocator());
                    }
                }
            } else {
                System.out.println("No records found.");
            }
            System.out.println("Query successfully executed.");

            return sObjectList;
        } else {
            List<T> sObjectList = new ArrayList<>();
            com.sforce.soap.tooling.QueryResult qResult = toolingConnection.query(query);
            boolean done = false;
            if (qResult.getSize() > 0) {
                System.out.println("Logged-in user can see a total of "
                        + qResult.getSize() + " contact records.");
                while (!done) {
                    com.sforce.soap.tooling.sobject.SObject[] records = qResult.getRecords();
                    for (com.sforce.soap.tooling.sobject.SObject record : records) {
                        sObjectList.add((T) record);
                    }
                    if (qResult.isDone()) {
                        done = true;
                    } else {
                        qResult = toolingConnection.queryMore(qResult.getQueryLocator());
                    }
                }
            } else {
                System.out.println("No records found.");
            }
            System.out.println("Query successfully executed.");

            return sObjectList;

        }
    }

    private List<RuleViolation> reviewResult (String body, String fileName, String extension, String ruleSetFilePath) throws IOException {
        PMDConfiguration pmdConfiguration = new PMDConfiguration();
        pmdConfiguration.setReportFormat("text");
        pmdConfiguration.setRuleSets(ruleSetFilePath);
        pmdConfiguration.setThreads(4);
        //pmdConfiguration.setAnalysisCache(new FileAnalysisCache());
        SourceCodeProcessor sourceCodeProcessor = new SourceCodeProcessor(pmdConfiguration);
        RuleSetFactory ruleSetFactory = RulesetsFactoryUtils.getRulesetFactory(pmdConfiguration, new ResourceLoader());
        RuleSets ruleSets = RulesetsFactoryUtils.getRuleSetsWithBenchmark(pmdConfiguration.getRuleSets(), ruleSetFactory);

        PmdReviewService pmdReviewService = new PmdReviewService(sourceCodeProcessor, ruleSets);
        return pmdReviewService.review(body, fileName + extension/*".cls"*/);
    }

    private static File stream2file (InputStream in) throws IOException {
        final File tempFile = File.createTempFile("ruleSet", ".xml");
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tempFile;
    }
}
