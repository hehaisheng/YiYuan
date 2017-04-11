package com.shawnway.nav.app.yylg.bean;

import java.util.ArrayList;

public class HeaderGson {
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String version;
    public String error;
    public String code;
    ArrayList<FieldError> fieldErrors;



    public ArrayList<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(ArrayList<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    class FieldError {
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        private String name;
        private String status;
    }
}
