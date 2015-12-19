package org.ligboy.preference;

import android.os.Bundle;
import android.support.v14.preference.PreferenceDialogFragment;
import android.view.View;
import android.widget.NumberPicker;

/**
 * @author Ligboy.Liu ligboy@gmail.com.
 */
public class NumberPickerPreferenceDialogFragment extends PreferenceDialogFragment {

    private NumberPicker mNumberPicker;

    public static NumberPickerPreferenceDialogFragment newInstance(String key) {

        final NumberPickerPreferenceDialogFragment
                fragment = new NumberPickerPreferenceDialogFragment();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mNumberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);
        if (getNumberPickerPreference().getMaxValue() >= 0) {
            mNumberPicker.setMaxValue(getNumberPickerPreference().getMaxValue());
        }
        if (getNumberPickerPreference().getMinValue() >= 0) {
            mNumberPicker.setMinValue(getNumberPickerPreference().getMinValue());
        }
        int value = getNumberPickerPreference().getValue();
        if (value >= 0 && value >= getNumberPickerPreference().getMinValue()) {
            mNumberPicker.setValue(value);
        }
        mNumberPicker.setWrapSelectorWheel(getNumberPickerPreference().isWrapSelectorWheel());

    }

    /** @hide */
    @Override
    protected boolean needInputMethod() {
        // We want the input method to show, if possible, when dialog is displayed
        return true;
    }

    private NumberPickerPreference getNumberPickerPreference() {
        return (NumberPickerPreference) getPreference();
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int value = mNumberPicker.getValue();
            if (getNumberPickerPreference().callChangeListener(value)) {
                getNumberPickerPreference().setValue(value);
            }
        }
    }
}
