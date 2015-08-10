package jp.ac.kanagawa_it.s1223066.ledcustomdisco;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    final Runtime runtime = Runtime.getRuntime();
    int rgb_hue_brightness = 0;
    int rgb_hue_saturation = 0;
    int rgb_hue_speed = 0;
    Timer rgb_hue_timer = null;
    int led_front_hue_brightness = 0;
    int led_front_hue_saturation = 0;
    int led_front_hue_speed = 0;
    Timer led_front_hue_timer = null;
    int led_back_hue_brightness = 0;
    int led_back_hue_saturation = 0;
    int led_back_hue_speed = 0;
    Timer led_back_hue_timer = null;
    Throwable recent_error = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* ********************
            Check board name and root permission
           ******************** */

        if(!Build.BOARD.equals("IS11N")){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Error");
            dialog.setMessage("Device isn't IS11N.\n\nIt does not work on this device.");
            dialog.setNeutralButton("OK", null);
            dialog.create();
            dialog.show();
            return;
        }

        setLedValueForce("rgb-pattern", 7);
        if(recent_error != null){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Error");
            dialog.setMessage("Access denied.\n\nThis app required root permission.");
            dialog.setNeutralButton("OK", null);
            dialog.create();
            dialog.show();
            return;
        }

        /* ********************
            Manual
           ******************** */

        final SeekBar manual_red = (SeekBar) findViewById(R.id.manual_red);
        manual_red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = seekBar.getProgress();
                setLedValue("red", value);
                setLedValue("led-front-red1", value);
                setLedValue("led-back-red1", value);
                setLedValue("led-front-red2", value);
                setLedValue("led-back-red2", value);
            }
        });

        final SeekBar manual_green = (SeekBar) findViewById(R.id.manual_green);
        manual_green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = seekBar.getProgress();
                setLedValue("green", value);
                setLedValue("led-front-green1", value);
                setLedValue("led-front-green2", value);
                setLedValue("led-back-green1", value);
                setLedValue("led-back-green2", value);
            }
        });

        final SeekBar manual_blue = (SeekBar) findViewById(R.id.manual_blue);
        manual_blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = seekBar.getProgress();
                setLedValue("blue", value);
                setLedValue("led-front-blue1", value);
                setLedValue("led-front-blue2", value);
                setLedValue("led-back-blue1", value);
                setLedValue("led-back-blue2", value);
            }
        });

        /* ********************
            Brightness Disco
           ******************** */

        final SeekBar brightness_speed = (SeekBar) findViewById(R.id.brightness_speed);
        brightness_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int speed = 7 - seekBar.getProgress();
                setLedValue("rgb-trgb", speed);
                setLedValue("led-front-time1", speed);
                setLedValue("led-front-time2", speed);
                setLedValue("led-back-time1", speed);
                setLedValue("led-back-time2", speed);
            }
        });

        final SeekBar brightness_length = (SeekBar) findViewById(R.id.brightness_length);
        brightness_length.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int length = 7 - seekBar.getProgress();
                setLedValue("rgb-patern", length);
                setLedValue("led-front-patern1", length);
                setLedValue("led-front-patern2", length);
                setLedValue("led-back-patern1", length);
                setLedValue("led-back-patern2", length);
            }
        });

        /* ********************
            Hue Disco
           ******************** */

        final SeekBar hue_brightness = (SeekBar) findViewById(R.id.hue_brightness);
        hue_brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = seekBar.getProgress();
                setLedValue("rgb-hue-brightness", value);
                setLedValue("led-front-hue-brightness1", value);
                setLedValue("led-front-hue-brightness2", value);
                setLedValue("led-back-hue-brightness1", value);
                setLedValue("led-back-hue-brightness2", value);
            }
        });

        final SeekBar hue_saturation = (SeekBar) findViewById(R.id.hue_saturation);
        hue_saturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = seekBar.getProgress();
                setLedValue("rgb-hue-saturation", value);
                setLedValue("led-front-hue-saturation1", value);
                setLedValue("led-front-hue-saturation2", value);
                setLedValue("led-back-hue-saturation1", value);
                setLedValue("led-back-hue-saturation2", value);
            }
        });

        final SeekBar hue_speed = (SeekBar) findViewById(R.id.hue_speed);
        hue_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = seekBar.getProgress();
                setLedValue("rgb-hue-speed", value);
                setLedValue("led-front-hue-speed1", value);
                setLedValue("led-front-hue-speed2", value);
                setLedValue("led-back-hue-speed1", value);
                setLedValue("led-back-hue-speed2", value);
            }
        });

        /* ********************
            Target
           ******************** */

        CheckBox target_notification = (CheckBox) findViewById(R.id.target_notification);
        target_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manual_red.setProgress(0);
                manual_green.setProgress(0);
                manual_blue.setProgress(0);
                brightness_speed.setProgress(0);
                brightness_length.setProgress(0);
                hue_brightness.setProgress(0);
                hue_saturation.setProgress(0);
                hue_speed.setProgress(0);
            }
        });

        CheckBox target_front = (CheckBox) findViewById(R.id.target_front);
        target_front.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manual_red.setProgress(0);
                manual_green.setProgress(0);
                manual_blue.setProgress(0);
                brightness_speed.setProgress(0);
                brightness_length.setProgress(0);
                hue_brightness.setProgress(0);
                hue_saturation.setProgress(0);
                hue_speed.setProgress(0);
            }
        });

        CheckBox target_back = (CheckBox) findViewById(R.id.target_back);
        target_back.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manual_red.setProgress(0);
                manual_green.setProgress(0);
                manual_blue.setProgress(0);
                brightness_speed.setProgress(0);
                brightness_length.setProgress(0);
                hue_brightness.setProgress(0);
                hue_saturation.setProgress(0);
                hue_speed.setProgress(0);
            }
        });

        /* ********************
            Reset
           ******************** */

        Button allreset = (Button) findViewById(R.id.allreset);
        allreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLedValueForce("red", 0);
                setLedValueForce("green", 0);
                setLedValueForce("blue", 0);
                setLedValueForce("rgb-trgb", 7);
                setLedValueForce("rgb-patern", 7);
                setLedValueForce("rgb-hue-brightness", 0);
                setLedValueForce("rgb-hue-saturation", 0);
                setLedValueForce("rgb-hue-speed", 0);
                setLedValueForce("led-front-red1", 0);
                setLedValueForce("led-front-green1", 0);
                setLedValueForce("led-front-blue1", 0);
                setLedValueForce("led-front-time1", 7);
                setLedValueForce("led-front-patern1", 7);
                setLedValueForce("led-front-hue-brightness1", 0);
                setLedValueForce("led-front-hue-saturation1", 0);
                setLedValueForce("led-front-hue-speed1", 0);
                setLedValueForce("led-front-red2", 0);
                setLedValueForce("led-front-green2", 0);
                setLedValueForce("led-front-blue2", 0);
                setLedValueForce("led-front-time2", 7);
                setLedValueForce("led-front-patern2", 7);
                setLedValueForce("led-front-hue-brightness2", 0);
                setLedValueForce("led-front-hue-saturation2", 0);
                setLedValueForce("led-front-hue-speed2", 0);
                setLedValueForce("led-back-red1", 0);
                setLedValueForce("led-back-green1", 0);
                setLedValueForce("led-back-blue1", 0);
                setLedValueForce("led-back-time1", 7);
                setLedValueForce("led-back-patern1", 7);
                setLedValueForce("led-back-hue-brightness1", 0);
                setLedValueForce("led-back-hue-saturation1", 0);
                setLedValueForce("led-back-hue-speed1", 0);
                setLedValueForce("led-back-red2", 0);
                setLedValueForce("led-back-green2", 0);
                setLedValueForce("led-back-blue2", 0);
                setLedValueForce("led-back-time2", 7);
                setLedValueForce("led-back-patern2", 7);
                setLedValueForce("led-back-hue-brightness2", 0);
                setLedValueForce("led-back-hue-saturation2", 0);
                setLedValueForce("led-back-hue-speed2", 0);
            }
        });

    }

    private void setLedValue(String where, int value){
        CheckBox target_notification = (CheckBox) findViewById(R.id.target_notification);
        CheckBox target_front = (CheckBox) findViewById(R.id.target_front);
        CheckBox target_back = (CheckBox) findViewById(R.id.target_back);
        if((where.startsWith("rgb-") && !target_notification.isChecked())
                || (where.equals("red") && !target_notification.isChecked())
                || (where.equals("green") && !target_notification.isChecked())
                || (where.equals("blue") && !target_notification.isChecked())
                || (where.startsWith("led-front-") && !target_front.isChecked())
                || (where.startsWith("led-back-") && !target_back.isChecked())){
            return;
        }
        setLedValueForce(where, value);
    }

    private void setLedValueForce(String where, int value){
        switch (where) {
            case "rgb-hue-brightness":
                rgb_hue_brightness = value;
                updateNotificationHue();
                break;
            case "rgb-hue-saturation":
                rgb_hue_saturation = value;
                updateNotificationHue();
                break;
            case "rgb-hue-speed":
                rgb_hue_speed = value;
                updateNotificationHue();
                break;
            case "led-front-hue-brightness1":
            case "led-front-hue-brightness2":
                led_front_hue_brightness = value;
                updateFrontHue();
                break;
            case "led-front-hue-saturation1":
            case "led-front-hue-saturation2":
                led_front_hue_saturation = value;
                updateFrontHue();
                break;
            case "led-front-hue_speed1":
            case "led-front-hue-speed2":
                led_front_hue_speed = value;
                updateFrontHue();
                break;
            case "led-back-hue-brightness1":
            case "led-back-hue-brightness2":
                led_back_hue_brightness = value;
                updateBackHue();
                break;
            case "led-back-hue-saturation1":
            case "led-back-hue-saturation2":
                led_back_hue_saturation = value;
                updateBackHue();
                break;
            case "led-back-hue_speed1":
            case "led-back-hue-speed2":
                led_back_hue_speed = value;
                updateBackHue();
                break;
            default:
                try {
                    Process process = runtime.exec(new String[]{"su"});
                    OutputStream out = process.getOutputStream();
                    out.write(("echo " + value + " > /sys/class/leds/" + where + "/brightness").getBytes("ASCII"));
                    out.flush();
                    out.close();
                    InputStream in = process.getInputStream();
                    in.close();
                    InputStream err = process.getErrorStream();
                    err.close();
                    process.waitFor();
                    process.destroy();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    recent_error = e;
                }
                break;
        }
    }

    private void updateNotificationHue(){
        if(rgb_hue_timer != null){
            rgb_hue_timer.cancel();
        }
        if(rgb_hue_speed == 0) {
            setLedValue("red", 0);
            setLedValue("green", 0);
            setLedValue("blue", 0);
            return;
        }
        rgb_hue_timer = new Timer();
        rgb_hue_timer.schedule(new TimerTask() {
            int hue = 0;

            @Override
            public void run() {
                int color = Color.HSVToColor(new float[]{hue % 360f, rgb_hue_saturation / 100f, rgb_hue_brightness / 100f});
                setLedValueForce("red", Color.red(color));
                setLedValueForce("green", Color.green(color));
                setLedValueForce("blue", Color.blue(color));
                hue += 5;
            }
        }, 0, 1010 - rgb_hue_speed * 10);
    }

    private void updateFrontHue(){
        if(led_front_hue_timer != null){
            led_front_hue_timer.cancel();
        }
        if(led_front_hue_speed == 0){
            setLedValue("led-front-red1", 0);
            setLedValue("led-front-green1", 0);
            setLedValue("led-front-blue1", 0);
            setLedValue("led-front-red2", 0);
            setLedValue("led-front-green2", 0);
            setLedValue("led-front-blue2", 0);
            return;
        }
        led_front_hue_timer = new Timer();
        led_front_hue_timer.schedule(new TimerTask() {
            int hue = 0;

            @Override
            public void run() {
                int color = Color.HSVToColor(new float[]{hue % 360f, led_front_hue_saturation / 100f, led_front_hue_brightness / 100f});
                setLedValueForce("led-front-red1", Color.red(color));
                setLedValueForce("led-front-green1", Color.green(color));
                setLedValueForce("led-front-blue1", Color.blue(color));
                setLedValueForce("led-front-red2", Color.red(color));
                setLedValueForce("led-front-green2", Color.green(color));
                setLedValueForce("led-front-blue2", Color.blue(color));
                hue += 5;
            }
        }, 0, 1010 - led_front_hue_speed * 10);
    }

    private void updateBackHue(){
        if(led_back_hue_timer != null){
            led_back_hue_timer.cancel();
        }
        if(led_back_hue_speed == 0){
            setLedValue("led-back-red1", 0);
            setLedValue("led-back-green1", 0);
            setLedValue("led-back-blue1", 0);
            setLedValue("led-back-red2", 0);
            setLedValue("led-back-green2", 0);
            setLedValue("led-back-blue2", 0);
            return;
        }
        led_back_hue_timer = new Timer();
        led_back_hue_timer.schedule(new TimerTask() {
            int hue = 0;

            @Override
            public void run() {
                int color = Color.HSVToColor(new float[]{hue % 360f, led_back_hue_saturation / 100f, led_back_hue_brightness / 100f});
                setLedValueForce("led-back-red1", Color.red(color));
                setLedValueForce("led-back-green1", Color.green(color));
                setLedValueForce("led-back-blue1", Color.blue(color));
                setLedValueForce("led-back-red2", Color.red(color));
                setLedValueForce("led-back-green2", Color.green(color));
                setLedValueForce("led-back-blue2", Color.blue(color));
                hue += 5;
            }
        }, 0, 1010 - led_back_hue_speed * 10);
    }

}
