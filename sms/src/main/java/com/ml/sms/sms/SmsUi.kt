package com.ml.sms.sms

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.StringDef
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.ml.sms.Mmkv
import com.ml.sms.activity.MainActivity
import com.ml.sms.activity.createButton
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog
import com.marosseleng.compose.material3.datetimepickers.time.ui.dialog.TimePickerDialog
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@StringDef(MsgType.SENT, MsgType.INBOX)
annotation class MsgType {
    companion object {
        const val SENT = "sent"
        const val INBOX = "inbox"
    }
}

data class MsgItem constructor(
    var body: String = "测试短信哦",
    var isRead: Boolean = false,
    var address: String = "10086",
    @MsgType
    var type: String = MsgType.SENT,
    var isEditDate: Boolean = false,
    var localDate: LocalDate = LocalDate.now(),
    var isEditTime: Boolean = false,
    var localTime: LocalTime = LocalTime.now(),
) {
    fun insertTime(): Long {
        val part = "yyyy-MM-dd HH:mm:ss.SSS"
        val localDateTime = LocalDateTime.of(localDate, localTime)
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun isSend() = type == MsgType.SENT

    fun check(): Boolean {
        return body.isNotEmpty() && address.isNotEmpty()
    }
}

@Composable
fun SmsUi(context: MainActivity) {
    var showSmsForwardDialog by remember {
        mutableStateOf(false)
    }
    Column(
        modifier =
        Modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    Mmkv.dd_tag = "136手机"
                    Mmkv.dd_token =
                        "ded554505a75987690f603906ab81f7a8a48297e87234a684e17ff7dc9fe1ca1"
                    Mmkv.dd_secret =
                        "SEC79f1e141503ebfbbba135062edbd05c142b99d0fc9c8af7366753e64eed9bc67"
                },
            text = "信息模拟器",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(10.dp))
        msgInputContentArea()
        createButton(text = "设置默认短信应用") {
            SmsUtil.setDefaultSms(context, context.smsResultLauncher)
        }
        createButton(text = "删除所有短信") {
            SmsUtil.deleteAll(context)
        }
        createButton(text = "查询能接受短信的应用") {
            SmsUtil.openSMSappChooser(context)
        }
        createButton(text = "默认应用设置") {
            val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, "default");
            context.startActivity(intent)
        }
        createButton(text = "卸载") {
            val uri = Uri.fromParts("package", context.packageName, null)
            val intent = Intent(Intent.ACTION_DELETE, uri)
            context.startActivity(intent)
        }
        createButton(text = "短信转发设置") {
            showSmsForwardDialog = true
        }
    }
    if (showSmsForwardDialog) {
        SmsForwardDialog() {
            showSmsForwardDialog = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Preview
@Composable
fun msgInputContentArea() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        var msgItem by remember {
            mutableStateOf(MsgItem())
        }
        var context = LocalContext.current
        Column(modifier = Modifier) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = msgItem.address,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                label = {
                    Text(text = "收件人")
                },
                onValueChange = {
                    msgItem = msgItem.copy(address = it)
                })
            OutlinedTextField(
                value = msgItem.body,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 60.dp)
                    .heightIn(60.dp, 100.dp),
                maxLines = 4,
                label = {
                    Text(text = "短信内容")
                },
                onValueChange = {
                    msgItem = msgItem.copy(body = it)
                })
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "发", modifier = Modifier)
                Checkbox(checked = msgItem.isSend(), onCheckedChange = {
                    msgItem = msgItem.copy(type = if (it) MsgType.SENT else MsgType.INBOX)
                })
                Spacer(modifier = Modifier.size(10.dp))
                Text(text = "收")
                Checkbox(checked = !msgItem.isSend(), onCheckedChange = {
                    msgItem = msgItem.copy(type = if (!it) MsgType.SENT else MsgType.INBOX)
                })
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "日期")
                TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        msgItem = msgItem.copy(isEditDate = true)
                    },
                ) {
                    Text(text = msgItem.localDate.toString())
                }
                Spacer(modifier = Modifier.size(10.dp))
                Text(text = "时间")
                TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        msgItem = msgItem.copy(isEditTime = true)
                    }
                ) {
                    Text(text = msgItem.localTime.toString())
                }
            }

            if (msgItem.isEditDate) {
                DatePickerDialog(
                    onDismissRequest = {
                        msgItem = msgItem.copy(isEditDate = false)
                    },
                    onDateChange = {
                        msgItem = msgItem.copy(isEditDate = false, localDate = it)
                    },
                    initialDate = msgItem.localDate
                )
            }
            if (msgItem.isEditTime) {
                TimePickerDialog(
                    onDismissRequest = {
                        msgItem = msgItem.copy(isEditTime = false)
                    },
                    onTimeChange = {
                        msgItem = msgItem.copy(isEditTime = false, localTime = it.withSecond(11))
                    },
                    initialTime = msgItem.localTime
                )
            }

            Button(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    if (msgItem.check()) {
                        if (SmsUtil.isDefault(context)) {
                            SmsUtil.mockSms(context, msgItem).also {
                                Toast.makeText(
                                    context,
                                    if (it) "插入成功" else "插入失败",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            SmsUtil.setDefaultSms(
                                context,
                                (context as MainActivity).smsResultLauncher
                            )
                        }
                    } else {
                        Toast.makeText(context, "数据不完善", Toast.LENGTH_SHORT).show()
                    }
                }) {
                Text(text = "插入")
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SmsForwardDialog(block: () -> Unit = {}) {
    var token by remember {
        mutableStateOf(Mmkv.dd_token)
    }
    var dd_tag by remember {
        mutableStateOf(Mmkv.dd_tag)
    }
    var secret by remember {
        mutableStateOf(Mmkv.dd_secret)
    }
    AlertDialog(
        onDismissRequest = {

        },
        confirmButton = {
            createButton(text = "确定") {
                block.invoke()
            }
        },
        dismissButton = {
            createButton(text = "取消") {
                block.invoke()
            }
        }, title = {
            Text(text = "设置钉钉机器人参数")
        }, text = {
            Column {
                OutlinedTextField(value = dd_tag, onValueChange = {
                    dd_tag = it
                    Mmkv.dd_tag = it
                }, label = {
                    Text(text = "钉钉机器人Tag.")
                },
                )
                OutlinedTextField(value = token, onValueChange = {
                    token = it
                    Mmkv.dd_token = it
                }, label = {
                    Text(text = "钉钉机器人token.")
                },
                )
                OutlinedTextField(value = secret, onValueChange = {
                    secret = it
                    Mmkv.dd_secret = it
                }, label = {
                    Text(text = "钉钉机器人签名.")
                })
            }

        },
        properties = DialogProperties(dismissOnClickOutside = true)
    )
}
