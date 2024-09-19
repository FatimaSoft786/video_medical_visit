package com.example.videomedicalvisit.utils

import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import java.io.*
import kotlin.collections.HashMap

open class VolleyMultipartRequest(
    method: Int,
    url: String,
    private val listener: Response.Listener<NetworkResponse>,
    errorListener: Response.ErrorListener
) : Request<NetworkResponse>(method, url, errorListener) {

    private val headers: MutableMap<String, String> = HashMap()

    override fun getHeaders(): MutableMap<String, String> {
        return headers
    }

    fun setHeaders(headers: MutableMap<String, String>) {
        this.headers.putAll(headers)
    }

    override fun getBodyContentType(): String {
        return "multipart/form-data;boundary=$boundary"
    }

    override fun getBody(): ByteArray {
        val bos = ByteArrayOutputStream()
        val dataOutputStream = DataOutputStream(bos)

        try {
            // Write parameters (form data)
            writeParams(dataOutputStream)

            // Write file data (multipart data)
            writeFileData(dataOutputStream)

            dataOutputStream.writeBytes("--$boundary--\r\n")
            dataOutputStream.flush()
            dataOutputStream.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bos.toByteArray()
    }

    private fun writeParams(dataOutputStream: DataOutputStream) {
        try {
            for ((key, value) in formParams) {
                dataOutputStream.writeBytes("--$boundary\r\n")
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"$key\"\r\n\r\n")
                dataOutputStream.writeBytes("$value\r\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun writeFileData(dataOutputStream: DataOutputStream) {
        try {
            for ((inputName, dataPart) in fileDataParams) {
                dataOutputStream.writeBytes("--$boundary\r\n")
                dataOutputStream.writeBytes(
                    "Content-Disposition: form-data; name=\"$inputName\"; filename=\"" + dataPart.fileName + "\"\r\n"
                )
                dataOutputStream.writeBytes("Content-Type: ${dataPart.type}\r\n\r\n")
                dataOutputStream.write(dataPart.content)
                dataOutputStream.writeBytes("\r\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    var formParams: MutableMap<String, String> = HashMap()
    var fileDataParams: Map<String, DataPart> = HashMap()

    data class DataPart(val fileName: String, val content: ByteArray, val type: String)

    companion object {
        private  val boundary = "apiclient-" + System.currentTimeMillis()
    }

    override fun parseNetworkResponse(response: NetworkResponse?): Response<NetworkResponse> {
        return try {
            Response.success(response, HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: Exception) {
            Response.error(ParseError(e))
        }
    }

    override fun deliverResponse(response: NetworkResponse?) {

    }
}
