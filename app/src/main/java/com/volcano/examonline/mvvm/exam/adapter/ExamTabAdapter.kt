package com.volcano.examonline.mvvm.exam.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.volcano.examonline.mvvm.exam.view.ExamListFragment
import com.volcano.examonline.mvvm.exam.model.ExamTab

class ExamTabAdapter(val mContext: Fragment, val tabs : ArrayList<ExamTab>) : FragmentStateAdapter(mContext) {
    private val fragments = mutableListOf<Fragment>()

    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun initFragments() {
        fragments.clear()
        tabs.forEach{
            fragments.add(ExamListFragment.newInstance(it.id!!))
        }

    }
}