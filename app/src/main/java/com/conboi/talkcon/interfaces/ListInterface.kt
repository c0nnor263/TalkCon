package com.conboi.talkcon.interfaces

import com.conboi.talkcon.data.model.UserChat

interface ListInterface {
    fun onClickItem(userChat: UserChat)
}