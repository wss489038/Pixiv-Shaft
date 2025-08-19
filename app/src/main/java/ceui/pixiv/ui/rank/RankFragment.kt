package ceui.pixiv.ui.rank

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import ceui.lisa.R
import ceui.lisa.databinding.FragmentRankViewpagerBinding
import ceui.pixiv.ui.circles.PagedFragmentItem
import ceui.pixiv.ui.circles.SmartFragmentPagerAdapter
import ceui.pixiv.ui.common.TitledViewPagerFragment
import ceui.pixiv.widgets.setUpWith
import ceui.pixiv.ui.common.viewBinding

class RankFragment : TitledViewPagerFragment(R.layout.fragment_rank_viewpager) {

    private val binding by viewBinding(FragmentRankViewpagerBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.rootLayout.updatePadding(0, insets.top, 0, 0)
            windowInsets
        }
        val adapter = SmartFragmentPagerAdapter(
            listOf(
                PagedFragmentItem(
                    builder = {
                        RankingIllustsFragment().apply {
                            arguments = RankingIllustsFragmentArgs("day").toBundle()
                        }
                    },
                    initialTitle = getString(R.string.daily_rank)
                ),
                PagedFragmentItem(
                    builder = {
                        RankingIllustsFragment().apply {
                            arguments = RankingIllustsFragmentArgs("week").toBundle()
                        }
                    },
                    initialTitle = getString(R.string.weekly_rank)
                ),
                PagedFragmentItem(
                    builder = {
                        RankingIllustsFragment().apply {
                            arguments = RankingIllustsFragmentArgs("month").toBundle()
                        }
                    },
                    initialTitle = getString(R.string.monthly_rank)
                ),
            ),
            this
        )
        binding.rankViewpager.adapter = adapter
        binding.tabLayoutList.setUpWith(binding.rankViewpager, binding.slidingCursor, viewLifecycleOwner, {})
    }
}