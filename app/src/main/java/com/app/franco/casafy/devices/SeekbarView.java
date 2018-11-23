package com.app.franco.casafy.devices;

import android.graphics.Color;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.app.franco.casafy.R;

import java.util.Locale;

public class SeekbarView {
    private View seekBar;
    private int min;
    private int max;
    private String text;
    private SeekBar seekBarElem;
    private TextView seekBarText;

    public SeekbarView(View seekBar, int minValue, int maxValue, String textStr) {
        this.seekBar = seekBar;
        this.min = minValue;
        this.max = maxValue;
        this.text = textStr;

        seekBarElem = this.seekBar.findViewById(R.id.seekBar);
        seekBarText = this.seekBar.findViewById(R.id.seekbarText);

        seekBarElem.setMax(maxValue - minValue);

        seekBarElem.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarText.setText(String.format(Locale.US, "%s: %d", text, getValue()));
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

    void setValue(int value) {
        seekBarText.setText(String.format(Locale.US, "%s: %d", text, getValue()));
        seekBarElem.setProgress(value - min);
    }

    SeekBar getSeekBar() {
        return seekBarElem;
    }

    View getView() {
        return seekBar;
    }
}
