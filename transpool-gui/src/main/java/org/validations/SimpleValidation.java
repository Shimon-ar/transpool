package org.validations;

import com.jfoenix.validation.base.ValidatorBase;

public class SimpleValidation extends ValidatorBase {

    private boolean has_error;

    public SimpleValidation(String message) {
        super(message);
        this.has_error = false;
    }


    @Override
    protected void eval() {
        hasErrors.set(has_error);
        has_error = false;
}

    public void hasError() {
        this.has_error = true;
    }
}

