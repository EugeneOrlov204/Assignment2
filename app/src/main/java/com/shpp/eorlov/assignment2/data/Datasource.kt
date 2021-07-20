package com.shpp.eorlov.assignment2.data

import com.shpp.eorlov.assignment2.R
import com.shpp.eorlov.assignment2.model.PersonData


class Datasource {

    fun loadPersonData(): List<PersonData> {
        return listOf<PersonData>(
            PersonData(R.string.name1, R.string.profession1, 0,0,0,0,0),
            PersonData(R.string.name2, R.string.profession2, 0,0,0,0,0),
            PersonData(R.string.name3, R.string.profession3, 0,0,0,0,0),
            PersonData(R.string.name4, R.string.profession4, 0,0,0,0,0),
            PersonData(R.string.name5, R.string.profession5, 0,0,0,0,0),
            PersonData(R.string.name6, R.string.profession6, 0,0,0,0,0),
            PersonData(R.string.name7, R.string.profession7, 0,0,0,0,0),
            PersonData(R.string.name8, R.string.profession8, 0,0,0,0,0),
            PersonData(R.string.name9, R.string.profession9, 0,0,0,0,0),
            PersonData(R.string.name10, R.string.profession10, 0,0,0,0,0)
        )
    }
}

