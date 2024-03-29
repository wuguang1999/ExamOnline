package com.volcano.examonline

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.volcano.examonline.base.BaseMvvmActivity
import com.volcano.examonline.databinding.ActivityMainBinding
import com.volcano.examonline.mvvm.forum.view.ForumFragment
import com.volcano.examonline.mvvm.study.view.StudyFragment
import com.volcano.examonline.mvvm.mine.view.MineFragment
import com.volcano.examonline.util.ToastUtils

class MainActivity : BaseMvvmActivity<ActivityMainBinding, MainViewModel>() {

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
    private var exitTime: Long = 0
    private val fragments = arrayOf(
        StudyFragment.newInstance(),
        ForumFragment.newInstance(),
        MineFragment.newInstance()
    )
    private val itemIds = arrayOf(
        R.id.navigation_first,
        R.id.navigation_second,
        R.id.navigation_third
    )


    override fun initView() {
        mBinding.mainViewPager.apply {
            adapter = object : FragmentStateAdapter(supportFragmentManager,lifecycle) {
                override fun getItemCount(): Int = fragments.size

                override fun createFragment(position: Int): Fragment = fragments[position]
            }
            offscreenPageLimit = fragments.size
            isUserInputEnabled = false
        }
        mBinding.navView.setOnNavigationItemSelectedListener {
            mBinding.mainViewPager.setCurrentItem(itemIds.indexOf(it.itemId),false)
            true
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exit() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            ToastUtils.show(this, "再按一次退出程序")
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }

    override fun initData() {

    }

}