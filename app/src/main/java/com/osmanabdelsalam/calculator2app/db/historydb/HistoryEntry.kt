package com.osmanabdelsalam.calculator2app.db.historydb

import android.provider.BaseColumns

object HistoryEntry {

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "calculator2app.history.db"

    const val TABLE_NAME = "histories"

    const val COLUMN_ID = BaseColumns._ID
    const val COLUMN_TEXT = "TEXT"
}