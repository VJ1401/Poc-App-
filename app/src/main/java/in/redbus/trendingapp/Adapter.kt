package `in`.redbus.trendingapp

import `in`.redbus.trendingapp.databinding.ViewListBinding
import `in`.redbus.trendingapp.model.Items
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import coil.load
import java.util.*
import kotlin.collections.ArrayList

class Adapter(private var listItem: ArrayList<Items>,
              private var callBack:AdapterCallBack,
              private var selectedItemPos:Int
) : RecyclerView.Adapter<Adapter.ViewHolder>(), Filterable {

    private var filterList = ArrayList<Items>()

    init {
        filterList = listItem
    }

    fun updateData(newData: ArrayList<Items>,selectedPos:Int) {
        this.listItem = newData
        this.selectedItemPos = selectedPos
        filterList = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_list, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filterList[holder.adapterPosition]
        if (item != null) {
            if(item.owner.avatar_url.isNotEmpty()){
                holder.binding.imageView.load(item.owner.avatar_url)
            } else {
                holder.binding.imageView.load(R.drawable.ic_defaultprofile)
            }
            holder.binding.tvName.text = item.name
        }
        if(selectedItemPos == position){
            holder.itemView.setBackgroundColor(Color.parseColor("#567845"));
            holder.binding.tvName.setTextColor(Color.parseColor("#ffffff"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.binding.tvName.setTextColor(Color.parseColor("#000000"));
        }
        holder.itemView.setOnClickListener {
            selectedItemPos = position;
            notifyDataSetChanged()
            callBack.onClick(position)
        }
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty() || charSearch.length < 3) {
                    filterList = listItem
                } else {
                    val resultList = ArrayList<Items>()
                    for (row in listItem) {
                        if (row.name.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    filterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filterList = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Items>
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ViewListBinding.bind(view)
    }



    interface AdapterCallBack {
        fun onClick(position: Int)
    }
}