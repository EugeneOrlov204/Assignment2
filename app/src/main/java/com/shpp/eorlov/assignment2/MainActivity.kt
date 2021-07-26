package com.shpp.eorlov.assignment2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.shpp.eorlov.assignment2.adapter.ItemAdapter
import com.shpp.eorlov.assignment2.databinding.ActivityMainBinding
import com.shpp.eorlov.assignment2.model.ViewModelForRecyclerView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val model: ViewModelForRecyclerView =
            ViewModelProvider(this)
            .get(ViewModelForRecyclerView::class.java)


        val recyclerView = binding.recyclerView
        val itemAdapter = ItemAdapter(this, model.getValue())
        recyclerView.adapter = itemAdapter


        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)
        ItemTouchHelper(itemAdapter.itemTouchHelperCallBack).attachToRecyclerView(recyclerView)

        setContentView(binding.root)
    }
}
