package com.lonewolf.jymstar.resources

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Constant {
    companion object{
        const val BASE_URL = "https://fcm.googleapis.com"
        const val SERVER_KEY = "AAAAKMiLwek:APA91bFCwmwk6L1Njho8v0ffyEBx96YP7kFOvZmqVMopNXfp6nFVHsuAHBUx59DE4m-x0tD0bQ1ndXCggakBHuwzpT82bTIxfZ1chvpsKlxEMfOYkDOTGphupxNcUCc733HzDtqOCSyW"
        const val CONTENT_TYPE = "application/json"
        const val auth = "Bearer sk_live_6603468b9bd59fae3e43ee47bf13a41d32c61688" //sk_test_f9da5d898e7c3d7975ad96aee7572f4a0e7c2e09
        const val URL = "https://api.paystack.co"
        const val URL_BACKUP ="https://api.paystack.co"

        const val username = "lonewolf.09@hotmail.com"
        const val password = "JymStarzero123--"


        //url for webservice append
        val TEACHERSLIST = "teacher/names?school_id="
        val ADDTEACHER = "teacher"

        val EMPLOYEELIST = "school/employees?school_id="

        val CLASSROOMLIST = "classroom?school_id="
        val ADDCLASSROOM = "classroom"

        val COURSELIST = "course?school_id="
        val ADDCOURSE = "course"

        val COUNTRYLIST = "country/codes"

        val ROLELIST = "role?school_id="

        val ADDSTAFF = "staff"
        val UPDATESTAFF = "staff/"

        val ADDVENDOR = "school/vendor"
        val VENDORLIST = "school/vendors?school_id="

        val USAGEREPORT = "user_activity?user_id="
        val STARTDATE = "&start_date="
        val ENDDATE = "&end_date="

        val USERPROFILE = "user/profile?user_id="
        val UPDATEPROFILE = "user/profile"

        val USERGETPROFILE = "user/profile?user_name="
        val USERUPDATEPROFILE = "user/profile"

        val ENROLLSTUDENT = "student"

        val FINDPARENT = "legal_guardian?"

        //==================================================================//
        val URL_CLASS: String = URL + "student?class_id"
        val TEACHERS_ROOM = "teachers_room"
        val STUDENTS = "students"
        val ATTENDANCE = "attendance"
        val ADDCLASSROOMMULTI = "multisynctoserver"

        val SEARCHSTUDENT = "searchstudent"
        val GETLGINFO = "legalguardian"
        val UPDATESTUDENT = "updatestudent"
        val UPDATEOTHERSTUDENTINFO = "updateotherinfo"
        val UPDATEGUARDIANINFO = "guardianinfoupdate"
        val UPDATEDOCUMENT = "updatedocuments"
        val UPLOADGRADESTOSERVER = "uploadgradestoserver"
        val STUDENTDOCUMENTS = "studentdocuments"
        val STUDENTPROFILEIMAGE = "profileimage"
        val STUDENTUPDATEDOC = "profimageupdatedoc"
        val UPDATEGRADESDOCUMENTS = "updategradesdocuments"
        val RECORDREVENUE = "recordrevenulist"
        val RECORDREQUET = "recordexpense"
        val RECORDEXPENSE = "recordexpensedocumet"
        val RECORDSEARCH = "recordsearchdata"
        val SCHOOLCONTACT = "schoolcontact"
        val UPDATESCHOOL = "updateschool"
        val UPDATESTUDENTPROFILEPIC = "updatestudentprofilepc"
        val SCHOOL_DOCUMENT = "schooldocuments"
        val LOGIN = "login"
        val USER_REGISTER = "user"
        val USER_LOGIN = "token"
        //val GET_STUDENT_DASHBOARD: String = STUDENT + "performance"
        val GET_SCHOOL_TERM = "school_term"
        val GET_STUDENT_BEHAVIOR = "behavior_discipline"
        val GET_STUDENT_GRADE = "grade"
        val GET_STUDENT_ATTENDACE = "attendance"
       // val STUDENT = "student/"
        val POST_FINAL_REPORT = "/final_report"
        val POST_STUDENT_BEHAVIOR = "behavior_discipline"

        val TEACHERS_MANAGEMENT = "teachers_management"
        val TEACHER_CLASSROOM = "teacher_getclassroom"
        val TEACHER_SETUP = "teachers_setup"
        val TEACHER_GET_PROFILE = "teachers_get_profile"
        val TEACHER_UPDATE_PROFILE = "teachers_update_profile"
        val SCHOOLNOTICE = "schoolnotice"
        val SCHOOLLIST = "schoollist"
        val GETCOURSE = "getcourse"
        val CLASSROOMNOTICE = "classroomnotice"
        val GETSCHOOLNOTIFYLIST = "schoolnotifylist"
        val TEACHERPROFILE = "teacherprofile"
        val GETTEACHERLIST = "teacherlist"
        val GETSTUDENTLIST = "studentlist"
        val GETCLASSROOMLIST = "classroomlist"
        val GETSTUDENTPROFILE = "getstudentprofile"
        val COURSEAVAILABLE = "courseavailable"
        val PARAM_STUDENT_ID = "student_id"
        val PARAM_STUDENT_NAME = "student_name"
        val NEWCOURSESETUP = "newcoursesetup"
        val GETCURRENCYLIST = "currencylist"

        val UPDATEUSERPROFILE = "updateuserprofile"
        val GETINVOICEITEM = "invoiceitem"
        val CREATEINVOICE = "createinvoice"

        val SELECT_CAMERA = 101
        val SELECT_GALLERY = 102
        val SELECT_DOCUMENT = 103
        val SELECT_VIDEO = 104
        val SELECT_AUDIO = 105


        var bitmapImage: Bitmap? = null

        var signature = false

        @JvmName("getSignature1")
        fun getSignature(): Boolean {
            return signature
        }

        @JvmName("setSignature1")
        fun setSignature(signature: Boolean) {
            Companion.signature = signature
        }

        @JvmName("setBitmapImage1")
        fun setBitmapImage(imagename: Bitmap?) {
            bitmapImage = imagename
        }

        @JvmName("getBitmapImage1")
        fun getBitmapImage(): Bitmap? {
            return bitmapImage
        }

        fun encodeTobase64(image: Bitmap): String? {
            val immagex = image
            val baos = ByteArrayOutputStream()
            immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }


        fun decodeBase64(input: String): Bitmap? {
            var input = input
            if (input.contains("base64,")) {
                input = input.split("base64,").toTypedArray()[1]
            }
            try {
                val decodedByte = Base64.decode(input, 0)
                return BitmapFactory.decodeByteArray(
                    decodedByte, 0,
                    decodedByte.size
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }


        fun StringToBitMap(encodedString: String?): Bitmap? {
            try {
                val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
                return BitmapFactory.decodeByteArray(
                    encodeByte, 0,
                    encodeByte.size
                )
            } catch (e: Exception) {
                e.message
                return null
            }
        }


        /**
         * Create a File for saving an image or video
         */
        @SuppressLint("SimpleDateFormat")
        fun getOutputMediaFile(type: Int): File? {
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.
            val mediaStorageDir = File(
                Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "prive"
            )
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null
                }
            }

            // Create a media file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(Date())
            val mediaFile: File
            if (type == 1) {
                mediaFile = File(
                    mediaStorageDir.path + File.separator
                            + "IMG_" + timeStamp + ".jpg"
                )
            } else {
                return null
            }
            return mediaFile
        }

        fun getResizedBitmap(bm: Bitmap, newHeight: Int, newWidth: Int): Bitmap? {
            val width = bm.width
            val height = bm.height
            val scaleWidth = (newWidth.toFloat()) / width
            val scaleHeight = (newHeight.toFloat()) / height
            // create a matrix for the manipulation
            val matrix = Matrix()
            // resize the bit map
            matrix.postScale(scaleWidth, scaleHeight)
            // recreate the new Bitmap
            return Bitmap.createBitmap(
                bm, 0, 0, width, height,
                matrix, false
            )
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        fun isMediaDocument(uri: Uri): Boolean {
            return ("com.android.providers.media.documents" == uri
                .authority)
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        fun isGooglePhotosUri(uri: Uri): Boolean {
            return ("com.google.android.apps.photos.content" == uri
                .authority)
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        fun isDownloadsDocument(uri: Uri): Boolean {
            return ("com.android.providers.downloads.documents" == uri
                .authority)
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context       The context.
         * @param uri           The Uri to query.
         * @param selection     (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        fun getDataColumn(
            context: Context, uri: Uri?,
            selection: String?, selectionArgs: Array<String?>?
        ): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)
            try {
                cursor = context.contentResolver.query(
                    (uri)!!, projection,
                    selection, selectionArgs, null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }

         fun isExternalStorageDocument(uri: Uri): Boolean {
            return ("com.android.externalstorage.documents" == uri
                .authority)
        }

        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager = activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (activity.currentFocus != null) inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!
                    .windowToken, 0
            )
        }

        fun getImageResize(bitmapOrg: Bitmap): Bitmap? {
            val width = bitmapOrg.width
            val height = bitmapOrg.height
            val newWidth = 300
            val newHeight = 300

            // calculate the scale - in this case = 0.4f
            val scaleWidth = (newWidth.toFloat()) / width
            val scaleHeight = (newHeight.toFloat()) / height

            // createa matrix for the manipulation
            val matrix = Matrix()
            // resize the bit map
            matrix.postScale(scaleWidth, scaleHeight)

            // recreate the new Bitmap
            return Bitmap.createBitmap(
                bitmapOrg, 0, 0, width,
                height, matrix, true
            )
        }

        fun roundLatLong(value: Double): Double {
            val factor = 1e5 // = 1 * 10^5 = 100000.
            return Math.round(value * factor) / factor
        }


        fun setupUI(view: View, activity: Activity) {

            // Set ic_down touch listener for non-text box views to hide keyboard.
            if (view !is EditText) {
                view.setOnTouchListener { v, event ->
                    hideSoftKeyboard(activity)
                    false
                }
            }

            // If a layout container, iterate over children and seed recursion.
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    val innerView = view.getChildAt(i)
                    setupUI(innerView, activity)
                }
            }
        }

       val tempFilesRoot =
            File(Environment.getExternalStorageDirectory().absoluteFile.toString() + "/Android/data/com.sws.infoviewsims/files/Temp/")

        var mainFilesRoot =
            Environment.getExternalStorageDirectory().absoluteFile.toString() + "/Android/data/io.appery.project288891/files/chat_pic/"

        var getExamsLabel = arrayOf(
            "Select Exam",
            "Cambridge Primary",
            "Cambridge Secondary",
            "IGCSE",
            "Advanced Subsidiary",
            "A Level"
        )

        var getExams = arrayOf(
            "Select Exam",
            "Cambridge Primary(Y4-Y6)",
            "Cambridge Secondary(Y7-Y9)",
            "IGCSE(Y8-Y10)",
            "Advanced Subsidiary(Y10-Y12)",
            "A Level(Y11-Y13)"
        )

    }
}