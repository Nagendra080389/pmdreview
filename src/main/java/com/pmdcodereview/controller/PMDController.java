package com.pmdcodereview.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pmdcodereview.daoLayer.PMDStructureDao;
import com.pmdcodereview.model.PMDStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
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

    public String FILE_NAME = null;

    @RequestMapping(value = "/getPMDResults", method = RequestMethod.GET)
    public String getPMDResult() throws IOException {

        FILE_NAME = new ClassPathResource("pmdTextTest.log").getFile().getAbsolutePath();
        Map<String, List<PMDStructure>> codeReviewByClass = new HashMap<>();
        List<String> stringList = new ArrayList<>();
        FileInputStream fstream = new FileInputStream(FILE_NAME);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {

            String codeReview;
            while ((codeReview = br.readLine()) != null) {
                if (!codeReview.equals("")) {
                    if(codeReview.contains(".cls")){
                        stringList.add(codeReview);
                    }
                }
            }

            for (String line : stringList) {
                String[] split = line.split("\\\\");
                codeReviewByClass = createMapOfClassAndReview(split[6], codeReviewByClass);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!codeReviewByClass.isEmpty()){
            Collection<List<PMDStructure>> values = codeReviewByClass.values();
            for (List<PMDStructure> value : values) {
                pmdStructureDao.save(value);
            }
        }

        if(!codeReviewByClass.isEmpty()){
            Gson gson = new GsonBuilder().create();
            return gson.toJson(codeReviewByClass);
        }

        return null;

    }

    @RequestMapping(value = "/getPMDResultsByDate", method = RequestMethod.GET)
    public String getPMDResultByDate(@RequestParam Long date) throws IOException {
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

    private Map<String, List<PMDStructure>> createMapOfClassAndReview(String line, Map<String, List<PMDStructure>> codeReviewByClass) {

        String[] classNameAndLineNumber = line.split(":");
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String format = simpleDateFormat.format(date);


        if(codeReviewByClass.containsKey(classNameAndLineNumber[0])){
            List<PMDStructure> pmdStructure = codeReviewByClass.get(classNameAndLineNumber[0]);
            PMDStructure pmdStructure1 = new PMDStructure();
            pmdStructure1.setClassname(classNameAndLineNumber[0]);
            pmdStructure1.setLineNumber(Integer.valueOf(classNameAndLineNumber[1]));
            pmdStructure1.setReviewFeedback(classNameAndLineNumber[2]);
            pmdStructure1.setDate(format);
            pmdStructure.add(pmdStructure1);
        }else {
            List<PMDStructure> pmdStructureList = new ArrayList<>();
            PMDStructure pmdStructure = new PMDStructure();
            pmdStructure.setClassname(classNameAndLineNumber[0]);
            pmdStructure.setLineNumber(Integer.valueOf(classNameAndLineNumber[1]));
            pmdStructure.setReviewFeedback(classNameAndLineNumber[2]);
            pmdStructure.setDate(format);
            pmdStructureList.add(pmdStructure);
            codeReviewByClass.put(classNameAndLineNumber[0], pmdStructureList);
        }

        return codeReviewByClass;
    }

}
