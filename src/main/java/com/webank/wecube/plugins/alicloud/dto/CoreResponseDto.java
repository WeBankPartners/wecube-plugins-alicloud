package com.webank.wecube.plugins.alicloud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author howechen
 */
public class CoreResponseDto<E extends CoreResponseOutputDto> {
    public final static String STATUS_OK = "0";
    public final static String STATUS_ERROR = "1";

    @JsonProperty(value = "result_code")
    private String resultCode;
    @JsonProperty(value = "result_message")
    private String resultMessage;
    @JsonProperty(value = "results")
    private List<E> results;

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

    public List<E> getResults() {
        return results;
    }

    public void setResults(List<E> results) {
        this.results = results;
    }

    public CoreResponseDto<E> withData(List<E> data) {
        setResults(data);
        return this;
    }

    public CoreResponseDto<E> withErrorCheck(List<E> data) {
        for (E singleData : data) {
            String errorCode = null;
            try {
                errorCode = String.valueOf(singleData.getClass().getMethod("getErrorCode").invoke(singleData));
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
            if (STATUS_ERROR.equals(errorCode)) {
                return error("Unsuccess").withData(data);
            }
        }
        return okay().withData(data);
    }

    public CoreResponseDto<E> okayWithData(List<E> data) {
        return okay().withData(data);
    }


}
