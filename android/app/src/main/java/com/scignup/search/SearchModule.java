package com.scignup.search;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeArray;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by matt on 12/23/15.
 */
public class SearchModule extends ReactContextBaseJavaModule {

    private final SearchSession ss;

    public SearchModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.ss = new SearchSession();
    }

    @ReactMethod
    public void search(String uscID, Callback errorCallback, Callback successCallback) {
        try {
            ss.login("USERNAME", "PASSWORD");
            Map<String, String> data = new HashMap<>();
            data.put("principalId", uscID);
            List<String[]> result = ss.search(data);
            WritableNativeArray arg = new WritableNativeArray();
            for (String[] a : result) {
                WritableNativeArray aa = new WritableNativeArray();
                for (String s : a) {
                    aa.pushString(s);
                }
                arg.pushArray(aa);
            }
            successCallback.invoke(arg);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            errorCallback.invoke(e.getMessage() + "\n" + sw.toString());
        }
    }

    @Override
    public String getName() {
        return "SearchModule";
    }

}
