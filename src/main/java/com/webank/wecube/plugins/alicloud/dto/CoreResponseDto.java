package com.webank.wecube.plugins.alicloud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author howechen
 */
public class CoreResponseDto<E> {
    public final static String STATUS_OK = "0";
    public final static String STATUS_ERROR = "1";

    @JsonProperty(value = "result_code")
    private String resultCode;
    @JsonProperty(value = "result_message")
    private String resultMessage;
    @JsonProperty(value = "results")
    private Results results;

    public static CoreResponseDto okay() {
        CoreResponseDto result = new CoreResponseDto();
        result.setResultCode(STATUS_OK);
        result.setResultMessage("Success");
        return result;
    }

    public static CoreResponseDto error(String errorMessage) {
        CoreResponseDto result = new CoreResponseDto();
        result.setResultCode(STATUS_ERROR);
        result.setResultMessage(errorMessage);
        return result;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public Results getResults() {
        return results;
    }

    public void setResults(List<E> responseResult) {

        this.results = new Results(responseResult);
    }

    public CoreResponseDto<E> withData(List<E> data) {
        setResults(data);
        return this;
    }

    public CoreResponseDto<E> okayWithData(List<E> data) {
        return okay().withData(data);
    }

    private class Results {
        private List<E> outputs;

        public Results(List<E> outputs) {
            this.outputs = outputs;
        }

        public Results() {
        }

        public List<E> getOutputs() {
            return outputs;
        }

        public void setOutputs(List<E> outputs) {
            this.outputs = outputs;
        }
    }
}
