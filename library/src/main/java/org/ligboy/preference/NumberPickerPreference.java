package org.ligboy.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.Preference;
import android.util.AttributeSet;

/**
 * A {@link Preference} that allows for integer.
 * <p>
 * This preference will store a string into the SharedPreferences.
 *
 * <p>
 * See {@link android.R.styleable#ListPreference_entries}.
 * See {@link android.R.styleable#ListPreference_entryValues}.
 * @attr ref android.R.styleable#ListPreference_entries
 * @attr ref android.R.styleable#ListPreference_entryValues
 *
 * @author Ligboy.Liu ligboy@gmail.com.
 */
@SuppressWarnings("JavadocReference")
public class NumberPickerPreference extends DialogPreference {

    private String mSummary;
    private int mValue = 0;
    private int mMinValue;
    private int mMaxValue;
    private boolean mWrapSelectorWheel;

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberPickerPreference, defStyleAttr, defStyleRes);
        mMinValue = a.getInt(
                R.styleable.NumberPickerPreference_minValue, -1);
        mMaxValue = a.getInt(
                R.styleable.NumberPickerPreference_maxValue, Integer.MAX_VALUE - 1);
        mWrapSelectorWheel = a.getBoolean(
                R.styleable.NumberPickerPreference_wrapSelectorWheel, true);
        a.recycle();

        /* Retrieve the Preference summary attribute since it's private
         * in the Preference class.
         */
        a = context.obtainStyledAttributes(attrs,
                R.styleable.Preference, defStyleAttr, defStyleRes);

        mSummary = TypedArrayUtils.getString(a, R.styleable.Preference_summary,
                R.styleable.Preference_android_summary);

        a.recycle();
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.numberPickerPreferenceStyle);
    }

    public NumberPickerPreference(Context context) {
        this(context, null);
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        mValue = value;
        final boolean wasBlocking = shouldDisableDependents();

        persistInt(mValue);

        notifyChanged();

        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }
    }



    /**
     * Returns the summary of this NumberPickerPreference. If the summary
     * has a {@linkplain java.lang.String#format String formatting}
     * marker in it (i.e. "%d" or "%1$d"), then the current entry
     * value will be substituted in its place.
     *
     * @return the summary with appropriate string substitution
     */
    @Override
    public CharSequence getSummary() {
        if (mSummary == null) {
            return super.getSummary();
        } else {
            CharSequence summary = mSummary;
            try {
                summary = String.format(mSummary, mValue);
            } catch (Exception ignored) {
            }
            return summary;
        }
    }

    /**
     * Sets the summary for this Preference with a CharSequence.
     * If the summary has a
     * {@linkplain java.lang.String#format String formatting}
     * marker in it (i.e. "%d" or "%1$d"), then the current entry
     * value will be substituted in its place when it's retrieved.
     *
     * @param summary The summary for the preference.
     */
    @Override
    public void setSummary(CharSequence summary) {
        super.setSummary(summary);
        if (summary == null && mSummary != null) {
            mSummary = null;
        } else if (summary != null && !summary.equals(mSummary)) {
            mSummary = summary.toString();
        }
    }

    public int getMinValue() {
        return mMinValue;
    }

    public void setMinValue(int minValue) {
        mMinValue = minValue;
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(int maxValue) {
        mMaxValue = maxValue;
    }

    public boolean isWrapSelectorWheel() {
        return mWrapSelectorWheel;
    }

    public void setWrapSelectorWheel(boolean wrapSelectorWheel) {
        mWrapSelectorWheel = wrapSelectorWheel;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? getPersistedInt(mValue) : (int) defaultValue);
    }

    @Override
    public boolean shouldDisableDependents() {
        return mValue < 0 || super.shouldDisableDependents();
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.value = getValue();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        setValue(myState.value);
    }
    private static class SavedState extends BaseSavedState {
        int value;

        public SavedState(Parcel source) {
            super(source);
            value = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(value);
        }

        protected SavedState(Parcelable superState) {
            super(superState);
        }

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
