package `in`.redbus.trendingapp.model

data class SampleModel(
    val total_count: Int,
    val items: List<Items>
)

data class Items(
    val id:String,
    val node_id:String,
    val name:String,
    val owner:Owner,
    val stargazers_count:Int,
    val watchers_count:Int,
    val watchers:Int,
    val score:Int,
    var isSelected:Boolean = false
)


data class Owner(
    val login:String,
    val id:String,
    val avatar_url:String,
)
