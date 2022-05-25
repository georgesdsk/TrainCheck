package com.finde.android.traincheck.Fragments.main_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.finde.android.traincheck.R

class HeaderCollectionFragment : Fragment(){

        // When requested, this adapter returns a DemoObjectFragment,
        // representing an object in the collection.
        private lateinit var demoCollectionPagerAdapter: DemoCollectionPagerAdapter
        private lateinit var viewPager: ViewPager


        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            demoCollectionPagerAdapter = DemoCollectionPagerAdapter(childFragmentManager)
            viewPager = view.findViewById(R.id.pager)
            viewPager.adapter = demoCollectionPagerAdapter
        }
    }

    // Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
    class DemoCollectionPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getCount(): Int  = 100

        override fun getItem(i: Int): Fragment {
            val fragment = DemoObjectFragment()
            fragment.arguments = Bundle().apply {
                // Our object is just an integer :-P
                putInt(ARG_OBJECT, i + 1)
            }
            return fragment
        }

        override fun getPageTitle(position: Int): CharSequence {
            return "OBJECT ${(position + 1)}"
        }
    }

    private const val ARG_OBJECT = "object"

    // Instances of this class are fragments representing a single
// object in our collection.
    class DemoObjectFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater,
                                  container: ViewGroup?,
                                  savedInstanceState: Bundle?): View {
            return inflater.inflate(R.layout.fragment_collection_object, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
                val textView: TextView = view.findViewById(android.R.id.text1)
                textView.text = getInt(ARG_OBJECT).toString()
            }
        }
    }


}