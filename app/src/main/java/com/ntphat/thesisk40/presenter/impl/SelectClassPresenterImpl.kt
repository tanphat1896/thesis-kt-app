package com.ntphat.thesisk40.presenter.impl

import android.content.Context
import com.ntphat.thesisk40.adapter.TeachingClassAdapter
import com.ntphat.thesisk40.data.response.TeachingClassResponse
import com.ntphat.thesisk40.entity.TeachingClass
import com.ntphat.thesisk40.model.impl.TeachingClassModelImpl
import com.ntphat.thesisk40.presenter.BasePresenter
import com.ntphat.thesisk40.presenter.SelectClassPresenter
import com.ntphat.thesisk40.util.JsonParser
import com.ntphat.thesisk40.util.StringParser
import com.ntphat.thesisk40.util.UiWidget

class SelectClassPresenterImpl(
        private val view: SelectClassPresenter.View
) : BasePresenter(view), SelectClassPresenter {

    private val teachingClassModel = TeachingClassModelImpl(this)
    private lateinit var adapter: TeachingClassAdapter
    private val teachingClasses: MutableList<TeachingClass> = mutableListOf()
    private val filteredClasses: MutableList<TeachingClass> = mutableListOf()

    override fun initList() {
        adapter = TeachingClassAdapter(
                view as Context,
                filteredClasses
        )
        view.setAdapter(adapter)
        teachingClassModel.fetchTeachingClass()
    }

    override fun onSearch(query: String?) {
        filteredClasses.clear()
        if (query == null || query.isEmpty()) {
            filteredClasses.addAll(teachingClasses)
            refreshList()
            return
        }
        filteredClasses.addAll(teachingClasses.filter {
            val fullInfo = "${it.subjectCode} ${it.name} ${it.group}"
            StringParser.removeVietnameseChar(
                    fullInfo.toLowerCase()
            ).contains(query.toLowerCase())
        })
        refreshList()
    }

    override fun checkClass(itemId: Int) {
        val teachingClass = filteredClasses[itemId]
        view.navigateToCheckListActivity(JsonParser.toJson(teachingClass))
    }

    override fun refreshList() {
        adapter.notifyDataSetChanged()
    }

    override fun onTeachingClassFetched(teachingClassResponse: TeachingClassResponse) {
        if (teachingClassResponse.isError()) {
            if (teachingClassResponse.isError()) {
                view.showError(teachingClassResponse.error)
                return
            }
        }

        teachingClasses.clear()
        filteredClasses.clear()
        teachingClasses.addAll(teachingClassResponse.content)
        filteredClasses.addAll(teachingClasses)

        view.refreshList()
    }
}