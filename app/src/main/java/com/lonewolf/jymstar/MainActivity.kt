package com.lonewolf.jymstar

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.lonewolf.jymstar.databinding.ActivityMainBinding
import com.lonewolf.jymstar.fragments.*
import com.lonewolf.jymstar.fragments.calculator.Calculators
import com.lonewolf.jymstar.fragments.equipment.Main
import com.lonewolf.jymstar.fragments.user.Profile
import com.lonewolf.jymstar.resources.Storage
import com.lonewolf.jymstar.resources.ShortCut_To
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val mDrawerList: ListView? = null
    private var mToggle: ActionBarDrawerToggle? = null

    //private TextView submit;
    private val mAdapter: ArrayAdapter<String>? = null
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    var toolbar: Toolbar? = null

    private lateinit var tabs : TabLayout

    private lateinit var storage: Storage
    var caseBack = 0
    var eventBack = 0
    var selectedEx = ArrayList<HashMap<String, String>>()
    var selectedExArr = HashMap<String, String>()
    var arrayListPersons = ArrayList<HashMap<String, String>>()
    var arrayListStaff = ArrayList<HashMap<String, String>>()
    var selectedExercise = HashMap<String, String>()
    // var arrayListCaseTypes = ArrayList<HashMap<String, String>>()
    var selectedCat = ""
    var selected = ""
    var selectedId = "0"
    var selectedFireRoute = ""
    var selectedPath  = ""
    lateinit var selectedImage : ImageView
    lateinit var selectedTxt : TextView
    var reloadBill = false
    var reloadSchedule = false

    private val PICK_FILE_REQUEST_CODE = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        storage = Storage(this)

        getButtons()
        navTo(StartPage(), "Body", "Register", 0)
    }


    private fun getButtons() {
        getDrawer()
    }

    private fun getDrawer() {
        toolbar = findViewById(R.id.tabSettings)
        navigationView = findViewById(R.id.navMain)
        drawerLayout = findViewById(R.id.drawLay)
        navigationView.setNavigationItemSelectedListener { item: MenuItem? -> false }
        mToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(mToggle!!)
        mToggle!!.syncState()
        //mToggle!!.setHomeAsUpIndicator(R.drawable.pencil_outline)
        mToggle!!.drawerArrowDrawable.color = resources.getColor(R.color.white, null);
       //
//        setSupportActionBar(toolbar)
//        supportActionBar?.title = ""
//        supportActionBar?.setDisplayShowCustomEnabled(true)
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)

       // val res = mutableListOf( R.drawable.home2, R.drawable.briefcase, R.drawable.target, R.drawable.paperclip, R.drawable.users, R.drawable.settings, R.drawable.ic_information_outline_black_24dp)
        val layoutInflater = LayoutInflater.from(this)
        for (a in 0 until ShortCut_To.getNavList().size) {
            val view = layoutInflater.inflate(R.layout.layout_nav, binding.linList, false)
            val item = view.findViewById<TextView>(R.id.txtItem)
            val logo = view.findViewById<ImageView>(R.id.imgLogo)
            item.text = ShortCut_To.getNavList()[a]
            Glide.with(this).load(ShortCut_To.getNavListIcon()[a]).into(logo)
//            item.setCompoundDrawablesWithIntrinsicBounds(
//                ResourcesCompat.getDrawable(resources, res[a], null), null, null, null)

            item.setOnClickListener {
                moveTo(a)
            }
            binding.linList.addView(view)
        }

        //ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        //  getStudents()
        binding.txtName.text = storage.fULLNAME
        binding.txtEmail.text = storage.email


        if(!storage.imageId.equals("")){
            Picasso.get().load(storage.imagePath).into(binding.imgPic)
        }


    }

    private fun moveTo(a: Int) {
        if(a==0){
            navTo(Body(), "Target Muscle", storage.currPage!!, 1)
        }else if(a==1){
            navTo(Main(), "Equipment", storage.currPage!!, 1)
        }else if(a==2){
            navTo(Workouts(), "Workout Plans", storage.currPage!!, 1)
        }else if(a==3){
            navTo(MyExercises(), "My Workout Plans", storage.currPage!!, 1)
        }else if(a==4){
            navTo(Calculators(), "Calculators", storage.currPage!!, 1)
        }else if(a==5){
            navTo(ScanNow(), "Use Code", storage.currPage!!, 1)
        }else if(a==6){
            navTo(Profile(), "My Profile", storage.currPage!!, 1)
        }else{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)

        }
        binding.drawLay.closeDrawer(GravityCompat.START)

    }


    fun navTo(frag: Fragment, page: String, prev: String, returnable: Int) {
        storage.currPage = page
        storage.fragValPrev = prev

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (returnable == 1) {
            fragmentTransaction.replace(R.id.frameMain, frag, page).addToBackStack(page)
        } else if (returnable == 2) {
            fragmentTransaction.replace(R.id.frameMain, frag, page).addToBackStack(prev)
        } else if (returnable == 3) {
            fragmentTransaction.replace(R.id.frameMain, frag).addToBackStack("prev")
        } else {
            fragmentTransaction.replace(R.id.frameMain, frag, page)
        }

        fragmentTransaction.commit()
        binding.txtTopic.text = page

    }


    fun setTitle(title: String){
        binding.txtTopic.text = title
    }



    //Pic Stuff
    fun firstCheck(imageView: ImageView, textView: TextView? = null) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Ask for permision
            Log.d("Permission Granted", "No")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                9
            )
        } else {
            selectedImage = imageView
            selectedTxt = textView!!
            startCrop()
            Log.d("Permission Granted", "Yes")

// Permission has already been granted
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCrop()
        } else {
            Toast.makeText(
                this,
                "Please allow permissions to access this",
                Toast.LENGTH_SHORT
            ).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(this) // optional usage
            Picasso.get().load(uriContent).into(selectedImage)
            selectedTxt.text = uriFilePath
            println(result.originalUri!!.path)
            //storage.selectedPath = uriFilePath
            println(storage.selectedPath+" // ooo")
            println("wassssssssss")

            // bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uriContent)

            // getTFI(bitmap)
        } else {
            // an error occurred
            val exception = result.error
            println(exception)
        }
    }
    private fun startCrop() {
        // start picker to get image for cropping and then use the image in cropping activity
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
            }
        )

    }

    // Function to start file selection
    fun selectFile(activity: Activity, imageView: ImageView, textView: TextView) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"  // Set the MIME type of the file(s) you want to select
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        activity.startActivityForResult(intent, PICK_FILE_REQUEST_CODE)

        selectedImage = imageView


    }

    // Handle the result of file selection
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                // Do something with the selected file URI (e.g., save it, process it)
                // You can access the file using the URI, for example:
                val filePath = uri.path


                val ext = ShortCut_To.getFileExtensionFromUri(contentResolver, uri)
                // Process the file further as needed
                println(filePath+"// "+uri.lastPathSegment+"//"+ ShortCut_To.getFileExtensionFromUri(contentResolver, uri))
                if(ShortCut_To.getAllPictureFormats().contains(ext)){
                    Picasso.get().load(uri).into(selectedImage)
                }else if(ShortCut_To.getAllAudioFormats().contains(ext)){
                    Picasso.get().load(R.drawable.place_audio).into(selectedImage)
                }else if(ShortCut_To.getAllDocFormats().contains(ext)){
                    Picasso.get().load(R.drawable.place_doc).into(selectedImage)
                }else if(ShortCut_To.getAllVidFormats().contains(ext)){
                    Picasso.get().load(R.drawable.place_vid).into(selectedImage)
                }else if(ext.equals("pdf")){
                    Picasso.get().load(R.drawable.place_pdf).into(selectedImage)
                }


                println(ShortCut_To.getAbsolutePathFromUri(this, uri))

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        binding.txtTopic.text = storage.fragValPrev
        super.onBackPressed()
    }
}