package com.example.quickcast.room_db.background_workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.quickcast.repositories.DatabaseRepository

class InvitationResponseBgWorker(
    appContext : Context,
    params : WorkerParameters,
    private val dbRepo : DatabaseRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {

        val siteId = inputData.getLong("site_Id", 0L)
        val response = inputData.getBoolean("response", false)
        val phoneNumber = inputData.getString("phone_Number") ?: return Result.failure()
        val contentString = inputData.getString("content_String") ?: return Result.failure()

        if(siteId == 0L){
            return Result.failure()
        }else{
            dbRepo.invitationResponse(
                siteId = siteId,
                b = response,
                phoneNumber = phoneNumber,
                contentString = contentString
            )
            return  Result.success()
        }

    }
}