package ceui.pixiv.ui.rank

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import ceui.pixiv.ui.common.PixivFragment
import ceui.lisa.R
import ceui.lisa.databinding.FragmentRankPreviewBinding
import ceui.loxia.Client
import ceui.loxia.pushFragment
import ceui.pixiv.ui.common.CommonAdapter
import ceui.pixiv.ui.common.createResponseStore
import ceui.pixiv.ui.common.pixivValueViewModel
import ceui.pixiv.utils.setOnClick
import ceui.pixiv.ui.common.viewBinding
import kotlin.getValue

class RankPreviewFragment : PixivFragment(R.layout.fragment_rank_preview) {

    private val binding by viewBinding(FragmentRankPreviewBinding::bind)
    private val rankIllustViewModel by pixivValueViewModel(
        responseStore = createResponseStore({ "rank-illust-day" })
    ) { hint ->
        Client.appApi.getRankingIllusts("day")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rankItems.setOnClick {
            pushFragment(R.id.navigation_rank)
        }
        val rankingAdapter = CommonAdapter(viewLifecycleOwner)
        binding.rankPreviewList.adapter = rankingAdapter
        binding.rankPreviewList.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL, false)
        rankIllustViewModel.result.observe(viewLifecycleOwner) { resp ->
            rankingAdapter.submitList(resp.displayList.map { RankPreviewHolder(it) })
        }
    }
}