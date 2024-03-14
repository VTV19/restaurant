package org.example.entities

class CookingWorker(private var _kitchen: CookingManager) {
    private var taskOnWork: CookingTask? = null
    private var isAlive: Boolean = true

    fun startWorking() {
        var counter = 0
        while (isAlive) {
            if (taskOnWork == null) {
                taskOnWork = _kitchen.getTask()
                if (taskOnWork != null) {
                    taskOnWork!!.order.status = OrderStatus.OnCook
                }
            }
            else {
                taskOnWork!!.order.requestOwnership()
                if (taskOnWork!!.order.status == OrderStatus.Cancelled) {
                    taskOnWork!!.order.releaseOwnership()
                    taskOnWork = null
                    counter = 0
                    continue
                }

                if (counter == taskOnWork!!.order.timeToCook) {
                    taskOnWork!!.finishTask()
                    taskOnWork!!.order.releaseOwnership()
                    taskOnWork = null
                    counter = 0
                    continue
                }
                counter++
                taskOnWork!!.order.releaseOwnership()
                Thread.sleep(1000)
            }
        }
    }

    fun stopWorking() {
        isAlive = false
    }
}