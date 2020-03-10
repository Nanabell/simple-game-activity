package com.nanabell.gameactivity

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class Commands(private val service: DiscordService) : ListenerAdapter() {

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (event.author.isBot || event.author.isFake)
            return

        if (!event.channel.canTalk())
            return

        val raw = event.message.contentRaw
        if (raw.toLowerCase().startsWith("--games")) {
            val arguments = raw.split(" ")
            if (arguments.size != 2) {
                event.channel.sendMessage("Argument Count mismatch!").queue()
                return
            }

            val game = arguments[1]
            val user = service.getUser(event.author)
            val count = user.getGame(game)

            event.channel.sendMessage("${event.author.asTag} has played $game $count times.")
        } else if (raw.toLowerCase().startsWith("--leaderboard")) {

            var message = "Top 10 Games played all time:\n"

            val games = service.getAll().flatMap { user -> user.getGames() }.groupingBy { it }.eachCount()
            games.toList().sortedBy { (_, value) -> value }.take(10).forEachIndexed { index, (game, count) ->
                run {
                    message += "$index: $game - $count"
                }

                event.channel.sendMessage(message).queue()
            }
        } else if (raw.toLowerCase() == "--stop") {
            Application.run = false
        }
    }
}