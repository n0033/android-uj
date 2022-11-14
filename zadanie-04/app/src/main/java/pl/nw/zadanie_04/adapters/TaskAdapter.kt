package pl.nw.zadanie_04.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import pl.nw.zadanie_04.models.Task
import pl.nw.zadanie_04.R

class TaskListAdapter(context: Context, items: ArrayList<Task>) : BaseAdapter() {
    private val context: Context
    val items: ArrayList<Task>

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView: View? = convertView
        if (convertView == null) {
            convertView =
                LayoutInflater.from(context).inflate(R.layout.task_list_view, parent, false)
        }
        val currentItem = getItem(position) as Task
        val textViewItemName = convertView?.findViewById(R.id.text_view_item_name) as TextView

        textViewItemName.text = currentItem.name
        return convertView
    }

    init {
        this.context = context
        this.items = items
    }
}