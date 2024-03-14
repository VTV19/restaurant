package org.example.entities

class CookingTaskComparator: Comparator<CookingTask> {
    override fun compare(o1: CookingTask?, o2: CookingTask?): Int {
        if (o1 == null && o2 == null) {
            return 0
        }
        if (o1 == null) {
            return -1
        }
        if (o2 == null) {
            return 1
        }

        return o1.order.priority.compareTo(o2.order.priority)
    }
}