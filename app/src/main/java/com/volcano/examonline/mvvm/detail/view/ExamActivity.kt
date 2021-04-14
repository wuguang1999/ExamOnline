package com.volcano.examonline.mvvm.detail.view

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.observe
import com.volcano.examonline.R
import com.volcano.examonline.base.BaseMvvmActivity
import com.volcano.examonline.databinding.ActivityExamDetailBinding
import com.volcano.examonline.mvvm.detail.adapter.ExamPagerAdapter
import com.volcano.examonline.mvvm.detail.viewmodel.ExamDetailViewModel
import com.volcano.examonline.util.ConstantData
import java.text.SimpleDateFormat
import java.util.*


class ExamActivity : BaseMvvmActivity<ActivityExamDetailBinding, ExamDetailViewModel>() {

    private var currentPos = 0
    private val examAdapter: ExamPagerAdapter by lazy { ExamPagerAdapter(this, mViewModel.questions) }
    private var mode : Int? = null
    private var subject: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mode = intent.getIntExtra("mode", ConstantData.ORDERLY_MODE)
        subject = intent.getIntExtra("subject", 1)
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        /// 初始化计时器
        mBinding.examDetailTimer.apply {
            setOnChronometerTickListener {
                val time = System.currentTimeMillis() - it.base
                val date = Date(time)
                val sdf = SimpleDateFormat("HH:mm:ss", Locale.US)
                sdf.timeZone = TimeZone.getTimeZone("UTC")
                text = sdf.format(date)
            }
            base = System.currentTimeMillis()
            start()
        }
        mBinding.examDetailBackImg.setOnClickListener {
            finish()
        }
        mBinding.examDetailRecord.setOnClickListener {
        }
        mBinding.examDetailViewpager2.adapter = examAdapter
        mBinding.examDetailViewpager2.isUserInputEnabled = false
        mBinding.examDetailNextQuest.setOnClickListener {
            if(currentPos < mViewModel.questions.size - 1) {
                mViewModel.setCurrentPos(++currentPos)
            }
        }
        mBinding.examDetailLastQuest.setOnClickListener {
            if(currentPos > 0) {
                mViewModel.setCurrentPos(--currentPos)
            }
        }
    }

    override fun initData() {
        mViewModel.currentPos.observe(this){
            mBinding.examDetailLastQuest.setTextColor(if(currentPos == 0) Color.GRAY else resources.getColor(R.color.colorAccent))
            mBinding.examDetailNextQuest.setTextColor(if(currentPos == mViewModel.questions.size - 1) Color.GRAY else resources.getColor(R.color.colorAccent))
            mBinding.examDetailViewpager2.currentItem = currentPos
        }
        mViewModel.question.observe(this) {
            if(!it.isNullOrEmpty()) {
                mBinding.examDetailViewpager2.offscreenPageLimit = it.size
                mViewModel.questions.addAll(it)
                examAdapter.notifyDataSetChanged()
                examAdapter.initFragments()
                mBinding.examDetailViewpager2.currentItem = 0
                mBinding.examDetailRecord.text = "0/${mViewModel.questions.size}"
            }
        }
        when(mode) {
            ConstantData.ORDERLY_MODE -> {
                mViewModel.getQuestions(subject!!)
            }
            ConstantData.SIMULATION_MODE -> {
                mViewModel.getRandomQuestions(subject!!, 5)
            }
        }
        mViewModel.setCurrentPos(currentPos)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.examDetailTimer.stop()
    }
}