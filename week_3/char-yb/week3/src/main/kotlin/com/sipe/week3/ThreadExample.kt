package com.sipe.week3

class ThreadExample {
}

fun main() {
    asyncLinedUp {
        stopMusic()
        ticketing()
        asyncTakeTheBus {
            stopMusic()
        }
        asyncPlayMusic()
    }

    asyncPlayMusic()

}

fun asyncLinedUp(myTurn: () -> Unit) {
    Thread {
        println("lined up")
        Thread.sleep(2000)
        myTurn.invoke()
    }.start()
}

fun asyncTakeTheBus(onTime: () -> Unit) {
    Thread {
        println("waiting the bus")
        Thread.sleep(2000)
        onTime.invoke()
        println("take the bus")
    }.start()
}

var playingMusic = false

fun asyncPlayMusic() {
    Thread {
        println("play music")
        playingMusic = true
        while(playingMusic) {
            println("listening..")
            Thread.sleep(500)
        }
    }.start()
}

fun stopMusic() {
    playingMusic = false
    println("stop music")
}