// Copyright 2016 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package com.example.flutter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dynamsoft.barcode.Barcode;
import com.dynamsoft.barcode.BarcodeReader;
import com.dynamsoft.barcode.ReadResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterView;

public class BarcodeReaderActivity extends Activity {
    private static final String TAG = "BarcodeReaderActivity";

    private FlutterView flutterView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FlutterMain.ensureInitializationComplete(getApplicationContext(), null);
        setContentView(R.layout.hello_services_layout);

        flutterView = (FlutterView) findViewById(R.id.flutter_view);
        flutterView.runFromBundle(FlutterMain.findAppBundlePath(getApplicationContext()), null);

        flutterView.addOnMessageListener("getBarcode",
            new FlutterView.OnMessageListener() {
                @Override
                public String onMessage(FlutterView view, String message) {
                    return onGetBarcode(message);
                }
            });
    }

    @Override
    protected void onDestroy() {
        if (flutterView != null) {
            flutterView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        flutterView.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        flutterView.onPostResume();
    }

    private String onGetBarcode(String json) {
        String filename;
        try {
            JSONObject message = new JSONObject(json);
            filename = message.getString("filename");
        } catch (JSONException e) {
            Log.e(TAG, "JSON exception", e);
            return null;
        }

        String locationProvider;
        String barcodeResult = "No barcode detected";
        File file = new File(filename);
        if (!file.exists()) {
            barcodeResult = "No file exists: " + file.toString();
            Toast.makeText(BarcodeReaderActivity.this, barcodeResult, Toast.LENGTH_LONG).show();

            return null;
        }
        else {
            Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
            BarcodeReader reader = new BarcodeReader("license");
            ReadResult result = reader.readSingle(bitmap, Barcode.QR_CODE);
            Barcode[] all = result.barcodes;
            if (all != null && all.length == 1) {
                barcodeResult = all[0].displayValue;
            }
            else {
                barcodeResult = "no barcode found: " + file.toString();
            }

            bitmap.recycle();

        }
//        String permission = "android.permission.ACCESS_FINE_LOCATION";
//        Location location = null;
//        if (checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
//            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            location = locationManager.getLastKnownLocation(locationProvider);
//        }

        JSONObject reply = new JSONObject();
        try {
            if (barcodeResult != null) {
              reply.put("result", barcodeResult);
            } else {
              reply.put("result", "No barcode detected");
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON exception", e);
            return null;
        }

        return reply.toString();
    }
}
