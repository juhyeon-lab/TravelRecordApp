package com.example.travelrecordapp

import android.net.Uri
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TravelAdapter(
    private val travelList: MutableList<TravelRecord>,
    private val onItemClick: (TravelRecord) -> Unit,
    private val onEditClick: (TravelRecord) -> Unit,
    private val onDeleteClick: (TravelRecord) -> Unit
) : RecyclerView.Adapter<TravelAdapter.TravelViewHolder>() {

    class TravelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layoutTravelItem: View = itemView.findViewById(R.id.layoutTravelItem)
        val ivTravelPhoto: ImageView = itemView.findViewById(R.id.ivTravelPhoto)
        val tvPhotoLabel: TextView = itemView.findViewById(R.id.tvPhotoLabel)
        val tvItemPlace: TextView = itemView.findViewById(R.id.tvItemPlace)
        val tvItemDate: TextView = itemView.findViewById(R.id.tvItemDate)
        val tvItemMemo: TextView = itemView.findViewById(R.id.tvItemMemo)
        val tvItemAction: TextView = itemView.findViewById(R.id.tvItemAction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_travel_record, parent, false)

        return TravelViewHolder(view)
    }

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        val record = travelList[position]

        holder.tvItemPlace.text = record.place
        holder.tvItemDate.text = record.visitDate
        holder.tvItemMemo.text = record.memo.ifBlank {
            "작성된 메모가 없습니다."
        }
        holder.tvItemAction.text = "상세 보기"

        if (record.photoUri.isNotBlank()) {
            holder.ivTravelPhoto.visibility = View.VISIBLE
            holder.tvPhotoLabel.visibility = View.GONE
            holder.ivTravelPhoto.setImageURI(Uri.parse(record.photoUri))
        } else {
            holder.ivTravelPhoto.visibility = View.GONE
            holder.tvPhotoLabel.visibility = View.VISIBLE
            holder.tvPhotoLabel.text = "PHOTO"
        }

        holder.layoutTravelItem.setOnClickListener {
            onItemClick(record)
        }

        holder.layoutTravelItem.isLongClickable = true
        holder.layoutTravelItem.setOnCreateContextMenuListener { menu: ContextMenu, _, _ ->
            menu.setHeaderTitle(record.place)

            menu.add("수정").setOnMenuItemClickListener {
                onEditClick(record)
                true
            }

            menu.add("삭제").setOnMenuItemClickListener {
                onDeleteClick(record)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return travelList.size
    }

    fun updateList(newList: List<TravelRecord>) {
        travelList.clear()
        travelList.addAll(newList)
        notifyDataSetChanged()
    }
}