package com.ashutosh.flicker.remote;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.WeakHashMap;

/**
 * Created by Reetesh on 3/5/2017.
 */

public class NetworkLoader extends AsyncTaskLoader<ServerResponse> {
    public static final String POST = "POST";
    public static final String GET = "GET";
    public static String URL_PARAM = "url";
    public static final String JSON_REQ_STR = "json_req_str";
    boolean isJson = true;
    private boolean mIsRunning;
    private ServerResponse mLoginResponseBean;
    private String mUrl;
    private String mReqType;

    public NetworkLoader(Context context, String url, String requestTypePost) {
        super(context);
        mUrl = url;
        mReqType = requestTypePost;
    }

    @Override
    public ServerResponse loadInBackground() {
        mIsRunning = true;
        ServerResponse streamBean = null;
        streamBean = WebUtils.getServerStream(mUrl, null, mReqType);
        mLoginResponseBean = streamBean;
        mIsRunning = false;
        return streamBean;
    }

    @Override
    protected void onStartLoading() {
        if (mLoginResponseBean != null) {
            deliverResult(mLoginResponseBean);
        } else {
            if (!mIsRunning) {
                forceLoad();
            }
        }
    }

    @Override
    protected void onReset() {
        WeakHashMap<ServerResponse, ServerResponse> weakHashMap = new WeakHashMap<ServerResponse, ServerResponse>();
        weakHashMap.put(mLoginResponseBean, mLoginResponseBean);
        mLoginResponseBean = null;
        super.onReset();
    }

    @Override
    public void deliverResult(ServerResponse data) {
        if (isStarted()) {
            super.deliverResult(mLoginResponseBean);
        }
    }

}