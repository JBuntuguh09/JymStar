package com.lonewolf.jymstar.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lonewolf.jymstar.R
import com.lonewolf.jymstar.databinding.FragmentCustomExerciseBinding
import com.lonewolf.jymstar.databinding.LayoutSmallPartBinding
import com.lonewolf.jymstar.resources.ShortCut_To
import com.lonewolf.jymstar.resources.Storage

class RecyclerWorkout(
    context: Context,
    arrayList: ArrayList<HashMap<String, String>>,
    arrayListSelect: ArrayList<HashMap<String, String>>,
    binding: FragmentCustomExerciseBinding
) : RecyclerView.Adapter<RecyclerWorkout.MyHolder>() {
    private var arrayList = ArrayList<HashMap<String, String>>()
    private var arrayListSelect = ArrayList<HashMap<String, String>>()
    private var context : Context
    private var storage : Storage
    private var binding : FragmentCustomExerciseBinding


    init {
        this.arrayList =arrayList
        this.context = context
        this.storage = Storage(context)
        this.binding = binding
        this.arrayListSelect = arrayListSelect
    }

    inner class MyHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        val pic : ImageView
        val name : TextView
        val card : CardView


        init {
            pic = itemview.findViewById(R.id.imgPic)
            name = itemview.findViewById(R.id.txtPart)
            card = itemview.findViewById(R.id.cardMain)


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_parts,parent, false)

        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val hashMap = arrayList[position]
        holder.name.text = hashMap["Name"]

        Glide.with(context).load(hashMap["Url"]).into(holder.pic)

        holder.card.setOnClickListener {
            hashMap["Position"] = position.toString()

            setHori(hashMap)
            ShortCut_To.hideKeyboard(context as Activity)
        }
    }



    fun setHori(hashMap: HashMap<String, String>){
        val layoutInflater = LayoutInflater.from(context)
        arrayListSelect.add(hashMap)


        val view = layoutInflater.inflate(R.layout.layout_small_part, binding.linMain, false)
        val bind = LayoutSmallPartBinding.bind(view)
        Glide.with(context).load(hashMap["Url"]).into(bind.imgPic)
        bind.txtName.text = hashMap["Name"].toString()

        binding.linMain.addView( view)
        binding.horiMain.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
        if(arrayListSelect.size>0){
            binding.txtSelected.text = buildString {
        append(arrayListSelect.size)
        append(context.getString(R.string.exerciseselected))
    }
            binding.btnConfirm.visibility = View.VISIBLE
        }else{
            binding.btnConfirm.visibility = View.GONE
        }
    }

}