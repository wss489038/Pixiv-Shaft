package ceui.lisa.repo

import android.text.TextUtils
import ceui.lisa.core.RemoteRepo
import ceui.lisa.http.Retro
import ceui.lisa.model.ListNovel
import ceui.lisa.utils.PixivSearchParamUtil
import ceui.lisa.viewmodel.SearchModel
import io.reactivex.Observable

class SearchNovelRepo(
    var keyword: String?,
    private var sortType: String?,
    var searchType: String?,
    var starSize: String?,
    private var isPremium: Boolean?,
    private var startDate: String?,
    private var endDate: String?,
    private var r18Restriction: Int?
) : RemoteRepo<ListNovel>() {

    override fun initApi(): Observable<ListNovel> {
        val assembledKeyword: String = (keyword + when {
            TextUtils.isEmpty(starSize) -> ""
            else -> " $starSize"
        } + when (r18Restriction) {
            null -> ""
            else -> " ${PixivSearchParamUtil.R18_RESTRICTION_VALUE[r18Restriction!!]}"
        }).trim()

        return if (sortType == PixivSearchParamUtil.POPULAR_SORT_VALUE && (isPremium != true)) {
            Retro.getAppApi().popularNovelPreview(token(),
                assembledKeyword,
                startDate,
                endDate,
                searchType)
        } else {
            Retro.getAppApi().searchNovel(
                token(),
                assembledKeyword,
                sortType,
                startDate,
                endDate,
                searchType
            )
        }
    }

    override fun initNextApi(): Observable<ListNovel> {
        return Retro.getAppApi().getNextNovel(token(), nextUrl)
    }

    fun update(searchModel: SearchModel) {
        keyword = searchModel.keyword.value
        sortType = searchModel.sortType.value
        searchType = searchModel.searchType.value
        starSize = searchModel.starSize.value
        isPremium = searchModel.isPremium.value
        startDate = searchModel.startDate.value
        endDate = searchModel.endDate.value
        r18Restriction = searchModel.r18Restriction.value
    }
}
