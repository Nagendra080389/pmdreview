package com.pmdcodereview.algo;

import java.util.ArrayList;
import java.util.List;

/**
 * Test
 */
public class SalesForceObjects {

    private List<String> classList = new ArrayList<>();
    private List<String> triggerList = new ArrayList<>();
    private List<String> pageList = new ArrayList<>();

    public List<String> getClassList() {
        return classList;
    }

    public void setClassList(List<String> classList) {
        this.classList = classList;
    }

    public List<String> getTriggerList() {
        return triggerList;
    }

    public void setTriggerList(List<String> triggerList) {
        this.triggerList = triggerList;
    }

    public List<String> getPageList() {
        return pageList;
    }

    public void setPageList(List<String> pageList) {
        this.pageList = pageList;
    }
}
