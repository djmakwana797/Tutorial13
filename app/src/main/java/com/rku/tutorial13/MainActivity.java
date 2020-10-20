package com.rku.tutorial13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editTextPhone, editTextSMS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextSMS = findViewById(R.id.editTextSMS);
    }

    public void callToNumber(View view) {
        if(editTextPhone.getText().toString().length()==10){
            if(isCallPermissionAllowed()) {
                call();
            }
        }
        else
        {
            Toast.makeText(this, "Please enter valid Mobile Number", Toast.LENGTH_SHORT).show();
        }
    }

    private void call(){
        String phone = editTextPhone.getText().toString();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }
    private void SMS(){
        String phone = editTextPhone.getText().toString();
        String smsText = editTextSMS.getText().toString();
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone,null,smsText,null,null);
        Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
    }

    public void sendTextMessage(View view) {
        if(editTextPhone.getText().toString().equals("")  || editTextPhone.getText().toString().length()!=10)
        {
            Toast.makeText(this, "Please enter valid number", Toast.LENGTH_SHORT).show();
        }
        else if(editTextSMS.getText().toString().equals("")) {
            Toast.makeText(this, "Please type a message", Toast.LENGTH_SHORT).show();
        }
        else if(isSMSPermissionAllowed())
        {
            SMS();
        }

    }

    private boolean isCallPermissionAllowed(){
        if(Build.VERSION.SDK_INT>=23) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
            {
                Log.v("TAG", "Permission Granted");
                call();
                return true;
            }
            else
            {
                Log.v("TAG","Permission revoked");
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
                return false;
            }
        }
        else
        {
            Log.v("TAG","Permission Granted");
            return true;
        }
    }

    private boolean isSMSPermissionAllowed() {
        if(Build.VERSION.SDK_INT>=23) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED)
            {
                Log.v("TAG", "Permission Granted");
                SMS();
                return true;
            }
            else
            {
                Log.v("TAG","Permission revoked");
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},2);
                return false;
            }
        }
        else
        {
            Log.v("TAG","Permission Granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0]  == PackageManager.PERMISSION_GRANTED){
                    call();
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;

            case 2:
                if(grantResults.length>0 && grantResults[0]  == PackageManager.PERMISSION_GRANTED){
                    SMS();
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}