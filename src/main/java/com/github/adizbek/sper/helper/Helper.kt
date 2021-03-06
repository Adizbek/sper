package com.github.adizbek.sper.helper

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.text.Html
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.util.Base64
import android.util.Base64OutputStream
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.subutil.util.ClipboardUtils
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.ToastUtils
import com.github.adizbek.sper.BaseApplication
import com.github.adizbek.sper.R
import com.github.adizbek.sper.Sper
import com.github.adizbek.sper.net.PicassoImageGetter
import com.mikepenz.fastadapter.FastAdapter
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.lang.Float.parseFloat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


/**
 * Created by adizbek on 2/18/18.
 */


fun String.md5(): String {
    return EncryptUtils.encryptMD5ToString(this);
}

object Helper {

    fun getUniqueID(): String {
        val deviceID =
            "35" + (Build.BOARD.length % 10) + (Build.BRAND.length % 10) + (Build.CPU_ABI.length % 10) + (Build.DEVICE.length % 10) + (Build.MANUFACTURER.length % 10) + (Build.MODEL.length % 10) + (Build.PRODUCT.length % 10)

        var serial: String

        try {
            serial = android.os.Build::class.java.getField("SERIAL").get(null).toString()
            return UUID(deviceID.hashCode().toLong(), serial.hashCode().toLong()).toString();
        } catch (exception: Exception) {
            serial = "serial"
        }

        return UUID(deviceID.hashCode().toLong(), serial.hashCode().toLong()).toString()
    }

    val defaultLang = "ru"
    private var locale: Locale? = null

    val lang: String
        get() = PreferenceManager.getDefaultSharedPreferences(Sper.getContext()).getString(
            "language",
            defaultLang
        )

    //    public static String urlImage(String src) {
    //        return urlImage(src, Application.getHost());
    //    }
    //
    //    public static String urlImage(String src, String host) {
    //        return String.format("%s/%s", host, src);
    //    }

    fun animateCurrency(to: Float, text: TextView) {
        val floatAnimator: ValueAnimator = ValueAnimator.ofFloat(0f, to)

        floatAnimator.startDelay = 1000
        floatAnimator.duration = 1000

        floatAnimator.addUpdateListener {
            val x = it.animatedValue as Float

            text.text = currencyFormatter(x)
        }

        floatAnimator.start()
    }

    fun setupToolbarBack(context: AppCompatActivity, v: View, title: Int) {
        setupToolbarBack(context, v, context.getString(title))
    }

    fun setupToolbarBack(context: AppCompatActivity, v: View, title: String): Toolbar {
        val toolbar = v.findViewById<Toolbar>(R.id.toolbar)
        context.setSupportActionBar(toolbar)
        toolbar.title = title

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)

        return toolbar
    }

    fun stringField(str: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), str)
    }

    fun stringField(str: Int): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), str.toString())
    }

    fun currencyFormatter(`in`: String): String {
        try {
            return currencyFormatter(parseFloat(`in`))
        } catch (e: Exception) {
            return currencyFormatter(0f)
        }

    }

    fun currencyFormatter(`in`: Float): String {
        val sym = DecimalFormatSymbols.getInstance()
        sym.groupingSeparator = ' '
        val formatter = DecimalFormat("#,###.##", sym)

        return formatter.format(`in`.toDouble())
    }


    fun loadSavedLang() {
        changeLocale(lang)
    }

    fun changeLocale(lang: String) {
        locale = Locale(lang)
        Locale.setDefault(locale)

        val config = Configuration()
        config.locale = locale

        BaseApplication.c.resources.updateConfiguration(config, BaseApplication.c.resources.displayMetrics)
    }

    fun bindHtml(tv: TextView, string: String) {
        // TODO correct img loading.
        val imageGetter = PicassoImageGetter(tv)
        val html: Spannable

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            html = Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY, imageGetter, null) as Spannable
        } else {
            html = Html.fromHtml(string, imageGetter, null) as Spannable
        }

        tv.movementMethod = LinkMovementMethod.getInstance()
        tv.text = html
    }

    fun bindHtml(tv: TextView, @StringRes res: Int) {
        bindHtml(tv, tv.context.resources.getString(res))
    }

    private fun getLocale(): Locale? {
        if (locale == null)
            loadSavedLang()

        return locale
    }

    fun setupToolbar(context: AppCompatActivity, v: View, title: Int): Toolbar {
        return setupToolbar(context, v, context.getString(title))
    }

    fun setupToolbar(context: AppCompatActivity, v: View, title: String): Toolbar {
        val toolbar = v.findViewById<Toolbar>(R.id.toolbar)
        context.setSupportActionBar(toolbar)
        toolbar.title = title

        val drawer = context.findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            context, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer.addDrawerListener(toggle)
        toggle.syncState()

        return toolbar
    }


    class Bundle {
        private val _bundle: android.os.Bundle

        init {
            _bundle = android.os.Bundle()
        }

        fun putInt(id: String, uid: Int): Bundle {
            _bundle.putInt(id, uid)
            return this
        }

        fun putParcelable(id: String, uid: Parcelable): Bundle {
            _bundle.putParcelable(id, uid)
            return this
        }

        fun putString(id: String, uid: String): Bundle {
            _bundle.putString(id, uid)
            return this
        }

        fun get(): android.os.Bundle {
            return _bundle
        }

    }

    object UI {
        val TAG = "StarterProject:UIHelper"

        fun rippleEffect(view: View) {
            try {
                val attrs = intArrayOf(R.attr.selectableItemBackground)
                val typedArray = view.context.obtainStyledAttributes(attrs)
                val backgroundResource = typedArray.getResourceId(0, 0)
                view.setBackgroundResource(backgroundResource)
                typedArray.recycle()
            } catch (e: NullPointerException) {
                Log.w(TAG, "rippleEffect: error")
            }

        }


        fun toolbar(activity: AppCompatActivity, toolbar: Toolbar, @StringRes title: Int = 0) {
            toolbar(activity, toolbar, if (title == 0) "" else activity.getString(title))
        }


        fun toolbar(activity: AppCompatActivity, toolbar: Toolbar, title: String?) {
            activity.setSupportActionBar(toolbar)

            toolbar.title = title

            val drawer = activity.findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawer_layout)
            val toggle = ActionBarDrawerToggle(
                activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
            )

            drawer.addDrawerListener(toggle)
            toggle.syncState()

        }

        fun setupList(
            list: androidx.recyclerview.widget.RecyclerView,
            adapter: FastAdapter<*>,
            context: Context,
            addDecorator: Boolean = true,
            decorPadding: Int = 0
        ) {
            list.isNestedScrollingEnabled = false
            list.setHasFixedSize(false)
            list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            list.itemAnimator = DefaultItemAnimator()

            list.adapter = adapter


            if (list.itemDecorationCount == 0 && addDecorator)
                addDecorater(list, decorPadding)
        }

        fun addDecorater(list: androidx.recyclerview.widget.RecyclerView, decorPadding: Int) {
            val context = list.context
            val attrs = intArrayOf(android.R.attr.listDivider)
            val a = context.obtainStyledAttributes(attrs)
            val divider = a.getDrawable(0)
            val inset = ConvertUtils.px2dp(decorPadding.toFloat())
            val insetDivider = InsetDrawable(divider, inset, 0, inset, 0);
            a.recycle()

            val itemDecoration = object : DividerItemDecoration(context, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL) {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: androidx.recyclerview.widget.RecyclerView,
                    state: androidx.recyclerview.widget.RecyclerView.State
                ) {
                    val position = parent.getChildAdapterPosition(view);
                    // hide the divider for the last child
                    if (position == parent.adapter!!.itemCount - 1) {
                        outRect.setEmpty()
                    } else {
                        super.getItemOffsets(outRect, view, parent, state);
                    }
                }
            }
            itemDecoration.setDrawable(insetDivider)
            list.addItemDecoration(itemDecoration)
        }

    }


    //    public static void showErrorAlert(Activity activity, String text) {
    //        showAlert(activity,
    //                text,
    //                R.drawable.ic_info_outline_white_24dp,
    //                android.R.color.holo_red_light);
    //    }
    //
    //    public static void showAlert(Activity c, String text, int icon, int color) {
    //        Alerter.create(c)
    //                .setText(text)
    //                .setIcon(icon)
    //                .setBackgroundColorRes(color)
    //                .show();
    //    }

    object Dialog {
        fun showMessage(activity: Activity, text: String) {
            showMessage(activity, activity.getString(R.string.dialog_message_title), text)
        }

        //
        fun showMessage(activity: Activity, title: String, text: String) {
            val builder = AlertDialog.Builder(
                activity)

            builder.setTitle(title)
                .setMessage(text)
                .setPositiveButton(R.string.ok, null)
                .show()

        }
    }

    //    public static class DateUtils {
    //        public static int MINUTE = 60;
    //        public static int HOUR = MINUTE * 60;
    //        public static int DAY = HOUR * 24;
    //
    //        public static boolean isOnline(Date date) {
    //            Calendar calendar = Calendar.getInstance();
    //
    //            calendar.add(Calendar.MINUTE, -5);
    //
    //            return date.after(calendar.getTime());
    //        }
    //
    //        /**
    //         * Get Human readable text of date
    //         */
    //        public static String getHRText(Date date) {
    //            Date now = new Date();
    //            long nt = now.getTime() / 1000;
    //            long dt = date.getTime() / 1000;
    //
    //            Context c = Application.c;
    //
    //            if (nt >= dt) {
    //                if (nt - dt < MINUTE) {
    //                    return c.getString(R.string.date_just_now);
    //                }
    //
    //                if (nt - dt < HOUR) {
    //                    int min = (int) Math.ceil((nt - dt) / MINUTE);
    //                    return c.getString(R.string.date_n_minute_ago, min);
    //                }
    //
    //                if (getYesterday().before(date)) {
    //                    if (now.getDate() == date.getDate()) {
    //                        int hour = (int) Math.ceil((nt - dt) / HOUR);
    //                        return c.getString(R.string.date_n_hour_ago, hour);
    //                    } else {
    //                        return c.getString(R.string.date_yesterday_at, date2Custom(date, "HH:mm"));
    //                    }
    //                }
    //
    //                if (getYearStart().before(date)) {
    //                    return date2Custom(date, "dd-MMM HH:mm");
    //                } else {
    //                    return date2Custom(date, "dd-MMM, YYYY");
    //                }
    //            }
    //
    //            return date2Custom(date, "dd-MMM, YYYY");
    //        }
    //
    //        public static String getOnlineText(Date date) {
    //            if (isOnline(date)) {
    //                return Application.c.getString(R.string.online);
    //            } else {
    //                return Application.c.getString(R.string.was_online, getHRText(date));
    //            }
    //        }
    //
    //        public static Date getYesterday() {
    //            Calendar calendar = Calendar.getInstance();
    //
    //            calendar.set(Calendar.MINUTE, 0);
    //            calendar.set(Calendar.HOUR, 0);
    //            calendar.set(Calendar.SECOND, 0);
    //
    //            calendar.add(Calendar.HOUR, -24);
    //
    //            return calendar.getTime();
    //        }
    //
    //        public static Date getYearStart() {
    //            Calendar calendar = Calendar.getInstance();
    //
    //            calendar.set(Calendar.MINUTE, 0);
    //            calendar.set(Calendar.HOUR, 0);
    //            calendar.set(Calendar.SECOND, 0);
    //            calendar.set(Calendar.DAY_OF_YEAR, 1);
    //
    //            return calendar.getTime();
    //        }
    //    }

    object ImageUtils {
        @JvmOverloads
        fun decodeSampledBitmapFromResource(file: File, size: Int = 800): Bitmap {

            // First decode with inJustDecodeBounds=true to check dimensions
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.path, options)

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, size)

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeFile(file.path, options)
        }

        fun calculateInSampleSize(
            options: BitmapFactory.Options, maxSize: Int
        ): Int {

            // Raw height and width of image
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            //        if (height > reqHeight || width > reqWidth) {
            //
            //            final int halfHeight = height / 2;
            //            final int halfWidth = width / 2;
            //
            //            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            //            // height and width larger than the requested height and width.
            //            while ((halfHeight / inSampleSize) >= reqHeight
            //                    && (halfWidth / inSampleSize) >= reqWidth) {
            //                inSampleSize *= 2;
            //            }
            //        }

            if (Math.max(width, height) > maxSize) {
                if (width > height) {
                    val scale = 800.toFloat() / width
                    inSampleSize = (1 / scale).toInt()
                    //                width = 800;
                    //                height *= scale;
                } else {
                    val scale = 800.toFloat() / height
                    //                height = 800;
                    //                width *= scale;
                    inSampleSize = (1 / scale).toInt()
                }
            }

            return inSampleSize
        }

        fun getBytes(bmp: Bitmap): ByteArray {
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 90, stream)
            val byteArray = stream.toByteArray()
            bmp.recycle()

            try {
                stream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }


            return byteArray
        }

        fun resize(file: File, size: Int): ByteArray {
            return getBytes(decodeSampledBitmapFromResource(file, size))
        }

        fun encodeTobase64(image: Bitmap): String {
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 90, baos)
            val b = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }

        fun decodeBase64(input: String): Bitmap {
            val decodedByte = Base64.decode(input, 0)
            return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
        }
    }

    private class Docs
    /**
     *
     * Tue, 22 Jan 2019 00:00:00 +0500 =
     * @url http://www.java2s.com/Tutorial/Java/0120__Development/0190__SimpleDateFormat.htm
     * 6.12.SimpleDateFormat
     * 6.12.1.	Get Today's Date
     * 6.12.2.	new SimpleDateFormat('hh')
     * 6.12.3.	new SimpleDateFormat('H') // The hour (0-23)
     * 6.12.4.	new SimpleDateFormat('m'):9 The minutes
     * 6.12.5.	new SimpleDateFormat('mm')
     * 6.12.6.	new SimpleDateFormat('s'): The seconds
     * 6.12.7.	new SimpleDateFormat('ss')
     * 6.12.8.	new SimpleDateFormat('a'): The am/pm marker
     * 6.12.9.	new SimpleDateFormat('z'): The time zone
     * 6.12.10.	new SimpleDateFormat('zzzz')
     * 6.12.11.	new SimpleDateFormat('Z')
     * 6.12.12.	new SimpleDateFormat('hh:mm:ss a')
     * 6.12.13.	new SimpleDateFormat('HH.mm.ss')
     * 6.12.14.	new SimpleDateFormat('HH:mm:ss Z')
     * 6.12.15.	The day token: SimpleDateFormat('d')
     * 6.12.16.	Two digits day token: SimpleDateFormat('dd')
     * 6.12.17.	The day in week: SimpleDateFormat('E')
     * 6.12.18.	Full day name: SimpleDateFormat('EEEE')
     * 6.12.19.	SimpleDateFormat('MM'): token based month value
     * 6.12.20.	SimpleDateFormat('MM/dd/yy')
     * 6.12.21.	SimpleDateFormat('dd-MMM-yy')
     * 6.12.22.	The month: SimpleDateFormat('M')
     * 6.12.23.	SimpleDateFormat('E, dd MMM yyyy HH:mm:ss Z')
     * 6.12.24.	SimpleDateFormat('yyyy')
     * 6.12.25.	Three letter-month value: SimpleDateFormat('MMM')
     * 6.12.26.	Full length of month name: SimpleDateFormat('MMMM')
     * 6.12.27.	Formatting a Date Using a Custom Format
     * 6.12.28.	Formatting date with full day and month name and show time up to milliseconds with AM/PM
     * 6.12.29.	Format date in dd/mm/yyyy format
     * 6.12.30.	Format date in mm-dd-yyyy hh:mm:ss format
     * 6.12.31.	Formatting day of week using SimpleDateFormat
     * 6.12.32.	Formatting day of week in EEEE format like Sunday, Monday etc.
     * 6.12.33.	Formatting day in d format like 1,2 etc
     * 6.12.34.	Formatting day in dd format like 01, 02 etc.
     * 6.12.35.	Format hour in h (1-12 in AM/PM) format like 1, 2..12.
     * 6.12.36.	Format hour in hh (01-12 in AM/PM) format like 01, 02..12.
     * 6.12.37.	Format hour in H (0-23) format like 0, 1...23.
     * 6.12.38.	Format hour in HH (00-23) format like 00, 01..23.
     * 6.12.39.	Format hour in k (1-24) format like 1, 2..24.
     * 6.12.40.	Format hour in kk (01-24) format like 01, 02..24.
     * 6.12.41.	Format hour in K (0-11 in AM/PM) format like 0, 1..11.
     * 6.12.42.	Format hour in KK (00-11) format like 00, 01,..11.
     * 6.12.43.	Formatting minute in m format like 1,2 etc.
     * 6.12.44.	Format minutes in mm format like 01, 02 etc.
     * 6.12.45.	Format month in M format like 1,2 etc
     * 6.12.46.	Format Month in MM format like 01, 02 etc.
     * 6.12.47.	Format Month in MMM format like Jan, Feb etc.
     * 6.12.48.	Format Month in MMMM format like January, February etc.
     * 6.12.49.	Format seconds in s format like 1,2 etc.
     * 6.12.50.	Format seconds in ss format like 01, 02 etc.
     * 6.12.51.	Format TimeZone in z (General time zone) format like EST.
     * 6.12.52.	Format TimeZone in zzzz format Eastern Standard Time.
     * 6.12.53.	Format TimeZone in Z (RFC 822) format like -8000.
     * 6.12.54.	Format year in yy format like 07, 08 etc
     * 6.12.55.	Format year in yyyy format like 2007, 2008 etc.
     * 6.12.56.	Parsing custom formatted date string into Date object using SimpleDateFormat
     * 6.12.57.	Date Formatting and Localization
     * 6.12.58.	Add AM PM to time using SimpleDateFormat
     * 6.12.59.	Check if a String is a valid date
     */
}

fun TextView.bindHtml(html: String): TextView {
    Helper.bindHtml(this, html)

    return this
}


fun TextView.bindHtml(@StringRes html: Int): TextView {
    Helper.bindHtml(this, html)

    return this
}

fun String.stripHtml(): String {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(this).toString()
    }
}

fun Uri.getRealPath(ctx: Context): String? {
    var path: String?

    val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
    val cursor = ctx.contentResolver.query(this, projection, null, null, null)

    if (cursor == null) {
        path = this.path
    } else {
        cursor.moveToFirst()
        val columnIndex = cursor.getColumnIndexOrThrow(projection[0])
        path = cursor.getString(columnIndex)
        cursor.close()
    }

    return if (path == null || path.isEmpty()) this.path else path
}

fun InputStream.toBase64(): String {
    val buffer = byteArrayOf()
    var bytesRead = 0

    val output = ByteArrayOutputStream()
    val output64 = Base64OutputStream(output, Base64.DEFAULT)

    try {
        while ({ bytesRead = this.read(buffer); bytesRead }() != -1) {
            output64.write(buffer, 0, bytesRead)
        }

    } catch (e: IOException) {
        e.printStackTrace()
    }

    output64.close()

    return output.toString()
}

fun String.toast(){
    ToastUtils.showShort(this)
}

fun String.copyToClipboard() {
    ClipboardUtils.copyText(this)
    ToastUtils.showLong("Copied")
}

fun String.navigateToUrl() {
    ActivityUtils.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(this)))
}

fun String.alertDialog(activity: Activity) {
    Helper.Dialog.showMessage(activity, this)
}

fun now(pattern: String = DateHelper.defaultDateFormat): String {
    return Calendar.getInstance().time.time.toDateStr(pattern)
}