package com.sipe.week3

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CoRoutineExample2 {
}

fun main() {
    runBlocking {
        val lineUp = launch {
            coroutineLinedUp()
        }

        val playMusicWithLinedUp = launch {
            coroutinePlayMusic()
        }

        lineUp.join()
        playMusicWithLinedUp.cancel()
        coroutineTicketing()

        val waitingBus = launch {
            coroutineWaitingTheBus()
        }

        val playMusicWithWaitingBus = launch {
            coroutinePlayMusic()
        }

        waitingBus.join()
        playMusicWithWaitingBus.cancel()
        coroutineTakeTheBus()
    }
}

suspend fun coroutineLinedUp() {
    println("lined up")
    delay(2000)
}

fun coroutineTicketing() {
    println("ticketing")
}

suspend fun coroutineWaitingTheBus() {
    println("waiting the bus")
    delay(2000)
}

fun coroutineTakeTheBus() {
    println("take the bus")
}

suspend fun coroutinePlayMusic() {
    println("play music")
    while(true) {
        println("listening..")
        delay(500)
    }
}