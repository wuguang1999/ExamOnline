package com.volcano.examonline.mvvm.study.view

import android.app.AlertDialog
import android.content.Intent
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.volcano.examonline.R
import com.volcano.examonline.base.BaseMvvmFragment
import com.volcano.examonline.databinding.FragmentStudyBinding
import com.volcano.examonline.mvvm.detail.view.ExamActivity
import com.volcano.examonline.mvvm.search.view.SearchActivity
import com.volcano.examonline.mvvm.study.adapter.SubjectAdapter
import com.volcano.examonline.mvvm.study.viewmodel.StudyViewModel
import com.volcano.examonline.util.ConstantData

class StudyFragment : BaseMvvmFragment<FragmentStudyBinding, StudyViewModel>() {

    companion object {
        fun newInstance() = StudyFragment()
    }

    private val subjectAdapter : SubjectAdapter by lazy { SubjectAdapter(this , mViewModel.subjectData) }
    private var tabLayoutMediator: TabLayoutMediator? = null
    private var subjectNames = arrayListOf<String>()

    override fun initView() {
        initToolbar()
        initListener()
        mBinding.tlSubjects.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                mBinding.vpQuestions.currentItem = tab!!.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        mBinding.vpQuestions.apply {
            adapter = subjectAdapter
        }
    }


    private fun initToolbar() {
        mBinding.toolbarHomepage.apply {
            toolbarTitle.text = "学习"
            toolbarRightImage.setImageResource(R.drawable.ic_grey_search)
            toolbarRightImage.setOnClickListener {
                val intent = Intent(activity, SearchActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun initListener() {
        mBinding.llOrderlyPractice.setOnClickListener {
            goToExamActivity(ConstantData.ORDERLY_MODE)
        }
        mBinding.llSimulationExam.setOnClickListener {
            goToExamActivity(ConstantData.SIMULATION_MODE)
        }
        mBinding.llRanking.setOnClickListener {
            val intent = Intent(activity, RankingActivity::class.java)
            startActivity(intent)
        }
        mBinding.llUploadQuestion.setOnClickListener {
            val intent = Intent(activity, QuestionUploadActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToExamActivity(simulationMode: Int) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("请选择学科~")
        builder.setItems(subjectNames.toTypedArray<CharSequence>()) { _, which ->
            val intent = Intent(activity, ExamActivity::class.java)
            intent.putExtra("mode", simulationMode)
            intent.putExtra("subject", mViewModel.subjectData[which].id)
            startActivity(intent)
        }
        builder.show()
    }

    override fun initData() {
        mViewModel.subjects.observe(activity!!) {
            if(!it.isNullOrEmpty()) {
                mBinding.vpQuestions.offscreenPageLimit = it.size
                tabBind(mBinding.vpQuestions)
                mViewModel.subjectData.addAll(it)
                subjectAdapter.notifyDataSetChanged()
                subjectAdapter.initFragments()
                it.forEach{
                    subjectNames.add(it.subjectname!!)
                }
            }
        }
        mViewModel.getSubjects()
    }

    private fun tabBind(pager: ViewPager2){
        tabLayoutMediator?.detach()
        tabLayoutMediator = TabLayoutMediator(mBinding.tlSubjects,pager){ tab, position ->
            tab.text = mViewModel.subjects.value!![position].subjectname
        }
        tabLayoutMediator?.attach()
    }

}

