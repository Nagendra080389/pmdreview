package com.pmdcodereview.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pmdcodereview.algo.AlgoForPMDResult;
import com.pmdcodereview.algo.MetadataLoginUtil;
import com.pmdcodereview.algo.ViewAndModifyRuleEngine;
import com.pmdcodereview.model.PMDMainWrapper;
import com.pmdcodereview.model.PMDStructure;
import com.pmdcodereview.model.PMDStructureWrapper;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Nagendra on 18-06-2017.
 */
@RestController
public class PMDController {

    @Autowired
    MetadataLoginUtil metadataLoginUtil;

    @Value("${partnerURL}")
    volatile String partnerURL;

    @Value("${toolingURL}")
    volatile String toolingURL;

    @RequestMapping(value = "/getPMDResultsByDateAndSeverity", method = RequestMethod.GET)
    public String getPMDResultByDate(HttpServletResponse response, HttpServletRequest request) throws Exception {
        PMDMainWrapper pmdMainWrapper = new PMDMainWrapper();
        Map<String, PMDStructureWrapper> codeReviewByClass = new HashMap<>();
        String partnerURL = this.partnerURL;
        String toolingURL = this.toolingURL;
        Cookie[] cookies = request.getCookies();


        List<PMDStructure> violationStructure = metadataLoginUtil.startReviewer(partnerURL, toolingURL, cookies);

        PMDStructureWrapper pmdStructureWrapper = null;
        List<PMDStructure> pmdStructureList = null;
        List<PMDStructure> pmdDuplicatesList = new ArrayList<>();


        for (PMDStructure pmdStructure : violationStructure) {

                if (codeReviewByClass.containsKey(pmdStructure.getName())) {
                    PMDStructureWrapper pmdStructureWrapper1 = codeReviewByClass.get(pmdStructure.getName());
                    List<PMDStructure> pmdStructures = pmdStructureWrapper1.getPmdStructures();
                    pmdStructures.add(pmdStructure);
                    pmdStructureWrapper1.setPmdStructures(pmdStructures);

                } else {
                    pmdStructureList = new ArrayList<>();
                    pmdStructureList.add(pmdStructure);
                    pmdStructureWrapper = new PMDStructureWrapper();
                    pmdStructureWrapper.setPmdStructures(pmdStructureList);
                    codeReviewByClass.put(pmdStructure.getName(), pmdStructureWrapper);
                }

        }

        //AlgoForPMDResult.checkForSOQLInsideForLoop(codeReviewByClass);

        if (!codeReviewByClass.isEmpty()) {
            pmdMainWrapper.setPmdStructureWrapper(codeReviewByClass);
            pmdMainWrapper.setPmdDuplicates(pmdDuplicatesList);
            Gson gson = new GsonBuilder().create();
            return gson.toJson(pmdMainWrapper);
        }

        return null;

    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.GET)
    public void auth(@RequestParam String code, @RequestParam String state, ServletResponse response, ServletRequest request) throws Exception {

        String environment = null;
        if(state.equals("b")) {
            environment = "https://login.salesforce.com/services/oauth2/token";
        }else {
            environment = "https://test.salesforce.com/services/oauth2/token";
        }
        HttpClient httpClient = new HttpClient();

        PostMethod post = new PostMethod(environment);
        post.addParameter("code",code);
        post.addParameter("grant_type","authorization_code");
        post.addParameter("redirect_uri","https://4d72ecf0.ngrok.io/authenticate");
        post.addParameter("client_id","3MVG9d8..z.hDcPLDlm9QqJ3hRVyHXBbETzqf4z6yQMvo3hxOw0MIHO6RC2MQVNDOrwxt59brCnQnng8FygaM");
        post.addParameter("client_secret","1789633258935765781");

        httpClient.executeMethod(post);
        String responseBody = post.getResponseBodyAsString();



        String accessToken = null;
        String issuedAt = null;
        String signature = null;
        String id_token = null;
        String instance_url = null;
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = null;

        jsonObject = parser.parse(responseBody).getAsJsonObject();


        try {

            accessToken = jsonObject.get("access_token")!= null ? jsonObject.get("access_token").getAsString() : null;
            issuedAt = jsonObject.get("issued_at")!= null ? jsonObject.get("issued_at").getAsString() : null;
            signature = jsonObject.get("signature")!= null ? jsonObject.get("signature").getAsString() : null;
            id_token = jsonObject.get("id_token") != null ? jsonObject.get("id_token").getAsString() : null;
            instance_url = jsonObject.get("instance_url")!= null ? jsonObject.get("instance_url").getAsString() : null;

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            post.releaseConnection();
        }

        HttpServletResponse httpResponse = (HttpServletResponse)response;
        Cookie session1 = new Cookie("ACCESS_TOKEN", accessToken);
        Cookie session2 = new Cookie("INSTANCE_URL", instance_url);
        Cookie session3 = new Cookie("ID_TOKEN", id_token);
        session1.setMaxAge(-1); //cookie not persistent, destroyed on browser exit
        httpResponse.addCookie(session1);
        httpResponse.addCookie(session2);
        httpResponse.addCookie(session3);
        httpResponse.sendRedirect("/html/pmdReview.html");

    }


}
