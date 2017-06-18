package com.pmdcodereview.model;

import java.io.Serializable;

/**
 * Created by Nagendra on 18-06-2017.
 */
public class PMDStructure implements Serializable{

    private Integer lineNumber;
    private String reviewFeedback;

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

        if (!lineNumber.equals(that.lineNumber)) return false;
        return reviewFeedback.equals(that.reviewFeedback);
    }

    @Override
    public int hashCode() {
        int result = lineNumber.hashCode();
        result = 31 * result + reviewFeedback.hashCode();
        return result;
    }
}
