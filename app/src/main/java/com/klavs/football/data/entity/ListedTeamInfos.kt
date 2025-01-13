package com.klavs.football.data.entity

sealed class ListedTeamInfos(val id: Int, val name: String, val logoUrl: String) {
    data object Fenerbahce : ListedTeamInfos(
        id = 611,
        name = "Fenerbahçe",
        logoUrl = "https://media.api-sports.io/football/teams/611.png"
    )

    data object Galatasaray : ListedTeamInfos(
        id = 645,
        name = "Galatasaray",
        logoUrl = "https://media.api-sports.io/football/teams/645.png"
    )

    data object Besiktas : ListedTeamInfos(
        id = 549,
        name = "Beşiktaş",
        logoUrl = "https://media.api-sports.io/football/teams/549.png"
    )

    data object ParisSaintGermain : ListedTeamInfos(
        id = 85,
        name = "Paris Saint Germain",
        logoUrl = "https://media.api-sports.io/football/teams/85.png"
    )

    data object Barcelona : ListedTeamInfos(
        id = 529,
        name = "Barcelona",
        logoUrl = "https://media.api-sports.io/football/teams/529.png"
    )

    data object RealMadrid : ListedTeamInfos(
        id = 541,
        name = "Real Madrid",
        logoUrl = "https://media.api-sports.io/football/teams/541.png"
    )

    data object ManchesterUnited : ListedTeamInfos(
        id = 33,
        name = " Manchester United",
        logoUrl = "https://media.api-sports.io/football/teams/33.png"
    )

    data object Liverpool : ListedTeamInfos(
        id = 40,
        name = "Liverpool",
        logoUrl = "https://media.api-sports.io/football/teams/40.png"
    )

    data object ManchesterCity : ListedTeamInfos(
        id = 50,
        name = "Manchester City",
        logoUrl = "https://media.api-sports.io/football/teams/50.png"
    )

    companion object {
        val teams: List<ListedTeamInfos> by lazy {
            listOf(
                Fenerbahce,
                Galatasaray,
                Besiktas,
                ParisSaintGermain,
                Barcelona,
                RealMadrid,
                ManchesterUnited,
                Liverpool,
                ManchesterCity
            )
        }
    }
}