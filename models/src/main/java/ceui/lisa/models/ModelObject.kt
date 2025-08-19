package ceui.lisa.models

interface ModelObject {
    val objectUniqueId: Long
    val objectType: Int
}

object ObjectSpec {
    const val UNKNOWN = 0
    const val POST = 1
    const val USER = 2
    const val ARTICLE = 3
    const val GIF_INFO = 4


    const val Illust = 5
    const val KUser = 6
    const val JNOVEL = 7

    const val UserProfile = 8
}