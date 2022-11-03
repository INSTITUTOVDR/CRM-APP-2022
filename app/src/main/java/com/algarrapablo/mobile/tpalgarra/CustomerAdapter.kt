package com.algarrapablo.mobile.tpalgarra

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView


class CustomerAdapter(
    private var context: Context,
    var list: MutableList<Types.Type>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<CustomerAdapter.ReceiptHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReceiptHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_view, parent, false)


        return ReceiptHolder(itemView)
    }


    override fun onBindViewHolder(
        holder: ReceiptHolder,
        position: Int
    ) {
        val item = list[position]
        holder.name.text = item.name!!.toUpperCase()
        //http://institutosuperiorvilladelrosario.edu.ar/crm/ws/ImagenesEmpresas/202210271113,52640759eiB.png
        var urlimage=""

        Glide.with(context)
            .load(item.Image)
            .placeholder(R.drawable.pablo)
            //.diskCacheStrategy(DiskCacheStrategy.NONE)
            //.skipMemoryCache(true)

            .centerCrop()
            .into(holder.image)
    }


    override fun getItemCount() = list.size

    inner class ReceiptHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val name: TextView = itemView.findViewById(R.id.tvholamundo)
        val image:CircleImageView=itemView.findViewById(R.id.foto)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition

            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(
                    position,
                    list[position]
                )
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(
            position: Int,
            url: Types.Type
        )

    }


}

