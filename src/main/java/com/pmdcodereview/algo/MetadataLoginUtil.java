package com.pmdcodereview.algo;

import com.sforce.ws.ConnectorConfig;
import org.apache.coyote.http2.ConnectionException;

public class MetadataLoginUtil {

    /*static final String USERNAME = "username@salesforce.com";
    static final String PASSWORD = "passwordSecurityToken";
    static EnterpriseConnection connection;

    public static void main(String[] args) {
        ConnectorConfig config = new ConnectorConfig();
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        try {
            connection = Connector.newConnection(config);
            // display some current settings
            System.out.println("Auth EndPoint:"+config.getAuthEndpoint());
            System.out.println("Service EndPoint:"+config.getServiceEndpoint());
            System.out.println("Username: "+config.getUsername());
            System.out.println("SessionId: "+config.getSessionId());
        } catch (ConnectionException e1) {
            e1.printStackTrace();
        }
    }*/

    /*public static MetadataConnection login() throws ConnectionException {
        final String USERNAME = "user@company.com";
        // This is only a sample. Hard coding passwords in source files is a bad practice.
        final String PASSWORD = "password";
        final String URL = "https://login.salesforce.com/services/Soap/c/40.0";
        final LoginResult loginResult = loginToSalesforce(USERNAME, PASSWORD, URL);
        return createMetadataConnection(loginResult);
    }

    private static MetadataConnection createMetadataConnection(
            final LoginResult loginResult) throws ConnectionException {
        final ConnectorConfig config = new ConnectorConfig();
        config.setServiceEndpoint(loginResult.getMetadataServerUrl());
        config.setSessionId(loginResult.getSessionId());
        return new MetadataConnection(config);
    }

    private static LoginResult loginToSalesforce(
            final String username,
            final String password,
            final String loginUrl) throws ConnectionException {
        final ConnectorConfig config = new ConnectorConfig();
        config.setAuthEndpoint(loginUrl);
        config.setServiceEndpoint(loginUrl);
        config.setManualLogin(true);
        return (new EnterpriseConnection(config)).login(username, password);
    }*/
}
