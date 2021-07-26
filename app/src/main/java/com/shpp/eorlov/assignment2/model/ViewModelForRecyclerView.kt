package com.shpp.eorlov.assignment2.model

import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment2.R
import com.shpp.eorlov.assignment2.data.PersonData

class ViewModelForRecyclerView : ViewModel() {
    private var personData: MutableList<PersonData>? = null
    fun getValue(): MutableList<PersonData> {
        if (personData == null) {
            personData = loadPersonData()
        }

        return personData as MutableList<PersonData>
    }

    private fun loadPersonData(): MutableList<PersonData> {
        return mutableListOf(
            PersonData(R.string.name1, R.string.profession1, R.string.photo1, 0, 0, 0, 0),
            PersonData(R.string.name2, R.string.profession2, R.string.photo2, 0, 0, 0, 0),
            PersonData(R.string.name3, R.string.profession3, R.string.photo3, 0, 0, 0, 0),
            PersonData(R.string.name4, R.string.profession4, R.string.photo4, 0, 0, 0, 0),
            PersonData(R.string.name5, R.string.profession5, R.string.photo5, 0, 0, 0, 0),
            PersonData(R.string.name6, R.string.profession6, R.string.photo6, 0, 0, 0, 0),
            PersonData(R.string.name7, R.string.profession7, R.string.photo7, 0, 0, 0, 0),
            PersonData(R.string.name8, R.string.profession8, R.string.photo8, 0, 0, 0, 0),
            PersonData(R.string.name9, R.string.profession9, R.string.photo9, 0, 0, 0, 0),
            PersonData(R.string.name10, R.string.profession10, R.string.photo10, 0, 0, 0, 0)
        )
    }

}