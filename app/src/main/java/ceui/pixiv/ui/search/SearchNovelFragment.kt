package ceui.pixiv.ui.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import ceui.lisa.R
import ceui.lisa.databinding.FragmentPixivListBinding
import ceui.loxia.ObjectType
import ceui.loxia.RefreshHint
import ceui.loxia.observeEvent
import ceui.pixiv.ui.common.ListMode
import ceui.pixiv.ui.common.PixivFragment
import ceui.pixiv.ui.common.setUpRefreshState
import ceui.pixiv.ui.list.pixivListViewModel
import ceui.pixiv.widgets.DialogViewModel
import ceui.pixiv.ui.common.viewBinding

class SearchNovelFragment : PixivFragment(R.layout.fragment_pixiv_list) {
    private val searchViewModel by viewModels<SearchViewModel>(ownerProducer = { requireParentFragment() })
    private val dialogViewModel by activityViewModels<DialogViewModel>()
    private val binding by viewBinding(FragmentPixivListBinding::bind)
    private val viewModel by pixivListViewModel({ Pair(searchViewModel, dialogViewModel) }) { (vm, dialogVM) ->
        SearchNovelDataSource {
            val count = dialogVM.chosenUsersYoriCount.value
            vm.buildSearchConfig(count, ObjectType.NOVEL)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRefreshState(binding, viewModel, ListMode.VERTICAL)
        binding.radioTab.setTabs(listOf(
            "热度预览",
            "从新到旧",
            "从旧到新",
            "热度排序",
        ))
        binding.radioTab.setItemCickListener { index ->
            searchViewModel.novelSelectedRadioTabIndex.value = index
            val now = System.currentTimeMillis()
            searchViewModel.triggerSearchNovelEvent(now)
        }
        searchViewModel.searchNovelEvent.observeEvent(viewLifecycleOwner) {
            viewModel.refresh(RefreshHint.InitialLoad)
        }
        searchViewModel.novelSelectedRadioTabIndex.observe(viewLifecycleOwner) { index ->
            binding.radioTab.selectTab(index)
            binding.usersYori.isVisible = (index == 1) || (index == 2)
        }
    }
}