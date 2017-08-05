package com.pmdcodereview.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pmdcodereview.daoLayer.PMDStructureDao;
import com.pmdcodereview.model.PMDStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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

        Map<String, List<PMDStructure>> codeReviewByClass = new HashMap<>();

        List<PMDStructure> allData = pmdStructureDao.findAll();
        List<PMDStructure> pmdStructureList = null;

        for (PMDStructure eachData : allData) {
            if(codeReviewByClass.containsKey(eachData.getClassname())){
                List<PMDStructure> pmdStructureList1 = codeReviewByClass.get(eachData.getClassname());
                Iterator<PMDStructure> iterator = pmdStructureList1.iterator();
                while (iterator.hasNext()){
                    PMDStructure next = iterator.next();
                    if(next.getReviewFeedback().equals(eachData.getReviewFeedback())){
                        iterator.remove();
                    }
                }
                pmdStructureList1.add(eachData);

            }else {
                pmdStructureList = new ArrayList<>();
                pmdStructureList.add(eachData);
                codeReviewByClass.put(eachData.getClassname(), pmdStructureList);
            }

        }


        if(!codeReviewByClass.isEmpty()){
            Gson gson = new GsonBuilder().create();
            return gson.toJson(codeReviewByClass);
        }

        return null;

    }

    @RequestMapping(value = "/getPMDResultsByDate", method = RequestMethod.GET)
    public String getPMDResultByDate(@RequestParam Date date) throws IOException {
        Map<String, List<PMDStructure>> codeReviewByClass = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String format = simpleDateFormat.format(date);
        List<PMDStructure> bydate = pmdStructureDao.findBydate(format);
        for (PMDStructure pmdStructure : bydate) {
            if(codeReviewByClass.containsKey(pmdStructure.getClassname())){
                List<PMDStructure> pmdStructureList = codeReviewByClass.get(pmdStructure.getClassname());
                pmdStructureList.add(pmdStructure);

            }else {
                List<PMDStructure> pmdStructureList = new ArrayList<>();
                pmdStructureList.add(pmdStructure);
                codeReviewByClass.put(pmdStructure.getClassname(), pmdStructureList);
            }
        }

        if(!codeReviewByClass.isEmpty()){
            Gson gson = new GsonBuilder().create();
            return gson.toJson(codeReviewByClass);
        }

        return null;

    }
}
