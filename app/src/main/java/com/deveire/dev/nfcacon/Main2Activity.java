package com.deveire.dev.nfcacon;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.deveire.dev.nfcacon.NfcScanner;

public class Main2Activity extends AppCompatActivity
{

    NfcAdapter nfcAdapter;
    TextView debug;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        debug = (TextView) findViewById(R.id.debug);

        nfcAdapter = NfcScanner.setupNfcScanner(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        nfcAdapter = NfcScanner.setupNfcScanner(this);
        if(nfcAdapter == null)
        {
            Toast.makeText(this, "Please turn on NFC scanner before continuing", Toast.LENGTH_LONG).show();
            finish();
        }
        else
        {
            NfcScanner.setupForegroundDispatch(this, nfcAdapter);
        }
    }

    @Override
    protected void onPause() {

        nfcAdapter = NfcScanner.setupNfcScanner(this);
        if(nfcAdapter == null)
        {
            Toast.makeText(this, "Please turn on NFC scanner before continuing", Toast.LENGTH_LONG).show();
            finish();
        }
        else
        {
            NfcScanner.setupForegroundDispatch(this, nfcAdapter);
        }

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        Log.i("NFCTEST", "onNewIntent: " + intent.toString());
        debug.setText(NfcScanner.getTagIDFromIntent(intent));
    }


}
