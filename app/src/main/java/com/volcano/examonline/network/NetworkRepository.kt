package com.volcano.examonline.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.volcano.examonline.base.Response
import com.volcano.examonline.base.transform
import com.volcano.examonline.mvvm.forum.model.Article
import com.volcano.examonline.mvvm.study.model.Comment
import com.volcano.examonline.mvvm.study.model.Subject
import com.volcano.examonline.mvvm.login.model.TokenBean
import com.volcano.examonline.mvvm.mine.model.UserInfo
import com.volcano.examonline.mvvm.study.model.Question
import com.volcano.examonline.mvvm.study.model.Ranking
import com.volcano.examonline.util.ConstantData
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkRepository {

    private val TAG = "NetworkRepository"

    private val api : API by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.107:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(API::class.java)
    }

    /**
     * 题库页
     */

    fun getQuestions(id: Int): LiveData<Response<List<Question>>> {
        var result = MutableLiveData<Response<List<Question>>>()
        api.getQuestions(id).transform(result)
        return result
    }

    fun searchQuestion(content: String): LiveData<Response<List<Question>>> {
        var result = MutableLiveData<Response<List<Question>>>()
        api.searchQuestion(content).transform(result)
        return result
    }

    fun getQuestionComments(questionId: Int): LiveData<Response<List<Comment>>> {
        var result = MutableLiveData<Response<List<Comment>>>()
        api.getQuestionComments(questionId).transform(result)
        return result
    }

    fun getQuestionHotKey(): LiveData<Response<List<Any>>> {
        var result = MutableLiveData<Response<List<Any>>>()
        api.getQuestionHotKey().transform(result)
        return result
    }

    fun getSubjects(): LiveData<Response<List<Subject>>> {
        val result = MutableLiveData<Response<List<Subject>>>()
        api.getSubjects().transform(result)
        return result
    }


    /**
     * 论坛页
     */

    fun getArticles(): LiveData<Response<List<Article>>> {
        var result = MutableLiveData<Response<List<Article>>>()
        api.getArticles().transform(result)
        return result
    }

    fun getHotArticles(): LiveData<Response<List<Article>>> {
        var result = MutableLiveData<Response<List<Article>>>()
        api.getHotArticles().transform(result)
        return result
    }

    fun getArticleComments(id: Int): LiveData<Response<List<Comment>>> {
        var result = MutableLiveData<Response<List<Comment>>>()
        api.getArticleComments(id).transform(result)
        return result
    }

    fun uploadArticle(obj: Article): LiveData<Response<Any>> {
        val result = MutableLiveData<Response<Any>>()
        api.uploadArticle(ConstantData.TOKEN!! ,obj).transform(result)
        return result
    }

    fun getArticleHotKey(): LiveData<Response<List<Any>>> {
        var result = MutableLiveData<Response<List<Any>>>()
        api.getArticleHotKey().transform(result)
        return result
    }

    /**
     * 考试页
     */
    fun getRandomQuestions(id:Int, num: Int): LiveData<Response<List<Question>>> {
        var result = MutableLiveData<Response<List<Question>>>()
        api.getRandomQuestions(id, num).transform(result)
        return result
    }

    /**
     * 我的页
     */

    fun register(userInfo: UserInfo): LiveData<Response<Any>> {
        var result = MutableLiveData<Response<Any>>()
        api.register(userInfo).transform(result)
        return result
    }

    fun login(userInfo: UserInfo): LiveData<Response<TokenBean>> {
        val result = MutableLiveData<Response<TokenBean>>()
        api.login(userInfo).transform(result)
        return result
    }

    fun getUserInfo(phone: String): LiveData<Response<UserInfo>> {
        val result = MutableLiveData<Response<UserInfo>>()
        api.getUserInfo(phone).transform(result)
        return result
    }

    fun getRanking(): LiveData<Response<List<Ranking>>> {
        var result = MutableLiveData<Response<List<Ranking>>>()
        api.getRanking().transform(result)
        return result
    }

}