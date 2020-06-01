package org.validations;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextInputControl;

public class IntegerPositiveValidation extends ValidatorBase {

    public IntegerPositiveValidation(String message) {
        super(message);
    }

        @Override

        protected void eval() {
            if (srcControl.get() instanceof TextInputControl) {
                evalTextInputField();
            }
        }

        private void evalTextInputField() {
            TextInputControl textField = (TextInputControl) srcControl.get();
            String text = textField.getText();
            try {
                hasErrors.set(false);
                if (!text.isEmpty()) {
                    int num  = Integer.parseInt(text);
                    if(num < 0)
                        hasErrors.set(true);
                }
            } catch (Exception e) {
                hasErrors.set(true);
            }
        }
    }

