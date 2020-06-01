package org.validations;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;

import java.util.function.Predicate;

public class GenericValidation extends ValidatorBase {
    Predicate<String> predicate;

    public GenericValidation(String message, Predicate<String> predicate) {
        super(message);
        this.predicate = predicate;
    }

    @Override
    protected void eval() {
        if(srcControl.get() instanceof TextInputControl)
            hasErrors.set(predicate.test(((TextInputControl)srcControl.get()).getText()));

        if(srcControl.get() instanceof ComboBox){
            Label label = ((ComboBox<Label>) (srcControl.get())).getValue();
            hasErrors.set(false);
            if(label != null)
              hasErrors.set(predicate.test(label.getText()));
        }
    }


}