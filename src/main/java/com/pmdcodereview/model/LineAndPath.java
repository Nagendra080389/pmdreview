package com.pmdcodereview.model;

import java.io.Serializable;
import java.util.Objects;

public class LineAndPath implements Serializable {

    private Integer line;
    private String path;

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineAndPath that = (LineAndPath) o;
        return Objects.equals(line, that.line) &&
                Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {

        return Objects.hash(line, path);
    }
}
