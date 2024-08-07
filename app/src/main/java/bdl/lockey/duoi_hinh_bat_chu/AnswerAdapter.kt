package bdl.lockey.duoi_hinh_bat_chu

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bdl.lockey.duoi_hinh_bat_chu.MainActivity.Companion.answerList

class AnswerAdapter(
    private val dataset: List<String>,
//    private val iClickItemListener: IClickItemListener
) : RecyclerView.Adapter<AnswerAdapter.ItemViewHolder>() {
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivHolder: ImageView = view.findViewById(R.id.imageView_holder)
        val tvLetter: TextView = view.findViewById(R.id.tv_letter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_letter_answer, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.tvLetter.text = item

//        holder.ivHolder.setOnClickListener {
//            iClickItemListener.onClickItem(position)
//            if (!holder.tvLetter.text.isNullOrEmpty()) {
//                for (i in answerList.indices) {
//                    if (answerList[i] == "") {
//                        answerList[i] = item
//                        holder.tvLetter.text = ""
//                        Log.d("answer", answerList.toString())
//                        break
//                    }
//                }
//            }
//        }
    }

}