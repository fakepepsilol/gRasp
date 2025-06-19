package com.fakepepsilol.grasp.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "config")

@Singleton
class Config @Inject constructor(
    @ApplicationContext private val context: Context
) {
    @Suppress("PropertyName")
    val TAG: String = "fpl->Config"

    ////////////////////////////////////////////////////////////////////
//    var counter: Int = 0
//        set(value) {
//            field = value
//            CoroutineScope(Dispatchers.IO).launch {
//                saveInt(I_COUNTER, counter)
//            }
//        }
    private var urls_serialized: String = ""
        set(value) {
            field = value
            CoroutineScope(Dispatchers.IO).launch {
                saveString(L_URLS, urls_serialized)
            }
        }

    var urls: ObservableList<UrlEntry> = ObservableList(onChange = {
        urls_serialized = Json.encodeToString(ObservableListSerializer, urls)
    })

    ////////////////////////////////////////////////////////////////////

    suspend fun preload() {
        Log.d(TAG, "preload")
        val items = Json.decodeFromString(
            ObservableListSerializer,
            context.dataStore.data.map { prefs -> prefs[L_URLS] ?: "[]" }.first()
        )
        var maxId = 0
        for (item in items) {
            item.id = maxId
            maxId++
        }
        urls.addAll(items)
    }


    companion object {
        //        val I_COUNTER = intPreferencesKey("counter")
        val L_URLS = stringPreferencesKey("urls")
    }

    @Suppress("unused")
    suspend fun saveInt(key: Preferences.Key<Int>, value: Int) {
        Log.d(TAG, "Saving: $key -> $value")
        context.dataStore.edit {
            it[key] = value
        }
        Log.d(TAG, "Saving: $key -> done.")
    }

    suspend fun saveString(key: Preferences.Key<String>, value: String) {
        Log.d(TAG, "Saving: $key -> $value")
        context.dataStore.edit {
            it[key] = value
        }
        Log.d(TAG, "Saving: $key -> done.")
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {

    @Provides
    @Singleton
    fun getConfig(@ApplicationContext context: Context): Config {
        return Config(context)
    }
}