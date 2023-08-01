package com.osmanabdelsalam.calculator2app.db.historydb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.osmanabdelsalam.calculator2app.data.History

object HistoryContract {
    object HistoryEntry: BaseColumns {
        const val TABLE_NAME = "histories"
        const val COLUMN_TEXT = "TEXT"
    }
    private const val SQL_CREATE_HiSTORIES = "CREATE TABLE ${HistoryEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${HistoryEntry.COLUMN_TEXT} TEXT)"

    private const val SQL_DELETE_HiSTORIES = "DROP TABLE IF EXISTS ${HistoryEntry.TABLE_NAME}"

    class HistoryDbHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null,
        DATABASE_VERSION
    ) {

        companion object {
            const val DATABASE_VERSION = 1
            const val DATABASE_NAME = "calculator2app.history.db"
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(SQL_CREATE_HiSTORIES)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL(SQL_DELETE_HiSTORIES)
            onCreate(db)
        }

        override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            super.onDowngrade(db, oldVersion, newVersion)
        }

        private fun data(
            tableName: String = HistoryEntry.TABLE_NAME,
            order: String = "${BaseColumns._ID} DESC",
            columns: Array<String> = arrayOf(BaseColumns._ID,HistoryEntry.COLUMN_TEXT)
        ): Cursor {
            return readableDatabase.query(
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
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(HistoryEntry.COLUMN_TEXT, text)
            }
            val result = db?.insert(HistoryEntry.TABLE_NAME, null, values)
            db?.close()
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
            val db = writableDatabase
            val result = db.delete(HistoryEntry.TABLE_NAME, "${BaseColumns._ID}=?", arrayOf("$id"))
            db.close()
            return result > -1
        }

        fun clearHistory() {
            val db = writableDatabase
            db.delete(HistoryEntry.TABLE_NAME, null, null)
            db.close()
        }

    }
}