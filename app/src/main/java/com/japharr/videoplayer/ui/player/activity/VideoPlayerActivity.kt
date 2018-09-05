package com.japharr.videoplayer.ui.player.activity

import android.content.pm.ActivityInfo
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.MotionEvent
import android.view.Surface
import android.view.View
import android.widget.ImageView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL
import com.google.android.exoplayer2.util.RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE
import com.google.android.exoplayer2.util.Util
import com.japharr.videoplayer.R
import com.japharr.videoplayer.common.extension.transparentToolbar
import com.japharr.videoplayer.common.ui.RxBaseActivity
import com.japharr.videoplayer.common.utils.DownloadUtils
import com.japharr.videoplayer.data.domain.VideoMedia
import com.japharr.videoplayer.data.model.Playlist
import kotlinx.android.synthetic.main.activity_video_player.*
import kotlinx.android.synthetic.main.exo_playback_control_view.*
import java.util.ArrayList

/**
 * Created by Japharr on 8/7/2018.
 */
class VideoPlayerActivity: RxBaseActivity(), View.OnClickListener {
    lateinit var simpleExoPlayerView: SimpleExoPlayerView
    var player: SimpleExoPlayer? = null
    var timeLineWindow: Timeline.Window? = null

    private var mediaDataSourceFactory: DataSource.Factory? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var shouldAutoPlay: Boolean = false
    private var bandwidthMeter: BandwidthMeter? = null

    private var display: Display? = null
    private var size: Point? = null

    private var sWidth: Int = 0
    private var sHeight: Int = 0

    var device_height: Int = 0
    var device_width: Int = 0

    lateinit var decorView: View
    private var uiImmersiveOptions: Int = 0

    private var ivHideControllerButton: ImageView? = null
    private var currentTrackIndex: Int = 0
    var position: Long = 0

    var video_id: Array<String?>? = arrayOf()
    var video_url: Array<String> = arrayOf()
    var video_type: Array<String> = arrayOf()
    var video_title: Array<String> = arrayOf()

    private lateinit var mHandler: Handler

    private var link: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // making toolbar transparent
        transparentToolbar()
        setContentView(R.layout.activity_video_player)

        //App.downloadComponent.downloadServiceComponent().inject(this)
        //presenter.bind(this)

        display = windowManager.defaultDisplay
        size = Point()
        display?.getSize(size)
        sWidth = size?.x!!
        sHeight = size?.y!!

        val displaymetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displaymetrics)
        device_height = displaymetrics.heightPixels
        device_width = displaymetrics.widthPixels

        uiImmersiveOptions = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        decorView = window.decorView
        decorView.systemUiVisibility = uiImmersiveOptions

        /* */
        val playlist = intent.getSerializableExtra(PLAYLIST) as Playlist?
        val mediaUrl = intent.getStringExtra(MEDIA_URL)
        if(playlist != null) {
            video_id = playlist.videoId
            video_type = playlist.videoType
            video_url = playlist.videoUrl
            video_title = playlist.videoTitle

            txt_title.text = video_title[currentTrackIndex]
        } else if(mediaUrl != null) {
            btn_more.visibility = View.GONE
            startPlayer(mediaUrl)
        }
        /**/

        shouldAutoPlay = true;
        bandwidthMeter = DefaultBandwidthMeter();
        mediaDataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"), bandwidthMeter as TransferListener<in DataSource>)
        timeLineWindow = Timeline.Window();
        ivHideControllerButton = findViewById(R.id.exo_controller);

        mHandler = Handler()

        btn_back.setOnClickListener(this)
        //btn_share.setOnClickListener(this)
        btn_screen_rotation.setOnClickListener(this)
        btn_more.setOnClickListener(this)
        //btn_download.setOnClickListener(this)
        /*
        btn_back.setOnClickListener(this)
        btn_share.setOnClickListener(this)
        btn_screen_rotation.setOnClickListener(this)
        btn_open_with.setOnClickListener(this)
        btn_more.setOnClickListener(this)
        */

        // Interstitial
    }


    private fun initializePlayer() {
        simpleExoPlayerView = findViewById<View>(R.id.player_view) as SimpleExoPlayerView
        simpleExoPlayerView.requestFocus()
        simpleExoPlayerView.setRepeatToggleModes(REPEAT_TOGGLE_MODE_ONE or REPEAT_TOGGLE_MODE_ALL)

        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)

        trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

        simpleExoPlayerView.player = player

        player?.playWhenReady = shouldAutoPlay
        player?.addListener(playerEventListener)

        val mediaSources: ArrayList<MediaSource> = ArrayList()
        for(url in video_url) {
            val mediaSource = ExtractorMediaSource(Uri.parse(url),
                    mediaDataSourceFactory, DefaultExtractorsFactory(), null, null)

            mediaSources.add(mediaSource)
        }

        if(mediaSources.isNotEmpty()) {
            val strings = mediaSources.toTypedArray()
            val concat = ConcatenatingMediaSource(*strings)

            player?.prepare(concat)
            player?.seekTo(currentTrackIndex, position)

        }
    }

    private fun releasePlayer() {
        Log.v(TAG, "releasePlayer")
        if (player != null) {
            position = player?.currentPosition!!
            currentTrackIndex = player?.currentWindowIndex!!

            shouldAutoPlay = player?.playWhenReady!!
            player?.release()
            player = null
            trackSelector = null
        }
    }

    private fun startPlayer(url: String) {
        link = url

        video_url = arrayOf(url)
        video_title = arrayOf("")

        txt_title.text = video_title[currentTrackIndex]

        //btn_share.visibility = View.VISIBLE
        //btn_download.visibility = View.VISIBLE

        initializePlayer()
    }

    private var hideControls = Runnable {  hideAllControls(); }
    private fun hideAllControls(){
        decorView.systemUiVisibility = uiImmersiveOptions
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                mHandler.removeCallbacks(hideControls)
                mHandler.post(hideControls)
            }
        }

        return super.onTouchEvent(event)
    }

    override fun onResume() {
        super.onResume()

        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();

            mHandler.removeCallbacks(hideControls)
            mHandler.post(hideControls)
        }
    }

    override fun onPause() {
        super.onPause()

        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    override fun onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    override fun onClick(v: View?) {
        val i1 = v?.id
        if (i1 == R.id.btn_back) {
            releasePlayer()
            finish()
        }
        if (i1 == R.id.btn_screen_rotation) {
            Log.v(TAG, "ScreenRotation is clicked")
            when(display?.rotation) {
                Surface.ROTATION_0  -> {
                    Log.v(TAG, "ScreenRotation is ORIENTATION_PORTRAIT")
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
                Surface.ROTATION_90 -> {
                    Log.v(TAG, "ScreenRotation is ORIENTATION_LANDSCAPE")
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }
        }
    }



    private val playerEventListener = object: Player.EventListener {
        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
        }

        override fun onSeekProcessed() {
        }

        override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {

        }

        override fun onPlayerError(error: ExoPlaybackException?) {
        }

        override fun onLoadingChanged(isLoading: Boolean) {
        }

        override fun onPositionDiscontinuity(reason: Int) {
            Log.v(TAG, "onPositionDiscontinuity")
            if(player != null) {
                currentTrackIndex = player?.currentWindowIndex!!

                Log.v(TAG, "currentWindowIndex: " + currentTrackIndex)
                Log.v(TAG, "video_title_size: " + video_title.size)
                txt_title.text = video_title[currentTrackIndex]
            } else {
                Log.v(TAG, "player is null")
            }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
        }

        override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_ENDED){
                //player back ended
                //finish()
            }

            if (playbackState == Player.STATE_BUFFERING){
                progress_bar.visibility = View.VISIBLE;
            } else {
                progress_bar.visibility = View.INVISIBLE;
            }
        }

    }

    companion object {
        const val CURRENT_POSITION = "CURRENT_POSITION"
        const val CURRENT_TRACK_INDEX = "CURRENT_TRACK_INDEX"
        const val PLAYLIST = "PLAYLIST"
        const val MEDIA_URL = "MEDIA_URL"
    }
}