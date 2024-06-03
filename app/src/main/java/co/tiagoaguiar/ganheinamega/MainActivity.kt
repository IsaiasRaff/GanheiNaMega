package co.tiagoaguiar.ganheinamega

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import java.io.File
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    private lateinit var txtResultOnly: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText: EditText = findViewById(R.id.edit_number)
        val txtResult: TextView = findViewById(R.id.txt_result)
        val btnGenerate: Button = findViewById(R.id.btn_generate)

        prefs = getSharedPreferences(
            "database",
            Context.MODE_PRIVATE
        )
        val result = prefs.getString("result", null)
        result?.let {
            txtResult.text = "Última aposta: $it"
        }

        btnGenerate.setOnClickListener {
            val text = editText.text.toString()
            numberGenerator(text, txtResult)
        }
        val btnOnly: Button = findViewById(R.id.btn_gen_only)
        txtResultOnly = findViewById(R.id.txt_result_only)
        btnOnly.setOnClickListener {
            generateAndSaveNumber()
        }
    }

    private fun generateAndSaveNumber() {
        val randomNumber = Random.nextInt(1, 61)

        txtResultOnly.text = randomNumber.toString()

        val file = File(filesDir, "txtResultOnly.txt")

        file.writeText(randomNumber.toString())
    }

    private fun numberGenerator(text: String, txtResult: TextView) {
        if (text.isEmpty()) {
            Toast.makeText(this, "Informe um número entre 6 e 15", Toast.LENGTH_LONG).show()
            return
        }
        val qntd = text.toInt()
        if (qntd < 6 || qntd > 15) {
            Toast.makeText(this, "Informe um número entre 6 e 15", Toast.LENGTH_LONG).show()
            return
        }
        val numbers = mutableSetOf<Int>()
        val random = java.util.Random()

        while (numbers.size < qntd) {
            val number = random.nextInt(60)
            numbers.add(number + 1)

            if (numbers.size == qntd) {
                break
            }
        }
        val sortedNumbers = numbers.sorted()
        txtResult.text = sortedNumbers.joinToString(" - ")

        val editor = prefs.edit()
        editor.putString("result", txtResult.text.toString())
        editor.apply()
    }
}