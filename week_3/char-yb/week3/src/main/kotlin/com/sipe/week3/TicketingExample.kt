package com.sipe.week3

class TicketingExample {
}

fun main() {
    linedUp()
    ticketing()
    takeTheBus()
}

fun linedUp() {
    println("lined up")
    Thread.sleep(2000)
}

fun ticketing() {
    println("ticketing")
}

fun takeTheBus() {
    println("waiting the bus")
    Thread.sleep(2000)
    println("take the bus")
}