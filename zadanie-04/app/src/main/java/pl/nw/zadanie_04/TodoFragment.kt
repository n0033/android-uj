package pl.nw.zadanie_04

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.nw.zadanie_04.databinding.TodoFragmentBinding
import pl.nw.zadanie_04.adapters.TaskListAdapter
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
        val adapter = TaskListAdapter(requireContext(), model.getTasks(requireContext()).value)
        binding.listView.adapter = adapter

        binding.add.setOnClickListener {
            val taskRequest = Task(null, binding.editText.text.toString())
            val createdTask = db.createTask(taskRequest)
            adapter.items.add(createdTask)
            adapter.notifyDataSetChanged()
            binding.editText.text.clear()
        }

        return binding.root
    }

}
