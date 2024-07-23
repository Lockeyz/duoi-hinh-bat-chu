package bdl.lockey.duoi_hinh_bat_chu

import android.annotation.SuppressLint
import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import bdl.lockey.duoi_hinh_bat_chu.const.Layout
import bdl.lockey.duoi_hinh_bat_chu.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity(), IClickItemListener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var questionAdapter: LetterAdapter
    private lateinit var answerAdapter: AnswerAdapter


    private val viewModel: GameViewModel by viewModels()

    companion object {
        var answerList = mutableListOf<String>()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        answerList = MutableList(viewModel.answerLength.value!!) { "" }

        answerAdapter = AnswerAdapter(answerList)
        questionAdapter = LetterAdapter(viewModel.questionLetterList.value!!, this)


        binding.ivQuestion.setImageResource(viewModel.currentImage.value!!)
        binding.recyclerViewAnswer.adapter = answerAdapter
        answerAdapter.notifyDataSetChanged()
        binding.recyclerViewCharacterHint.adapter = questionAdapter


//        binding.submit.setOnClickListener { onSubmitWord() }
        binding.layoutNext.setOnClickListener { onNextQuestion() }
        if (viewModel.isGameOver()) {
            showFinalScoreDialog()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickItem(position: Int) {
        viewModel.getLetter(position)
        answerAdapter.notifyDataSetChanged()
        questionAdapter.notifyDataSetChanged()
        Log.d("MainAnswer", answerList.toString())
        Log.d("MainAnswer", viewModel.questionLetterList.value.toString())
        Log.d("MainAnswer", answerList.joinToString(separator = "") { it })
        Log.d("MainAnswer", answerList.joinToString(separator = "") { it }.length.toString())
        Log.d("MainAnswer", viewModel.answerList.value?.toString().toString())
        Log.d("MainAnswer", viewModel.answerList.value?.size.toString())

        if (answerList.joinToString(separator = "") { it }.length == viewModel.answerList.value?.size) {
            if (answerList == viewModel.answerList.value) {
                setResult(true)
            } else {
                setResult(false)
            }
        }
    }


//    private fun onSubmitWord() {
//        val playerWord = binding.textInputEditText.text.toString()
//
//        if (viewModel.isUserWordCorrect(playerWord)) {
//            setWrongAnswer(false)
//            if (!viewModel.nextWord()) {
//                showFinalScoreDialog()
//            }
//        } else {
//            setWrongAnswer(true)
//        }
//    }


    /*
     * Skips the current word without changing the score.
     * Increases the word count.
     * After the last word, the user is shown a Dialog with the final score.
     */
    private fun onNextQuestion() {
        if (viewModel.nextQuestion()) {
            setWrongAnswer(false)
        } else {
            showFinalScoreDialog()
        }
    }


    // Kết thúc trò chơi, hiển thị thông báo điểm, có chơi tiếp hay không?
    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score.value))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame()
            }
            .show()
    }


    //Khởi tạo và cập nhật dữ liệu mới khi chơi lại
    private fun restartGame() {
        viewModel.reinitializeData()
        setWrongAnswer(false)
    }

    // Thoát game
    private fun exitGame() {
        finish()
    }

    // Thiết lập khi đáp án sai
    private fun setResult(error: Boolean) {
        if (error) {
            binding.recyclerViewAnswer.adapter = ResultAdapter(Layout.CORRECT, answerList)
        } else {
            binding.recyclerViewAnswer.adapter = ResultAdapter(Layout.WRONG, answerList)
        }
    }

    // Thiết lập khi đáp án sai
    private fun setWrongAnswer(error: Boolean) {
//        if (error) {
//            binding.textField.isErrorEnabled = true
//            binding.textField.error = getString(R.string.try_again)
//        } else {
//            binding.textField.isErrorEnabled = false
//            binding.textInputEditText.text = null
//        }
    }


}
