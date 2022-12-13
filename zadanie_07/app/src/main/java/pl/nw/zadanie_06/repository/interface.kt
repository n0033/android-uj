package pl.nw.zadanie_06.repository

import com.google.firebase.database.DatabaseReference


interface Repository<T> {

    fun create(query: String, data: T) : Any

    fun read(query: String): Any

    fun update(query: String, data: T) : Any

    fun delete(query: String) : Any
}