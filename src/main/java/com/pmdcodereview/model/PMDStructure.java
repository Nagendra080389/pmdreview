package com.pmdcodereview.model;

import java.io.Serializable;

/**
 * Created by Nagendra on 18-06-2017.
 */
public class PMDStructure implements Serializable{

    private String classname;
    private Integer lineNumber;
    private String reviewFeedback;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PMDStructure that = (PMDStructure) o;

        if (classname != null ? !classname.equals(that.classname) : that.classname != null) return false;
        if (lineNumber != null ? !lineNumber.equals(that.lineNumber) : that.lineNumber != null) return false;
        return reviewFeedback != null ? reviewFeedback.equals(that.reviewFeedback) : that.reviewFeedback == null;
    }

    @Override
    public int hashCode() {
        int result = classname != null ? classname.hashCode() : 0;
        result = 31 * result + (lineNumber != null ? lineNumber.hashCode() : 0);
        result = 31 * result + (reviewFeedback != null ? reviewFeedback.hashCode() : 0);
        return result;
    }
}
