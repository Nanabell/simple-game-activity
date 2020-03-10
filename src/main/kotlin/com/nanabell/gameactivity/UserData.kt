package com.nanabell.gameactivity

data class UserData(val id: Long) {

    private val games: MutableList<String> = mutableListOf()

    fun getGame(game: String): Int? {
        return games.count { it == game }
    }

    fun getGames(): List<String> {
        return games
    }

    fun addGame(game: String) {
        games.add(game)
    }
}