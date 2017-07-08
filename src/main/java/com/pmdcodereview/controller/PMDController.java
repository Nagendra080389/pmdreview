package com.pmdcodereview.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pmdcodereview.model.PMDStructure;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nagendra on 18-06-2017.
 */
@RestController
public class PMDController {

    public static final String FILE_NAME = "G:\\Codes\\pmdreview\\src\\main\\resources\\PMDResultsSample.txt";

    @RequestMapping(value = "/getPMDResults", method = RequestMethod.GET)
    public String getPMDResult() throws FileNotFoundException {
        Map<String, List<PMDStructure>> codeReviewByClass = new HashMap<>();
        List<String> stringList = new ArrayList<>();
        FileInputStream fstream = new FileInputStream(FILE_NAME);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {

            String codeReview;
            while ((codeReview = br.readLine()) != null) {
                if (codeReview != null && (!codeReview.equals(""))) {
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
            Gson gson = new GsonBuilder().create();
            return gson.toJson(codeReviewByClass);
        }

        return null;

    }

    private Map<String, List<PMDStructure>> createMapOfClassAndReview(String line, Map<String, List<PMDStructure>> codeReviewByClass) {

        String[] classNameAndLineNumber = line.split(":");

        if(codeReviewByClass.containsKey(classNameAndLineNumber[0])){
            List<PMDStructure> pmdStructure = codeReviewByClass.get(classNameAndLineNumber[0]);
            PMDStructure pmdStructure1 = new PMDStructure();
            pmdStructure1.setClassname(classNameAndLineNumber[0]);
            pmdStructure1.setLineNumber(Integer.valueOf(classNameAndLineNumber[1]));
            pmdStructure1.setReviewFeedback(classNameAndLineNumber[2]);
            pmdStructure.add(pmdStructure1);
        }else {
            List<PMDStructure> pmdStructureList = new ArrayList<>();
            PMDStructure pmdStructure = new PMDStructure();
            pmdStructure.setClassname(classNameAndLineNumber[0]);
            pmdStructure.setLineNumber(Integer.valueOf(classNameAndLineNumber[1]));
            pmdStructure.setReviewFeedback(classNameAndLineNumber[2]);
            pmdStructureList.add(pmdStructure);
            codeReviewByClass.put(classNameAndLineNumber[0], pmdStructureList);
        }

        return codeReviewByClass;
    }

}
