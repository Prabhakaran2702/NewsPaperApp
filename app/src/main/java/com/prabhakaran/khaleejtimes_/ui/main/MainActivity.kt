package com.prabhakaran.khaleejtimes_.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ActionTypes
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemChangeListener
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.interfaces.TouchListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.prabhakaran.khaleejtimes_.R
import com.prabhakaran.khaleejtimes_.databinding.ActivityMainBinding
import com.prabhakaran.khaleejtimes_.ui.bookmark.BookmarksActivity
import com.prabhakaran.khaleejtimes_.ui.adapter.ViewPagerAdapter
import com.prabhakaran.khaleejtimes_.ui.base.BaseActivity
import com.prabhakaran.khaleejtimes_.utils.NetworkObserver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel, MainActivity>() {

    // Create and register notification channel api 26+
    val channelId = "com.prabhakaran.khaleejtimes"
    val notificationId = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        defineLayout()
        observeUserNetworkConnection()

        observeRetrofitErrors()

        loadSliderComponents()

        createNotificationChannel(channelId)

    }



    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            viewModel.category.value = getTitle(position)
            viewModel.refreshResponse(false)


        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)

        }
    }


    private fun defineLayout() {

        setUpToolBar()

        pager.adapter = createPagerAdapter()

        pager.registerOnPageChangeCallback(onPageChangeCallback)

        setUpTabLayout()

        binding.bookmarks.setOnClickListener {
            val intent = Intent(applicationContext, BookmarksActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    private fun setUpToolBar() {
        toolbar = binding.toolBar
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setUpTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.pager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.customView = when (position) {
                    0 -> addCustomView(
                        "Top Headlines", 16f,
                        Color.WHITE
                    )
                    1 -> addCustomView("General")
                    2 -> addCustomView("Technology")
                    3 -> addCustomView("Business")
                    4 -> addCustomView("Health")
                    5 -> addCustomView("Science")
                    6 -> addCustomView("Entertainment")
                    7 -> addCustomView("Sports")
                    else -> addCustomView("null")
                }


            }).attach()

        binding.tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

                tab?.view?.children?.forEach {
                    if (it is LinearLayout) {
                        val view1 = it.getChildAt(0)

                        if (view1 is CardView)
                            view1.setCardBackgroundColor(resources.getColor(R.color.colorSecondary))

                        val view2 = view1.findViewById<TextView>(R.id.title)

                        if (view2 is TextView) {
                            view2.post {
                                view2.setTextSize(14f)
                                view2.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                            }
                        }
                    }
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {


                tab?.view?.children?.forEach {
                    if (it is LinearLayout) {
                        val view1 = it.getChildAt(0)

                        if (view1 is CardView)
                            view1.setCardBackgroundColor(resources.getColor(R.color.colorBlue))

                        val view2 = view1.findViewById<TextView>(R.id.title)

                        if (view2 is TextView) {
                            view2.post {
                                view2.setTextSize(16f)
                                view2.setTextColor(Color.WHITE)
                            }
                        }
                    }
                }

            }

        })

    }


    @SuppressLint("ResourceAsColor")
    fun addCustomView(
        title: String,
        size: Float = 14f,
        color: Int = R.color.colorPrimaryDark
    ): View {
        val view = layoutInflater.inflate(R.layout.item_tab, binding.tabLayout, false)
        val titleTextView = view.findViewById<TextView>(R.id.title)
        titleTextView.apply {
            text = title


            if (color == Color.WHITE) {
                (view.findViewById<CardView>(R.id.cardView)).setCardBackgroundColor(
                    resources.getColor(
                        R.color.colorBlue
                    )
                )
                setTextColor(color)
            } else {
                setTextColor(resources.getColor(color))
            }

            textSize = size
        }

        return view
    }


    private fun getTitle(position: Int): String {
        return when (position) {
            0 -> "Top Headlines"
            1 -> "General"
            2 -> "Technology"
            3 -> "Business"
            4 -> "Health"
            5 -> "Science"
            6 -> "Entertainment"
            7 -> "Sports"
            else -> "null"
        }
    }

    private fun createPagerAdapter(): ViewPagerAdapter =
        ViewPagerAdapter(this)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.view_options -> {

                showDialogWithRadioButtons(
                    "Choose view type", "List", "Tab", positiveButtonAction = { dialog ->
                        dialog.dismiss()
                        val radioGroup =
                            dialog.findViewById<RadioGroup>(R.id.radiogroup_dialog_main)
                        val id = radioGroup.checkedRadioButtonId
                        viewModel.changeViewType(id)
                    }
                )
            }

            R.id.show_notification -> {

                showNotification()
            }

            R.id.choose_theme -> {


                showDialogWithRadioButtons(
                    "Choose your theme", "Light", "Dark", positiveButtonAction = { dialog ->
                        dialog.dismiss()
                        val radioGroup =
                            dialog.findViewById<RadioGroup>(R.id.radiogroup_dialog_main)


                        val id = radioGroup.checkedRadioButtonId
                        when (id) {
                            R.id.radio_button1 -> {
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                                lifecycleScope.launch {
                                    prefrences.saveUserTheme("Light")
                                }
                            }
                            R.id.radio_button2 -> {
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                                lifecycleScope.launch {
                                    prefrences.saveUserTheme("Dark")
                                }
                            }
                        }
                    }
                )
            }
        }
        return true
    }


    private fun showDialogWithRadioButtons(
        titleText: String,
        firstRadioButtonText: String,
        secondRadioButtonText: String,
        negativeButtonText: String = "Cancel",
        positiveButtonText: String = "Apply",
        positiveButtonAction: (Dialog) -> Unit,
        negativeButtonAction: (Dialog) -> Unit = { dialog ->
            dialog.dismiss()
        }
    ) {


        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_main_with_radiobtns)

        val titleTextView = dialog.findViewById<TextView>(R.id.title_main_dialog)
               val radioButton1 =
            dialog.findViewById<RadioButton>(R.id.radio_button1)
        val radioButton2 =
            dialog.findViewById<RadioButton>(R.id.radio_button2)


        titleTextView.text = titleText
        radioButton1.text = firstRadioButtonText
        radioButton2.text = secondRadioButtonText


        dialog.findViewById<Button>(R.id.apply_button_dialog).setOnClickListener {
            positiveButtonAction(dialog)
        }
        dialog.findViewById<Button>(R.id.leave_button_dialog).setOnClickListener {
            negativeButtonAction(dialog)
        }



        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java
    override fun getViewModelStoreOwner(): MainActivity = this
    override fun getContext(): Context = this

    private fun observeUserNetworkConnection(){
        NetworkObserver.getNetworkLiveData(applicationContext).observe(
            this,
            androidx.lifecycle.Observer { isConnected ->
                if (!isConnected) {
                    binding.textViewNetworkStatus.text = "No internet connection"
                    binding.networkStatusLayout.apply {
                        binding.networkStatusLayout.visibility = View.VISIBLE
                        setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                android.R.color.holo_red_light
                            )
                        )
                        viewModel.refreshResponse(true)
                    }
                } else {
                    binding.textViewNetworkStatus.text = "Back Online"

                    viewModel.refreshResponse(false)

                    binding.networkStatusLayout.apply {
                        animate()
                            .alpha(1f)
                            .setStartDelay(1000)
                            .setDuration(1000)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator?) {
                                    binding.networkStatusLayout.visibility = View.GONE
                                }
                            }).start()
                        setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                android.R.color.holo_green_light
                            )
                        )
                    }
                }
            })
    }

    private fun observeRetrofitErrors(){
        viewModel.errorMessage.observe(this, Observer {
            it?.getContentIfNotHandled()?.let {
                //  Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object{
        lateinit var toolbar: Toolbar
    }

    private fun loadSliderComponents(){

        val imageSlider = findViewById<ImageSlider>(R.id.image_slider)

        val imageList = ArrayList<SlideModel>() // Create image list
        imageList.add(
            SlideModel(
                "https://ktvz.b-cdn.net/2021/04/COVID-19-statewide-numbers-4r221.jpg",
                "Oregon reports no new COVID-19 related deaths, 499 new cases - KTVZ",
                ScaleTypes.CENTER_CROP
            )
        )
        imageList.add(
            SlideModel(
                "https://cdn.vox-cdn.com/thumbor/dvG9miSD2nI3LCGqD2zDvAlCQvQ=/0x0:3402x1781/fit-in/1200x630/cdn.vox-cdn.com/uploads/chorus_asset/file/22417945/1310523762.jpg",
                "Lakers vs. Kings Final Score: L.A. makes it rain in Sacramento - Silver Screen and Roll ",
                ScaleTypes.CENTER_CROP
            )
        )
        imageList.add(
            SlideModel(
                "https://static-11.sinclairstoryline.com/resources/media/fe1a29d8-bef0-4944-b1d7-17bf2b7d3dd5-large16x9_EstherShortParkcaptioned.jpg?1617414393662",
                "Clark County issues travel warning as COVID cases rise - KATU.",
                ScaleTypes.CENTER_CROP
            )
        )
        imageList.add(
            SlideModel(
                "https://media13.s-nbcnews.com/j/MSNBC/Components/Video/202103/W3.nbcnews-fp-1200-630.jpg",
                "SpaceX rocket debris found in Washington state after streaks in night sky - NBC News.",
                ScaleTypes.CENTER_CROP
            )
        )

        imageSlider.setImageList(imageList)

        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                // You can listen here.
            }
        })

        imageSlider.setItemChangeListener(object : ItemChangeListener {
            override fun onItemChanged(position: Int) {
                //println("Pos: " + position)
            }
        })

        imageSlider.setTouchListener(object : TouchListener {
            override fun onTouched(touched: ActionTypes) {
                if (touched == ActionTypes.DOWN) {
                    imageSlider.stopSliding()
                } else if (touched == ActionTypes.UP) {
                    imageSlider.startSliding(1000)
                }
            }
        })

    }



    private fun createNotificationChannel(channelId: String) {
        // Create the NotificationChannel, but only on API 26+ (Android 8.0) because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "My Channel"
            val channelDescription = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, name, importance)
            channel.apply {
                description = channelDescription
            }

            // Finally register the channel with system
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(){

        val myIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            myIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.download)
            .setContentTitle("Breaking News")
            .setContentText("Test notification from a demo project.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    // Provide the bitmap to be used as the payload for the BigPicture notification.
                    .bigPicture(BitmapFactory.decodeResource(resources, R.drawable.khaleej_times))
                    .bigLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.download))
                    .setBigContentTitle("Breaking News")
                    .setSummaryText("This is a test notification from demo project.")

            )
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setAutoCancel(true)


        with(NotificationManagerCompat.from(this)){
            notify(notificationId, notificationBuilder.build())
        }
    }




}