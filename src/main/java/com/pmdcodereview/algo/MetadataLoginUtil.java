package com.pmdcodereview.algo;

import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.ws.ConnectorConfig;
import org.apache.coyote.http2.ConnectionException;
import com.sforce.soap.partner.*;
import com.sforce.soap.partner.sobject.*;
import com.sforce.ws.*;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MetadataLoginUtil {
    public static final String FILE_NAME = "C:\\Jenkins\\ConfigurationFile.txt";


    static PartnerConnection partnerConnection;
    static List<String> classList = new ArrayList<>();
    static List<String> triggerList = new ArrayList<>();
    static List<String> pageList = new ArrayList<>();
    static List<String> allObjectsList = new ArrayList<>();

    public static void main() throws Exception {
        BufferedWriter bufferedWriter = null;
        Map<String, String> propertiesMap = new HashMap<String, String>();
        FileReader fileReader = new FileReader(FILE_NAME);
        createMapOfProperties(fileReader, propertiesMap);
        clearTheFile(propertiesMap);

        ConnectorConfig config = new ConnectorConfig();
        config.setUsername(propertiesMap.get("SalesforceUserName"));
        config.setPassword(propertiesMap.get("SalesforcePassword"));
        config.setAuthEndpoint(propertiesMap.get("SalesforceURL"));
        try {
            try {
                partnerConnection = Connector.newConnection(config);
            }catch (Exception e){
                throw new com.sforce.ws.ConnectionException("Cannot connect to Org");
            }
            String apexClass = "SELECT ID, NAME FROM APEXCLASS";
            String apexTrigger = "SELECT ID, NAME FROM APEXTRIGGER";
            String apexPage = "SELECT ID, NAME FROM APEXPAGE";
            QueryResult resultClass = partnerConnection.query(apexClass);
            QueryResult resultTrigger = partnerConnection.query(apexTrigger);
            QueryResult resultPage = partnerConnection.query(apexPage);

            if (resultClass.getSize() > 0) {
                for (SObject sClass : resultClass.getRecords()) {
                    classList.add(sClass.getField("Name") + ".cls");
                }
            }
            if(resultTrigger.getSize() > 0) {
                for (SObject sTrigger : resultTrigger.getRecords()) {
                    triggerList.add(sTrigger.getField("Name") + ".trigger");
                }
            }
            if(resultPage.getSize() > 0) {
                for (SObject sPage : resultPage.getRecords()) {
                    pageList.add(sPage.getField("Name") + ".page");
                }
            }


            allObjectsList.addAll(classList);
            allObjectsList.addAll(triggerList);
            allObjectsList.addAll(pageList);


            try {

                bufferedWriter = new BufferedWriter(new FileWriter(propertiesMap.get("ClassesTextFilepath")));
                ;
                for (String salesForceClass : allObjectsList) {
                    bufferedWriter.write(salesForceClass + '\n');
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            }

            /*SalesForceObjects salesForceObjects = new SalesForceObjects();
            salesForceObjects.setClassList(classList);
            salesForceObjects.setPageList(pageList);
            salesForceObjects.setTriggerList(triggerList);*/

            // Execute Ant Script
            File buildFile = new File(propertiesMap.get("AntMigrationTool"));
            Project antProject = new Project();
            antProject.setUserProperty("ant.file", buildFile.getAbsolutePath());
            antProject.init();
            ProjectHelper helper = ProjectHelper.getProjectHelper();
            antProject.addReference("ant.projectHelper", helper);
            helper.parse(antProject, buildFile);
            antProject.executeTarget("runRuleTestFromJava");


            // Execute batchFile for PMD
            try{
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy_HH-mm-ss");
                Date date = new Date();
                ProcessBuilder processBuilder = new ProcessBuilder(propertiesMap.get("PmdBatFile"));
                File log = new File(propertiesMap.get("JenkinsLogs")+"\\"+"SyngentaJenkinsLog"+"_"+dateFormat.format(date)+".txt");
                processBuilder.redirectErrorStream(true);
                processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
                Process process = processBuilder.start();
                process.waitFor();
                System.out.println("Done");



                /*Process p = Runtime.getRuntime().exec(propertiesMap.get("PmdBatFile"));
                p.waitFor();

                BufferedReader stdInput = new BufferedReader(
                        new InputStreamReader( p.getInputStream() ));

                String s ;
                while ((s = stdInput.readLine()) != null) {
                    System.out.println(s);
                }*/

            }catch( IOException ex ){
                ex.printStackTrace();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("");
            // SaveLogs to MongoDB
            SaveLogs.savelogs();



        } catch (com.sforce.ws.ConnectionException e) {
            throw new com.sforce.ws.ConnectionException("Cannot Connect to Org.");
        }

    }



    public static MetadataConnection login(String USERNAME, String PASSWORD, String URL) throws ConnectionException, com.sforce.ws.ConnectionException {
        final LoginResult loginResult = loginToSalesforce(USERNAME, PASSWORD, URL);
        return createMetadataConnection(loginResult);
    }

    private static MetadataConnection createMetadataConnection(
            final LoginResult loginResult) throws ConnectionException, com.sforce.ws.ConnectionException {
        final ConnectorConfig config = new ConnectorConfig();
        config.setServiceEndpoint(loginResult.getMetadataServerUrl());
        config.setSessionId(loginResult.getSessionId());
        return new MetadataConnection(config);
    }

    private static LoginResult loginToSalesforce(
            final String username,
            final String password,
            final String loginUrl) throws ConnectionException, com.sforce.ws.ConnectionException {
        final ConnectorConfig config = new ConnectorConfig();
        config.setAuthEndpoint(loginUrl);
        config.setServiceEndpoint(loginUrl);
        config.setManualLogin(true);

        return (new PartnerConnection(config)).login(username, password);
    }

    private static void createMapOfProperties(FileReader fileReader, Map<String, String> propertiesMap) throws IOException {
        BufferedReader bufferedReader = null;
        String sCurrentLine;

        bufferedReader = new BufferedReader(fileReader);

        while ((sCurrentLine = bufferedReader.readLine()) != null) {
            sCurrentLine= sCurrentLine.replaceAll("\\s+","");
            String[] split = sCurrentLine.split("=");
            propertiesMap.put(split[0], split[1]);

        }
    }

    private static void clearTheFile(Map<String, String> propertiesMap) throws IOException {
        FileWriter fwOb = new FileWriter(propertiesMap.get("ClassesTextFilepath"), false);
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();
    }
}
