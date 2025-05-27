package com.example.medicalinventryapp.pref

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.datastore by preferencesDataStore("user_prefrences")

class PrefrenceDatastore(private val context: Context) {

    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val IS_USER_APPROVED= stringPreferencesKey("is_user_approved")
    }

    suspend fun saveUserId(userId: String) {
        context.datastore.edit {
            it[USER_ID_KEY] = userId
            Log.d("TAG", "saveUserId: $userId")
        }
    }

    val getUserId: Flow<String?> = context.datastore.data.map {
     it[USER_ID_KEY].toString()
//        Log.d("TAG", "getUserId: ${userId.toString()}").toString()

    }

    suspend fun clearUserId() {
        context.datastore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
            Log.d("TAG", "clearUserId: User ID cleared")
        }
    }

    suspend fun saveIsUserApproved(isApproved: String) {
        context.datastore.edit {
            it[IS_USER_APPROVED] = isApproved
            Log.d("TAG", "saveUserId: $isApproved")
        }
    }

    val getIsUserApproved: Flow<String?> = context.datastore.data.map {
        it[IS_USER_APPROVED].toString()
//        Log.d("TAG", "getUserId: ${userId.toString()}").toString()
    }


}