package ua.shishkoam.appcoockingbook.utils.interactors

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import ua.shishkoam.appcoockingbook.oldutils.ui.OpenFileDialog
import ua.shishkoam.appcoockingbook.R
import java.io.File
import java.io.FileWriter
import java.io.IOException

class PrintHtmlManager(activity: Activity, html: String, title: String) {
    private var mWebView: WebView? = null
    private fun openSaveForJellyBeanAndLowerVersion(
        data: ByteArray,
        extension: String,
        context: Context,
        title: String
    ) {
        val ofd = OpenFileDialog(context)
        ofd.setFilterOnlyDirectory()
        ofd.setAccessDeniedMessage(context.getString(R.string.openFileDialogAccessDenied))
        ofd.setOpenDialogListener(object : OpenFileDialog.OpenDialogListener {
            override fun onSelectedFile(fileName: String?) {
                val file = File(fileName, title + extension)
                try {
                    val fileWriter = FileWriter(file)
                    fileWriter.write(String(data))
                    fileWriter.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
        ofd.show()
    }

    private fun doWebViewPrint(activity: Activity, html: String, title: String) {
        val webView = WebView(activity)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                createWebPrintJob(activity, view, title)
                mWebView = null
            }
        }
        webView.loadDataWithBaseURL(null, html, "text/HTML", "UTF-8", null)
        mWebView = webView
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createWebPrintJob(activity: Activity, webView: WebView, title: String) {
        val printManager = activity
            .getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = webView.createPrintDocumentAdapter(title)
        val printJob = printManager.print(
            title, printAdapter,
            PrintAttributes.Builder().build()
        )
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            doWebViewPrint(activity, html, title)
        } else {
            openSaveForJellyBeanAndLowerVersion(html.toByteArray(), ".html", activity, title)
        }
    }
}