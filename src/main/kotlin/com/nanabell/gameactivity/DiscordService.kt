package com.nanabell.gameactivity

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.user.UserActivityStartEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class DiscordService(token: String) : ListenerAdapter() {

    private val jda: JDA = JDABuilder(token).addEventListeners(this).build()
    private val users: MutableList<UserData> = mutableListOf()

    init {
        jda.awaitReady()
    }

    override fun onUserActivityStart(event: UserActivityStartEvent) {
        if (event.newActivity.type == Activity.ActivityType.DEFAULT) {
            val user = getUser(event.user)
            user.addGame(event.newActivity.name)
        }
    }

    fun getUser(user: User): UserData {
        if (users.none { it.id == user.idLong }) {
            users.add(UserData(user.idLong))
        }

        return users.first { it.id == user.idLong }
    }

    fun getAll(): List<UserData> {
        return users
    }
}