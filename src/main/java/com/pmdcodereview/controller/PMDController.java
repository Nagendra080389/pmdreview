package com.pmdcodereview.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pmdcodereview.algo.AlgoForPMDResult;
import com.pmdcodereview.algo.MetadataLoginUtil;
import com.pmdcodereview.daoLayer.PMDStructureDao;
import com.pmdcodereview.model.PMDStructure;
import com.pmdcodereview.model.PMDStructureWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Nagendra on 18-06-2017.
 */
@RestController
public class PMDController {

    @Autowired
    PMDStructureDao pmdStructureDao;

    @RequestMapping(value = "/getPMDResults", method = RequestMethod.GET)
    public String getPMDResult() throws IOException {

        Map<String, PMDStructureWrapper> codeReviewByClass = new HashMap<>();

        List<PMDStructure> allData = pmdStructureDao.findAll();
        PMDStructureWrapper pmdStructureWrapper = null;
        List<PMDStructure> pmdStructureList = null;

        for (PMDStructure eachData : allData) {
            if(codeReviewByClass.containsKey(eachData.getClassname())){
                PMDStructureWrapper pmdStructureWrapper1 = codeReviewByClass.get(eachData.getClassname());
                List<PMDStructure> pmdStructures = pmdStructureWrapper1.getPmdStructures();
                Iterator<PMDStructure> iterator = pmdStructureWrapper1.getPmdStructures().iterator();
                while (iterator.hasNext()){
                    PMDStructure next = iterator.next();
                    if(next.getReviewFeedback().equals(eachData.getReviewFeedback())){
                        iterator.remove();
                    }
                }
                pmdStructures.add(eachData);
                pmdStructureWrapper1.setPmdStructures(pmdStructures);

            }else {
                createNewWrapper(codeReviewByClass, eachData);
            }

        }

        AlgoForPMDResult.checkForSOQLInsideForLoop(codeReviewByClass);

        if(!codeReviewByClass.isEmpty()){
            Gson gson = new GsonBuilder().create();
            return gson.toJson(codeReviewByClass);
        }

        return null;

    }

    private void createNewWrapper(Map<String, PMDStructureWrapper> codeReviewByClass, PMDStructure eachData) {
        List<PMDStructure> pmdStructureList;
        PMDStructureWrapper pmdStructureWrapper;
        pmdStructureList = new ArrayList<>();
        pmdStructureList.add(eachData);
        pmdStructureWrapper = new PMDStructureWrapper();
        pmdStructureWrapper.setPmdStructures(pmdStructureList);
        codeReviewByClass.put(eachData.getClassname(), pmdStructureWrapper);
    }

    @RequestMapping(value = "/getPMDResultsByDate", method = RequestMethod.GET)
    public String getPMDResultByDate(@RequestParam Date date) throws IOException {
        Map<String, PMDStructureWrapper> codeReviewByClass = new HashMap<>();

        PMDStructureWrapper pmdStructureWrapper = null;
        List<PMDStructure> pmdStructureList = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String format = simpleDateFormat.format(date);
        List<PMDStructure> bydate = pmdStructureDao.findBydate(format);
        for (PMDStructure pmdStructure : bydate) {
            if(codeReviewByClass.containsKey(pmdStructure.getClassname())){
                PMDStructureWrapper pmdStructureWrapper1 = codeReviewByClass.get(pmdStructure.getClassname());
                List<PMDStructure> pmdStructures = pmdStructureWrapper1.getPmdStructures();
                pmdStructureWrapper1.getPmdStructures().removeIf(next -> next.getReviewFeedback().equals(pmdStructure.getReviewFeedback()));
                pmdStructures.add(pmdStructure);
                pmdStructureWrapper1.setPmdStructures(pmdStructures);

            }else {
                pmdStructureList = new ArrayList<>();
                pmdStructureList.add(pmdStructure);
                pmdStructureWrapper = new PMDStructureWrapper();
                pmdStructureWrapper.setPmdStructures(pmdStructureList);
                codeReviewByClass.put(pmdStructure.getClassname(), pmdStructureWrapper);
            }
        }

        AlgoForPMDResult.checkForSOQLInsideForLoop(codeReviewByClass);

        if(!codeReviewByClass.isEmpty()){
            Gson gson = new GsonBuilder().create();
            return gson.toJson(codeReviewByClass);
        }

        return null;

    }

    @RequestMapping(value = "/getPMDResultsForFullOrgByDate", method = RequestMethod.GET)
    public String getPMDResultForFullOrgByDate(@RequestParam Date date) throws Exception {
        Map<String, PMDStructureWrapper> codeReviewByClass = new HashMap<>();

        MetadataLoginUtil.main();

        PMDStructureWrapper pmdStructureWrapper = null;
        List<PMDStructure> pmdStructureList = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String format = simpleDateFormat.format(date);
        List<PMDStructure> bydate = pmdStructureDao.findBydate(format);
        for (PMDStructure pmdStructure : bydate) {
            if(codeReviewByClass.containsKey(pmdStructure.getClassname())){
                PMDStructureWrapper pmdStructureWrapper1 = codeReviewByClass.get(pmdStructure.getClassname());
                List<PMDStructure> pmdStructures = pmdStructureWrapper1.getPmdStructures();
                pmdStructureWrapper1.getPmdStructures().removeIf(next -> next.getReviewFeedback().equals(pmdStructure.getReviewFeedback()));
                pmdStructures.add(pmdStructure);
                pmdStructureWrapper1.setPmdStructures(pmdStructures);

            }else {
                pmdStructureList = new ArrayList<>();
                pmdStructureList.add(pmdStructure);
                pmdStructureWrapper = new PMDStructureWrapper();
                pmdStructureWrapper.setPmdStructures(pmdStructureList);
                codeReviewByClass.put(pmdStructure.getClassname(), pmdStructureWrapper);
            }
        }

        AlgoForPMDResult.checkForSOQLInsideForLoop(codeReviewByClass);

        if(!codeReviewByClass.isEmpty()){
            Gson gson = new GsonBuilder().create();
            return gson.toJson(codeReviewByClass);
        }

        return null;

    }

    @RequestMapping(value = "/getPMDResultsByDateAndSeverity", method = RequestMethod.GET)
    public String getPMDResultByDate(@RequestParam Date date, @RequestParam List<Integer> severityLevel) throws IOException {
        Map<String, PMDStructureWrapper> codeReviewByClass = new HashMap<>();

        PMDStructureWrapper pmdStructureWrapper = null;
        List<PMDStructure> pmdStructureList = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String format = simpleDateFormat.format(date);

        List<PMDStructure> bydate = pmdStructureDao.findByDateAndSeverityIn(format, severityLevel);
        for (PMDStructure pmdStructure : bydate) {
            if(codeReviewByClass.containsKey(pmdStructure.getClassname())){
                PMDStructureWrapper pmdStructureWrapper1 = codeReviewByClass.get(pmdStructure.getClassname());
                List<PMDStructure> pmdStructures = pmdStructureWrapper1.getPmdStructures();
                pmdStructureWrapper1.getPmdStructures().removeIf(next -> next.getReviewFeedback().equals(pmdStructure.getReviewFeedback()));
                pmdStructures.add(pmdStructure);
                pmdStructureWrapper1.setPmdStructures(pmdStructures);

            }else {
                pmdStructureList = new ArrayList<>();
                pmdStructureList.add(pmdStructure);
                pmdStructureWrapper = new PMDStructureWrapper();
                pmdStructureWrapper.setPmdStructures(pmdStructureList);
                codeReviewByClass.put(pmdStructure.getClassname(), pmdStructureWrapper);
            }
        }

        AlgoForPMDResult.checkForSOQLInsideForLoop(codeReviewByClass);

        if(!codeReviewByClass.isEmpty()){
            Gson gson = new GsonBuilder().create();
            return gson.toJson(codeReviewByClass);
        }

        return null;

    }


}
