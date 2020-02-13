package com.webank.wecube.plugins.alicloud.dto;

import java.util.List;

/**
 * @author howechen
 */
public class CoreRequestDto<E> {
    private String requestId;
    private String operator;
    private List<E> inputs;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<E> getInputs() {
        return inputs;
    }

    public void setInputs(List<E> inputs) {
        this.inputs = inputs;
    }

}
