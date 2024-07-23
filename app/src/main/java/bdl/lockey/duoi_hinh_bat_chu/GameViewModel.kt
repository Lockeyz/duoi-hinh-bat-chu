package bdl.lockey.duoi_hinh_bat_chu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private val _currentLife = MutableLiveData(0)
    val currentLife: LiveData<Int>
        get() = _currentLife

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _currentImage = MutableLiveData<Int>(0)
    val currentImage: LiveData<Int>
        get() = _currentImage

    private val _answerLength = MutableLiveData(0)
    val answerLength: LiveData<Int>
        get() = _answerLength

    private var _questionLetterList = MutableLiveData<MutableList<String>>()
    val questionLetterList: LiveData<MutableList<String>>
        get() = _questionLetterList

    private var _answerList = MutableLiveData<List<String>>()
    val answerList: LiveData<List<String>>
        get() = _answerList

    // List of questions to be used in the game
    private var questionsList: MutableList<Question> = mutableListOf()
    
    
    private lateinit var currentWord: String

    private var isGameOver: Boolean = false

    init {
        getNextQuestion()
    }

//    fun setAnswerList() {
//        _answerList.value = MainActivity.answerList
//    }

    fun getLetter(i: Int) {
        val list: MutableList<String>? = _questionLetterList.value
        list?.set(i, "")
        _questionLetterList.value = list
    }



    /*
     * Lấy câu hỏi tiếp theo
     */
    private fun getNextQuestion() {
        // Lấy ra câu hỏi ngẫu nhiên
        val currentQuestion = allQuestions.random()

        // Tạo ra danh sách chứa các kí tự của câu trả lời
        currentWord = currentQuestion.answer
        var wordList = currentWord.map { it.toString() }.toMutableList()

        // Thêm các kí tự ngẫu nhiên vào danh sách
        val alphabetList = listOf(
            "a", "b", "c", "d", "e", "g", "h", "i", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "x", "y"
        )
        val subLetters = alphabetList.shuffled().take(MAX_LETTER - currentWord.length)
        wordList.addAll(subLetters)
        wordList.shuffled()

        // Kiểm tra câu hỏi tiếp theo có trùng với những câu hỏi cũ hay không?
        // Nếu có thì lấy ra câu hỏi
        if (questionsList.contains(currentQuestion)) {
            getNextQuestion()
        } else {
            Log.d("Unscramble", "currentWord= $currentWord")
            _currentImage.value = currentQuestion.imageQuestion
            _answerList.value = currentWord.map { it.toString() }
            _answerLength.value = currentWord.length
            _questionLetterList.value = wordList
            questionsList.add(currentQuestion)
        }
    }

    /*
     * Re-initializes the game data to restart the game.
     */
    fun reinitializeData() {
        _score.value = 0
        _currentLife.value = MAX_NO_OF_LIVES
        questionsList.clear()
        getNextQuestion()
        isGameOver = false
    }

    // Tăng điểm
    private fun increaseScore() {
        _score.value = _score.value?.plus(SCORE_INCREASE)
    }

    // Kiểm tra câu trả lời đúng hay không?
    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        } else {
            _currentLife.value = _currentLife.value?.dec()
        }
        return false
    }

    /*
    * Returns true if the current word count is less than MAX_NO_OF_WORDS
    */
    fun nextQuestion(): Boolean {
        return if (_currentLife.value!! > 0) {
            getNextQuestion()
            true
        } else {
            isGameOver = true
            false
        }
    }

    fun isGameOver() = isGameOver
}
