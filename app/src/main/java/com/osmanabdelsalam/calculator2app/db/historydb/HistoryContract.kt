package com.osmanabdelsalam.calculator2app.db.historydb

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

object HistoryContract {
    private const val SQL_CREATE_HiSTORIES = "CREATE TABLE ${HistoryEntry.TABLE_NAME} (" +
            "${HistoryEntry.COLUMN_ID} INTEGER PRIMARY KEY," +
            "${HistoryEntry.COLUMN_TEXT} TEXT)"

    private const val SQL_DELETE_HiSTORIES = "DROP TABLE IF EXISTS ${HistoryEntry.TABLE_NAME}"

    class HistoryDbHelper(context: Context): SQLiteOpenHelper(context, HistoryEntry.DATABASE_NAME, null,
        HistoryEntry.DATABASE_VERSION
    ) {

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(SQL_CREATE_HiSTORIES)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL(SQL_DELETE_HiSTORIES)
            onCreate(db)
        }

        override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            super.onDowngrade(db, oldVersion, newVersion)
        }

    }
}