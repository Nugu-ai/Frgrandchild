package com.example.test

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.test.fragment.*


class MainFragmentStatePagerAdapter(fm : FragmentManager, val fragmentCount : Int) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> HomeFragment()
            1 -> GuideFragment()
            2 -> HelperFragment()
            3 -> VideoFragment()
            4 -> UserFragment()
            else -> HomeFragment()
        }
    }

    override fun getCount(): Int = fragmentCount // 자바에서는 { return fragmentCount }

}
