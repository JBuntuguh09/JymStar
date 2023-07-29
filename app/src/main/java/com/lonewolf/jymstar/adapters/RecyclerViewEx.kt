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
import com.lonewolf.jymstar.fragments.ExerciseDetails
import com.lonewolf.jymstar.resources.ShortCut_To
import com.lonewolf.jymstar.resources.Storage

class RecyclerViewEx(context: Context, arrayList: ArrayList<HashMap<String, String>>) : RecyclerView.Adapter<RecyclerViewEx.MyHolder>() {

    private var arrayList = ArrayList<HashMap<String, String>>()
    private var context : Context
    private var storage : Storage

    init {
        this.arrayList =arrayList
        this.context = context
        this.storage = Storage(context)
    }


    inner class MyHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        val count : TextView
        val rep : TextView
        val time : TextView
        val card : CardView
        val pic : ImageView
        val name : TextView



        init {
            count = itemview.findViewById(R.id.txtCount)
            rep = itemview.findViewById(R.id.txtReps)
            time = itemview.findViewById(R.id.txtTime)
            card = itemview.findViewById(R.id.cardMain)
            pic = itemview.findViewById(R.id.imgPic)
            name = itemview.findViewById(R.id.txtName)


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewEx.MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_exercises,parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewEx.MyHolder, position: Int) {
        val hash = arrayList[position]

        if(!hash["Rep"].equals("null")){
            holder.rep.text = "Reps : ${hash["Rep"]}"
        }

        if(!hash["Count"].equals("null")){
            holder.count.text = "Sets : ${hash["Count"]}"
        }

        if(!hash["Time"].equals("null")){
            holder.time.text = "Rest : ${hash["Time"]}"
        }
        holder.name.text = hash["Name"]

        Glide.with(context).load(hash["Url"]).into(holder.pic)

        holder.card.setOnClickListener {
            (context as MainActivity).selectedExArr = hash
            (context as MainActivity).navTo(ExerciseDetails(), "Details", storage.currPage!!, 1)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}