package com.japharr.videoplayer.ui.main.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import android.provider.MediaStore
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.japharr.videoplayer.App
import com.japharr.videoplayer.R
import com.japharr.videoplayer.common.extension.dpToPx
import com.japharr.videoplayer.common.helper.PrefManager
import com.japharr.videoplayer.common.ui.GridSpacingItemDecoration
import com.japharr.videoplayer.common.ui.RxBaseActivity
import com.japharr.videoplayer.common.utils.DownloadUtils
import com.japharr.videoplayer.data.adapter.VideoMediaClickListener
import com.japharr.videoplayer.data.adapter.VideoMediaGridAdapter
import com.japharr.videoplayer.data.adapter.VideoMediaListAdapter
import com.japharr.videoplayer.data.domain.VideoMedia
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject
import android.opengl.ETC1.getWidth
import android.view.animation.TranslateAnimation




class MainActivity : RxBaseActivity(), MainView {
    override val TAG = MainActivity::class.java.simpleName

    val videoMedias = ArrayList<VideoMedia>()

    @Inject lateinit var presenter: MainPresenter
    @Inject lateinit var prefManager: PrefManager

    var gridItemDecor: RecyclerView.ItemDecoration? = null
    lateinit var defSharedPref: SharedPreferences

    var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        (application as App).appComponent.inject(this)
        presenter.bind(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            needStoragePermission()
        }

        initCollapsingToolbar()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            printNamesToLogCat(this)
        }

        gridItemDecor = GridSpacingItemDecoration(2, dpToPx(10.0f), true)

        presenter.initRecyclerView()
        presenter.loadAllVideos()
        defSharedPref = PreferenceManager.getDefaultSharedPreferences(this)
    }

    private fun initCollapsingToolbar() {
        collapsing_toolbar.title = " "
        appbar.setExpanded(true)

        //val titleLayout = findViewById(R.id.layout_title);

        appbar.addOnOffsetChangedListener(object: AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {

                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    //collapsingToolbar.setTitle("Omopar Omorele \n Adesina Jaleel");
                    //layout_title.visibility = View.VISIBLE
                    isShow = true

                    //details.visibility = View.GONE
                    //hideElements()
                } else if (isShow) {
                    //changeStatusBarColor();
                    //collapsingToolbar.setTitle(" ");
                    //layout_title.visibility = View.GONE
                    //top.visibility = View.VISIBLE
                    //details.visibility = View.VISIBLE
                    //showElements()
                    isShow = false
                }


                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // If collapsed, then do this
                    //imageViewSmallLogo.setVisibility(View.VISIBLE);
                    layout_title.visibility = View.VISIBLE
                    details.setVisibility(View.GONE);
                    //hideElements()
                } else if (verticalOffset == 0) {
                    // If expanded, then do this
                    //imageViewBigLogo.setVisibility(View.VISIBLE);
                    layout_title.visibility = View.GONE
                    details.setVisibility(View.VISIBLE);
                    //showElements()
                }
            }
        })
    }

    private fun showElements() {
        if (details.visibility == View.VISIBLE) {
            Log.w(TAG, "The view is already visible. Nothing to do here");
            return;
        }

        details.visibility = View.VISIBLE
        details.animate()
                //.setDuration(200)
                .alpha(1.0f)
                .setListener(object: AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation);
                        details.animate().setListener(null)
                    }
                })
    }

    private fun hideElements() {
        if (details.visibility == View.GONE) {
            Log.w(TAG, "The view is already visible. Nothing to do here");
            return;
        }

        details.animate()
                //.setDuration(200)
                .alpha(0.0f)
                .setListener(object: AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation);
                        details.visibility = View.GONE
                        details.animate().setListener(null)
                    }
                })
    }

    private fun printNamesToLogCat(context: Context) {
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Video.VideoColumns.DATA)
        val c = context.getContentResolver().query(uri, projection, null, null, null)
        var videoCount = 0
        val paths = ArrayList<String>()
        if (c != null) {
            videoCount = c.getCount()
            while (c.moveToNext()) {
                paths.add(c.getString(0))
                Log.d(TAG, "VIDEO: " + c.getString(0))
            }
            c.close()
        }
        Log.v(TAG, "Video count; " + videoCount)

        presenter.persistVideos(*paths.toTypedArray())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        this.menu = menu
        changeMenuIcon()
        return true
    }

    private fun changeMenuIcon() {
        if(!presenter.isListOrGrid())
            menu?.getItem(0)?.icon = resources.getDrawable(R.drawable.baseline_list_white_36)
        else
            menu?.getItem(0)?.icon = resources.getDrawable(R.drawable.baseline_grid_on_white_24)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_item_view -> {
                presenter.persistItemView()
                presenter.initRecyclerView()
                changeMenuIcon()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onLoadVideoSuccess(it: List<VideoMedia>) {
        Log.v(TAG, "onLoadDownloadSuccess")
        Log.v(TAG, videoMedias.toString())

        this.videoMedias.clear()
        this.videoMedias.addAll(it)

        if(this.videoMedias.isEmpty()) {
            txv_nothing.setText(R.string.nothing_to_show)
        }

        rcv_history.adapter.notifyDataSetChanged()
    }

    override fun onLoadVideoFailed(it: Throwable) {
        it.printStackTrace()
        Toast.makeText(this, "Unable to load video files", Toast.LENGTH_LONG).show()
    }

    override fun showLoadingView() {
        txv_nothing.text = getString(R.string.please_wait)
    }

    val videoMediaClickListener = object: VideoMediaClickListener {
        override fun onClick(videoMedia: VideoMedia, position: Int) {
            openVideoPlayer(videoMedia)
        }

        override fun onOverviewClick(view: View, videoMedia: VideoMedia, position: Int) {

        }
    }

    private fun openVideoPlayer(vararg selDownloads: VideoMedia) {
        DownloadUtils.openVideoPlayer(this, *selDownloads)
    }

    override fun onInitRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        rcv_history.setHasFixedSize(true)
        rcv_history.layoutManager = layoutManager
        rcv_history.removeItemDecoration(gridItemDecor)
        rcv_history.setEmptyView(txv_nothing)
        rcv_history.adapter = VideoMediaListAdapter(this, videoMedias, videoMediaClickListener)
    }

    override fun onInitGridRecyclerView() {
        val layoutManager = GridLayoutManager(this, 2)
        rcv_history.setHasFixedSize(true)
        rcv_history.layoutManager = layoutManager
        //rcv_history.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10.0f), true));
        //rcv_history.addItemDecoration(gridItemDecor)
        //rcv_history.itemAnimator = DefaultItemAnimator();
        rcv_history.setEmptyView(txv_nothing)
        rcv_history.adapter = VideoMediaGridAdapter(this, videoMedias, videoMediaClickListener)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.unbind()
    }
}
