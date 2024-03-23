package com.raitech.assignment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import kotlin.coroutines.CoroutineContext


class UserViewModel:ViewModel(), CoroutineScope {
    val sJob = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + sJob
    var panCardNumber: String = ""
    var date: String = ""
    var month: String = ""
    var year: String = ""

    private var result = MutableLiveData<String>()

    fun getLogInResult(): LiveData<String> = result
   private var _isEnable: MutableLiveData<Boolean>? = MutableLiveData(false)
    val isEnable: LiveData<Boolean>
        get() {
            return _isEnable!!
        }


    fun performValidation() {
      launch {
          val panPattern: Pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
          val birthDate: Pattern = Pattern.compile("[0-9]{2}")
          val birthMonth: Pattern = Pattern.compile("[0-9]{2}")
          val birthYear: Pattern = Pattern.compile("[0-9]{4}")
          val panMatcher = panPattern.matcher(panCardNumber)
          val dateMatcher = birthDate.matcher(date)
          val monthMatcher = birthMonth.matcher(month)
          val yearMatcher = birthYear.matcher(year)
          if (!panMatcher.matches()) {
              _isEnable!!.postValue(false)
              result.value = "Invalid PAN number"
          } else if (!dateMatcher.matches()||date.toInt()>31) {
              _isEnable!!.postValue(false)
              result.value = "Invalid Date"
          }else if (!monthMatcher.matches()||month.toInt()>12) {
              _isEnable!!.postValue(false)
              result.value = "Invalid Month"
          }else if (!yearMatcher.matches()) {
              _isEnable!!.postValue(false)
              result.value = "Invalid Year"
          }else {
              _isEnable!!.postValue(true)
            result.value=""
          }
      }
    }

}