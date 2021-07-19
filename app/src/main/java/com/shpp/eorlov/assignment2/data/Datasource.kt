package com.shpp.eorlov.assignment2.data

import com.shpp.eorlov.assignment2.R
import com.shpp.eorlov.assignment2.model.PersonData


class Datasource {

    fun loadPersonData(): List<PersonData> {
        return listOf<PersonData>(
            PersonData(R.string.name1, R.string.profession1, R.drawable.person_image_example, 0,0,0,0,0),
            PersonData(R.string.name2, R.string.profession2, R.drawable.person_image_example, 0,0,0,0,0),
            PersonData(R.string.name3, R.string.profession3, R.drawable.person_image_example, 0,0,0,0,0),
            PersonData(R.string.name4, R.string.profession4, R.drawable.person_image_example, 0,0,0,0,0),
            PersonData(R.string.name5, R.string.profession5, R.drawable.person_image_example, 0,0,0,0,0),
            PersonData(R.string.name6, R.string.profession6, R.drawable.person_image_example, 0,0,0,0,0),
            PersonData(R.string.name7, R.string.profession7, R.drawable.person_image_example, 0,0,0,0,0),
            PersonData(R.string.name8, R.string.profession8, R.drawable.person_image_example, 0,0,0,0,0),
            PersonData(R.string.name9, R.string.profession9, R.drawable.person_image_example, 0,0,0,0,0),
            PersonData(R.string.name10, R.string.profession10, R.drawable.person_image_example, 0,0,0,0,0)
        )
    }
}

