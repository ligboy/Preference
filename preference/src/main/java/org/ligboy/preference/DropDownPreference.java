package org.ligboy.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A {@link Preference} that displays a list of entries as
 * a dropdown.
 * <p>
 * This preference will store a string into the SharedPreferences. This string will be the value
 * from the {@link #setEntryValues(CharSequence[])} array.
 *
 * @attr ref android.R.styleable#ListPreference_entries
 * @attr ref android.R.styleable#ListPreference_entryValues
 *
 * @author Ligboy.Liu ligboy@gmail.com.
 */
public class DropDownPreference extends Preference {
    private static final String TAG = "DropDownPreference";

    private final Context mContext;
    private final ArrayAdapter<CharSequence> mAdapter;
    private final AppCompatSpinner mSpinner;

    private final ArrayList<CharSequence> mEntryList = new ArrayList<>();
    private final ArrayList<CharSequence> mEntryValueList = new ArrayList<>();
    private String mValue;
    private String mSummary;

    private int mSelectedPosition = AdapterView.INVALID_POSITION;

    public DropDownPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        mAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_dropdown_item, mEntryList);

        mSpinner = new AppCompatSpinner(context);

        mSpinner.setVisibility(View.INVISIBLE);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                DropDownPreference.this.onItemSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                /* no-op */
            }
        });

        // Support XML specification like ListPreferences do.
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownPreference);
        CharSequence[] entries = a.getTextArray(R.styleable.DropDownPreference_android_entries);
        CharSequence[] entryValues = a.getTextArray(R.styleable.DropDownPreference_android_entryValues);
        if (entries != null && entryValues != null) {
            Collections.addAll(mEntryValueList, entryValues);
            mAdapter.addAll(entries);
        }
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

    public DropDownPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DropDownPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.dropDownPreferenceStyle);
    }

    public DropDownPreference(Context context) {
        this(context, null);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        mSpinner.setSelection(mSelectedPosition);
        if (holder.itemView.equals(mSpinner.getParent())) return;
        if (mSpinner.getParent() != null) {
            ((ViewGroup)mSpinner.getParent()).removeView(mSpinner);
        }
        final ViewGroup vg = (ViewGroup)holder.itemView;
        vg.addView(mSpinner, 0);
        final ViewGroup.LayoutParams lp = mSpinner.getLayoutParams();
        lp.width = 0;
        mSpinner.setLayoutParams(lp);
    }

    private void onItemSelected(int position) {
        if (position == mSelectedPosition) {
            return;
        }
        CharSequence value = mEntryValueList.get(position);

        if (callChangeListener(value)) {
            mSpinner.setSelection(position);
            mSelectedPosition = mSpinner.getSelectedItemPosition();
            setValue(value != null ? value.toString() : null);
        } else {
            mSpinner.setSelection(mSelectedPosition, false);
        }
    }

    public void addItem(int captionResId, String value) {
        addItem(mContext.getResources().getString(captionResId), value);
    }

    public void addItem(String caption, String value) {
        mEntryValueList.add(value);
        mAdapter.add(caption);
    }

    public int getEntriesCount() {
        return mAdapter.getCount();
    }

    public void clearEntries(){
        mEntryValueList.clear();
        mAdapter.clear();
    }

    /**
     * Sets the human-readable entries to be shown in the list. This will be
     * shown in subsequent dialogs.
     * <p>
     * Each entry must have a corresponding index in
     * {@link #setEntryValues(CharSequence[])}.
     *
     * @param entries The entries.
     * @see #setEntryValues(CharSequence[])
     */
    public void setEntries(CharSequence[] entries) {
        mEntryList.clear();
        mAdapter.addAll((String[]) entries);
    }

    /**
     * @see #setEntries(CharSequence[])
     * @param entriesResId The entries array as a resource.
     */
    public void setEntries(@ArrayRes int entriesResId) {
        setEntries(getContext().getResources().getTextArray(entriesResId));
    }

    /**
     * The list of entries to be shown in the list in subsequent dialogs.
     *
     * @return The list as an array.
     */
    public CharSequence[] getEntries() {
        return (CharSequence[]) mEntryList.toArray();
    }

    /**
     * The array to find the value to save for a preference when an entry from
     * entries is selected. If a user clicks on the second item in entries, the
     * second item in this array will be saved to the preference.
     *
     * @param entryValues The array to be used as values to save for the preference.
     */
    public void setEntryValues(CharSequence[] entryValues) {
        Collections.addAll(mEntryValueList, entryValues);
    }

    /**
     * @see #setEntryValues(CharSequence[])
     * @param entryValuesResId The entry values array as a resource.
     */
    public void setEntryValues(@ArrayRes int entryValuesResId) {
        setEntryValues(getContext().getResources().getTextArray(entryValuesResId));
    }

    /**
     * Returns the array of values to be saved for the preference.
     *
     * @return The array of values.
     */
    public CharSequence[] getEntryValues() {
        return ((CharSequence[]) mEntryValueList.toArray());
    }

    /**
     * Sets the value of the key. This should be one of the entries in
     * {@link #getEntryValues()}.
     *
     * @param value The value to set for the key.
     */
    public void setValue(String value) {
        // Always persist/notify the first time.
        final boolean changed = !TextUtils.equals(mValue, value);
        if (changed) {
            int indexOfValue = findIndexOfValue(value);
            mSelectedPosition = indexOfValue;
            mValue = value;
            persistString(value);
            notifyChanged();
            final boolean wasBlocking = shouldDisableDependents();
            final boolean isBlocking = shouldDisableDependents();
            if (isBlocking != wasBlocking) {
                notifyDependencyChange(isBlocking);
            }
        }
    }

    @Override
    public boolean shouldDisableDependents() {
        return mValue == null || super.shouldDisableDependents();
    }

    /**
     * Returns the summary of this DropDownPreference. If the summary
     * has a {@linkplain java.lang.String#format String formatting}
     * marker in it (i.e. "%s" or "%1$s"), then the current entry
     * value will be substituted in its place.
     *
     * @return the summary with appropriate string substitution
     */
    @Override
    public CharSequence getSummary() {
        final CharSequence entry = getEntry();
        if (mSummary == null) {
            return super.getSummary();
        } else {
            CharSequence summary = mSummary;
            try {
                summary = String.format(mSummary, entry == null ? "" : entry);
            } catch (Exception e) {
                if (BuildConfig.DEBUG) Log.d(TAG, e.getLocalizedMessage());
            }
            return summary;
        }
    }

    /**
     * Sets the summary for this Preference with a CharSequence.
     * If the summary has a
     * {@linkplain java.lang.String#format String formatting}
     * marker in it (i.e. "%s" or "%1$s"), then the current entry
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

    /**
     * Sets the value to the given index from the entry values.
     *
     * @param index The index of the value to set.
     */
    public void setValueIndex(int index) {
        setValue(mEntryValueList.get(index).toString());
    }

    /**
     * Returns the value of the key. This should be one of the entries in
     * {@link #getEntryValues()}.
     *
     * @return The value of the key.
     */
    public String getValue() {
        return mValue;
    }

    /**
     * Returns the entry corresponding to the current value.
     *
     * @return The entry corresponding to the current value, or null.
     */
    public CharSequence getEntry() {
        int index = getValueIndex();
        return index >= 0 ? mEntryList.get(index) : null;
    }

    /**
     * Returns the index of the given value (in the entry values array).
     *
     * @param value The value whose index should be returned.
     * @return The index of the value, or -1 if not found.
     */
    public int findIndexOfValue(String value) {
        if (value != null && mEntryValueList != null) {
            for (int i = mEntryValueList.size() - 1; i >= 0; i--) {
                if (mEntryValueList.get(i).equals(value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int getValueIndex() {
        return findIndexOfValue(mValue);
    }

    @Override
    protected String onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String value = restoreValue ? getPersistedString(mValue) : (String) defaultValue;
        setValue(value);
    }

    @Override
    protected void onClick() {
        mSpinner.performClick();
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
        String value;

        public SavedState(Parcel source) {
            super(source);
            value = source.readString();
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(value);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
