package pl.nw.zadanie_04.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.nw.zadanie_04.R
import pl.nw.zadanie_04.models.Task

class TaskListAdapter(itemList: ArrayList<Task>) : RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {
     val data = itemList;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.linear_text_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskListAdapter.ViewHolder, position: Int) {
        holder.itemText.text = data[position].name
    }



    override fun getItemCount(): Int = data.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemText: TextView

        init {
            itemText = itemView.findViewById(R.id.text_item) as TextView
        }
    }
}