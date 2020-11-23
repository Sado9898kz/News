package com.beeline.news

import io.realm.RealmList
import io.realm.RealmObject
import java.util.*

open class ArticleSave(
    var author: String = "",
    var content: String = "",
    var description: String = "",
    var publishedAt: Date = Date(),
    var source: RealmList<SourceSave> = RealmList<SourceSave>(),
    var title: String = "",
    var url: String = "",
    var urlToImage: String = ""
) : RealmObject()

open class SourceSave(
    var id: String = "",
    var name: String = ""
) : RealmObject()