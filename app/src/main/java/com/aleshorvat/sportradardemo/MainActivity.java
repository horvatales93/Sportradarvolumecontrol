package com.aleshorvat.sportradardemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText currentVolumeInput;
    private EditText scaleInput;
    private Button setVolumeBtn;
    private Button setScaleBtn;
    private VolumeControl volumeControl;
    private boolean volumeInputValid = false;
    private boolean scaleInputValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentVolumeInput = findViewById(R.id.etCurrentVolume);
        scaleInput = findViewById(R.id.etVolumeScale);
        setVolumeBtn = findViewById(R.id.btnSetCurrentVolume);
        setScaleBtn = findViewById(R.id.btnSetVolumeScale);
        volumeControl = findViewById(R.id.volumeControl);

        setVolumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(volumeInputValid){
                    volumeControl.setVolumeLevel(Integer.parseInt(currentVolumeInput.getText().toString()));
                }
            }
        });

        setScaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scaleInputValid) {
                    volumeControl.setVolumeScale(Integer.parseInt(scaleInput.getText().toString()));
                }
            }
        });

        currentVolumeInput.addTextChangedListener(new TextValidator(currentVolumeInput) {
            @Override
            public void validate(TextView textView, String text) {
                if(text.isEmpty()) {
                    volumeInputValid = false;
                    setVolumeBtn.setEnabled(false);
                }else {
                    int currentVolumeInput = Integer.parseInt(text);
                    if (currentVolumeInput < 0 || currentVolumeInput > 100) {
                        textView.setError(getString(R.string.volume_input_validation_message));
                        volumeInputValid = false;
                        setVolumeBtn.setEnabled(false);
                    } else {
                        volumeInputValid = true;
                        setVolumeBtn.setEnabled(true);
                    }
                }
            }
        });

        scaleInput.addTextChangedListener(new TextValidator(scaleInput) {
            @Override
            public void validate(TextView textView, String text) {
                if(text.isEmpty()) {
                    scaleInputValid = false;
                    setScaleBtn.setEnabled(false);
                }else {
                    scaleInputValid = true;
                    setScaleBtn.setEnabled(true);
                }
            }
        });

        volumeControl.setEventListener(new VolumeControl.IVolumeControlEventListener() {
            @Override
            public void onVolumeControlChanged() {
                String volumeLevel = String.valueOf(volumeControl.getVolumeLevel());
                String volumeScale = String.valueOf(volumeControl.getVolumeScale());

                if(!volumeLevel.equals(currentVolumeInput.getText().toString())){
                    currentVolumeInput.setText(volumeControl.getVolumeLevel() + "");
                }

                if(!volumeScale.equals(scaleInput.getText().toString())) {
                    scaleInput.setText(volumeControl.getVolumeScale() + "");
                }
            }
        });

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey("VolumeLevel")){
                volumeControl.setVolumeLevel(savedInstanceState.getInt("VolumeLevel"));
            }

            if(savedInstanceState.containsKey("VolumeScale")){
                volumeControl.setVolumeScale(savedInstanceState.getInt("VolumeScale"));
            }
        }

        currentVolumeInput.setText(String.valueOf(volumeControl.getVolumeLevel()));
        scaleInput.setText(String.valueOf(volumeControl.getVolumeScale()));
    }

    private abstract class TextValidator implements TextWatcher {
        private final TextView textView;

        public TextValidator(TextView textView) {
            this.textView = textView;
        }

        public abstract void validate(TextView textView, String text);

        @Override
        final public void afterTextChanged(Editable s) {
            String text = textView.getText().toString();
            validate(textView, text);
        }

        @Override
        final public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

        @Override
        final public void onTextChanged(CharSequence s, int start, int before, int count) {  }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("VolumeLevel", volumeControl.getVolumeLevel());
        outState.putInt("VolumeScale", volumeControl.getVolumeScale());
    }
}
