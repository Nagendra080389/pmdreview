package com.pmdcodereview.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Nagendra on 18-06-2017.
 */
@Document(collection = "SalesForceClass")
public class PMDStructure implements Serializable{

    @Id
    private String id;
    private String fileName;
    private String salesforceID;
    private String classname;
    private Integer lineNumber;
    private String reviewFeedback;
    private String date;
    private String branchName;
    private Integer severity;
    private String ruleName;
    private String helpURL;
    private String ruleSet;
    private Integer beginLine;
    private Integer endLine;
    private Integer numberOfDuplicates;
    private String codeFragment;
    private Integer noOfDuplicatesFiles;
    private String duplicationInFile;
    private List<LineAndPath> fileLineAndPath;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSalesforceID() {
        return salesforceID;
    }

    public void setSalesforceID(String salesforceID) {
        this.salesforceID = salesforceID;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getReviewFeedback() {
        return reviewFeedback;
    }

    public void setReviewFeedback(String reviewFeedback) {
        this.reviewFeedback = reviewFeedback;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getHelpURL() {
        return helpURL;
    }

    public void setHelpURL(String helpURL) {
        this.helpURL = helpURL;
    }

    public String getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(String ruleSet) {
        this.ruleSet = ruleSet;
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

    public List<LineAndPath> getFileLineAndPath() {
        return fileLineAndPath;
    }

    public void setFileLineAndPath(List<LineAndPath> fileLineAndPath) {
        this.fileLineAndPath = fileLineAndPath;
    }


    public Integer getNoOfDuplicatesFiles() {
        return noOfDuplicatesFiles;
    }

    public void setNoOfDuplicatesFiles(Integer noOfDuplicatesFiles) {
        this.noOfDuplicatesFiles = noOfDuplicatesFiles;
    }

    public String getDuplicationInFile() {
        return duplicationInFile;
    }

    public void setDuplicationInFile(String duplicationInFile) {
        this.duplicationInFile = duplicationInFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PMDStructure that = (PMDStructure) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        if (salesforceID != null ? !salesforceID.equals(that.salesforceID) : that.salesforceID != null) return false;
        if (classname != null ? !classname.equals(that.classname) : that.classname != null) return false;
        if (lineNumber != null ? !lineNumber.equals(that.lineNumber) : that.lineNumber != null) return false;
        if (reviewFeedback != null ? !reviewFeedback.equals(that.reviewFeedback) : that.reviewFeedback != null)
            return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (branchName != null ? !branchName.equals(that.branchName) : that.branchName != null) return false;
        if (severity != null ? !severity.equals(that.severity) : that.severity != null) return false;
        if (ruleName != null ? !ruleName.equals(that.ruleName) : that.ruleName != null) return false;
        if (helpURL != null ? !helpURL.equals(that.helpURL) : that.helpURL != null) return false;
        if (ruleSet != null ? !ruleSet.equals(that.ruleSet) : that.ruleSet != null) return false;
        if (beginLine != null ? !beginLine.equals(that.beginLine) : that.beginLine != null) return false;
        return endLine != null ? endLine.equals(that.endLine) : that.endLine == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (salesforceID != null ? salesforceID.hashCode() : 0);
        result = 31 * result + (classname != null ? classname.hashCode() : 0);
        result = 31 * result + (lineNumber != null ? lineNumber.hashCode() : 0);
        result = 31 * result + (reviewFeedback != null ? reviewFeedback.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (branchName != null ? branchName.hashCode() : 0);
        result = 31 * result + (severity != null ? severity.hashCode() : 0);
        result = 31 * result + (ruleName != null ? ruleName.hashCode() : 0);
        result = 31 * result + (helpURL != null ? helpURL.hashCode() : 0);
        result = 31 * result + (ruleSet != null ? ruleSet.hashCode() : 0);
        result = 31 * result + (beginLine != null ? beginLine.hashCode() : 0);
        result = 31 * result + (endLine != null ? endLine.hashCode() : 0);
        return result;
    }
}
