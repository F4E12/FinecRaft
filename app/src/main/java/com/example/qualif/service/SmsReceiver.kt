package com.example.qualif.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.widget.Toast

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val bundle: Bundle? = intent.extras

        if (bundle != null) {
            val pdus = bundle["pdus"] as Array<*>?
            pdus?.forEach {
                val sms = SmsMessage.createFromPdu(it as ByteArray)
                val sender = sms.displayOriginatingAddress
                val message = sms.displayMessageBody
                Toast.makeText(context, "SMS from $sender: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}