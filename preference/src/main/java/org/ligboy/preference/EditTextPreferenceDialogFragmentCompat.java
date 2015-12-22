package org.ligboy.preference;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;

/**
 * @author Ligboy.Liu ligboy@gmail.com.
 */
public class EditTextPreferenceDialogFragmentCompat extends android.support.v7.preference.PreferenceDialogFragmentCompat {
    private AppCompatEditText mEditText;

    public static EditTextPreferenceDialogFragmentCompat newInstance(String key) {
        final EditTextPreferenceDialogFragmentCompat
                fragment = new EditTextPreferenceDialogFragmentCompat();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected View onCreateDialogView(Context context) {
        View view = super.onCreateDialogView(context);
        mEditText = getEditTextPreference().getEditText();

        // Give it an ID so it can be saved/restored
        mEditText.setId(android.R.id.edit);
        //if mEditText added, then remove it from it's parent
        ViewParent editTextParent = mEditText.getParent();
        if (editTextParent != null) {
            ((ViewGroup) editTextParent).removeView(mEditText);
        }
        EditText oldEditText = (EditText) view.findViewById(android.R.id.edit);
        if (oldEditText != null) {
            ViewParent oldParent = oldEditText.getParent();
            if (oldParent != null) {
                ((ViewGroup) oldParent).removeView(oldEditText);
            }

        }
        onAddEditTextToDialogView(view, mEditText);
        return view;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mEditText.setText(getEditTextPreference().getText());
        mEditText.setSelection(mEditText.getText().length());
    }

    private EditTextPreference getEditTextPreference() {
        return (EditTextPreference) getPreference();
    }

    /** @hide */
    @Override
    protected boolean needInputMethod() {
        // We want the input method to show, if possible, when dialog is displayed
        return true;
    }

    /**
     * Adds the EditText widget of this preference to the dialog's view.
     *
     * @param dialogView The dialog view.
     */
    protected void onAddEditTextToDialogView(View dialogView, EditText editText) {
        ViewGroup container = (ViewGroup) dialogView
                .findViewById(R.id.edittext_container);
        if (container != null) {
            container.addView(editText, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {

        if (positiveResult) {
            String value = mEditText.getText().toString();
            if (getEditTextPreference().callChangeListener(value)) {
                getEditTextPreference().setText(value);
            }
        }
    }
}
