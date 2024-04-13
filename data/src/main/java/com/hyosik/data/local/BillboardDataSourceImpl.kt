package com.hyosik.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hyosik.model.BILLBOARD_KEY
import com.hyosik.model.Billboard
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
            Billboard(
                key = key,
                description = preferences[stringPreferencesKey(key)] ?: ""
            )
        }

    override suspend fun editBillboard(billboard: Billboard) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(billboard.key)] = billboard.description
        }
    }
}