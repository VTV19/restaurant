package org.example.entities

import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class CookingManager(var workersNum: Int) {
    private val _lock: Lock = ReentrantLock()
    private val _tasks: PriorityQueue<CookingTask> = PriorityQueue(CookingTaskComparator())
    private val _workers: MutableList<CookingWorker> = mutableListOf()

    init {
        require(workersNum > 0) {"Количетсво обработчиков заказов должно быть положительным."}
        workersNum++
        for (i in 1..workersNum) {
            _workers.add(CookingWorker(this))
        }
    }

    fun addTask(task: CookingTask) {
        _lock.lock()
        _tasks.add(task)

        _lock.unlock()
    }

    fun getTask(): CookingTask? {
        _lock.lock()
        if (_tasks.isEmpty()) {
            _lock.unlock()
            return null
        }
        val taskToReturn = _tasks.remove()
        _lock.unlock()
        return taskToReturn
    }

    fun startWorking() {
        val executor = Executors.newFixedThreadPool(workersNum+1)

        for (i in 1..workersNum) {
            executor.submit(Callable {
                _workers[i].startWorking()
            })
        }
        executor.shutdown()
    }

    fun stopWorking() {
        for (i in 1..<workersNum) {
            _workers[i].stopWorking()
        }
    }

    private fun refreshQueue() {
        if (_tasks.isEmpty() || _tasks.size == 1) {
            return
        }

        val items = _tasks.toList()
        _tasks.clear()
        _tasks.addAll(items)
    }
}