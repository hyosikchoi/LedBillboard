package com.hyosik.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hyosik.model.BILLBOARD_KEY
import com.hyosik.model.Billboard
import com.hyosik.model.Direction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class BillboardDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : BillboardDataSource {

    override fun getBillboard(key: String): Flow<Billboard> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val json = preferences[stringPreferencesKey(key)]
            val type = object : TypeToken<Billboard>() {}.type
            Gson().fromJson(json, type) ?: Billboard(
                key = BILLBOARD_KEY,
                description = "",
                fontSize = 100,
                direction = Direction.STOP,
                textColor = "FFFFFFFF",
                billboardTextWidth = 1
            )
        }

    override suspend fun editBillboard(billboard: Billboard) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(billboard.key)] = Gson().toJson(billboard)
        }
    }
}