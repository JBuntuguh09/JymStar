package com.lonewolf.jymstar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lonewolf.jymstar.MainActivity
import com.lonewolf.jymstar.R
import com.lonewolf.jymstar.fragments.equipment.Details
import com.lonewolf.jymstar.resources.Storage
import com.lonewolf.jymstar.resources.ShortCut_To

class RecyclerExercise (context: Context, arrayList: ArrayList<HashMap<String, String>>)
    : RecyclerView.Adapter<RecyclerExercise.MyHolder>() {

    private var arrayList = ArrayList<HashMap<String, String>>()
    private var context : Context
    private var storage : Storage

    init {
        this.arrayList =arrayList
        this.context = context
        this.storage = Storage(context)
    }

    inner class MyHolder(itemview:View):RecyclerView.ViewHolder(itemview){
        val pic : ImageView
        val name : TextView
        val equipment : TextView
        val diff : TextView
        val card : CardView

        init {
            pic = itemview.findViewById(R.id.imgPic)
            name = itemview.findViewById(R.id.txtName)
            equipment = itemview.findViewById(R.id.txtEquip)
            diff = itemview.findViewById(R.id.txtDifficulty)
            card = itemview.findViewById(R.id.cardMain)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerExercise.MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_exercise,parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerExercise.MyHolder, position: Int) {
        val hashMap = arrayList[position]
        holder.name.text = hashMap["exercise_name"]
        holder.diff.text = hashMap["Difficulty"]
        holder.equipment.text = hashMap["Category"]

        Glide.with(context).load(ShortCut_To.getImageFromPart(hashMap["muscle"]!!)).into(holder.pic)

        holder.card.setOnClickListener {
            (context as MainActivity).selectedExercise = hashMap
            (context as MainActivity).selectedId = hashMap["exercise_name"]!!
            (context as MainActivity).navTo(Details(), "Details", storage.fragValPrev!!, 1)
        }

    }

    override fun getItemCount(): Int {
        return  arrayList.size
    }
}