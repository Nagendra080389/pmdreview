package com.pmdcodereview.algo;

import com.pmdcodereview.model.PMDStructure;
import com.pmdcodereview.model.PMDStructureWrapper;

import java.util.List;
import java.util.Map;

/**
 * Created by Nagendra on 12-08-2017.
 */
public class AlgoForPMDResult {

    public static void checkForSOQLInsideForLoop(Map<String, PMDStructureWrapper> pmdStructureWrapperMap){

        for (String eachKey : pmdStructureWrapperMap.keySet()) {
            List<PMDStructure> pmdStructureList = pmdStructureWrapperMap.get(eachKey).getPmdStructures();
            for (PMDStructure pmdStructure : pmdStructureList) {
                if(pmdStructure.getReviewFeedback().contains("inside loops")){
                    pmdStructureWrapperMap.get(eachKey).setSoqlInForLoop(true);
                }
            }

        }


    }
}
