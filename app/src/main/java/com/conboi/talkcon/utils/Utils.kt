package com.conboi.talkcon.utils

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


val Firebase.databaseWithURL: FirebaseDatabase get() = database("https://talkcon-debug-default-rtdb.europe-west1.firebasedatabase.app")


fun NavController.popBackStackAllInstances(destination: Int, inclusive: Boolean): Boolean {
    var popped: Boolean
    while (true) {
        popped = popBackStack(destination, inclusive)
        if (!popped) {
            break
        }
    }
    return popped
}


fun concatenateTwoUserIds(currentUserId: String, otherUserId: String): String? {
    val compareIds = currentUserId.compareTo(otherUserId)
    return when {
        compareIds < 0 -> "${otherUserId}_$currentUserId"
        compareIds > 0 -> "${currentUserId}_$otherUserId"
        else -> null
    }
}

fun showErrorToast(context: Context, exception: Exception?) {
    exception?.let {
        Toast.makeText(context, exception.message.toString(), Toast.LENGTH_SHORT).show()
    }
}
