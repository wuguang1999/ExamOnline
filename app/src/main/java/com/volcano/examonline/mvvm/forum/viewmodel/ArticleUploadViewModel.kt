package com.volcano.examonline.mvvm.forum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.volcano.examonline.mvvm.forum.model.Article
import com.volcano.examonline.network.NetworkRepository

class ArticleUploadViewModel : ViewModel() {

    var mutableUploadArticle = MutableLiveData<Article>()

    val uploadArticle = Transformations.switchMap(mutableUploadArticle) {obj ->
        NetworkRepository.uploadArticle(obj)
    }

    // 发布讨论文章
    fun uploadArticle(title: String, desc: String, field: String) {
        var article = Article()
        article.description = desc
        article.title = title
        article.field = field
        mutableUploadArticle.value = article
    }
}