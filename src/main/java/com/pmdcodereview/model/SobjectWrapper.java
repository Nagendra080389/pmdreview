package com.pmdcodereview.model;

import com.sforce.soap.tooling.SymbolTable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SobjectWrapper {

    public String name;

    public String body;

    private Map<Integer, List<String>> lineNumberError = new HashMap<>();

    private Integer lineNumber;

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

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }
}
