package com.example.quickcast.SmsUtils

import com.example.quickcast.data_classes.SelectedContacts
import com.example.quickcast.data_classes.SmsFormats.InvitationResponse
import com.example.quickcast.data_classes.SmsFormats.SiteInvite
import com.example.quickcast.data_classes.SmsFormats.SmsPackage
import com.example.quickcast.enum_classes.SmsTypes
import java.util.Calendar
import java.util.SimpleTimeZone

class SmsCreator {

    /**
     * [createSmsPackageWithNumber] creates a list<[SmsPackage] from [_selectedContacts]
     * */
    fun createSmsPackageWithNumber(selectedContacts : List<SelectedContacts>, siteName : String) : List<Pair<String, SmsPackage>>{

        val smsList = mutableListOf<Pair<String, SmsPackage>>()

        selectedContacts.forEach {
            if(it.contact != null){
                smsList.add(
                    Pair(
                        it.contact.number,
                        SmsPackage(
                            type = SmsTypes.SITE_INVITE,
                            message = SiteInvite(
                                ts = Calendar.getInstance().timeInMillis,
                                n = siteName,
                                l = null
                            )
                        )
                    )
                )
            }
        }

        return smsList
    }

    fun createSmsPackageWithNumber(b : Boolean, phoneNumber : String, siteInvite: SiteInvite) : List<Pair<String, SmsPackage>>{

        val invitationResponse = InvitationResponse(b, siteInvite.ts)
        val smsPackage = SmsPackage(SmsTypes.INVITATION_RESPONSE,invitationResponse)

        return listOf(Pair(phoneNumber, smsPackage))
    }

    /*fun createSmsPackageWithNumber(messageProperties : List<MessageProperties>, site : Site) : List<Pair<String, SmsPackage>>{

        val smsList = mutableListOf<Pair<String, SmsPackage>>()

        val sendableMessagePropertyList = mutableListOf<SendableMessageProperty>()

        messageProperties.forEach {
            sendableMessagePropertyList.add(SendableMessageProperty(
                k = it.name,
                v = it.value!!,
                t = it.type
            ))
        }

        site.contactsList.forEach {
            smsList.add(
                Pair(
                    it,
                    SmsPackage(
                        type = SmsTypes.CREATE_TASK,
                        message = CreateTask(sendableMessagePropertyList)
                    )
                )
            )
        }

        return smsList
    }*/

}