package com.volcano.examonline.mvvm.homepage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.volcano.examonline.mvvm.exam.model.Question
import com.volcano.examonline.mvvm.homepage.model.Subject
import com.volcano.examonline.network.NetworkRepository

class HomepageViewModel : ViewModel() {

    var subjectData = arrayListOf<Subject>()

    private var mutableSubjects = MutableLiveData<Boolean>()

    val subjects : LiveData<List<Subject>> = Transformations.switchMap(mutableSubjects){
        NetworkRepository.getInstance().getSubjects()
    }

    fun getSubjects() {
        mutableSubjects.value = true
    }

    var questionData = arrayListOf<Question>()

    private var mutableQuestions = MutableLiveData<Int>()

    val questions: LiveData<List<Question>> = Transformations.switchMap(mutableQuestions) { id ->
        NetworkRepository.getInstance().getQuestions(id)
    }

    fun getQuestions(subjectId: Int) {
        mutableQuestions.value = subjectId
    }
}