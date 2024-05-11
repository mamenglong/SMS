package com.example.http

import android.util.Log
import androidx.compose.ui.graphics.GraphicsLayerScope
import com.dingtalk.api.DefaultDingTalkClient
import com.dingtalk.api.DingTalkClient
import com.dingtalk.api.request.OapiRobotSendRequest
import com.dingtalk.api.response.OapiRobotSendResponse
import com.example.Mmkv
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.json.defaultSerializer
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.commons.codec.binary.Base64
import java.net.URLEncoder
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


object HttpManager : UploadData {
    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json()
            defaultSerializer()
        }
        engine {
            connectTimeout = 100_000
            socketTimeout = 100_000

            /**
             * Proxy address to use.
             */

            /**
             * Proxy address to use.
             */
            // proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("localhost", serverPort))
        }
    }

    override fun upload(msg: String) {
        GlobalScope.launch(Dispatchers.IO) {
            DingTalkUpload.upload(msg)
        }
    }

}

object DingTalkUpload : UploadData {
    override fun upload(msg: String) {
        val sign = getSecret(Mmkv.dd_secret)
        //sign字段和timestamp字段必须拼接到请求URL上，否则会出现 310000 的错误信息
        val client: DingTalkClient =
            DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?sign=${sign.second}&timestamp=${sign.first}")
        val req = OapiRobotSendRequest()

        /**
         * 发送文本消息
         */
        //定义文本内容
        val text = OapiRobotSendRequest.Text()
        text.content = msg
        //定义 @ 对象
        val at = OapiRobotSendRequest.At()
        at.atMobiles = listOf("18438603985")

        //设置消息类型
        req.msgtype = "text"
        req.setText(text)
        req.setAt(at)
        val rsp = client.execute<OapiRobotSendResponse>(req, Mmkv.dd_token)
        Log.d("DingTalkUpload",rsp.body)
    }

    private fun getSecret(secret: String): Pair<Long, String> {
        val timestamp = System.currentTimeMillis()
        val stringToSign = """
            $timestamp
            $secret
            """.trimIndent()
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(secret.toByteArray(charset("UTF-8")), "HmacSHA256"))
        val signData: ByteArray = mac.doFinal(stringToSign.toByteArray(charset("UTF-8")))
        val sign = URLEncoder.encode(String(Base64.encodeBase64(signData)), "UTF-8")
        return timestamp to sign
    }
}

interface UploadData {
    fun upload(msg: String)
}