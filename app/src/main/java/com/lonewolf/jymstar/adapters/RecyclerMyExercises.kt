package com.lonewolf.jymstar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.lonewolf.jymstar.MainActivity
import com.lonewolf.jymstar.R
import com.lonewolf.jymstar.fragments.VewExercise
import com.lonewolf.jymstar.resources.ShortCut_To
import com.lonewolf.jymstar.resources.Storage
import org.json.JSONArray

class RecyclerMyExercises(context: Context, arrayList: ArrayList<HashMap<String, String>>):RecyclerView.Adapter<RecyclerMyExercises.MyHolder>() {
    private var arrayList = ArrayList<HashMap<String, String>>()
    private var context : Context
    private var storage : Storage

    init {
        this.arrayList =arrayList
        this.context = context
        this.storage = Storage(context)
    }


    inner class MyHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        val name : TextView
         val code : TextView
         val date : TextView
         val card : CardView
         val share : ImageView

        init {
            code = itemview.findViewById(R.id.txtCode)
            name = itemview.findViewById(R.id.txtName)
            date = itemview.findViewById(R.id.txtDate)
            card = itemview.findViewById(R.id.cardMain)
            share = itemview.findViewById(R.id.imgShare)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerMyExercises.MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_myexercise,parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerMyExercises.MyHolder, position: Int) {
        val hashMap =arrayList[position]
        holder.name.text = hashMap["Name"]
        holder.date.text = hashMap["Date"]
        holder.code.text = hashMap["Code"]
        if(!hashMap["QR_Code"].equals("null")){
            val decode = ShortCut_To.decodeBase64(hashMap["QR_Code"])
            Glide.with(context).load(decode).into(holder.share)
        }


        holder.card.setOnClickListener {
            try {
                if(arrayList.size>0){
                    (context as MainActivity).selectedFireRoute = hashMap["Path"]!!
                    (context as MainActivity).selectedEx.clear()
                    (context as MainActivity).selectedExercise = hashMap
                    (context as MainActivity).navTo(VewExercise(), hashMap["Name"]!!, storage.currPage!!, 1)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }


        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}