package com.volcano.examonline.mvvm.detail.view

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.observe
import com.volcano.examonline.R
import com.volcano.examonline.base.BaseMvvmActivity
import com.volcano.examonline.databinding.ActivityExamDetailBinding
import com.volcano.examonline.mvvm.detail.adapter.ExamPagerAdapter
import com.volcano.examonline.mvvm.detail.viewmodel.ExamViewModel
import com.volcano.examonline.util.ConstantData
import java.text.SimpleDateFormat
import java.util.*


class ExamActivity : BaseMvvmActivity<ActivityExamDetailBinding, ExamViewModel>() {

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
            showExitDialog()
        }
        mBinding.examDetailRecord.setOnClickListener {
        }
        mBinding.examDetailViewpager2.adapter = examAdapter
        mBinding.examDetailViewpager2.isUserInputEnabled = false

        mBinding.examDetailNextQuest.setOnClickListener {
            if(currentPos < mViewModel.questions.size - 1) {
                mViewModel.setCurrentPos(++currentPos)
            }else {
                //提交
                showCommitDialog()
            }
        }
        mBinding.examDetailLastQuest.setOnClickListener {
            if(currentPos > 0) {
                mViewModel.setCurrentPos(--currentPos)
            }
        }
    }

    private fun showCommitDialog() {
        mBinding.examDetailTimer.stop()
        val builder = Dialog(this, R.style.dialog)
        builder.setContentView(R.layout.dialog_common)
        val content = builder.findViewById<TextView>(R.id.dialog_content)
        content.text = "您确定要提交并查看结果吗？"
        builder.findViewById<Button>(R.id.dialog_sure).apply {
            text = "确定提交"
            setOnClickListener {
                builder.dismiss()
                val intent = Intent(context, CommitResultActivity::class.java).apply {
                    putExtra("questions", mViewModel.questions)
                    putExtra("answers", mViewModel.myAnswers.value)
                    putExtra("time", mBinding.examDetailTimer.text.toString())
                }
                startActivity(intent)
                finish()
            }
        }
        builder.findViewById<Button>(R.id.dialog_cancel).apply{
            text = "我再想想"
            setOnClickListener {
                mBinding.examDetailTimer.start()
                builder.dismiss()
            }
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    private fun showExitDialog() {
        mBinding.examDetailTimer.stop()
        val builder = Dialog(this, R.style.dialog)
        builder.setContentView(R.layout.dialog_common)
        val content = builder.findViewById<TextView>(R.id.dialog_content)
        content.text = "您要结束本次模拟考试吗？"
        builder.findViewById<Button>(R.id.dialog_sure).setOnClickListener {
            builder.dismiss()
            finish()
        }
        builder.findViewById<Button>(R.id.dialog_cancel).setOnClickListener {
            mBinding.examDetailTimer.start()
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    override fun initData() {
        mViewModel.currentPos.observe(this){
            mBinding.examDetailLastQuest.setTextColor(if(currentPos == 0) Color.GRAY else resources.getColor(R.color.colorAccent))
            if(currentPos == mViewModel.questions.size - 1) {
                mBinding.examDetailNextQuest.text = "提交"
            }else {
                mBinding.examDetailNextQuest.text = "下一题"
            }
            mBinding.examDetailViewpager2.currentItem = currentPos
        }
        mViewModel.question.observe(this) {
            if(!it.isNullOrEmpty()) {
                mBinding.examDetailViewpager2.offscreenPageLimit = it.size
                mViewModel.questions.addAll(it)
                examAdapter.notifyDataSetChanged()
                examAdapter.initFragments()
                mBinding.examDetailViewpager2.currentItem = 0
                mBinding.examDetailRecordTotal.text = "/${mViewModel.questions.size}"
            }
        }
        mViewModel.myAnswers.observe(this) {
            mBinding.examDetailRecord.text = "${it.size}"

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

    override fun onBackPressed() {
        showExitDialog()
    }
}