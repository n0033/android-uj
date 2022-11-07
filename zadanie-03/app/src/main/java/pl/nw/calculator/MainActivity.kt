package pl.nw.calculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.objecthunter.exp4j.ExpressionBuilder
import pl.nw.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var expression: String = ""
    private val operators = arrayOf("-", "+", "/", "*", "log", "%", "^")


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) //initializing the binding class
        setContentView(binding.root)

        // numbers
        binding.numberZero.setOnClickListener{
            if (getTrailingOperator(expression) != null) { clearResult() }
            appendToExpression("0")
            appendToResult("0")
        }
        binding.numberOne.setOnClickListener{
            if (getTrailingOperator(expression) != null) { clearResult() }
            appendToExpression("1")
            appendToResult("1")
        }
        binding.numberTwo.setOnClickListener{
            if (getTrailingOperator(expression) != null) { clearResult() }
            appendToExpression("2")
            appendToResult("2")
        }
        binding.numberThree.setOnClickListener{
            if (getTrailingOperator(expression) != null) { clearResult() }
            appendToExpression("3")
            appendToResult("3")
        }
        binding.numberFour.setOnClickListener{
            if (getTrailingOperator(expression) != null) { clearResult() }
            appendToExpression("4")
            appendToResult("4")
        }
        binding.numberFive.setOnClickListener{
            if (getTrailingOperator(expression) != null) { clearResult() }
            appendToExpression("5")
            appendToResult("5")
        }
        binding.numberSix.setOnClickListener{
            if (getTrailingOperator(expression) != null) { clearResult() }
            appendToExpression("6")
            appendToResult("6")
        }
        binding.numberSeven.setOnClickListener{
            if (getTrailingOperator(expression) != null) { clearResult() }
            appendToExpression("7")
            appendToResult("7")
        }
        binding.numberEight.setOnClickListener{
            if (getTrailingOperator(expression) != null) { clearResult() }
            appendToExpression("8")
            appendToResult("8")
        }
        binding.numberNine.setOnClickListener{
            if (getTrailingOperator(expression) != null) { clearResult() }
            appendToExpression("9")
            appendToResult("9")
        }
        binding.mantissa.setOnClickListener{
            val operator = getTrailingOperator(expression)
            if (operator != null) return@setOnClickListener
            if (expression.isEmpty()) return@setOnClickListener
            if (binding.result.text.contains('.')) return@setOnClickListener
            appendToExpression(".")
            appendToResult(".")
        }

        // operators
        binding.addition.setOnClickListener {
            if (expression.isEmpty() || getTrailingOperator(expression) != null){
                return@setOnClickListener
            }
            if (getOperator(expression) != null) {
                clearResult()
                val result = evaluateExpression(expression)
                setExpression("$result+")
                setResult(result)
            }
            else
            {
                appendToExpression("+")
            }
        }

        binding.subtraction.setOnClickListener {
            if (expression.isEmpty() || getTrailingOperator(expression) != null){
                return@setOnClickListener
            }
            if (getOperator(expression) != null) {
                clearResult()
                val result = evaluateExpression(expression)
                setExpression("$result-")
                setResult(result)
            }
            else
            {
                appendToExpression("-")
            }
        }

        binding.multiplication.setOnClickListener {
            if (expression.isEmpty() || getTrailingOperator(expression) != null){
                return@setOnClickListener
            }
            if (getOperator(expression) != null) {
                clearResult()
                val result = evaluateExpression(expression)
                setExpression("$result*")
                setResult(result)
            }
            else
            {
                appendToExpression("*")
            }
        }


        binding.division.setOnClickListener {
            if (expression.isEmpty() || getTrailingOperator(expression) != null){
                return@setOnClickListener
            }
            if (getOperator(expression) != null) {
                clearResult()
                val result = evaluateExpression(expression)
                setExpression("$result/")
                setResult(result)
            }
            else
            {
                appendToExpression("/")
            }
        }


        binding.power.setOnClickListener {
            if (expression.isEmpty() || getTrailingOperator(expression) != null){
                return@setOnClickListener
            }
            if (getOperator(expression) == null) {
                clearResult()
                val result = evaluateExpression(expression)
                setExpression("$result^")
                setResult(result)
            }
            else
            {
                appendToExpression("^")
            }
        }

        binding.log.setOnClickListener {
            if (expression.isEmpty() || getTrailingOperator(expression) != null) {
                return@setOnClickListener
            }
            expression = "log{$expression)"
            val result = evaluateExpression(expression)
            setExpression(result)
            setResult(result)
        }

        binding.negation.setOnClickListener {
            if (expression.isEmpty() ||
                getTrailingOperator(expression) != null) {
                return@setOnClickListener
            }
            val operator = getOperator(expression)
            if (operator == null) {
                expression = "-$expression"
                binding.result.text = "-" + binding.result.text.toString()
            }
            else if (expression.startsWith("-")){
                expression = expression.drop(1)
                binding.result.text = binding.result.text.toString().drop(1)
            }
            else {
                val expressionSplitted = expression.split(*operators)
                expression = "${expressionSplitted[0]}$operator-${expressionSplitted[1]}"
                binding.result.text = "-" + binding.result.text.toString()
            }
        }

        binding.percent.setOnClickListener {
            if (expression.isEmpty() || getTrailingOperator(expression) != null) {
                return@setOnClickListener
            }
            val operator = getOperator(expression)
            if (operator != null) {
                val expressionSplitted = expression.split(*operators)
                expression = "${expressionSplitted[0]}$operator" +
                        "(${expressionSplitted[1]}/100*${expressionSplitted[0]})"
                val result = evaluateExpression("${expressionSplitted[1]}/100*" +
                        expressionSplitted[0]
                )
                setResult(result)
            }
            else {
                expression += "/100"
                val result = evaluateExpression(expression)
                setExpression(result)
                setResult(result)
            }
        }


        // others
        binding.equals.setOnClickListener{
            val operator = getTrailingOperator(expression)
            if (operator != null) {
                setExpression(expression.split(*operators)[0])
            }
            if (expression.isEmpty()) return@setOnClickListener
            val result = evaluateExpression(expression)
            setExpression(result)
            setResult(result)
        }

        binding.clear.setOnClickListener{
            val operator = getOperator(expression)
            if (operator != null) {
                val expressionSplitted = expression.split(*operators)
                expression = "${expressionSplitted[0]}$operator"
            }
            clearResult()
        }

        binding.reset.setOnClickListener{
            clearExpression()
            clearResult()
        }
    }

    private fun getTrailingOperator(string: String): String? {
        for (operator in operators) {
            if (string.endsWith(operator)){
                return operator
            }
        }
        return null
    }

    private fun getOperator(string: String): String? {
        for (operator in operators) {
            if (string.contains(operator)){
                return operator
            }
        }
        return null
    }

    private fun evaluateExpression(expression: String): String {
        val builder = ExpressionBuilder(expression).build()
        try{
            val expressionResult = builder.evaluate()
            if (expressionResult == expressionResult.toInt().toDouble()){
                return expressionResult.toInt().toString()
            }
            return expressionResult.toString()
        } catch (e: ArithmeticException) {
            return "0"
        }
    }

    private fun setResult(string: String) {
       binding.result.text = string
    }

    private fun setExpression(string: String){
        expression = string
    }

    private fun clearExpression() {
        expression = ""
    }

    private fun clearResult() {
        binding.result.text = ""
    }

    private fun appendToExpression(symbol: String) {
        expression += symbol
    }

    private fun appendToResult(symbol: String) {
        binding.result.append(symbol)
    }

}