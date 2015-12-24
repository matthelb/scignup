package com.scignup.search;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeArray;

/**
 * Created by matt on 12/23/15.
 */
public class SearchModule extends ReactContextBaseJavaModule {

    public SearchModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    public void search(String uscID, Callback errorCallback, Callback successCallback) {
        try {
            WritableNativeArray arg = new WritableNativeArray();
            String[] result = new String[]{"matthelb", "Matthew", "Burke"};
            for (String r : result) {
                arg.pushString(r);
            }
            successCallback.invoke(arg);
        } catch (Exception e) {
            errorCallback.invoke(e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "SearchModule";
    }

}
