package com.nitin.transactionandproduct.rest.model;

import java.util.List;

public class Result<T> {
    private List<T> summary;
    public Result(List<T> summary) {
        this.summary = summary;
    }

    public List<T> getSummary() {
        return summary;
    }
}
