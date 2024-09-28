package com.example.videomedicalvisit

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.videomedicalvisit.databinding.ActivityWebViewBinding
import java.util.Locale


class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding
    lateinit var title: String
    lateinit var url: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.WV.settings.javaScriptEnabled = true
        binding.tv.text = intent.extras!!.getString("name")
        binding.WV.webViewClient = WebViewClient()
       // binding.WV.loadUrl()

        val pdfWebViewClient = PdfWebViewClient(this, binding.WV)
        pdfWebViewClient.loadPdfUrl(
            "https://www.google.co.in/url?${intent.extras!!.getString("url").toString()}"
        )


    }

    private class PdfWebViewClient(private val mContext: Context, private val mWebView: WebView) : WebViewClient() {
        private var mProgressDialog: ProgressDialog? = null
        private var isLoadingPdfUrl = false

        init {
            mWebView.webViewClient = this
        }

        fun loadPdfUrl(url: String) {
            mWebView.stopLoading()

            if (!TextUtils.isEmpty(url)) {
                isLoadingPdfUrl = isPdfUrl(url)
                if (isLoadingPdfUrl) {
                    mWebView.clearHistory()
                }

                showProgressDialog()
            }

            mWebView.loadUrl(url)
        }

        @Suppress("deprecation")
        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            return shouldOverrideUrlLoading(url)
        }

        @Suppress("deprecation")
        override fun onReceivedError(webView: WebView, errorCode: Int, description: String, failingUrl: String) {
            handleError(errorCode, description.toString(), failingUrl)
        }

        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(webView: WebView, request: WebResourceRequest): Boolean {
            val uri = request.url
            return shouldOverrideUrlLoading(webView, uri.toString())
        }

        @TargetApi(Build.VERSION_CODES.N)
        override fun onReceivedError(webView: WebView, request: WebResourceRequest, error: WebResourceError) {
            val uri = request.url
            handleError(error.errorCode, error.description.toString(), uri.toString())
        }

        override fun onPageFinished(view: WebView, url: String) {
            Log.i(TAG, "Finished loading. URL : $url")
            dismissProgressDialog()
        }

        private fun shouldOverrideUrlLoading(url: String): Boolean {
            Log.i(TAG, "shouldOverrideUrlLoading() URL : $url")

            if (!isLoadingPdfUrl && isPdfUrl(url)) {
                mWebView.stopLoading()

                val pdfUrl = PDF_VIEWER_URL + url

                Handler().postDelayed(Runnable { loadPdfUrl(pdfUrl) }, 300)

                return true
            }

            return false // Load url in the webView itself
        }

        private fun handleError(errorCode: Int, description: String, failingUrl: String) {
            Log.e(TAG, "Error : $errorCode, $description URL : $failingUrl")
        }

        private fun showProgressDialog() {
            dismissProgressDialog()
            mProgressDialog = ProgressDialog.show(mContext, "", "Loading...")
        }

        private fun dismissProgressDialog() {
            if (mProgressDialog != null && mProgressDialog!!.isShowing) {
                mProgressDialog!!.dismiss()
                mProgressDialog = null
            }
        }

        private fun isPdfUrl(url: String): Boolean {
            var url = url
            if (!TextUtils.isEmpty(url)) {
                url = url.trim { it <= ' ' }
                val lastIndex = url.lowercase(Locale.getDefault()).lastIndexOf(PDF_EXTENSION)
                if (lastIndex != -1) {
                    return url.substring(lastIndex).equals(PDF_EXTENSION, ignoreCase = true)
                }
            }
            return false
        }

        companion object {
            private const val TAG = "PdfWebViewClient"
            private const val PDF_EXTENSION = ".pdf"
            private const val PDF_VIEWER_URL = "http://docs.google.com/gview?embedded=true&url="
        }
    }

}