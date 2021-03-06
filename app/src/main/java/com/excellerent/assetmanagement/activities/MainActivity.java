package com.excellerent.assetmanagement.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.excellerent.assetmanagement.R;

import org.tensorflow.lite.Interpreter;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView textValue;
    private Button copyButton;
    private Button mailTextButton;
    //private Button continueButton;

    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "MainActivity";
    SharedPreferences sp;
    public static final String USER_PREF = "USER_PREF" ;
    public static final String KEY_TEXT = "KEY_TEXT";

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;

    protected Interpreter tflite;

    CameraActivity camactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        statusMessage = (TextView)findViewById(R.id.status_message);
        textValue = (TextView)findViewById(R.id.text_value);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);
        sp = getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);

        Button readTextButton = (Button) findViewById(R.id.read_text_button);
        readTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch Ocr capture activity.
                Intent intent = new Intent(getApplicationContext(), OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());

                startActivityForResult(intent, RC_OCR_CAPTURE);
            }
        });

        copyButton = (Button) findViewById(R.id.copy_text_button);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard =
                            (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(textValue.getText().toString());
                } else {
                    android.content.ClipboardManager clipboard =
                            (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", textValue.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), R.string.clipboard_copy_successful_message, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), clip.toString(), Toast.LENGTH_SHORT).show();
                    //go to the other activity

                    Intent t;
                    PackageManager manager = getPackageManager();

                    t= manager.getLaunchIntentForPackage("info.androidhive.sqlite");
                    if (t != null) {
                        //Toast.makeText(getApplicationContext(), "opening other app", Toast.LENGTH_SHORT).show();
                        t.addFlags((Intent.FLAG_ACTIVITY_NEW_TASK));
                        //t.addCategory(Intent.CATEGORY_LAUNCHER);
                        t.setAction(Intent.ACTION_SEND);
                        t.putExtra("EXTRA_TEXT", "hdfhdhjfgjf");
                        t.setType("text/plain");
                        startActivity(t);
                    }else{
                        Toast.makeText(getApplicationContext(), "not opening other app", Toast.LENGTH_SHORT).show();
                        t= new Intent(Intent.ACTION_VIEW);
                        t.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(t);
//                            Toast.makeText(getApplicationContext(),
//                                    R.string.no_email_client_error, Toast.LENGTH_SHORT).show();
                    }

                }
                //Toast.makeText(getApplicationContext(), textValue.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        mailTextButton = (Button) findViewById(R.id.mail_text_button);
        mailTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetectLogoActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (textValue.getText().toString().isEmpty()) {
            copyButton.setVisibility(View.GONE);
            mailTextButton.setVisibility(View.GONE);
        } else {
            //copyButton.setVisibility(View.VISIBLE);
            mailTextButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    statusMessage.setText(R.string.ocr_success);
                    textValue.setText(text);
                    Log.d(TAG, "Text read: " + text);


                    //copyButton.setVisibility(View.GONE);
                    //mailTextButton.setVisibility(View.GONE);
                    //continueButton.setVisibility(View.VISIBLE);



                } else {
                    statusMessage.setText(R.string.ocr_failure);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(this, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.one:
                openDetectLogoActivity();
                // do your code
                return true;
            case R.id.two:
                return true;
                // do your code
            default:
                return false;
        }
    }

    public void openDetectLogoActivity(){
        Intent intent = new Intent(this, DetectLogo.class);
        intent.putExtra("serial_num", textValue.getText().toString());
        startActivity(intent);
    }


}
