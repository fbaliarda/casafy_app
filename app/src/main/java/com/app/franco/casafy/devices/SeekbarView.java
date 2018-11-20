package com.app.franco.casafy.devices;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.app.franco.casafy.R;

import java.util.Locale;

public class SeekbarView {
    private View seekBar;
    private int min;
    private int max;
    private String name;
    private SeekBar seekBarElem;
    private TextView seekBarText;

    public SeekbarView(View seekBar, int minValue, int maxValue, String nameStr) {
        this.seekBar = seekBar;
        this.min = minValue;
        this.max = maxValue;
        this.name = nameStr;

        seekBarElem = this.seekBar.findViewById(R.id.seekBar);
        seekBarText = this.seekBar.findViewById(R.id.seekbarText);

        seekBarElem.setMax(maxValue - minValue);

        seekBarElem.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarText.setText(String.format(Locale.US, "%s: %d", name, seekBarElem.getProgress() + min));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    int getValue() {
        return min + seekBarElem.getProgress();
    }

    SeekBar getSeekBar() {
        return seekBarElem;
    }

    View getView() {
        return seekBar;
    }
}
