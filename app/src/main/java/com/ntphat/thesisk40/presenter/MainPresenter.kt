package com.ntphat.thesisk40.presenter

import com.ntphat.thesisk40.adapter.ScheduleAdapter
import com.ntphat.thesisk40.data.response.ScheduleResponse

interface MainPresenter : Presenter {

    fun initList()

    fun onScheduleFetched(scheduleResponse: ScheduleResponse)

    fun refreshList()

    interface View : Presenter.View {
        fun setAdapter(adapter: ScheduleAdapter)

        fun refreshList()

        fun toggleProgress(visible: Boolean)

        fun toggleMainContent(visible: Boolean)
    }
}