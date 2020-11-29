package ua.shishkoam.appcoockingbook.utils.interactors.storage

interface StringStorage {
    fun load(path: String) : String?
    fun save(data: String, path: String) : Boolean
}