package org.ligboy.preference;

import android.os.Bundle;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.KeyEvent;
import android.view.View;

/**
 * @author Ligboy.Liu ligboy@gmail.com.
 */
public class SeekBarDialogPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat implements View.OnKeyListener {

    private AppCompatSeekBar mSeekBar;
    private SeekBarDialogPreference mPreference;

    public static SeekBarDialogPreferenceDialogFragmentCompat newInstance(String key) {

        final SeekBarDialogPreferenceDialogFragmentCompat
                fragment = new SeekBarDialogPreferenceDialogFragmentCompat();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreference = (SeekBarDialogPreference) getPreference();
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mSeekBar = (AppCompatSeekBar) view.findViewById(R.id.seekbar);
        view.setOnKeyListener(this);
        mSeekBar.setMax(mPreference.getMax());
        mSeekBar.setProgress(mPreference.getProgress());
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() != KeyEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_PLUS
                    || keyCode == KeyEvent.KEYCODE_EQUALS) {
                mSeekBar.setProgress(mSeekBar.getProgress() + 1);
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_MINUS) {
                mSeekBar.setProgress(mSeekBar.getProgress() - 1);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int progress = mSeekBar.getProgress();
            if (mPreference.callChangeListener(progress)) {
                mPreference.setProgress(progress);
            }
        }
    }
}
