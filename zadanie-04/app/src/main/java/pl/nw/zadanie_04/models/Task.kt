package pl.nw.zadanie_04.models

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pl.nw.zadanie_04.DBHelper
import java.time.LocalDateTime


enum class TaskStatusEnum {
    PENDING, DONE
}

data class Task(
    var id: Int?,
    val name: String,
    val description: String = "",
    val dueDate: LocalDateTime = LocalDateTime.now(),
    val status: TaskStatusEnum = TaskStatusEnum.PENDING
)

class TaskViewModel : ViewModel() {
    private var tasks = MutableStateFlow<ArrayList<Task>>(ArrayList());

    fun getTasks(context: Context) : StateFlow<ArrayList<Task>> {
        if (tasks.value.isEmpty()) {
            val db = DBHelper(context, null)
            tasks.value = db.getTasks()
        }
        return tasks
    }

}