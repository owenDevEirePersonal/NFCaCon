package com.deveire.dev.nfcacon;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

        nfcAdapter = NFCScanner.setupNFCScanner(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        NFCScanner.setupForegroundDispatch(this, nfcAdapter);
    }

    @Override
    protected void onPause() {

        NFCScanner.stopForegroundDispatch(this, nfcAdapter);

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
        debug.setText(NFCScanner.getTagIDFromIntent(intent));
    }


}
