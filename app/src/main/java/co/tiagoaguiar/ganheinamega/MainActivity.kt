package co.tiagoaguiar.ganheinamega

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

class MainActivity() : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    private lateinit var txtResult2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Pode ter mais telas

        // objetos via ID
        val editText: EditText = findViewById(R.id.edit_number)
        val txtResult: TextView = findViewById(R.id.txt_result)
        val btnGenerate: Button = findViewById(R.id.btn_generate)

        // banco de dados de preferências
        prefs = getSharedPreferences(
            "database",
            Context.MODE_PRIVATE
        ) // N é compartilhado em outros apps
        val result = prefs.getString("result", null)
        /*
        if (result != null){
            txtResult.text = "Última aposta: $result"
        }
         */
        result?.let {
            txtResult.text = "Última aposta: $it"
        }

        btnGenerate.setOnClickListener {
            val text = editText.text.toString()
            numberGenerator(text, txtResult)
        }
        val btn2: Button = findViewById(R.id.btn2)
        txtResult2 = findViewById(R.id.txt_result2)
        btn2.setOnClickListener {
            generateAndSaveNumber()
        }
    }

    private fun generateAndSaveNumber() {
        // Gera um número aleatório entre 1 e 60
        val randomNumber = Random.nextInt(1, 61)

        // Atualiza o TextView com o número gerado
        txtResult2.text = randomNumber.toString()

        // Obtém o caminho do arquivo
        val file = File(filesDir, "txtResult2.txt")

        // Escreve o número no arquivo
        file.writeText(randomNumber.toString())
    }

    private fun numberGenerator(text: String, txtResult: TextView) {
        // Validar o campo vazio
        if (text.isEmpty()) {
            Toast.makeText(this, "Informe um número entre 6 e 15", Toast.LENGTH_LONG).show()
            return
        }
        val qntd = text.toInt() // Converte string para inteiro
        if (qntd < 6 || qntd > 15) {
            // deu falha
            Toast.makeText(this, "Informe um número entre 6 e 15", Toast.LENGTH_LONG).show()
            return
        }
        // sucess
        val numbers = mutableSetOf<Int>()
        val random = java.util.Random()

        while (true) {
            val number = random.nextInt(60) // 0 a 59
            numbers.add(number + 1)

            if (numbers.size == qntd) {
                break
            }
        }
        txtResult.text = numbers.joinToString(" - ") // Gerar os numeros c/hífen

        val editor = prefs.edit()
        editor.putString("result", txtResult.text.toString())
        editor.apply()
    }
}