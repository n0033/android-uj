package pl.nw.zadanie_06.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import pl.nw.zadanie_06.fragments.CartFragment
import pl.nw.zadanie_06.fragments.CategoryListFragment
import pl.nw.zadanie_06.fragments.ProductListFragment
import pl.nw.zadanie_06.fragments.ProductDetailsFragment;

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return ProductListFragment()
            1 -> return CartFragment()
            2 -> return CategoryListFragment()
            else -> return ProductDetailsFragment()
        }
    }
}
