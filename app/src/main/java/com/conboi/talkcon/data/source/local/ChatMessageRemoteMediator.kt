package com.conboi.talkcon.data.source.local

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.conboi.talkcon.data.dao.ChatDao
import com.conboi.talkcon.data.model.ChatMessage
import com.conboi.talkcon.data.source.local.database.AppDatabase
import com.conboi.talkcon.data.source.remote.repo.FirebaseRepository
import com.conboi.talkcon.utils.UserType
import com.google.firebase.database.ktx.getValue

@OptIn(ExperimentalPagingApi::class)
class ChatMessageRemoteMediator(
    private val firebaseRepository: FirebaseRepository,
    private val appDatabase: AppDatabase,
    private val chatDao: ChatDao,
    private val chatId: String
) : RemoteMediator<Int, ChatMessage>() {
    private val currentUserId = firebaseRepository.getCurrentUser()!!.uid

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ChatMessage>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    lastItem.messageId
                }
            }

            val response = firebaseRepository.getChatMessagesDataSnapshot(
                chatId = chatId,
                startAfter = loadKey
            )

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    chatDao.clearAll()
                }

                val data =
                    response.children.map { snapshot ->
                        snapshot.getValue<ChatMessage>()!!.apply {
                            this.viewType =
                                if (this.sendBy != currentUserId) {
                                    UserType.OTHER
                                } else {
                                    UserType.CURRENT
                                }
                        }
                    }
                chatDao.insertAll(data)
            }

            MediatorResult.Success(
                endOfPaginationReached = response.value == null
            )
        } catch (e: Exception) {
            Log.d("TAG", "ExceptionLoad: $e")
            MediatorResult.Error(e)
        }
    }
}