package pl.nw.zadanie_06.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import pl.nw.zadanie_06.fragments.*

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return ProductListFragment()
            1 -> return CartFragment()
            2 -> return CategoryListFragment()
            3 -> return AddProductFragment()
            else -> return ProductListFragment()
        }
    }
}
