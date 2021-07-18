package com.shpp.eorlov.assignment2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment2.adapter.ItemAdapter
import com.shpp.eorlov.assignment2.data.Datasource
import com.shpp.eorlov.assignment2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Initialize data.
        val myDataset = Datasource().loadAffirmations()

        val recyclerView = binding.recyclerView
        recyclerView.adapter = ItemAdapter(this, myDataset)

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)

        setContentView(binding.root)
    }
}
