package com.github.assertion.samples.http

import java.util.*

data class CardIssueRq(val id: String)

data class CardIssueRs(val id: String, val pan: String, val psn: Int, val rc: Int = 0)

data class ChangeCardStatusRq(val id: String, val status: String)

data class ChangeCardStatusRs(
    val id: String,
    val rc: Int = 0
)

data class CardInfoRq(val id: String, val pan: String, val psn: Int)

data class CardInfoRs(val id: String, val card: Card? = null, val rc: Int = 0)

data class Status(val status: String) {

    fun change(new: String): Status {
        return this.copy(status = new)
    }

}

data class Card(
    val id: String,
    val pan: String,
    val status: Status,
    val contractId: String,
    val psn: Int = 1
)

class CardService {
    private val panPool: List<Char> = ('0'..'9').toList()

    private val cards = mutableMapOf<String, Card>()
    private val idx = mutableMapOf<Pair<String, Int>, String>()

    fun issue(issueRq: CardIssueRq): CardIssueRs {
        val pan = (1..16)
            .map { kotlin.random.Random.nextInt(0, panPool.size) }
            .map(panPool::get)
            .joinToString("")
        val card = Card(issueRq.id, pan, Status("new"), UUID.randomUUID().toString())
        cards[issueRq.id] = card
        idx[Pair(pan, 1)] = issueRq.id
        return CardIssueRs(issueRq.id, card.pan, card.psn)
    }

    fun status(rq: ChangeCardStatusRq): ChangeCardStatusRs {
        val card = cards[rq.id] ?: return ChangeCardStatusRs(rq.id, rc = 96)
        cards[rq.id] = card.copy(status = card.status.change(rq.status))
        return ChangeCardStatusRs(rq.id)
    }

    fun info(rq: CardInfoRq): CardInfoRs {
        val id = idx[Pair(rq.pan, rq.psn)] ?: return CardInfoRs(rq.id, rc = 96)
        val card = cards[id]
        return if (card == null) {
            CardInfoRs(rq.id, rc = 96)
        } else {
            CardInfoRs(rq.id, card)
        }
    }

}