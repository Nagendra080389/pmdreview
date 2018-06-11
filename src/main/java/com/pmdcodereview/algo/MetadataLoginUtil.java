package com.pmdcodereview.algo;

import com.google.gson.Gson;
import com.pmdcodereview.model.PMDMainWrapper;
import com.pmdcodereview.model.PMDStructure;
import com.pmdcodereview.model.PMDStructureWrapper;
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
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.System.out;

@Service
public class MetadataLoginUtil {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MetadataLoginUtil.class);

    private static PartnerConnection partnerConnection;

    @Autowired
    Gson gson;


    public List<PMDStructure> startReviewer(String partnerURL, String toolingURL, Cookie[] cookies, OutputStream outputStream) throws Exception {
        String instanceUrl = null;
        String accessToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("ACCESS_TOKEN")) {
                accessToken = cookie.getValue();
            }
            if (cookie.getName().equals("INSTANCE_URL")) {
                instanceUrl = cookie.getValue();
                instanceUrl = instanceUrl + partnerURL;
            }
        }

        LOGGER.info("accessToken ->"+accessToken);
        ConnectorConfig config = new ConnectorConfig();
        config.setSessionId(accessToken);
        config.setServiceEndpoint(instanceUrl);
        try {
            try {
                partnerConnection = Connector.newConnection(config);
            } catch (Exception e) {
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
            if (resourceAsStream != null) {
                File file = null;
                try {
                    file = stream2file(resourceAsStream);
                } catch (IOException e) {
                    LOGGER.error("Exception while converting file: " + e.getMessage());
                } finally {
                    resourceAsStream.close();
                }
                ruleSetFilePath = file != null ? file.getPath() : null;

            }

            PMDConfiguration pmdConfiguration = new PMDConfiguration();
            pmdConfiguration.setReportFormat("text");
            pmdConfiguration.setRuleSets(ruleSetFilePath);
            int i = Runtime.getRuntime().availableProcessors();
            pmdConfiguration.setThreads(i);
            SourceCodeProcessor sourceCodeProcessor = new SourceCodeProcessor(pmdConfiguration);
            RuleSetFactory ruleSetFactory = RulesetsFactoryUtils.getRulesetFactory(pmdConfiguration, new ResourceLoader());
            RuleSets ruleSets = RulesetsFactoryUtils.getRuleSetsWithBenchmark(pmdConfiguration.getRuleSets(), ruleSetFactory);

            PmdReviewService pmdReviewService = new PmdReviewService(sourceCodeProcessor, ruleSets);

            List<PMDStructure> pmdStructures = new ArrayList<>();
            PMDStructure pmdStructure = null;

            long start = System.currentTimeMillis();
            for (SObject aClass : apexClasses) {
                try {
                    createViolationsForAll(pmdStructure, pmdStructures, (String) aClass.getChild("Body").getValue(),
                            (String) aClass.getChild("Name").getValue(), ".cls", pmdReviewService, outputStream);
                } catch (IOException e) {
                    LOGGER.error("Exception while creating violation for classes: " + e.getMessage());
                }
            }

            /*apexClasses.parallelStream().forEachOrdered(aClass -> {
                try {
                    createViolationsForAll(pmdStructure, pmdStructures, (String) aClass.getChild("Body").getValue(),
                            (String) aClass.getChild("Name").getValue(), ".cls", pmdReviewService, outputStream);
                } catch (IOException e) {
                    LOGGER.error("Exception while creating violation for classes: " + e.getMessage());
                }
            });*/

            for (SObject aTrigger : apexTriggers) {
                try {
                    createViolationsForAll(pmdStructure, pmdStructures, (String) aTrigger.getChild("Body").getValue(),
                            (String) aTrigger.getChild("Name").getValue(), ".trigger", pmdReviewService, outputStream);
                } catch (IOException e) {
                    LOGGER.error("Exception while creating violation for triggers: " + e.getMessage());
                }
            }

            /*apexTriggers.parallelStream().forEachOrdered(aTrigger -> {
                try {
                    createViolationsForAll(pmdStructure, pmdStructures, (String) aTrigger.getChild("Body").getValue(),
                            (String) aTrigger.getChild("Name").getValue(), ".trigger", pmdReviewService, outputStream);
                } catch (IOException e) {
                    LOGGER.error("Exception while creating violation for triggers: " + e.getMessage());
                }
            });*/

            for (SObject aPage : apexPages) {
                try {
                    createViolationsForAll(pmdStructure, pmdStructures, (String) aPage.getChild("Markup").getValue(),
                            (String) aPage.getChild("Name").getValue(), ".page", pmdReviewService, outputStream);
                } catch (IOException e) {
                    LOGGER.error("Exception while creating violation for pages: " + e.getMessage());
                }
            }

            /*apexPages.parallelStream().forEachOrdered(aPage -> {
                try {
                    createViolationsForAll(pmdStructure, pmdStructures, (String) aPage.getChild("Markup").getValue(),
                            (String) aPage.getChild("Name").getValue(), ".page", pmdReviewService, outputStream);
                } catch (IOException e) {
                    LOGGER.error("Exception while creating violation for pages: " + e.getMessage());
                }
            });*/

            long stop = System.currentTimeMillis();
            LOGGER.info("Total Time Taken " + String.valueOf(stop - start));

            return pmdStructures;


        } catch (Exception e) {
            LOGGER.error("Exception in startReviewer " + e.getMessage());
        }
        return Collections.EMPTY_LIST;
    }

    private void createViolationsForAll(PMDStructure pmdStructure, List<PMDStructure> pmdStructures, String body,
                                        String name, String extension,
                                        PmdReviewService pmdReviewService, OutputStream outputStream) throws IOException {
        List<RuleViolation> ruleViolations = reviewResult(body, name, extension, pmdReviewService);

        createViolations(pmdStructure, pmdStructures, name, ruleViolations, extension, outputStream);
    }

    private void createViolations(PMDStructure pmdStructure, List<PMDStructure> pmdStructures, String name, List<RuleViolation> ruleViolations, String extension, OutputStream outputStream) throws IOException {
        int ruleViolationsSize = ruleViolations.size();
        try {
            List<PMDStructure> pmdStructureList = new ArrayList<>();

            for (int i = 0; i < ruleViolationsSize; i++) {
                pmdStructure = new PMDStructure();
                pmdStructure.setReviewFeedback(ruleViolations.get(i).getDescription());
                pmdStructure.setLineNumber(ruleViolations.get(i).getBeginLine());
                pmdStructure.setName(name + extension);
                pmdStructure.setRuleName(ruleViolations.get(i).getRule().getName());
                pmdStructure.setRuleUrl(ruleViolations.get(i).getRule().getExternalInfoUrl());
                pmdStructure.setRulePriority(ruleViolations.get(i).getRule().getPriority().getPriority());
                pmdStructures.add(pmdStructure);
                pmdStructureList.add(pmdStructure);
            }

            if (outputStream != null && !pmdStructureList.isEmpty()) {
                Map<String, PMDStructureWrapper> codeReviewByClass = new HashMap<>();
                PMDStructureWrapper pmdStructureWrapper = new PMDStructureWrapper();
                pmdStructureWrapper.setPmdStructures(pmdStructureList);
                codeReviewByClass.put(name+extension, pmdStructureWrapper);
                PMDMainWrapper pmdMainWrapper = new PMDMainWrapper();
                pmdMainWrapper.setPmdStructureWrapper(codeReviewByClass);
                outputStream.write(gson.toJson(pmdMainWrapper).getBytes());
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> List<T> queryRecords(String query, PartnerConnection partnerConnection, ToolingConnection toolingConnection, boolean usePartner)
            throws com.sforce.ws.ConnectionException {
        if (usePartner) {
            List<T> sObjectList = new ArrayList<>();
            QueryResult qResult;
            qResult = partnerConnection.query(query);
            boolean done = false;
            if (qResult.getSize() > 0) {
                LOGGER.info("Logged-in user can see a total of "
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
                LOGGER.info("No records found.");
            }
            LOGGER.info("Query successfully executed.");

            return sObjectList;
        } else {
            List<T> sObjectList = new ArrayList<>();
            com.sforce.soap.tooling.QueryResult qResult = toolingConnection.query(query);
            boolean done = false;
            if (qResult.getSize() > 0) {
                out.println("Logged-in user can see a total of "
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
                LOGGER.info("No records found.");
            }
            LOGGER.info("Query successfully executed.");

            return sObjectList;

        }
    }

    private List<RuleViolation> reviewResult(String body, String fileName, String extension, PmdReviewService pmdReviewService) throws IOException {
        return pmdReviewService.review(body, fileName + extension);
    }

    private static File stream2file(InputStream in) throws IOException {
        final File tempFile = File.createTempFile("ruleSet", ".xml");
        tempFile.deleteOnExit();
        try (OutputStream out = Files.newOutputStream(Paths.get(tempFile.toURI()))) {
            IOUtils.copy(in, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempFile;
    }
}
