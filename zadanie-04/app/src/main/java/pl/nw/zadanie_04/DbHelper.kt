package pl.nw.zadanie_04

import android.annotation.SuppressLint
import pl.nw.zadanie_04.models.Task
import pl.nw.zadanie_04.models.TaskStatusEnum
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME_COL + " TEXT," +
                DESCRIPTION_COL + " TEXT," +
                DUE_DATE_COL + " DATETIME, " +
                STATUS_COL + " TEXT" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun createTask(task: Task): Task {
        val values = ContentValues()
        values.put(NAME_COL, task.name)
        values.put(DESCRIPTION_COL, task.description)
        values.put(DUE_DATE_COL, task.dueDate.format(DateTimeFormatter.ISO_DATE_TIME))
        values.put(STATUS_COL, task.status.toString())
        val db = this.writableDatabase
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        task.id = id.toInt()
        return task
    }

    fun deleteTaskById(id: Int){
        val db = this.readableDatabase
        db.delete(TABLE_NAME, "id=?", arrayOf(id.toString()));
        db.close();
    }

    @SuppressLint("Range")
    fun getTasks(): ArrayList<Task> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val taskList = ArrayList<Task>()
        try {
            if (cursor.count != 0) {
                cursor.moveToFirst()
                if (cursor.count > 0) {
                    do {
                        val id: Int = cursor.getInt(cursor.getColumnIndex(ID_COL))
                        val name: String = cursor.getString(cursor.getColumnIndex(NAME_COL))
                        val description: String = cursor.getString(cursor.getColumnIndex(DESCRIPTION_COL))
                        val dueDate: LocalDateTime =
                            LocalDateTime.parse(
                                cursor.getString(cursor.getColumnIndex(DUE_DATE_COL)),
                                DateTimeFormatter.ISO_DATE_TIME
                            )
                        val status: TaskStatusEnum =
                            TaskStatusEnum.valueOf(cursor.getString(cursor.getColumnIndex(STATUS_COL)))
                        val task = Task(
                            id, name, description, dueDate, status
                        )
                        taskList.add(task)
                    } while ((cursor.moveToNext()))
                }
            }
        } finally {
            cursor.close()
        }

        return taskList

    }

    companion object {
        private val DATABASE_NAME = "TODO_LIST"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "tasks"
        val ID_COL = "id"
        val NAME_COL = "name"
        val DESCRIPTION_COL = "description"
        val DUE_DATE_COL = "due_date"
        val STATUS_COL = "status"
    }
}
