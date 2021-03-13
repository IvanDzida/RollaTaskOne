package com.yolo.rollataskone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import com.yolo.rollataskone.adapters.ItemAdapter
import com.yolo.rollataskone.data.Item
import com.yolo.rollataskone.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.LinkedBlockingQueue

class MainActivity : AppCompatActivity(), ItemClickListener, DisplayLoading {
    lateinit var binding: ActivityMainBinding
    private val scopeMainThread = CoroutineScope(Dispatchers.Main)

    private val items = mutableListOf<Item>()
    private val selectedItems = mutableListOf<Item>()

    val lbq = LinkedBlockingQueue<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindViews()
        initItems()

        setupRecycler()

        Thread(Worker(this, lbq) {
            runOnUiThread {
                Log.i("maxxx", "worker begin - listSize: ${selectedItems.size}")

                if (selectedItems.isNotEmpty()) {
                    if (selectedItems[0] == it) {
                        selectedItems.removeAt(0)
                        binding.resultMessage.text = "Nothing selected"
                    } else {
                        selectedItems.removeAt(0)
                        selectedItems.add(it)
                        binding.resultMessage.text = it.description
                    }
                } else {
                    selectedItems.add(it)
                    binding.resultMessage.text = it.description
                }

                //  binding.loadingView.visibility = GONE
                Log.e("maxxx", "worker end - listSize: ${selectedItems.size}")
                Log.e("IVANN", "Task $it finished!!")
            }
        }).start()
    }

    private fun bindViews() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initItems() {
        for (i in 1..8) {
            items.add(Item(i, "Button$i", "Button $i"))
        }
        items.add(Item(9, "Reset", "Nothing selected"))
    }

    private fun setupRecycler() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = ItemAdapter(this, items, this)
    }

    private fun handleItemClick(item: Item) {
        lbq.add(item)
    }

    override fun onItemClickListener(item: Item) {
        handleItemClick(item)
    }

    override fun displayLoading(visible: Boolean) {
        scopeMainThread.launch {
            if (visible) {
                binding.loadingView.visibility = VISIBLE
            } else {
                binding.loadingView.visibility = GONE
            }
        }

    }
}

class Worker(
        private val displayLoading: DisplayLoading,
        private val queue: LinkedBlockingQueue<Item>,
        private val onNext: (Item) -> (Unit)
) :
        Runnable {
    var running = false
    override fun run() {
        doSomeWork()
    }

    private fun doSomeWork() {
        running = true
        while (running) {
            val item = queue.take()
            displayLoading.displayLoading(true)
            val delay = (1000..3000).random().toLong()
            Thread.sleep(delay) // random delay
            displayLoading.displayLoading(false)
            onNext.invoke(item)
        }
    }
}

interface DisplayLoading {
    fun displayLoading(visible: Boolean)
}

interface ItemClickListener {
    fun onItemClickListener(item: Item)
}