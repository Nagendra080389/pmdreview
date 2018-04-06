package com.pmdcodereview.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nagendra on 18-06-2017.
 */
public class PMDStructure implements Serializable{

    public String name;

    public String body;

    private Map<Integer, List<String>> lineNumberError = new HashMap<>();

    private Integer beginLine;
    private Integer endLine;
    private Integer numberOfDuplicates;
    private String codeFragment;
    private Integer noOfDuplicatesFiles;
    private List<LineAndPath> fileLineAndPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<Integer, List<String>> getLineNumberError() {
        return lineNumberError;
    }

    public void setLineNumberError(Map<Integer, List<String>> lineNumberError) {
        this.lineNumberError = lineNumberError;
    }

    public Integer getBeginLine() {
        return beginLine;
    }

    public void setBeginLine(Integer beginLine) {
        this.beginLine = beginLine;
    }

    public Integer getEndLine() {
        return endLine;
    }

    public void setEndLine(Integer endLine) {
        this.endLine = endLine;
    }

    public Integer getNumberOfDuplicates() {
        return numberOfDuplicates;
    }

    public void setNumberOfDuplicates(Integer numberOfDuplicates) {
        this.numberOfDuplicates = numberOfDuplicates;
    }

    public String getCodeFragment() {
        return codeFragment;
    }

    public void setCodeFragment(String codeFragment) {
        this.codeFragment = codeFragment;
    }

    public Integer getNoOfDuplicatesFiles() {
        return noOfDuplicatesFiles;
    }

    public void setNoOfDuplicatesFiles(Integer noOfDuplicatesFiles) {
        this.noOfDuplicatesFiles = noOfDuplicatesFiles;
    }

    public List<LineAndPath> getFileLineAndPath() {
        return fileLineAndPath;
    }

    public void setFileLineAndPath(List<LineAndPath> fileLineAndPath) {
        this.fileLineAndPath = fileLineAndPath;
    }
}
