package bdl.lockey.duoi_hinh_bat_chu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bdl.lockey.duoi_hinh_bat_chu.LetterAdapter.ItemViewHolder

class ResultAdapter(private val layout: Int, private val dataset: List<String>): RecyclerView.Adapter<ResultAdapter.ItemViewHolder>() {
    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val ivHolder: ImageView =  view.findViewById(R.id.imageView_holder)
        val tvLetter: TextView = view.findViewById(R.id.tv_letter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = when(layout){
            3 -> LayoutInflater.from(parent.context).inflate(R.layout.item_answer_correct, parent, false)
            4 -> LayoutInflater.from(parent.context).inflate(R.layout.item_answer_wrong, parent, false)
            else -> LayoutInflater.from(parent.context).inflate(R.layout.item_answer_wrong, parent, false)
        }
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.tvLetter.text = item
    }
}