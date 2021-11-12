package com.grand.Mygrandchildren

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.grand.Mygrandchildren.fragment.GuideFragment
import com.grand.Mygrandchildren.fragment.HomeFragment
import com.grand.Mygrandchildren.fragment.UserFragment
import com.grand.Mygrandchildren.fragment.VideoFragment


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
