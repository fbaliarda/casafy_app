package com.app.franco.casafy.devices;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.app.franco.casafy.R;

public class ColorPickerView {
    private View colorPicker;
    private ImageView colorPickerButton;
    private View colorPickerDialog;

    private SeekBar redSeekbar;
    private SeekBar greenSeekbar;
    private SeekBar blueSeekbar;

    private int color = Color.WHITE;

    private View previewColor;

    public static ColorPickerView defaultColorPicker;

    private AlertDialog colorPickerAlert;

    public ColorPickerView(Context context, LayoutInflater layoutInflater) {
        colorPicker = layoutInflater.inflate(R.layout.content_colorpicker, null);
        colorPickerButton = colorPicker.findViewById(R.id.btnColorPicker);
        colorPickerDialog = layoutInflater.inflate(R.layout.content_colorpicker_dialog, null);
        redSeekbar = colorPickerDialog.findViewById(R.id.seekBarRed);
        greenSeekbar = colorPickerDialog.findViewById(R.id.seekBarGreen);
        blueSeekbar = colorPickerDialog.findViewById(R.id.seekBarBlue);
        previewColor = colorPickerDialog.findViewById(R.id.previewColor);

        redSeekbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        greenSeekbar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        blueSeekbar.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

        redSeekbar.setMax(255);
        greenSeekbar.setMax(255);
        blueSeekbar.setMax(255);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                previewColor.setBackgroundColor(Color.rgb(redSeekbar.getProgress(), greenSeekbar.getProgress(), blueSeekbar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };

        redSeekbar.setOnSeekBarChangeListener(seekBarChangeListener);
        greenSeekbar.setOnSeekBarChangeListener(seekBarChangeListener);
        blueSeekbar.setOnSeekBarChangeListener(seekBarChangeListener);

        builder.setView(colorPickerDialog)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        setColor(Color.rgb(redSeekbar.getProgress(), greenSeekbar.getProgress(), blueSeekbar.getProgress()));
                    }
                });

        colorPickerAlert = builder.create();

        colorPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redSeekbar.setProgress(Color.red(color));
                greenSeekbar.setProgress(Color.green(color));
                blueSeekbar.setProgress(Color.blue(color));
                colorPickerAlert.show();
            }
        });
    }

    public void setColor(int color) {
        this.color = color;
        colorPickerButton.setBackgroundColor(color);
        previewColor.setBackgroundColor(color);
    }

    public String getColor() {
        return Integer.toHexString(color);
    }

    public View getView() {
        return colorPicker;
    }
}
