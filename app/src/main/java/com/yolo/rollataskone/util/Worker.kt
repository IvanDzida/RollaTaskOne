package com.yolo.rollataskone.util

import com.yolo.rollataskone.DisplayLoading
import com.yolo.rollataskone.data.Item
import java.util.concurrent.LinkedBlockingQueue

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