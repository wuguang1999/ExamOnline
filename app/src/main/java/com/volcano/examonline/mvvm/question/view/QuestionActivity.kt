package com.volcano.examonline.mvvm.question.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import com.volcano.examonline.R
import com.volcano.examonline.base.BaseMvvmActivity
import com.volcano.examonline.databinding.ActivityQuestionBinding
import com.volcano.examonline.mvvm.question.adapter.QuestionAdapter
import com.volcano.examonline.mvvm.question.viewmodel.QuestionViewModel
import com.volcano.examonline.mvvm.study.model.Question
import com.volcano.examonline.util.ConstantData

class QuestionActivity : BaseMvvmActivity<ActivityQuestionBinding, QuestionViewModel>() {

    companion object {
        fun actionStart(context: Context, type: Int, question: ArrayList<Question>, myAnswers: ArrayList<String>) {
            val intent = Intent(context, QuestionActivity::class.java).apply {
                putExtra("type", type)
                putExtra("question", question)
                putExtra("myAnswers", myAnswers)
            }
            context.startActivity(intent)
        }
    }

    private var type:Int? = null
    private val questionAdapter by lazy { QuestionAdapter(this, mViewModel.questions)}

    override fun initView() {
        type = intent.getIntExtra("type", ConstantData.SINGLE_QUESTION)
        mViewModel.myAnswers = intent.getSerializableExtra("myAnswers") as ArrayList<String>
        mViewModel.questions = intent.getSerializableExtra("question") as ArrayList<Question>
        initToolbar()
        mBinding.fabEditComment.setOnClickListener{
            //发表评论
        }
        mBinding.vpDetail.apply {
            adapter = questionAdapter
            isUserInputEnabled = true
            registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mBinding.toolbarDetail.toolbarRightTv.text = "${position+1}/${mViewModel.questions.size}"
                }
            })
        }
    }

    private fun initToolbar() {
        mBinding.toolbarDetail.toolbarLeftImageBack.apply {
            setImageResource(R.drawable.ic_black_back)
            setOnClickListener { finish() }
        }
        mBinding.toolbarDetail.toolbarTitle.text = if(type == ConstantData.SINGLE_QUESTION) "试题详情" else "试题解析"
        if(type == ConstantData.MULTI_QUESTION) {
            mBinding.toolbarDetail.toolbarRightImage.visibility = View.GONE
            mBinding.toolbarDetail.toolbarRightTv.visibility = View.VISIBLE
        }
    }

    override fun initData() {
        questionAdapter.initFragments()
        mBinding.vpDetail.currentItem = 0
        mBinding.vpDetail.offscreenPageLimit = mViewModel.questions.size
        mBinding.toolbarDetail.toolbarRightTv.text = "1/${mViewModel.questions.size}"
    }
}