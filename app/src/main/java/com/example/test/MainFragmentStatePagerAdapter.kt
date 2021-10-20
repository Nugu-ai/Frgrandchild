package com.example.test

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.test.fragment.GuideFragment
import com.example.test.fragment.HomeFragment
import com.example.test.fragment.UserFragment
import com.example.test.fragment.VideoFragment


class MainFragmentStatePagerAdapter(fm : FragmentManager, val fragmentCount : Int) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> HomeFragment()
            1 -> GuideFragment()
            2 -> VideoFragment()
            3 -> UserFragment()
            else -> HomeFragment()
        }
    }

    override fun getCount(): Int = fragmentCount // 자바에서는 { return fragmentCount }

}
