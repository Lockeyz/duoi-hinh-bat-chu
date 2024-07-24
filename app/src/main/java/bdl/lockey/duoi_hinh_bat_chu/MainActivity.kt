package bdl.lockey.duoi_hinh_bat_chu

import android.annotation.SuppressLint
import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.util.Log
import android.view.View
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

        viewModel.currentImage.observe(this) {
            binding.ivQuestion.setImageResource(viewModel.currentImage.value!!)
        }

        viewModel.answerLength.observe(this) {
            answerList = MutableList(viewModel.answerLength.value!!) { "" }
            answerAdapter = AnswerAdapter(answerList)
            binding.recyclerViewAnswer.adapter = answerAdapter
        }

        viewModel.questionLetterList.observe(this) {
            questionAdapter = LetterAdapter(viewModel.questionLetterList.value!!, this)
            binding.recyclerViewCharacterHint.adapter = questionAdapter
        }

        viewModel.currentLife.observe(this) {
            binding.tvLife.text = viewModel.currentLife.value.toString()
        }

        viewModel.score.observe(this) {
            binding.tvScore.text = viewModel.score.value.toString()
        }

        binding.layoutNext.setOnClickListener {
            onNextQuestion()
            Log.d("MainAnswer", viewModel.questionLetterList.value.toString())
        }
        if (viewModel.isGameOver()) {
            showFinalScoreDialog()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickItem(position: Int) {
        viewModel.getLetter(position)
        answerAdapter.notifyDataSetChanged()
        questionAdapter.notifyDataSetChanged()
//        Log.d("MainAnswer", answerList.toString())
//        Log.d("MainAnswer", viewModel.questionLetterList.value.toString())
//        Log.d("MainAnswer", answerList.joinToString(separator = "") { it })
//        Log.d("MainAnswer", answerList.joinToString(separator = "") { it }.length.toString())
//        Log.d("MainAnswer", viewModel.answerList.value?.toString().toString())
//        Log.d("MainAnswer", viewModel.answerList.value?.size.toString())

        onSubmitWord()
    }


    private fun onSubmitWord() {
        if (answerList.joinToString(separator = "") { it }.length == viewModel.answerList.value?.size){
            if (viewModel.isUserWordCorrect(answerList)) {
                setResult(true)
                binding.layoutNext.visibility = View.VISIBLE
//                if (!viewModel.nextQuestion()) {
//                    showFinalScoreDialog()
//                }
            } else {
                setResult(false)
                binding.layoutNext.visibility = View.VISIBLE
            }
        }
    }


    /*
     * Skips the current word without changing the score.
     * Increases the word count.
     * After the last word, the user is shown a Dialog with the final score.
     */
    private fun onNextQuestion() {
        if (viewModel.nextQuestion()) {
            setWrongAnswer(false)
            binding.layoutNext.visibility = View.GONE
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
        setResult(false)
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
