package com.materialsteam.materials.config;

import android.widget.EditText;

public class Functions {

    public static void setValidate(EditText inputField, String label) {
        inputField.setError(label);
        inputField.requestFocus();
    }
}
