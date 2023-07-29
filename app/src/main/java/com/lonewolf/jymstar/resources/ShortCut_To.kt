package com.lonewolf.jymstar.resources

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.lonewolf.jymstar.R
import com.lonewolf.jymstar.Send
import com.lonewolf.jymstar.fragments.*
import com.lonewolf.jymstar.fragments.calculator.Calculators
import com.lonewolf.jymstar.fragments.equipment.Main
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit


object ShortCut_To {
    const val DATEWITHTIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val DATEWITHOUTTIME = "yyyy-MM-dd"
    const val DATEFORMATDDMMYYYY = "dd/MM/yyyy"
    const val DATEFORMATDDMMYYYY2 = "dd-MM-yyyy"
    const val DATEFORMATYYYYMMDD = "yyyy-MM-dd"
    const val DATEFORMATYYYYMMDD2 = "yyyy/MM/dd"
    const val TIME = "hh:mm a"
    const val DATEWITHTIMEDDMMYYY = "dd-MM-yyyy'T'HH:mm:ss.SSS'Z'"
    val currentDatewithTime: String
        get() {
            val dateFormat = SimpleDateFormat(DATEWITHTIMEDDMMYYY, Locale.getDefault())
            val date = Date()
            return dateFormat.format(date)
        }

    fun hideKeyboard(activity: Activity) {
        try {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            Log.d("No keyboard", "No keyboard to drop")
        }
    }

    val currentDates: String
        get() {
            val dateFormat = SimpleDateFormat(DATEFORMATYYYYMMDD, Locale.getDefault())
            val date = Date()
            return dateFormat.format(date)
        }
    val currentDateFormat2: String
        get() {
            val dateFormat = SimpleDateFormat(DATEFORMATDDMMYYYY, Locale.getDefault())
            val date = Date()
            return dateFormat.format(date)
        }
    val currentDateFormat3: String
        get() {
            val dateFormat = SimpleDateFormat(DATEFORMATYYYYMMDD2, Locale.getDefault())
            val date = Date()
            return dateFormat.format(date)
        }

    fun decodeBase64(input: String?): Bitmap? {
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

    fun getTimeFromDate(str: String?): String {
        return if (str != null && !str.equals(
                "null",
                ignoreCase = true
            ) && str.trim { it <= ' ' }.length != 0
        ) {
            val sdf1 = SimpleDateFormat(DATEWITHTIME, Locale.US)
            val sdf2 = SimpleDateFormat(TIME, Locale.US)
            try {
                val date = sdf1.parse(str)
                sdf2.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        } else {
            " "
        }
    }

    val currentDay: String
        get() {
            val daysArray = arrayOf("sun", "mon", "tue", "wed", "thu", "fri", "sat")
            val calendar = Calendar.getInstance()
            val day = calendar[Calendar.DAY_OF_WEEK]
            val mt = calendar[Calendar.MONTH]
            val yr = calendar[Calendar.YEAR]
            return yr.toString() + daysArray[day - 1] + mt
        }
    val currentMonthYear: String
        get() {
            val c = Calendar.getInstance()
            val currMonth = c[Calendar.MONTH] + 1
            val currYear = c[Calendar.YEAR]
            val curDay = c[Calendar.DAY_OF_MONTH]
            return "$currMonth/$currYear"
        }
    val currentDayMonthYear: String
        get() {
            val c = Calendar.getInstance()
            val currMonth = c[Calendar.MONTH]
            val currYear = c[Calendar.YEAR]
            val curDay = c[Calendar.DAY_OF_MONTH]
            return if (currMonth == 0) {
                "January $curDay, $currYear"
            } else if (currMonth == 1) {
                "February $curDay, $currYear"
            } else if (currMonth == 2) {
                "March $curDay, $currYear"
            } else if (currMonth == 3) {
                "April $curDay, $currYear"
            } else if (currMonth == 4) {
                "May $curDay, $currYear"
            } else if (currMonth == 5) {
                "June $curDay, $currYear"
            } else if (currMonth == 6) {
                "July $curDay, $currYear"
            } else if (currMonth == 7) {
                "August $curDay, $currYear"
            } else if (currMonth == 8) {
                "September $curDay, $currYear"
            } else if (currMonth == 9) {
                "October $curDay, $currYear"
            } else if (currMonth == 10) {
                "November $curDay, $currYear"
            } else if (currMonth == 11) {
                "December $curDay, $currYear"
            } else {
                ""
            }
        }

    fun isDate(string: String, format: String = "dd/MM/yyyy"): Boolean {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        dateFormat.isLenient = false
        return try {
            dateFormat.parse(string)
            true
        } catch (e: Exception) {
            false
        }
    }
    fun getDateTimeForAPI(dateFormatted: String?): String {
        val apiDate = Calendar.getInstance()
        try {
            val dateFormat = SimpleDateFormat(DATEFORMATDDMMYYYY)
            apiDate.time = dateFormat.parse(dateFormatted)
            val corrTime = Calendar.getInstance()
            apiDate[Calendar.HOUR_OF_DAY] = corrTime[Calendar.HOUR_OF_DAY]
            apiDate[Calendar.MINUTE] = corrTime[Calendar.MINUTE]
            apiDate[Calendar.SECOND] = corrTime[Calendar.SECOND]
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        //2014-03-15T21:04:43.162Z
        val dateFormat = SimpleDateFormat(DATEWITHTIME)
        return dateFormat.format(apiDate.time)
    }

    fun getDateForAPP(strDate: String?): String? {
        return if (strDate != null && !strDate.equals(
                "null",
                ignoreCase = true
            ) && strDate.trim { it <= ' ' }.length != 0
        ) {
            val sdf1 = SimpleDateFormat(DATEWITHTIME)
            val sdf2 = SimpleDateFormat(DATEFORMATDDMMYYYY)
            try {
                val date = sdf1.parse(strDate)
                sdf2.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            ""
        }
    }

    fun getDateShortForAPP(strDate: String?): String? {
        return if (strDate != null && !strDate.equals(
                "null",
                ignoreCase = true
            ) && strDate.trim { it <= ' ' }.length != 0
        ) {
            val sdf1 = SimpleDateFormat(DATEWITHOUTTIME)
            val sdf2 = SimpleDateFormat(DATEFORMATDDMMYYYY)
            try {
                val date = sdf1.parse(strDate)
                sdf2.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            ""
        }
    }
    fun reverseDate(date: String, split: String, newSplit: String): String {
        return "${date.split(split)[2]}$newSplit${date.split(split)[1]}$newSplit${date.split(split)[0]}"
    }

    fun getFormatDateAPI(str: String?): String? {
        return if (str != null && !str.equals(
                "null",
                ignoreCase = true
            ) && str.trim { it <= ' ' }.length != 0
        ) {
            val sdf1 = SimpleDateFormat(DATEFORMATDDMMYYYY)
            val sdf2 = SimpleDateFormat(DATEFORMATYYYYMMDD)
            try {
                val date = sdf1.parse(str)
                sdf2.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            ""
        }
    }
    fun getFormatDateAPI2(str: String?): String? {
        return if (str != null && !str.equals(
                "null",
                ignoreCase = true
            ) && str.trim { it <= ' ' }.length != 0
        ) {
            val sdf1 = SimpleDateFormat(DATEFORMATDDMMYYYY2)
            val sdf2 = SimpleDateFormat(DATEFORMATYYYYMMDD2)
            try {
                val date = sdf1.parse(str)
                sdf2.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            ""
        }
    }

    fun sortData(list: ArrayList<HashMap<String, String>>, field: String?) {
        Collections.sort(list) { lhs: HashMap<String, String>, rhs: HashMap<String, String> ->
            lhs[field]!!
                .compareTo(rhs[field]!!)
        }
    }

    fun sortDataInvert(list: ArrayList<HashMap<String, String>>?, field: String?) {
        Collections.sort(list, { lhs, rhs ->
            rhs[field]!!.compareTo(
                lhs[field]!!
            )
        })
    }

    fun convertDate(date: String): String {
        val nDate = date.split("T".toRegex()).toTypedArray()
        val mDate = nDate[0]
        val oDate = mDate.split("-".toRegex()).toTypedArray()
        return oDate[2] + "/" + oDate[1] + "/" + oDate[0]
    }

    fun addDaysToDate(date: String, numDays: Int): String {
        val arrDate = date.split("/".toRegex()).toTypedArray()
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val cal = Calendar.getInstance()
        cal[Calendar.DAY_OF_MONTH] = arrDate[0].toInt()
        cal[Calendar.MONTH] = arrDate[1].toInt() - 1
        cal[Calendar.YEAR] = arrDate[2].toInt()
        cal.add(Calendar.DAY_OF_MONTH, numDays)
        return sdf.format(cal.time)
    }

    fun getMonthYear(date: String): String {
        val c = Calendar.getInstance()
        val currMonth = date.split("/".toRegex()).toTypedArray()[1].toInt() - 1
        val currYear = date.split("/".toRegex()).toTypedArray()[2].toInt()
        val curDay = date.split("/".toRegex()).toTypedArray()[0].toInt()
        return if (currMonth == 0) {
            "January, $currYear"
        } else if (currMonth == 1) {
            "February, $currYear"
        } else if (currMonth == 2) {
            "March, $currYear"
        } else if (currMonth == 3) {
            "April, $currYear"
        } else if (currMonth == 4) {
            "May, $currYear"
        } else if (currMonth == 5) {
            "June, $currYear"
        } else if (currMonth == 6) {
            "July, $currYear"
        } else if (currMonth == 7) {
            "August, $currYear"
        } else if (currMonth == 8) {
            "September, $currYear"
        } else if (currMonth == 9) {
            "October, $currYear"
        } else if (currMonth == 10) {
            "November, $currYear"
        } else if (currMonth == 11) {
            "December, $currYear"
        } else {
            ""
        }
    }

    fun getMonthYearShort(date: String): String {
        val c = Calendar.getInstance()
        val currMonth = date.split("/".toRegex()).toTypedArray()[1].toInt() - 1
        val currYear = date.split("/".toRegex()).toTypedArray()[2].toInt()
        val curDay = date.split("/".toRegex()).toTypedArray()[0].toInt()
        return if (currMonth == 0) {
            "Jan. $currYear"
        } else if (currMonth == 1) {
            "February, $currYear"
        } else if (currMonth == 2) {
            "Mar. $currYear"
        } else if (currMonth == 3) {
            "Apr. $currYear"
        } else if (currMonth == 4) {
            "May. $currYear"
        } else if (currMonth == 5) {
            "Jun. $currYear"
        } else if (currMonth == 6) {
            "Jul. $currYear"
        } else if (currMonth == 7) {
            "Aug. $currYear"
        } else if (currMonth == 8) {
            "Sep. $currYear"
        } else if (currMonth == 9) {
            "Oct. $currYear"
        } else if (currMonth == 10) {
            "Nov. $currYear"
        } else if (currMonth == 11) {
            "Dec. $currYear"
        } else {
            ""
        }
    }


    fun convertDateFormat(dateTimeString: String): String {
        println(Build.VERSION.SDK_INT)
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val formatterInput = DateTimeFormatter.ISO_INSTANT
                val formatterOutput = DateTimeFormatter.ofPattern("EEE dd MMM yyyy")
                val instant = Instant.parse(dateTimeString)
                val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                return formatterOutput.format(dateTime)


            }else{
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                val outputFormat = SimpleDateFormat("EEE dd MMM yyyy", Locale.US)
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                val date = inputFormat.parse(dateTimeString)
                return outputFormat.format(date)
            }
        }catch (e:Exception){
            e.printStackTrace()
            return ""
        }
    }

    fun convertTimeToMilitary(timeString: String): String {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val timeFormatter12 = DateTimeFormatter.ofPattern("h:mma")
            val timeFormatter24 = DateTimeFormatter.ofPattern("HH:mm")
            val time = LocalTime.parse(timeString, timeFormatter12)
            return time.format(timeFormatter24)
        }
        else{
            val timeFormatter12 = SimpleDateFormat("h:mma", Locale.US)
            val timeFormatter24 = SimpleDateFormat("HH:mm", Locale.US)
            val time = timeFormatter12.parse(timeString)
            return timeFormatter24.format(time)
        }
    }

    fun convertTimeFormat(timeString: String): String {
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val formatterInput = DateTimeFormatter.ofPattern("HH:mm:ss")
                val formatterOutput = DateTimeFormatter.ofPattern("h:mma")
                val time = LocalTime.parse(timeString, formatterInput)
                return formatterOutput.format(time)

            }else{
                val inputFormat = SimpleDateFormat("HH:mm")
                val outputFormat = SimpleDateFormat("h:mma")
                val time = inputFormat.parse(timeString)
                return outputFormat.format(time)
            }
        }catch (e:Exception){
            e.printStackTrace()
            return ""
        }
    }


    fun getDayOFWeek(dDate: String): String {
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_MONTH] = dDate.split("/".toRegex()).toTypedArray()[0].toInt()
        calendar[Calendar.MONTH] = dDate.split("/".toRegex()).toTypedArray()[1].toInt() - 1
        calendar[Calendar.YEAR] = dDate.split("/".toRegex()).toTypedArray()[2].toInt()
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US).uppercase(
            Locale.getDefault()
        )
    }

    fun daysBetween(startDate: String, endDate: String): Long {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            val start = LocalDate.parse(startDate)
            val end = LocalDate.parse(endDate)
            return ChronoUnit.DAYS.between(start, end)
        }else{
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val start = sdf.parse(startDate)
            val end = sdf.parse(endDate)
            val diffInMillis = (end?.time ?: 0) - (start?.time ?: 0)
            return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)
        }
    }

    fun getClosestOclock(): String {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val currentTime = LocalTime.now()
            val minute = currentTime.minute
            return if (minute >= 30) {
                "${currentTime.withHour(currentTime.hour + 1).withMinute(0)}"
            } else {
                "${currentTime.withMinute(0)}"
            }

        } else {
            val currentTime = Calendar.getInstance()
            val minute = currentTime.get(Calendar.MINUTE)
            val closestOclock = Calendar.getInstance()
            closestOclock.timeInMillis = currentTime.timeInMillis

            if (minute >= 30) {
                closestOclock.set(Calendar.HOUR_OF_DAY, currentTime.get(Calendar.HOUR_OF_DAY) + 1)
            }

            closestOclock.set(Calendar.MINUTE, 0)
            closestOclock.set(Calendar.SECOND, 0)
            closestOclock.set(Calendar.MILLISECOND, 0)

            return "${closestOclock.get(Calendar.HOUR_OF_DAY)}:${closestOclock.get(Calendar.MINUTE)}"
        }

    }

    fun getClosestOclockPlusOne(): String {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val currentTime = LocalTime.now()
            val minute = currentTime.minute
            return if (minute >= 30) {
                "${currentTime.withHour(currentTime.hour + 2).withMinute(0)}"
            } else {
                "${currentTime.withHour(currentTime.hour + 1).withMinute(0)}"
            }

        } else {
            val currentTime = Calendar.getInstance()
            val minute = currentTime.get(Calendar.MINUTE)
            val closestOclock = Calendar.getInstance()
            closestOclock.timeInMillis = currentTime.timeInMillis

            if (minute >= 30) {
                closestOclock.set(Calendar.HOUR_OF_DAY, currentTime.get(Calendar.HOUR_OF_DAY))
            }

            closestOclock.set(Calendar.MINUTE, 0)
            closestOclock.set(Calendar.SECOND, 0)
            closestOclock.set(Calendar.MILLISECOND, 0)

            return "${closestOclock.get(Calendar.HOUR_OF_DAY)+1}:${closestOclock.get(Calendar.MINUTE)}"
        }

    }

    fun getCompany(company: String?, limit: String): String {
        var newCompany = "Alpha"
        if (limit == "Charlie") {
            when (company) {
                "None" -> newCompany = "Alpha"
                "Alpha" -> newCompany = "Bravo"
                "Bravo" -> newCompany = "Charlie"
                "Charlie" -> newCompany = "Alpha"
            }
        } else if (limit == "Delta") {
            when (company) {
                "None" -> newCompany = "Alpha"
                "Alpha" -> newCompany = "Bravo"
                "Bravo" -> newCompany = "Charlie"
                "Charlie" -> newCompany = "Delta"
                "Delta" -> newCompany = "Alpha"
            }
        } else if (limit == "Echo") {
            when (company) {
                "None" -> newCompany = "Alpha"
                "Alpha" -> newCompany = "Bravo"
                "Bravo" -> newCompany = "Charlie"
                "Charlie" -> newCompany = "Alpha"
                "Delta" -> newCompany = "Echo"
                "Echo" -> newCompany = "Alpha"
            }
        } else if (limit == "Foxtrot") {
            when (company) {
                "None" -> newCompany = "Alpha"
                "Alpha" -> newCompany = "Bravo"
                "Bravo" -> newCompany = "Charlie"
                "Charlie" -> newCompany = "Delta"
                "Delta" -> newCompany = "Echo"
                "Echo" -> newCompany = "Foxtrot"
                "Foxtrot" -> newCompany = "Alpha"
            }
        } else if (limit == "Gulf") {
            when (company) {
                "None" -> newCompany = "Alpha"
                "Alpha" -> newCompany = "Bravo"
                "Bravo" -> newCompany = "Charlie"
                "Charlie" -> newCompany = "Delta"
                "Delta" -> newCompany = "Echo"
                "Echo" -> newCompany = "Foxtrot"
                "Foxtrot" -> newCompany = "Gulf"
                "Gulf" -> newCompany = "Alpha"
            }
        }
        return newCompany
    }

    fun getAgeAlt(dob: String): Int {
        val arrYear = dob.split("/".toRegex()).toTypedArray()
        val currYear = currentDateFormat2.split("/".toRegex()).toTypedArray()
        var newYear = currYear[2].toInt() - arrYear[2].toInt()
        if (arrYear[1].toInt() > currYear[1].toInt()) {
            newYear = newYear - 1
        } else {
            if (arrYear[1].toInt() == currYear[1].toInt()) {
                if (arrYear[0].toInt() > currYear[0].toInt()) {
                    newYear = newYear - 1
                }
            }
        }
        return newYear
    }

    fun getAge(dobString: String?): Int {
        var date: Date? = null
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        try {
            date = sdf.parse(dobString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (date == null) return 0
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob.time = date
        val year = dob[Calendar.YEAR]
        val month = dob[Calendar.MONTH]
        val day = dob[Calendar.DAY_OF_MONTH]
        dob[year, month + 1] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        return age + 1
    }

    fun getDateAfterNumberOfDays(strDate: String?, num: Int): String {
        if (strDate == null || strDate.equals(
                "null",
                ignoreCase = true
            ) || strDate.trim { it <= ' ' }.length == 0
        ) {
            return ""
        }
        try {
            val sdf = SimpleDateFormat(DATEFORMATYYYYMMDD)
            val calendar = Calendar.getInstance()
            calendar.time = sdf.parse(strDate)
            calendar.add(Calendar.DATE, -num)
            val resultdate = Date(calendar.timeInMillis)
            return sdf.format(resultdate)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun addMonth(date: Date?, i: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MONTH, i)
        return cal.time
    }

    fun getCountOfDays(createdDateString: String?, expireDateString: String?): Int {
        val dateFormat = SimpleDateFormat("dd/mm/yyyy")
        var createdConvertedDate: Date? = null
        var expireCovertedDate: Date? = null
        try {
            createdConvertedDate = dateFormat.parse(createdDateString)
            expireCovertedDate = dateFormat.parse(expireDateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val start: Calendar = GregorianCalendar()
        start.time = createdConvertedDate
        val end: Calendar = GregorianCalendar()
        end.time = expireCovertedDate
        val diff = end.timeInMillis - start.timeInMillis
        val dayCount = diff.toFloat() / (24 * 60 * 60 * 1000)
        return dayCount.toInt()
    }

    fun getCountOfDay(createdDateString: String?, expireDateString: String?): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        var createdConvertedDate: Date? = null
        var expireCovertedDate: Date? = null
        var todayWithZeroTime: Date? = null
        try {
            createdConvertedDate = dateFormat.parse(createdDateString)
            expireCovertedDate = dateFormat.parse(expireDateString)
            val today = Date()
            todayWithZeroTime = dateFormat.parse(dateFormat.format(today))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        var cYear = 0
        var cMonth = 0
        var cDay = 0
        if (createdConvertedDate!!.after(todayWithZeroTime)) {
            val cCal = Calendar.getInstance()
            cCal.time = createdConvertedDate
            cYear = cCal[Calendar.YEAR]
            cMonth = cCal[Calendar.MONTH]
            cDay = cCal[Calendar.DAY_OF_MONTH]
        } else {
            val cCal = Calendar.getInstance()
            cCal.time = todayWithZeroTime
            cYear = cCal[Calendar.YEAR]
            cMonth = cCal[Calendar.MONTH]
            cDay = cCal[Calendar.DAY_OF_MONTH]
        }


        /*Calendar todayCal = Calendar.getInstance();
    int todayYear = todayCal.get(Calendar.YEAR);
    int today = todayCal.get(Calendar.MONTH);
    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
    */
        val eCal = Calendar.getInstance()
        eCal.time = expireCovertedDate
        val eYear = eCal[Calendar.YEAR]
        val eMonth = eCal[Calendar.MONTH]
        val eDay = eCal[Calendar.DAY_OF_MONTH]
        val date1 = Calendar.getInstance()
        val date2 = Calendar.getInstance()
        date1.clear()
        date1[cYear, cMonth] = cDay
        date2.clear()
        date2[eYear, eMonth] = eDay
        val diff = date2.timeInMillis - date1.timeInMillis
        val dayCount = diff.toFloat() / (24 * 60 * 60 * 1000)
        return "" + dayCount.toInt()
    }

    fun showAlert(activity: Activity?, title: String?, message: String?) {
        val alert = AlertDialog.Builder(activity)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setCancelable(false)
        alert.show()
    }

    val regions: List<String>
        get() {
            val listRegions: MutableList<String> = ArrayList()
            listRegions.add("Select your Current region")
            listRegions.add("Ahafo")
            listRegions.add("Ashanti")
            listRegions.add("Bono")
            listRegions.add("Bono East")
            listRegions.add("Central")
            listRegions.add("Eastern")
            listRegions.add("Greater Accra")
            listRegions.add("North East")
            listRegions.add("Northern")
            listRegions.add("Oti")
            listRegions.add("Savannah")
            listRegions.add("Upper East")
            listRegions.add("Upper West")
            listRegions.add("Volta")
            listRegions.add("Western")
            listRegions.add("Western North")
            return listRegions
        }
    val regionsHome: List<String>
        get() {
            val listRegions: MutableList<String> = ArrayList()
            listRegions.add("Select your Home region")
            listRegions.add("Ahafo")
            listRegions.add("Ashanti")
            listRegions.add("Bono")
            listRegions.add("Bono East")
            listRegions.add("Central")
            listRegions.add("Eastern")
            listRegions.add("Greater Accra")
            listRegions.add("North East")
            listRegions.add("Northern")
            listRegions.add("Oti")
            listRegions.add("Savannah")
            listRegions.add("Upper East")
            listRegions.add("Upper West")
            listRegions.add("Volta")
            listRegions.add("Western")
            listRegions.add("Western North")
            return listRegions
        }
    val categoryName: List<String>
        get() {
            val list: MutableList<String> = ArrayList()
            list.add("Harmonies")
            list.add("Beats")
            list.add("Instrumentals")
            list.add("Vocals")
            list.add("Educational")
            list.add("New Release")
            return list
        }
    val countryies: Array<String>
        get() = arrayOf(
            "Select Country",
            "Ghana",
            "Afghanistan",
            "Albania",
            "Algeria",
            "Andorra",
            "Angola",
            "Antarctica",
            "Antigua and Barbuda",
            "Argentina",
            "Armenia",
            "Australia",
            "Austria",
            "Azerbaijan",
            "Bahamas",
            "Bahrain",
            "Bangladesh",
            "Barbados",
            "Belarus",
            "Belgium",
            "Belize",
            "Benin",
            "Bermuda",
            "Bhutan",
            "Bolivia",
            "Bosnia and Herzegovina",
            "Botswana",
            "Brazil",
            "Brunei",
            "Bulgaria",
            "Burkina Faso",
            "Burma",
            "Burundi",
            "Cambodia",
            "Cameroon",
            "Canada",
            "Cape Verde",
            "Central African Republic",
            "Chad",
            "Chile",
            "China",
            "Colombia",
            "Comoros",
            "Congo, Democratic Republic",
            "Congo, Republic of the",
            "Costa Rica",
            "Cote d'Ivoire",
            "Croatia",
            "Cuba",
            "Cyprus",
            "Czech Republic",
            "Denmark",
            "Djibouti",
            "Dominica",
            "Dominican Republic",
            "East Timor",
            "Ecuador",
            "Egypt",
            "El Salvador",
            "Equatorial Guinea",
            "Eritrea",
            "Estonia",
            "Ethiopia",
            "Fiji",
            "Finland",
            "France",
            "Gabon",
            "Gambia",
            "Georgia",
            "Germany",
            "Greece",
            "Greenland",
            "Grenada",
            "Guatemala",
            "Guinea",
            "Guinea-Bissau",
            "Guyana",
            "Haiti",
            "Honduras",
            "Hong Kong",
            "Hungary",
            "Iceland",
            "India",
            "Indonesia",
            "Iran",
            "Iraq",
            "Ireland",
            "Israel",
            "Italy",
            "Jamaica",
            "Japan",
            "Jordan",
            "Kazakhstan",
            "Kenya",
            "Kiribati",
            "Korea, North",
            "Korea, South",
            "Kuwait",
            "Kyrgyzstan",
            "Laos",
            "Latvia",
            "Lebanon",
            "Lesotho",
            "Liberia",
            "Libya",
            "Liechtenstein",
            "Lithuania",
            "Luxembourg",
            "Macedonia",
            "Madagascar",
            "Malawi",
            "Malaysia",
            "Maldives",
            "Mali",
            "Malta",
            "Marshall Islands",
            "Mauritania",
            "Mauritius",
            "Mexico",
            "Micronesia",
            "Moldova",
            "Mongolia",
            "Morocco",
            "Monaco",
            "Mozambique",
            "Namibia",
            "Nauru",
            "Nepal",
            "Netherlands",
            "New Zealand",
            "Nicaragua",
            "Niger",
            "Nigeria",
            "Norway",
            "Oman",
            "Pakistan",
            "Panama",
            "Papua New Guinea",
            "Paraguay",
            "Peru",
            "Philippines",
            "Poland",
            "Portugal",
            "Qatar",
            "Romania",
            "Russia",
            "Rwanda",
            "Samoa",
            "San Marino",
            " Sao Tome",
            "Saudi Arabia",
            "Senegal",
            "Serbia and Montenegro",
            "Seychelles",
            "Sierra Leone",
            "Singapore",
            "Slovakia",
            "Slovenia",
            "Solomon Islands",
            "Somalia",
            "South Africa",
            "Spain",
            "Sri Lanka",
            "Sudan",
            "Suriname",
            "Swaziland",
            "Sweden",
            "Switzerland",
            "Syria",
            "Taiwan",
            "Tajikistan",
            "Tanzania",
            "Thailand",
            "Togo",
            "Tonga",
            "Trinidad and Tobago",
            "Tunisia",
            "Turkey",
            "Turkmenistan",
            "Uganda",
            "Ukraine",
            "United Arab Emirates",
            "United Kingdom",
            "United States",
            "Uruguay",
            "Uzbekistan",
            "Vanuatu",
            "Venezuela",
            "Vietnam",
            "Yemen",
            "Zambia",
            "Zimbabwe"
        )
    val netWorks: Array<String>
        get() = arrayOf("Select Network", "MTN", "Vodafone", "AirtelTigo")
    val netWorksVal: Array<String>
        get() = arrayOf("", "mtn", "vod", "tgo")

    fun getGrade(mark : Int) : String{
        if(mark>=80){
            return "A"
        }else if(mark>=70){
            return "B2"
        }else if(mark>=65){
            return "B3"
        }else if(mark>=60){
            return "C4"
        }else if(mark>=55){
            return "C5"
        }else if(mark>=50){
            return "C6"
        }else if(mark>=45){
            return "D7"
        }else if(mark>=40){
            return "E8"
        }else {
            return "F9"
        }
    }





    fun getColorGrade(mark: Int):Int{
        if(mark>=80){
            return R.color.green
        }else if(mark>=55){
            return R.color.yellow
        }else{
            return R.color.red
        }
    }

    fun getNavList():MutableList<String>{
        return mutableListOf("Target Muscle", "Equipment","Exercises", "My Exercises", "Calculators", "Use Code","My Profile", "Login")
    }

    fun getNavListIcon():MutableList<Int>{
        return mutableListOf(R.drawable.icons8_middle_back, R.drawable.icons8_dumbbell,R.drawable.icons8_bench_press,R.drawable.icons8_pushups, R.drawable.icons8_calculator, R.drawable.icons8_qr_code, R.drawable.profile, R.drawable.loginlogout)
    }

    fun containsLetters(inp: String): Boolean {
        return inp.any { it.isLetter() }
    }

    fun containsNumbers(inp: String): Boolean {
        return inp.any { it.isDigit() }
    }

    fun containsLowerLetters(inp: String): Boolean {
        return inp.any { it.isLowerCase() }
    }

    fun containsUpperLetters(inp: String): Boolean {
        return inp.any { it.isUpperCase() }
    }
    fun containsSpecial(inp: String): Boolean {
        return inp.any { !it.isLetterOrDigit() }
    }

    fun isMoreThan(inp:String, num:Int):Boolean{
        return inp.length>num
    }

    fun warnMess(txt : TextView, lin: LinearLayout, message: String?, activity: Activity,
    color1:Int, bg:Int, bg2:Int){
        txt.setTextColor(ResourcesCompat.getColor(activity.resources, color1, null))
        txt.text = message
        lin.setBackgroundResource(bg)
        txt.setCompoundDrawablesWithIntrinsicBounds(null, null, ResourcesCompat.getDrawable(activity.resources, bg2, null), null)
        lin.visibility = View.VISIBLE
    }

//    fun showAlert(activity: Activity, mess : String, state:String, constraintLayout: ConstraintLayout){
//        val layoutInflater = LayoutInflater.from(activity)
//        val alert = AlertDialog.Builder(activity)
//        val dialog = alert.create()
//
//        val view = layoutInflater.inflate(R.layout.layout_pop, constraintLayout, false)
//        val bind = LayoutPopBinding.bind(view)
//        bind.textView5.text = state
//        bind.txtMess.text = mess
//
//        if(state.equals("Success")){
//            bind.toolbar.setBackgroundResource(R.drawable.bg_success_straight)
//            bind.textView5.setTextColor(ResourcesCompat.getColor(activity.resources, R.color.success_green, null))
//            bind.textView5.setCompoundDrawablesWithIntrinsicBounds(null, null, ResourcesCompat.getDrawable(activity.resources, R.drawable.greencheck, null), null)
//        }else if(state.equals("Error")){
//            bind.toolbar.setBackgroundResource(R.drawable.bg_error_straight)
//            bind.textView5.setTextColor(ResourcesCompat.getColor(activity.resources, R.color.error_red, null))
//            bind.textView5.setCompoundDrawablesWithIntrinsicBounds(null, null, ResourcesCompat.getDrawable(activity.resources, R.drawable.close, null), null)
//        }else if(state.equals("Warning")){
//            bind.toolbar.setBackgroundResource(R.drawable.bg_warning_straight)
//            bind.textView5.setTextColor(ResourcesCompat.getColor(activity.resources, R.color.warn_orange, null))
//            bind.textView5.setCompoundDrawablesWithIntrinsicBounds(null, null, ResourcesCompat.getDrawable(activity.resources, R.drawable.info, null), null)
//        }
//        bind.txtYes.setOnClickListener {
//            dialog.dismiss()
//        }
//        dialog.setView(view)
//        dialog.show()
//    }


    //Date picker
    fun showCal(edt : EditText, context: Context){
        // on below line we are getting
        // the instance of our calendar.
        val c = Calendar.getInstance()

        // on below line we are getting
        // our day, month and year.
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // on below line we are creating a
        // variable for date picker dialog.
        val datePickerDialog = DatePickerDialog(
            // on below line we are passing context.
            context,
            { view, year, monthOfYear, dayOfMonth ->
                // on below line we are setting
                // date to our edit text.
                val dat = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                edt.setText(dat)
            },
            // on below line we are passing year, month
            // and day for the selected date in our date picker.
            year,
            month,
            day
        )
        // at last we are calling show
        // to display our date picker dialog.
        datePickerDialog.show()
    }

    fun showClock(chooseTime: EditText, context: Context){
        val calendar = Calendar.getInstance()
        val currentHour = calendar[Calendar.HOUR_OF_DAY]
        val currentMinute = calendar[Calendar.MINUTE]

        val timePickerDialog = TimePickerDialog(context,
            { timePicker, hourOfDay, minutes ->
                var sHr = hourOfDay.toString()
                var sMin = minutes.toString()
                if(hourOfDay<10){
                    sHr = "0$hourOfDay"
                }
                if(minutes<10){
                    sMin = "0$minutes"
                }
                val time = "$sHr:$sMin"
                chooseTime.setText(time)
            },
            currentHour,
            currentMinute,
            false
        )
        timePickerDialog.show()
    }

    fun getTitle():MutableList<String>{
        return mutableListOf("Select Title", "Mr", "Miss", "Mrs", "Dr", "Alhaji", "Hajia")
    }

    fun getGender():MutableList<String>{
        return mutableListOf("Select Gender", "Male", "Female")
    }

    fun getType():MutableList<String>{
        return mutableListOf("Select Type", "Staff", "Client")
    }

    fun currencyBy2decimals(num: Float): String {
        val df = DecimalFormat("#.##")
        df.maximumFractionDigits = 2
        return df.format(num)
    }

    fun getCountryCode(number :String):String {
        if (number.startsWith("0")){
            return number
        }
        val phoneNumberUtil = PhoneNumberUtil.getInstance()

        val parsedNumber: PhoneNumber = try {
            phoneNumberUtil.parse(number, null)
        } catch (e: java.lang.Exception) {
            // Handle any parsing exceptions
            e.printStackTrace()
            return ""
        }

        val countryCode = parsedNumber.countryCode

        return countryCode.toString()
    }

    fun getFileExtensionFromUri(contentResolver: ContentResolver, uri: Uri): String? {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        val extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
        return extension?.lowercase(Locale.getDefault())
    }

    fun getAllPictureFormats(): ArrayList<String> {
        val pictureFormats = ArrayList<String>()
        pictureFormats.add("jpg")
        pictureFormats.add("jpeg")
        pictureFormats.add("png")
        pictureFormats.add("gif")
        pictureFormats.add("bmp")
        pictureFormats.add("tiff")
        pictureFormats.add("tif")
        pictureFormats.add("webp")
        pictureFormats.add("heif")
        pictureFormats.add("heic")
        pictureFormats.add("svg")
        pictureFormats.add("ico")

        return pictureFormats
    }

    fun getAllAudioFormats(): ArrayList<String> {
        val pictureFormats = ArrayList<String>()
        pictureFormats.add("mp3")
        pictureFormats.add("aac")
        pictureFormats.add("wav")
        pictureFormats.add("flac")
        pictureFormats.add("ogg")
        pictureFormats.add("wma")
        pictureFormats.add("aiff")
        pictureFormats.add("aif")
        pictureFormats.add("m4a")
        pictureFormats.add("amr")
        pictureFormats.add("mid")
        pictureFormats.add("midi")
        pictureFormats.add("opus")
        pictureFormats.add("wv")
        pictureFormats.add("mp1")
        pictureFormats.add("mp2")
        pictureFormats.add("ac3")


        return pictureFormats
    }

    fun getAllDocFormats(): ArrayList<String> {
        val pictureFormats = ArrayList<String>()
        pictureFormats.add("doc")
        pictureFormats.add("docx")
        pictureFormats.add("xls")
        pictureFormats.add("xlsx")
        pictureFormats.add("ppt")
        pictureFormats.add("pptx")
        pictureFormats.add("txt")
        pictureFormats.add("rtf")
        pictureFormats.add("odt")
        pictureFormats.add("ods")
        pictureFormats.add("odp")
        pictureFormats.add("pdfa")

        return pictureFormats
    }

    fun getAllVidFormats(): ArrayList<String> {
        val pictureFormats = ArrayList<String>()
        pictureFormats.add("mp4")
        pictureFormats.add("3gp")
        pictureFormats.add("mkv")
        pictureFormats.add("avi")
        pictureFormats.add("webm")
        pictureFormats.add("m4v")
        pictureFormats.add("wmv")
        pictureFormats.add("flv")
        pictureFormats.add("mov")
        pictureFormats.add("ogv")

        return pictureFormats
    }


    fun getAbsolutePathFromUri(context: Context, uri: Uri): String? {
        val documentFile = DocumentFile.fromSingleUri(context, uri)
        return documentFile?.let {
            if (it.isFile) {
                it.uri.path
            } else {
                null // Handle directories or other non-file URIs
            }
        }
    }

    fun getImageFromPart(value:String):Int{
        if (value.equals("back")){
            return R.drawable.m5
        }else if (value == "Shoulders"){
            return R.drawable.shoulders
        }else if (value == "Triceps"){
            return R.drawable.triceps
        }else if (value == "Chest"){
            return R.drawable.chest
        }else if (value.equals("Abdominals")){
            return R.drawable.abdominals
        }else if (value.equals("Biceps")){
            return R.drawable.upper_arm_biceps
        }else if (value.equals("Forearms")){
            return R.drawable.lower_arms_foreams
        }else if (value.equals("Mid back")){
            return R.drawable.back_muscle
        }else if (value.equals("Lower Back")){
            return R.drawable.lowerback
        }else if (value.equals("Glutes")){
            return R.drawable.glutes
        }else if (value.equals("Hamstring")){
            return R.drawable.hamstring
        }else if (value.equals("Calves")){
            return R.drawable.calves
        }else {
            return R.drawable.m5
        }
    }



    fun getBodyParts(): ArrayList<String>{
        val parts = ArrayList<String>()
        parts.add("back")
        parts.add("Shoulders")
        parts.add("Triceps")
        parts.add("Chest")
        parts.add("Abdominals")
        parts.add("Biceps")
        parts.add("Forearms")
        parts.add("Mid back")
        parts.add("Lower back")
        parts.add("Glutes")
        parts.add("Hamstring")
        parts.add("Calves")



        return parts
    }

    fun getBodyPartsDraw(): ArrayList<Int>{
        val parts = ArrayList<Int>()
        parts.add(R.drawable.m5)
        parts.add(R.drawable.shoulders)
        parts.add(R.drawable.triceps)
        parts.add(R.drawable.chest)
        parts.add(R.drawable.abdominals)
        parts.add(R.drawable.upper_arm_biceps)
        parts.add(R.drawable.lower_arms_foreams)
        parts.add(R.drawable.back_muscle)
        parts.add(R.drawable.lowerback)
        parts.add(R.drawable.glutes)
        parts.add(R.drawable.hamstring)
        parts.add(R.drawable.calves)

        return parts
    }

    fun getBodyPartDraw(part:String): Int{
        if(part == "back"){
            return R.drawable.m8
        }else if(part == "cardio"){
            return R.drawable.fullbody
        }else if(part == "chest"){
            return R.drawable.chest
        }else if(part == "lower arms"){
            return R.drawable.m8
        }else if(part == "lower legs"){
            return R.drawable.lower_arms_foreams
        }else if(part == "neck"){
            return R.drawable.backneck
        }else if(part == "shoulders"){
            return R.drawable.shoulders
        }else if(part == "upper arms"){
            return R.drawable.upper_arm_biceps
        }else if(part == "upper legs"){
            return R.drawable.upper_leg_quad
        }else if(part == "waist"){
            return R.drawable.lowerback
        }else {
            return R.drawable.m5
        }
    }

    fun getStartDraw(): ArrayList<Int> {
        val parts = ArrayList<Int>()
        parts.add(R.drawable.icons8_middle_back)
        parts.add(R.drawable.icons8_dumbbell)
        parts.add(R.drawable.icons8_bench_press)
        parts.add(R.drawable.icons8_pushups)
        parts.add(R.drawable.icons8_calculator)
        parts.add(R.drawable.icons8_qr_code)
        parts.add(R.drawable.spin_wheel)



        return parts
    }

    fun getStart(): ArrayList<String> {
        val parts = ArrayList<String>()
        parts.add("Body")
        parts.add("Equipment")
        parts.add("Workouts")
        parts.add("My Workouts")
        parts.add("Calculator")
        parts.add("Use Code")
        parts.add("Spin")



        return parts
    }

    fun getStartNav(): ArrayList<Fragment> {
        val parts = ArrayList<Fragment>()
        parts.add(Body())
        parts.add(Main())
        parts.add(Workouts())
        parts.add(MyExercises())
        parts.add(Calculators())
        parts.add(ScanNow())
        parts.add(Send())



        return parts
    }

    fun getEquipmentOther() : ArrayList<String>{
        val equipment = ArrayList<String>()
        equipment.add("Assisted")
        equipment.add("Band")
        equipment.add("Barbells")
        equipment.add("Body weight")
        equipment.add("bosu ball")
        equipment.add("cable")
        equipment.add("Dumbbell")
        equipment.add("elliptical machine")
        equipment.add("ez barbell")
        equipment.add("hammer")
        equipment.add("kettlebell")
        equipment.add("leverage machine")
        equipment.add("medicine ball")
        equipment.add("olympic barbell")
        equipment.add("resistance band")
        equipment.add("roller")
        equipment.add("rope")
        equipment.add("skierg machine")
        equipment.add("sled machine")
        equipment.add("smith machine")
        equipment.add("stability ball")
        equipment.add("stationary bike")
        equipment.add("stepmill machine")
        equipment.add("tire")
        equipment.add("trap bar")
        equipment.add("upper body ergometer")
        equipment.add("weighted")
        equipment.add("wheel roller")

        return equipment
    }

    fun getEquip() : ArrayList<String> {
        val equipment = ArrayList<String>()
        equipment.add("Barbell")
        equipment.add("Dumbbells")
        equipment.add("Kettlebells")
        equipment.add("Stretches")
        equipment.add("Cables")
        equipment.add("Band")
        equipment.add("Plate")
        equipment.add("TRX")
        equipment.add("Bodyweight")
        equipment.add("Yoga")
        equipment.add("Machine")

        return equipment

    }

    fun getBodyEquipDraw(): ArrayList<Int> {
        val equipment = ArrayList<Int>()
        equipment.add(R.drawable.barbell)
        equipment.add(R.drawable.dumbbell)
        equipment.add(R.drawable.kettleball)
        equipment.add(R.drawable.stretch)//Stretches
        equipment.add(R.drawable.cable)
        equipment.add(R.drawable.band)
        equipment.add(R.drawable.weighted)
        equipment.add(R.drawable.trx) //TRX
        equipment.add(R.drawable.bodyweight)
        equipment.add(R.drawable.yoga)
        equipment.add(R.drawable.stationary)



        return equipment
    }

    fun getEquipment() : ArrayList<String>{
        val equipment = ArrayList<String>()
        equipment.add("assisted")
        equipment.add("Band")
        equipment.add("Barbell")
        equipment.add("Bodyweight")
        equipment.add("bosu ball")
        equipment.add("Cables")
        equipment.add("Dumbbells")
        equipment.add("elliptical machine")
        equipment.add("ez barbell")
        equipment.add("hammer")
        equipment.add("kettlebell")
        equipment.add("leverage machine")
        equipment.add("medicine ball")
        equipment.add("olympic barbell")
        equipment.add("resistance band")
        equipment.add("roller")
        equipment.add("rope")
        equipment.add("skierg machine")
        equipment.add("sled machine")
        equipment.add("smith machine")
        equipment.add("stability ball")
        equipment.add("stationary bike")
        equipment.add("stepmill machine")
        equipment.add("tire")
        equipment.add("trap bar")
        equipment.add("upper body ergometer")
        equipment.add("weighted")
        equipment.add("wheel roller")

        return equipment
    }

    fun getBodyEquipmentDraw(): ArrayList<Int> {
        val equipment = ArrayList<Int>()
        equipment.add(R.drawable.assisted)
        equipment.add(R.drawable.band)
        equipment.add(R.drawable.barbell)
        equipment.add(R.drawable.bodyweight)
        equipment.add(R.drawable.bosuball)
        equipment.add(R.drawable.cable)
        equipment.add(R.drawable.dumbbell)
        equipment.add(R.drawable.elliptical)
        equipment.add(R.drawable.ezbarbell)
        equipment.add(R.drawable.hammer)
        equipment.add(R.drawable.kettleball)
        equipment.add(R.drawable.leverage)
        equipment.add(R.drawable.medicine)
        equipment.add(R.drawable.olympicbarbell)
        equipment.add(R.drawable.resistanceband)
        equipment.add(R.drawable.roller)
        equipment.add(R.drawable.rope)
        equipment.add(R.drawable.skierg)
        equipment.add(R.drawable.sled)
        equipment.add(R.drawable.smith)
        equipment.add(R.drawable.stability)
        equipment.add(R.drawable.stationary)
        equipment.add(R.drawable.assisted)
        equipment.add(R.drawable.stepmill)
        equipment.add(R.drawable.tire)
        equipment.add(R.drawable.trapbar)
        equipment.add(R.drawable.ergormeter)
        equipment.add(R.drawable.weighted)
        equipment.add(R.drawable.whellroller)

        return equipment
    }

    fun getEquipbyEx(value:String):Int{
        if(value.equals("Barbell")){
            val equips = arrayOf(R.drawable.barbell, R.drawable.barbells)
            return equips[getRand(0, equips.size-1)]
        }else if(value.equals("Dumbbells")){
            val equips = arrayOf(R.drawable.dumbbell, R.drawable.dumbbell2, R.drawable.dumbbell5, R.drawable.dumbells4, R.drawable.dumbell3)
            return equips[getRand(0, equips.size-1)]
        }else if(value.equals("Kettlebells")){
            val equips = arrayOf(R.drawable.kettleball, R.drawable.kettle4, R.drawable.kettle2, R.drawable.kettle3)
            return equips[getRand(0, equips.size-1)]
        }else if(value.equals("Stretches")){
            val equips = arrayOf(R.drawable.stretch, R.drawable.band)
            return equips[getRand(0, equips.size-1)]
        }else if(value.equals("Cables")){
            val equips = arrayOf(R.drawable.cable, R.drawable.cable2, R.drawable.cable3, R.drawable.cable4, R.drawable.cable5)
            return equips[getRand(0, equips.size-1)]
        }else if(value.equals("Band")){
            val equips = arrayOf(R.drawable.band, R.drawable.resistanceband)
            return equips[getRand(0, equips.size-1)]
        }else if(value.equals("Plate")){
            val equips = arrayOf(R.drawable.weighted, R.drawable.band)
            return equips[getRand(0, equips.size-1)]
        }else if(value.equals("TRX")){
            val equips = arrayOf(R.drawable.trx, R.drawable.trx2, R.drawable.trx4, R.drawable.trx3, R.drawable.trx0 )
            return equips[getRand(0, equips.size-1)]
        }else if(value.equals("Bodyweight")){
            val equips = arrayOf(R.drawable.bodyweight, R.drawable.body2, R.drawable.body3, R.drawable.body4, R.drawable.body5)
            return equips[getRand(0, equips.size-1)]
        }else if(value.equals("Yoga")){
            val equips = arrayOf(R.drawable.yoga, R.drawable.band)
            return equips[getRand(0, equips.size-1)]
        }else if(value.equals("Machine")){
            val equips = arrayOf(R.drawable.skierg, R.drawable.sled, R.drawable.ergormeter, R.drawable.elliptical)
            return equips[getRand(0, equips.size-1)]
        }else{
            return R.drawable.band
        }

    }

    fun getRand(min: Int, max: Int): Int {

        val random = Random()
        return random.nextInt(max - min + 1) + min
    }

    fun getGifs(): ArrayList<String> {
        return arrayListOf("TRX-Mountain-Climber", "Walking", "Cable-Rope-Overhead-Triceps-Extension","Anjaneyasana-Low-Lunge-Pose",
            "Anuvittasana-Standing-Backbend-Pose", "Archer-Push-Up", "Arm-Circles_Shoulders", "Arm-Scissors", "Ardha-Uttanasana-Standing-Half-Forward-Bend-yoga",
            "Balasana-Child-Pose", "Barbell-Bench-Press", "Barbell-Bent-Over-Row", "Barbell-Upright-Row", "Bear-Crawl",
            "Bench-Pike-Push-up", "Bent-Over-Barbell-Reverse-Raise", "Bodyweight-Single-Leg-Deadlift", "Bodyweight-Squat",
            "Bridge-Pose", "Briskly-Walking", "burpees", "cable-rear-delt-fly", "Clap-Push-Up", "Cossack-Squat",
            "Cross-Arm-Push-up", "Cross-Body-Push-up-Plyometric", "Dandayamana-Bharmanasana-Balancing-Table-Pose",
            "Dead-Bug", "Decline-Push-Up", "Diamond-Push-up", "Drop-Push-Up", "Dumbbell-Raise", "Dumbbell-Shrug",
            "Face-Pull", "Fast-Feet-Run", "Forearm-Push-up", "Hamstrings-Lying-Stretch", "High-Knee-Skips_Cardio", "Incline-Barbell-Bench-Press",
            "INNER-THIGH-SIDE-STRETCH", "Jump-Squat", "Kneeling-High-Pulley-Row", "Kneeling-Push-up",
            "Lever-Shrug", "Lying-Leg-Raise", "Lying-Scissor-Kick", "Modified-Hindu-Push-up", "One-Arm-Push-Ups-With-Support",
            "Pec-Deck-Fly", "Pike-Push-up", "Pike-to-Cobra", "Plyo-Jacks", "Prone-Cervical-Extension",
            "Pull-up", "Push-Up-Plus", "Push-Up", "Rear-Delt-Machine-Flys", "Reverse-Lunge-Knee",
            "Ring-Inverted-Row", "Rotating-Neck-Stretch", "Rowing-Boat-Yoga-Pose", "Run", "Side-Plank-Leg-Raises",
            "Skater", "Sphinx-Stretch", "Spider-Plank", "Split-Squat", "stability-ball-decline-push-ups", "Standing-Cross-Leg-Hamstring-Stretch",
            "Standing-Quadriceps-Stretch", "Suspended-Push-Up", "Swimming", "Tadasana-Mountain-Pose", "Tiger-Yoga-Pose",
            "Titli-Asana-Butterfly-Pose", "Triceps-dips-on-floors", "Trx-Chest-Flyes", "Trx-Chest-Press",
            "TRX-Mountain-Climber", "TRX-Pistol-Squat", "Upavistha-Konasana", "Ustrasana", "Utthita-Trikonasana-Extended-Triangle-Pose",
            "wall-slide", "Warrior-1-Pose", "Warrior-3-Pose", "Wrist-Circles-Stretch", "Adho-Mukha-Svanasana-Downward-Facing-Dog-Yoga")
    }

    // weight in kilograms
    // height in meters
    fun calculateBMI(weight: Double, height: Double, txtBMI : TextView, txtLabel:TextView,
                     txtExplain : TextView) {
        var bmi =  weight / (height * height)

        bmi = String.format("%.2f", bmi).toDouble()
        txtBMI.text = bmi.toString()
        txtLabel.visibility = View.VISIBLE
        if(bmi<18.5){
            txtExplain.text = "Your BMI is considered underweight. Keep in mind that an underweight BMI calculation may pose certain health risks. Please consult your healthcare provider for more information about BMI calculations."
        }else if(bmi<24.9){
            txtExplain.text = "Your BMI is considered normal. This healthy weight helps reduce your risk of serious health conditions and means you’re close to your fitness goals."
        }else if(bmi<29.9){
            txtExplain.text = "You’re in the overweight range. You are at increased risk for a variety of illnesses at your present weight. You should lose weight by changing your diet and exercising more."
        }else{
            txtExplain.text = "Your BMI is considered overweight. Being overweight may increase your risk of cardiovascular disease. Consider making lifestyle changes through healthy eating and fitness to improve your health."
        }

    }

    fun calculateBodyFatPercentage(gender: String, age: Int, weight: Double, height: Double, skinfoldMeasurements: List<Double>): Double {
        // Constants for the Jackson-Pollock equation
        val a = if (gender == "male") 1.10938 else 1.0994921
        val b = if (gender == "male") 0.0008267 else 0.0009929
        val c = if (gender == "male") 0.0000016 else 0.0000023
        val d = if (gender == "male") 0.76614 else 0.54626

        // Sum of skinfold measurements
        val sumOfSkinfolds = skinfoldMeasurements.sum()

        // Body fat percentage calculation
        val bodyFatPercentage = a - (b * sumOfSkinfolds) + (c * sumOfSkinfolds * sumOfSkinfolds) - (d * age)

        return bodyFatPercentage
    }

    fun main() {
        val gender = "female"
        val age = 25
        val weight = 60.0 // weight in kilograms
        val height = 165.0 // height in centimeters
        val skinfoldMeasurements = listOf(10.0, 12.0, 9.0) // in millimeters

        val bodyFatPercentage = calculateBodyFatPercentage(gender, age, weight, height, skinfoldMeasurements)

        println("Estimated Body Fat Percentage: %.2f".format(bodyFatPercentage))
    }


    fun convertToBase64(imageView: ImageView): String {
        val drawable: Drawable? = imageView.drawable
        val bitmap: Bitmap = (drawable as BitmapDrawable).bitmap
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray: ByteArray = outputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)

    }

    fun convertBitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray: ByteArray = outputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)

    }

    fun decodeBase64(encodedString: String, context: Context): Drawable {
        val decodedString: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
        val bitmap: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

        return BitmapDrawable(context.resources, bitmap)

    }

    fun generateRandomCode(count:Int): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val codeLength = count
        val random = Random()

        val sb = StringBuilder(codeLength)
        for (i in 0 until codeLength) {
            val randomIndex = random.nextInt(characters.length)
            val randomChar = characters[randomIndex]
            sb.append(randomChar)
        }

        return sb.toString()
    }


    val timestamp = System.currentTimeMillis()



    fun sendToWhatsapp(message: String, bitmap: Bitmap, context: Context){
// Create a temporary file to save the Bitmap
        val file = File(context.cacheDir, "image.png")
        try {
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

       // val imageFile = File(bitmap)

// Get the URI for the image file using FileProvider
        val fileUri: Uri = FileProvider.getUriForFile(
            context,
            "your.package.name.fileprovider",
            file
        )

// Create the intent to share the image via WhatsApp
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "*/*"
        val imageUri: Uri = Uri.fromFile(file)
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        shareIntent.setPackage("com.whatsapp") // Set the package name of WhatsApp
        context.startActivity(shareIntent)
// Verify that WhatsApp is installed on the device
        if (shareIntent.resolveActivity(context.packageManager) != null) {

        } else {
            Toast.makeText(context, "Install whatsapp", Toast.LENGTH_SHORT).show()
            // WhatsApp is not installed
            // Handle this case or show an error message
        }
    }

    fun sendMessToWhatsApp(message: String, context: Context){
// Create the intent to share the message via WhatsApp
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        shareIntent.setPackage("com.whatsapp") // Set the package name of WhatsApp
        context.startActivity(shareIntent)
// Verify that WhatsApp is installed on the device
        if (shareIntent.resolveActivity(context.packageManager) != null) {

        } else {
            Toast.makeText(context, "Install whatsapp", Toast.LENGTH_SHORT).show()
            // WhatsApp is not installed
            // Handle this case or show an error message
        }
    }

    fun getFilePathFromUri(context: Context, uri: Uri): String? {
        var filePath: String? = null

        // Handle different URI schemes
        when {
            "file" == uri.scheme -> {
                filePath = uri.path
            }
            "content" == uri.scheme -> {
                filePath = getFilePathFromContentUri(context, uri)
            }
            else -> {
                // Unsupported URI scheme
                filePath = null
            }
        }

        return filePath
    }

    private fun getFilePathFromContentUri(context: Context, uri: Uri): String? {
        val contentResolver: ContentResolver = context.contentResolver

        // Check if the URI is a MediaStore content URI
        if (isMediaStoreUri(uri)) {
            val projection = arrayOf(MediaStore.MediaColumns.DATA)

            val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                    return it.getString(columnIndex)
                }
            }
        }

        // Fallback to DocumentFile for other content URIs
        val documentFile = DocumentFile.fromSingleUri(context, uri)
        return documentFile?.uri?.path
    }

    private fun isMediaStoreUri(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
                || "media" == uri.authority
                || "downloads" == uri.authority
    }

    fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null

        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                fileName = it.getString(displayNameIndex)
            }
        }

        return fileName
    }

    fun scanQRCodeFromImageView(imageView: ImageView): String? {
        val drawable: Drawable? = imageView.drawable
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            val intArray = IntArray(bitmap.width * bitmap.height)
            bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
            val source = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            val reader = MultiFormatReader()
            try {
                val result = reader.decode(binaryBitmap)
                return result.text

            } catch (e: Exception) {
                Log.e("QRCodeScanner", "Error scanning QR code: ${e.message}")
            }
        } else {
            Log.e("QRCodeScanner", "Invalid ImageView drawable")
        }
        return null
    }


}