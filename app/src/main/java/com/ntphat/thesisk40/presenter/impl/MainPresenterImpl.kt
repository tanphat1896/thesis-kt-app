package com.ntphat.thesisk40.presenter.impl

import android.content.Context
import com.ntphat.thesisk40.adapter.ScheduleAdapter
import com.ntphat.thesisk40.adapter.TeachingClassAdapter
import com.ntphat.thesisk40.data.response.ScheduleResponse
import com.ntphat.thesisk40.data.response.TeachingClassResponse
import com.ntphat.thesisk40.entity.Schedule
import com.ntphat.thesisk40.entity.TeachingClass
import com.ntphat.thesisk40.model.impl.ScheduleModelImpl
import com.ntphat.thesisk40.model.impl.TeachingClassModelImpl
import com.ntphat.thesisk40.presenter.BasePresenter
import com.ntphat.thesisk40.presenter.MainPresenter
import com.ntphat.thesisk40.presenter.SelectClassPresenter
import com.ntphat.thesisk40.util.JsonParser
import com.ntphat.thesisk40.util.StringParser
import com.ntphat.thesisk40.util.UiWidget

class MainPresenterImpl(
        private val view: MainPresenter.View
) : BasePresenter(view), MainPresenter {

    private val scheduleModel = ScheduleModelImpl(this)
    private lateinit var adapter: ScheduleAdapter
    private val schedules: MutableList<Schedule> = mutableListOf()

    override fun initList() {
        adapter = ScheduleAdapter(
                view as Context,
                schedules
        )
        view.setAdapter(adapter)
        scheduleModel.fetchSchedule()
    }

    override fun refreshList() {
        adapter.notifyDataSetChanged()
    }

    override fun onScheduleFetched(scheduleResponse: ScheduleResponse) {
        if (scheduleResponse.isError()) {
            view.showError(scheduleResponse.error)
            return
        }

        schedules.clear()
        schedules.addAll(scheduleResponse.content)

        view.refreshList()
    }
}