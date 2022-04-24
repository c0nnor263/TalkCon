package com.conboi.talkcon.utils.shared

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class NoPreAnLinearLayoutManager(context: Context) : LinearLayoutManager(context) {
    override fun supportsPredictiveItemAnimations(): Boolean = false
}