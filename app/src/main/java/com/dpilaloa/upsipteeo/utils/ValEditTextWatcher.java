package com.dpilaloa.upsipteeo.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.dpilaloa.upsipteeo.data.interfaces.ValidatorInterface;

public class ValEditTextWatcher implements TextWatcher {

    private final EditText editText;
    private final ValidatorInterface validator;
    private final String errorMessage;

    public ValEditTextWatcher(EditText editText, ValidatorInterface validator, String errorMessage) {
        this.editText = editText;
        this.validator = validator;
        this.errorMessage = errorMessage;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        if (!validator.isValid(editable.toString().trim())) {
            editText.setError(errorMessage);
        }
    }

}
