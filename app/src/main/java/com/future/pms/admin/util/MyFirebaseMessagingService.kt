package com.future.pms.admin.util

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.future.pms.admin.util.Constants.Companion.FCM_CUSTOMER_NAME
import com.future.pms.admin.util.Constants.Companion.FCM_LEVEL_NAME
import com.future.pms.admin.util.Constants.Companion.FCM_PARKING_ZONE
import com.future.pms.admin.util.Constants.Companion.MY_FIREBASE_MESSAGING
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
  override fun onMessageReceived(remoteMessage: RemoteMessage?) {
    if (remoteMessage?.data != null) {
      val intent = Intent(MY_FIREBASE_MESSAGING)
      intent.putExtra(FCM_CUSTOMER_NAME, remoteMessage.data["customerName"])
      intent.putExtra(FCM_PARKING_ZONE, remoteMessage.data["parkingZoneName"])
      intent.putExtra(FCM_LEVEL_NAME, remoteMessage.data["levelName"])

      LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
  }
}
