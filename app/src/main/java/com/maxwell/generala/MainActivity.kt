package com.maxwell.generala

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random

/*
    * Reglas del Juego:
    * Cada dado suma el punto de su valor
    * Escalera: 5 dados en orden consecutivo (20 puntos)
    * Full: 2 y 3 dados con el mismo numero. Ej: 11555 (30 puntos)
    * Poker: 4 dados iguales (40 puntos)
    * Generala: 5 dados iguales (50 puntos)
*/
class MainActivity : AppCompatActivity() {
    private var scorePlayer: Int = 0
    private var scorePC: Int = 0
    private val maxScore: Int = 100
    private lateinit var tvLogContainer: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvLogContainer = findViewById(R.id.tvLogContainer)

        findViewById<Button>(R.id.btRollDice).setOnClickListener { startTurn() }
    }

    private fun startTurn() {
        val dices = rollDices()
        tvLogContainer.text = dicesPrint(dices)
    }

    private fun dicesPrint(dices: List<Int>): String {
        var printDicesStr = ""

        for(dice in dices) {
            printDicesStr = printDicesStr.plus(dice).plus(" ")
        }

        return printDicesStr
    }

    private fun rollDices(): List<Int> {
        return List(5) { Random.nextInt(1, 6) }
    }
}