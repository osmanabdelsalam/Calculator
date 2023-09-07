package com.osmanabdelsalam.calculator2app.repositories

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import com.osmanabdelsalam.calculator2app.data.History
import com.osmanabdelsalam.calculator2app.db.historydb.HistoryContract
import com.osmanabdelsalam.calculator2app.db.historydb.HistoryEntry

class HistoryRepository(context: Context) {

    private val historyDB by lazy {
        HistoryContract.HistoryDbHelper(context)
    }


    private fun data(
        tableName: String = HistoryEntry.TABLE_NAME,
        order: String = "${HistoryEntry.COLUMN_ID} DESC",
        columns: Array<String> = arrayOf(BaseColumns._ID, HistoryEntry.COLUMN_TEXT)
    ): Cursor {
        return historyDB.writableDatabase.query(
            tableName,
            columns,
            null,
            null,
            null,
            null,
            order
        )
    }

    fun insertEntry(text: String): Long? {
        val values = ContentValues().apply {
            put(HistoryEntry.COLUMN_TEXT, text)
        }
        val result = historyDB.writableDatabase?.insert(HistoryEntry.TABLE_NAME, null, values)
        historyDB.writableDatabase?.close()
        return result
    }

    fun getHistoryList(): ArrayList<History> {
        val cursor = data()
        val historyList = arrayListOf<History>()

        with(cursor) {
            while (moveToNext()) {
                historyList.add(History(getInt(0), getString(1)))
            }
            close()
        }
        return historyList
    }

    fun deleteById(id: Int): Boolean {
        val result = historyDB.writableDatabase.delete(HistoryEntry.TABLE_NAME, "${BaseColumns._ID}=?", arrayOf("$id"))
        historyDB.writableDatabase.close()
        return result > -1
    }

    fun clearHistory() {
        historyDB.writableDatabase.delete(HistoryEntry.TABLE_NAME, null, null)
        historyDB.writableDatabase.close()
    }
}