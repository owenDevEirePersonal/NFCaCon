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

public class Main3Activity extends AppCompatActivity
{
    NfcAdapter nfcAdapter;
    TextView debug;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        debug = (TextView) findViewById(R.id.debug);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null)
        {
            Log.e("NFCTEST", "Error: default nfcAdapter is null");
            Toast.makeText(this, "NFC Adapter Not Found", Toast.LENGTH_LONG).show();
        }
        else
        {
            Log.i("NFCTEST", "NFC adapter found");
            if(nfcAdapter.isEnabled())
            {
                Log.i("NFCTEST", "NFC is enabled");
                handleIntent(getIntent());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupForegroundDispatch(this, nfcAdapter);
    }

    @Override
    protected void onPause() {

        stopForegroundDispatch(this, nfcAdapter);

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
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Log.i("NFCTEST", "tag: " + tag);
            Log.i("NFCTEST", "tag translated: " + bytesToHexString(tag.getId()));
            debug.setText(bytesToHexString(tag.getId()));
            /*String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }*/
        }
    }


    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        Log.i("NFCTEST", "setupForgroundDispatch Start");
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        IntentFilter[] filters = new IntentFilter[2];
        filters[0] = new IntentFilter();
        filters[0].addAction("android.nfc.action.TECH_DISCOVERED");
        filters[1] = new IntentFilter();
        filters[1].addAction("android.nfc.action.TAG_DISCOVERED");
        filters[1].addCategory(Intent.CATEGORY_DEFAULT);

        Log.i("NFCTEST", "setupForgroundDispatch enabling");
        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
        Log.i("NFCTEST", "setupForgroundDispatch End");
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        Log.i("NFCTEST", "stopForegroundDispatch");
        adapter.disableForegroundDispatch(activity);
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString();
    }
}
