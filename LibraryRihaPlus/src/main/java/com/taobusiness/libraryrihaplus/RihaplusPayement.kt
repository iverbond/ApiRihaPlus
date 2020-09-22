package com.taobusiness.libraryrihaplus

import android.content.Context
import android.os.Bundle
import android.view.*
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class RihaplusPayement(): DialogFragment() {

    val TAG = "RechargeDialog"

    private lateinit var id: String

    fun setId(id: String){
        this.id = id
    }

    private lateinit var webView: WebView

    private var url = "https://account.rihaplus.com"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.dialog_payement, container, false)

        dialog!!.window!!.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER)
        dialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)

        webView = v.findViewById(R.id.webView)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        myWeb()
    }

    private fun myWeb() {
        assert(webView != null)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        webView.isScrollbarFadingEnabled = false
        webView.settings.allowFileAccess
        webView.settings.allowUniversalAccessFromFileURLs
        webView.settings.pluginState = WebSettings.PluginState.ON
        webView.settings.domStorageEnabled = true
        webView.settings.allowContentAccess = true
        webView.settings.allowFileAccess = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.setSupportZoom(false)
        webView.settings.builtInZoomControls = true
        webView.settings.setAppCacheEnabled(true)

        webView.webViewClient = ourViewClient()
        webView.webChromeClient = ourChromeClient()
        webView.loadUrl(url)
        val myJavaScriptInterface = MyJavaScriptInterface(context!!)
        webView.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction")
    }

    inner class MyJavaScriptInterface internal constructor(var context: Context) {
        @JavascriptInterface
        fun showToast(toast: String?) {
            Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
            webView.loadUrl("javascript:document.getElementById(\"Button3\").innerHTML = \"bye\";")
        }

        @JavascriptInterface
        fun openAndroidDialog() {
            val myDialog = AlertDialog.Builder(context)
            myDialog.setTitle("DANGER!")
            myDialog.setMessage("You can do what you want!")
            myDialog.setPositiveButton("ON", null)
            myDialog.show()
        }

    }

    inner class ourViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            CookieManager.getInstance().setAcceptCookie(true)
            return true
        }

        override fun onReceivedError(
            view: WebView,
            errorCode: Int,
            description: String,
            failingUrl: String
        ) {
            webView.loadUrl("file:///android_asset/error.html")
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
        }
    }

    open inner class ourChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView, progress: Int) {
            val webUrl = webView.url
        }
    }
}