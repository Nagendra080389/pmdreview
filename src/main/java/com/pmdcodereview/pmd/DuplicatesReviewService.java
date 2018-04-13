package com.pmdcodereview.pmd;

import net.sourceforge.pmd.cpd.CPD;
import net.sourceforge.pmd.cpd.CPDConfiguration;

public class DuplicatesReviewService {

    private CPDConfiguration cpdConfiguration;

    public DuplicatesReviewService(CPDConfiguration arguments) {
        this.cpdConfiguration = arguments;
    }

    public void findDuplicateCodes(){
        CPD cpd = new CPD(cpdConfiguration);
        cpd.go();


    }
}
