package com.tpv.mantis.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tpv.mantis.myapplication.utils.AccessGpioUtil;
import com.tpv.mantis.myapplication.utils.CommonUtil;

public class GpioActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "GpioActivity";
    TextView tv;
    TextView ledValueView;
    String mReadLedColor = AccessGpioUtil.COLOR_RED;
    String mWriteLedColor = AccessGpioUtil.COLOR_RED;
    Spinner mReadLedColorSpinner;
    Spinner mWriteLedColorSpinner;
    EditText mBrightnessEdit;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpio);

        // Example of a call to a native method
        tv = (TextView) findViewById(R.id.ledsTest);
//        tv.setText(stringFromJNI());
        ledValueView = findViewById(R.id.ledRedValueView);
        Button readBtn = findViewById(R.id.readBtn);
        Button writeBtn = findViewById(R.id.writeBtn);
        readBtn.setOnClickListener(this);
        writeBtn.setOnClickListener(this);
        mReadLedColorSpinner = findViewById(R.id.r_led_color_spinner);
        mWriteLedColorSpinner = findViewById(R.id.w_led_color_spinner);
        mBrightnessEdit = findViewById(R.id.etBrightness);
        CommonUtil.inputWatch(this, mBrightnessEdit);

    }




    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.readBtn:
                Log.d(TAG, "==>readBtn");
                StringBuilder fileSB = new StringBuilder("/sys/class/leds/");
                if (null != mReadLedColorSpinner.getSelectedItem()) {
                    Log.d(TAG, "==>mReadLedColorSpinner: " + mReadLedColorSpinner.getSelectedItem());
                    fileSB.append(mReadLedColorSpinner.getSelectedItem()).append("/brightness");
                }
                String brightness = AccessGpioUtil.readSysFile(fileSB.toString());
                Log.d(TAG, "==>led brightness: " + brightness);
                ledValueView.setText(brightness);
                break;

            case R.id.writeBtn:
                Log.d(TAG, "==>writeBtn");
                if (null != mWriteLedColorSpinner.getSelectedItem()
                        && !TextUtils.isEmpty(mBrightnessEdit.getText())) {
                    String writeLedColor = mWriteLedColorSpinner.getSelectedItem().toString();
                    int brightnessValue = Integer.parseInt(mBrightnessEdit.getText().toString());
                    AccessGpioUtil.writeSysFile("/sys/class/leds/" + writeLedColor + "/brightness", brightnessValue);
                }

                break;
        }
    }
}
