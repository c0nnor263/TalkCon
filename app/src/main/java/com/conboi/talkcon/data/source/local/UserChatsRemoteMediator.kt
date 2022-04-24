package com.conboi.talkcon.data.source.local

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.conboi.talkcon.data.model.UserChat
import com.conboi.talkcon.data.source.local.database.AppDatabase
import com.conboi.talkcon.data.source.remote.repo.FirebaseRepository
import com.google.firebase.database.ktx.getValue

@OptIn(ExperimentalPagingApi::class)
class UserChatsRemoteMediator(
    val firebaseRepository: FirebaseRepository,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, UserChat>() {
    private val userChatDao = appDatabase.getUserChatsDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserChat>
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
                    lastItem.id
                }
            }

            val response = firebaseRepository.getUserChatsDataSnapshot(startAfter = loadKey)

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    userChatDao.clearAll()
                }

                val data = response.children.map { it.getValue<UserChat>()!! }
                userChatDao.insertAll(data)
            }

            MediatorResult.Success(
                endOfPaginationReached = response.value == null
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}