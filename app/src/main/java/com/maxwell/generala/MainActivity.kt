package com.maxwell.generala

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
    private var turn: Int = 1 // 1 player, 2 pc
    private val maxScore: Int = 100
    private lateinit var tvDices: TextView
    private lateinit var tvScorePlayer: TextView
    private lateinit var tvScorePC: TextView
    private lateinit var tvGame: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDices = findViewById(R.id.tvDices)
        tvScorePlayer = findViewById(R.id.tvScorePlayer)
        tvScorePC = findViewById(R.id.tvScorePC)
        tvGame = findViewById(R.id.tvGame)

        findViewById<Button>(R.id.btRollDice).setOnClickListener { startTurn() }
    }

    private fun startTurn() {
        if(scorePlayer < maxScore && scorePC < maxScore) {
            val dices = rollDices()
            val result = calculateScore(dices)

            if(turn == 1) {
                tvDices.text = "Tus dados: " + dicesPrint(dices) + "\nJuego: " + result.game
                scorePlayer += result.score
                tvScorePlayer.text = getString(R.string.score_player, scorePlayer)
                turn = 2
            } else {
                tvDices.text = "Dados PC: " + dicesPrint(dices) + "\nJuego: " + result.game
                scorePC += result.score
                tvScorePC.text = getString(R.string.score_pc, scorePC)
                turn = 1
            }

            return
        }

        if(scorePlayer > scorePC) {
            fireWinnerDialog("Has ganado!")
        } else {
            fireWinnerDialog("Lo siento, PC ha ganado...")
        }
    }

    private fun resetGame() {
        scorePlayer = 0
        scorePC = 0
        turn = 1
        tvScorePlayer.text = getString(R.string.score_player, scorePlayer)
        tvScorePC.text = getString(R.string.score_pc, scorePC)
        tvDices.text = getString(R.string.press_roll_dice_button)
    }

    private fun fireWinnerDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("OK"
            ) { dialog, id ->
                dialog.dismiss()
                resetGame()
            }
        // Create the AlertDialog object and return it
        val dialog: AlertDialog = builder.create()

        dialog.show()
    }


    private fun calculateScore(dices: List<Int>): Result {
        if(isEscalera(dices)) {
            return Result("Escalera", 20)
        } else {
            val sameDices = dices.groupingBy { it }.eachCount().filter { it.value > 1 }
            var maxDicesEquals = 0
            var twoDicesEquals = false

            for (dice in sameDices) {
                if(dice.value > maxDicesEquals) {
                    maxDicesEquals = dice.value
                }

                if(dice.value == 2) {
                    twoDicesEquals = true
                }
            }

            return if(maxDicesEquals == 5) {
                Result("Generala", 50)
            } else if (maxDicesEquals == 4) {
                Result("Poker", 40)
            } else if (maxDicesEquals == 3 && twoDicesEquals) {
                Result("Full", 30)
            } else {
                var dicesScore = 0

                for (dice in dices) {
                    dicesScore += dice
                }

                Result("Dados", dicesScore)
            }
        }
    }

    // Por cada numero, se compara el anterior menos uno. Si son iguales, existe la posibilidad de una escalera.
    private fun isEscalera(dices: List<Int>): Boolean {
        var diceCounter = 0

        dices.forEachIndexed { i, dice ->
            // Para recorrer un valor anterior, necesito que el indice sea el siguiente
            if(i > 0) {
                val prevDice = dices[i - 1] - 1
                if(dices[i] == prevDice)
                    diceCounter++
            }
        }

        /*
        * Posibilidades:
        *   1 2 3 4 5
        *   2 3 4 5 6
        */
        return diceCounter == 5
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