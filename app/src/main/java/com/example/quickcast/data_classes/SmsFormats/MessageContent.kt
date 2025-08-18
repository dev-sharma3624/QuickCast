package com.example.quickcast.data_classes.SmsFormats

import android.os.Parcelable
import com.example.quickcast.enum_classes.MessagePropertyTypes
import kotlinx.parcelize.Parcelize


/**
 * [MessageContent] works as the base class for all message types.
 * */

@Parcelize
sealed class MessageContent : Parcelable


/**
 * [SiteInvite] : data class representing the object that will be sent
 * for sending a invitation to someone to join a preexisting or new site/group.
 *
 * @param n represent site name
 * @param l represents the list of contacts that exist in a site/group.
 * It will be null if the site/group is new.
 * */

@Parcelize
data class SiteInvite (
    val n : String, //site name
    val l : List<String>?, //list of all other contacts
    val ts : Long
) : MessageContent()

/**
 * [InvitationResponse] : data class representing the object that will be sent as a response to an invitation for joining a site/group.
 *
 * @param b represents the accept/reject response.
 * @param id the id of the site for which the response has come.
 * */

@Parcelize
data class InvitationResponse(
    val b : Boolean,
    val id : Long,
) : MessageContent()


@Parcelize
data class CreateTask(
    val siteId : Long,
    val taskName : String,
    val l : List<SendableMessageProperty>
) : MessageContent()

@Parcelize
data class SendableMessageProperty(
    val k : String,
    val v : Int,
    val t : MessagePropertyTypes
): Parcelable


@Parcelize
data class TaskUpdate(
    val fId : Long,
    val taskName : String,
    val l : List<SendableMessageProperty>
) : MessageContent()
