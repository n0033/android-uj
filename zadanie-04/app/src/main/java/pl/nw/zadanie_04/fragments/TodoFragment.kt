package pl.nw.zadanie_04.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import pl.nw.zadanie_04.DBHelper
import pl.nw.zadanie_04.R
import pl.nw.zadanie_04.adapters.TaskListAdapter
import pl.nw.zadanie_04.databinding.TodoFragmentBinding
import pl.nw.zadanie_04.models.Task
import pl.nw.zadanie_04.models.TaskViewModel


class TodoFragment : Fragment(R.layout.todo_fragment) {
    private lateinit var binding: TodoFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = TodoFragmentBinding.inflate(layoutInflater)

        val db = DBHelper(requireContext(), null)
        val model = TaskViewModel()
        val adapter = TaskListAdapter(model.getTasks(requireContext()).value)

        binding.taskHolder.layoutManager = LinearLayoutManager(requireContext())
        binding.taskHolder.adapter = adapter


        val itemTouchHelper =  ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // no up/down swipe
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task = adapter.data[position]
                task.id?.let {
                    db.deleteTaskById(it)
                    adapter.data.removeAt(position);
                    adapter.notifyDataSetChanged();
                }
                println("deleted task: $task");
            }

        })

        itemTouchHelper.attachToRecyclerView(binding.taskHolder)

        binding.add.setOnClickListener {
            val taskRequest = Task(null, binding.editText.text.toString())
            val createdTask = db.createTask(taskRequest)
            adapter.data.add(createdTask)
            println("created task: $createdTask")
            adapter.notifyDataSetChanged()
            binding.editText.text.clear()
        }

        return binding.root
    }

}
