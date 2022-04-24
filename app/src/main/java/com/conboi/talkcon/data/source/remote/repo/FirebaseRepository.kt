package com.conboi.talkcon.data.source.remote.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.conboi.talkcon.data.dao.ChatDao
import com.conboi.talkcon.data.dao.UserChatDao
import com.conboi.talkcon.data.model.ChatMessage
import com.conboi.talkcon.data.model.UserChat
import com.conboi.talkcon.data.source.local.ChatMessageRemoteMediator
import com.conboi.talkcon.data.source.local.UserChatsRemoteMediator
import com.conboi.talkcon.data.source.local.database.AppDatabase
import com.conboi.talkcon.utils.CHAT_MESSAGE_PAGER_SIZE
import com.conboi.talkcon.utils.USER_CHAT_PAGER_SIZE
import com.conboi.talkcon.utils.concatenateTwoUserIds
import com.conboi.talkcon.utils.databaseWithURL
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.random.Random

@Module
@InstallIn(ActivityRetainedComponent::class)
class FirebaseRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val userChatDao: UserChatDao,
    private val chatDao: ChatDao
) {
    private val auth = Firebase.auth
    private val database = Firebase.databaseWithURL

    private val user = auth.currentUser

    fun getCurrentUser() = auth.currentUser

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, callback: () -> Unit) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback()
                }
            }
    }


    fun sendMessageToUser(otherUserId: String, message: ChatMessage) {
        val currentUserId = user?.uid!!
        val chatId = concatenateTwoUserIds(currentUserId, otherUserId) ?: return

        val mapMessage = HashMap<String, Any>()
        mapMessage[message.messageId] = message

        database.reference.child("Chats/$chatId")
            .updateChildren(mapMessage)
    }

    suspend fun getUserChatsDataSnapshot(startAfter: String? = null): DataSnapshot {
        val query =
            database.reference.child("UserChats/${user?.uid!!}").orderByChild("lastSendTimeMessage")
                .limitToFirst(USER_CHAT_PAGER_SIZE)
        return if (startAfter != null) {
            query.startAfter(startAfter)
                .get().await()

        } else {
            query.get().await()
        }
    }

    suspend fun getChatMessagesDataSnapshot(
        chatId: String,
        startAfter: String? = null
    ): DataSnapshot {
        val query = database.reference.child("Chats/$chatId").limitToFirst(CHAT_MESSAGE_PAGER_SIZE)
        return if (startAfter != null) {
            query.startAfter(startAfter)
                .get()
                .await()
        } else {
            query.get().await()
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getUserChatsPager(): Pager<Int, UserChat> {
        val pagerConfig = PagingConfig(pageSize = USER_CHAT_PAGER_SIZE, enablePlaceholders = true)
        val pager = Pager(
            config = pagerConfig,
            remoteMediator = UserChatsRemoteMediator(this, appDatabase)
        ) {
            userChatDao.pagingSource()
        }
        return pager
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getChatMessagePager(otherUserId: String): Pager<Int, ChatMessage> {
        val chatId = concatenateTwoUserIds(user?.uid!!, otherUserId)!!

        val pagerConfig =
            PagingConfig(pageSize = CHAT_MESSAGE_PAGER_SIZE)
        val pager = Pager(
            config = pagerConfig,
            remoteMediator = ChatMessageRemoteMediator(this, appDatabase, chatDao,chatId)
        ) {
            chatDao.pagingSource(chatId)
        }
        return pager
    }

    fun getQueryUserChats() = database.reference.child("UserChats/${user?.uid!!}")

    fun getQueryChat(otherUserId: String): DatabaseReference {
        val chatId = concatenateTwoUserIds(user?.uid!!, otherUserId)
        return database.reference.child("Chats/$chatId")
    }

    fun testAddNewUserChat() {
        val randomId = Random.nextInt(1, 100).toString()
        database.reference.child("UserChats/${user?.uid!!}/${randomId}")
            .setValue(UserChat(id = randomId))
    }
}