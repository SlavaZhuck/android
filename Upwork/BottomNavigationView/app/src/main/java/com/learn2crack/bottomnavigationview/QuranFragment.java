package com.learn2crack.bottomnavigationview;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Juk_VA on 26.09.2017.
 */

public class QuranFragment extends Fragment {
    public WebView mWebView;

//    public static QuranFragment newInstance() {
//
//        return new QuranFragment();
//    }
    private static QuranFragment m_A = new QuranFragment();

    static QuranFragment getQuranFragment() {
        return m_A;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_quran, container, false);
        mWebView = (WebView) v.findViewById(R.id.quaran_web);
        mWebView.getSettings().setAppCachePath(getActivity().getCacheDir().getAbsolutePath() );
        mWebView.getSettings().setAllowFileAccess( true );
        mWebView.getSettings().setAppCacheEnabled( true );
        mWebView.getSettings().setJavaScriptEnabled( true );
        mWebView.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT ); // load online by default

             mWebView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );


        mWebView.loadUrl("http://quran.islamicaffairs.gov.mv/index.php");

        // Enable Javascript
       // WebSettings webSettings = mWebView.getSettings();
        //webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());

        return v;
    }

}