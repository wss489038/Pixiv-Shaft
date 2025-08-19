package ceui.pixiv.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.R
import ceui.lisa.activities.Shaft
import ceui.loxia.Client
import ceui.loxia.Illust
import ceui.loxia.IllustSeriesResp
import ceui.loxia.RefreshHint
import ceui.loxia.RefreshState
import ceui.pixiv.ui.chats.RedSectionHeaderHolder
import ceui.pixiv.ui.common.DataSource
import ceui.pixiv.ui.common.HoldersContainer
import ceui.pixiv.ui.common.ListItemHolder
import ceui.pixiv.ui.common.LoadMoreOwner
import ceui.pixiv.ui.common.LoadingHolder
import ceui.pixiv.ui.common.RefreshOwner
import ceui.pixiv.ui.common.createResponseStore
import ceui.pixiv.ui.novel.NovelSeriesHeaderHolder
import ceui.pixiv.ui.user.UserPostHolder
import kotlinx.coroutines.launch
import timber.log.Timber

class IllustSeriesViewModel(
    private val seriesId: Long,
) : ViewModel(), RefreshOwner, LoadMoreOwner, HoldersContainer {

    private val _itemHolders = MutableLiveData<List<ListItemHolder>>()
    private val _refreshState = MutableLiveData<RefreshState>()
    private var _lastOrder: Int? = null

    private val _series = MutableLiveData<IllustSeriesResp>()
    val series: LiveData<IllustSeriesResp> = _series

    private val _seriesIllustsDataSource = object : DataSource<Illust, IllustSeriesResp>(
        dataFetcher = { Client.appApi.getIllustSeries(seriesId, _lastOrder) },
        responseStore = createResponseStore({ "illust-series-$seriesId" }),
        itemMapper = { illust -> listOf(UserPostHolder(illust)) }
    ) {
        override fun updateHolders(holders: List<ListItemHolder>) {
            // 从现有列表中剔除 LoadingHolder
            val filteredList =
                (_itemHolders.value ?: listOf()).filterNot { it is LoadingHolder }.toMutableList()

            // 添加新数据
            filteredList.addAll(holders)

            // 更新列表
            _itemHolders.value = filteredList
            _refreshState.value = RefreshState.LOADED(
                hasContent = true,
                hasNext = hasNext()
            )
        }
    }

    init {
        refresh(RefreshHint.InitialLoad)
    }

    override fun refresh(hint: RefreshHint) {
        viewModelScope.launch {
            try {
                _refreshState.value = RefreshState.LOADING(refreshHint = hint)
                val context = Shaft.getContext()
                val resp = Client.appApi.getIllustSeries(seriesId)
                _series.value = resp
                val result = mutableListOf<ListItemHolder>()
                resp.illust_series_detail?.let {
                    result.add(NovelSeriesHeaderHolder(it))
                }
                result.add(RedSectionHeaderHolder(context.getString(R.string.string_432)))
                result.add(UserInfoHolder(resp.illust_series_detail?.user?.id ?: 0L))
                result.add(RedSectionHeaderHolder(
                    context.getString(
                        R.string.total_works_count,
                        resp.illust_series_detail?.content_count
                    )))
                result.addAll(resp.displayList.map { illust -> UserPostHolder(illust) })
                _lastOrder = resp.illusts?.size
                _itemHolders.value = result
                val hasNext = resp.next_url != null
                _refreshState.value = RefreshState.LOADED(
                    hasContent = true, hasNext = hasNext
                )
                if (hasNext) {
                    _seriesIllustsDataSource.refreshImpl(hint)
                }
            } catch (ex: Exception) {
                _refreshState.value = RefreshState.ERROR(ex)
                Timber.e(ex)
            }
        }
    }

    override fun prepareIdMap(fragmentUniqueId: String) {
        val filteredList = _itemHolders.value.orEmpty()
            .filterIsInstance<UserPostHolder>() // 直接过滤为特定类型
            .map { it.illust.id } // 提取 id 列表

        ArtworksMap.store[fragmentUniqueId] = filteredList
    }

    override val refreshState: LiveData<RefreshState>
        get() = _refreshState
    override val holders: LiveData<List<ListItemHolder>>
        get() = _itemHolders

    override fun loadMore() {
        viewModelScope.launch {
            _seriesIllustsDataSource.loadMoreImpl()
        }
    }
}