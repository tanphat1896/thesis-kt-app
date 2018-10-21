package com.ntphat.thesisk40.presenter

import com.ntphat.thesisk40.adapter.TeachingClassAdapter
import com.ntphat.thesisk40.data.response.TeachingClassResponse

interface SelectClassPresenter : Presenter {

    fun initList()

    fun onSearch(query: String?)

    fun checkClass(itemId: Int)

    fun onTeachingClassFetched(teachingClassResponse: TeachingClassResponse)

    fun refreshList()

    interface View : Presenter.View {
        fun setAdapter(adapter: TeachingClassAdapter)

        fun refreshList()

        fun toggleProgress(visible: Boolean)

        fun toggleMainContent(visible: Boolean)

        fun navigateToCheckListActivity(teachingClassJson: String)
    }
}