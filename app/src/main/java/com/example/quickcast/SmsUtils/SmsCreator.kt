package com.example.quickcast.SmsUtils

import android.util.Log
import com.example.quickcast.data_classes.MessageProperties
import com.example.quickcast.data_classes.SelectedContacts
import com.example.quickcast.data_classes.SmsFormats.CreateTask
import com.example.quickcast.data_classes.SmsFormats.InvitationResponse
import com.example.quickcast.data_classes.SmsFormats.SendableMessageProperty
import com.example.quickcast.data_classes.SmsFormats.SiteInvite
import com.example.quickcast.data_classes.SmsFormats.SmsPackage
import com.example.quickcast.enum_classes.SmsTypes
import com.example.quickcast.repositories.DatabaseRepository
import com.example.quickcast.room_db.entities.Site
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.Calendar

class SmsCreator(
    private val databaseRepository: DatabaseRepository
) {

    /**
     * [createSmsPackageWithNumber] creates a list<[SmsPackage] from [_selectedContacts]
     * */
    suspend fun createSmsPackageWithNumber(selectedContacts : List<SelectedContacts>, siteName : String, joinedMember : List<String>?) : List<Pair<String, SmsPackage>>{

        val smsList = mutableListOf<Pair<String, SmsPackage>>()
        val ts = Calendar.getInstance().timeInMillis

        databaseRepository.addSite(
            SiteInvite(
                ts = ts,
                n = siteName,
                l = joinedMember
            ),
            false
        )

        selectedContacts.forEach {
            if(it.contact != null){
                smsList.add(
                    Pair(
                        it.contact.number,
                        SmsPackage(
                            type = SmsTypes.SITE_INVITE,
                            message = SiteInvite(
                                ts = ts,
                                n = siteName,
                                l = joinedMember
                            )
                        )
                    )
                )
            }
        }

        return smsList
    }

    suspend fun createSmsPackageWithNumber(b : Boolean, phoneNumber : String, siteInvite: SiteInvite) : List<Pair<String, SmsPackage>>{

        val invitationResponse = InvitationResponse(b, siteInvite.ts)
        val smsPackage = SmsPackage(SmsTypes.INVITATION_RESPONSE,invitationResponse)

        databaseRepository.addSite(siteInvite, false)

        return listOf(Pair(phoneNumber, smsPackage))
    }

    suspend fun createSmsPackageWithNumber(messageProperties : List<MessageProperties>, site : Site) : List<Pair<String, SmsPackage>>{

        val smsList = mutableListOf<Pair<String, SmsPackage>>()

        val sendableMessagePropertyList = mutableListOf<SendableMessageProperty>()

        messageProperties.forEach {
            sendableMessagePropertyList.add(SendableMessageProperty(
                k = it.name,
                v = it.value!!,
                t = it.type
            ))
        }

        val format = Gson().toJson(sendableMessagePropertyList)

        databaseRepository.messagePropertyAddition(
            siteId = site.id,
            format = format,
            phoneNumber = "SELF"
        )


        site.contactsList.forEach {
            Log.d("Contacts List", it.removePrefix("+91"))
            smsList.add(
                Pair(
                    it.removePrefix("+91"),
                    SmsPackage(
                        type = SmsTypes.CREATE_TASK,
                        message = CreateTask(site.id, sendableMessagePropertyList)
                    )
                )
            )
        }

        return smsList
    }

}