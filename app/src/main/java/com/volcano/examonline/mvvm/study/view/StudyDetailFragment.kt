package com.volcano.examonline.mvvm.study.view

import androidx.recyclerview.widget.LinearLayoutManager
import com.volcano.examonline.R
import com.volcano.examonline.base.BaseMvvmFragment
import com.volcano.examonline.databinding.FragmentStudyDetailBinding
import com.volcano.examonline.mvvm.study.adapter.QuestionListAdapter
import com.volcano.examonline.mvvm.study.viewmodel.StudyViewModel
import com.volcano.examonline.util.ConstantData

class StudyDetailFragment(private val subjectId : Int)
    : BaseMvvmFragment<FragmentStudyDetailBinding, StudyViewModel>(ConstantData.VIEWMODEL_EXCLUSIVE) {

    companion object {
        fun newInstance(id : Int) = StudyDetailFragment(id)
    }

    private val questionAdapter by lazy { QuestionListAdapter(activity!! , mViewModel.questionData) }

    override fun initView() {
        mBinding.srlQuestionDetail.apply{
            setColorSchemeResources(R.color.colorAccent)
            setOnRefreshListener {
                mViewModel.getQuestions(subjectId)
                mBinding.srlQuestionDetail.isRefreshing = true
            }
        }
        mBinding.rcvQuestionDetail.apply {
            layoutManager = LinearLayoutManager(activity!!)
            adapter = questionAdapter
        }
    }

    override fun initData() {
        setDataStatus(mViewModel.questions, {
            mBinding.srlQuestionDetail.isRefreshing = false
        }, {
            if(!it.isNullOrEmpty()) {
                mViewModel.questionData.clear()
                mViewModel.questionData.addAll(it)
                questionAdapter.notifyDataSetChanged()
            }
            mBinding.srlQuestionDetail.isRefreshing = false
        })
        mViewModel.getQuestions(subjectId)
    }

    override fun doRetry() {
        super.doRetry()
        mViewModel.getQuestions(subjectId)
    }

}

