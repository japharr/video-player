package com.japharr.videoplayer.data.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.japharr.videoplayer.Constants
import com.japharr.videoplayer.R
import com.japharr.videoplayer.common.extension.*
import com.japharr.videoplayer.data.domain.VideoMedia
import kotlinx.android.synthetic.main.video_list_item.view.*
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by Japharr on 8/6/2018.
 */
class VideoMediaListAdapter (var context: Context, var videoMedias: ArrayList<VideoMedia>, var action: VideoMediaClickListener): RecyclerView.Adapter<VideoMediaListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return videoMedias.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    private fun getItem(position: Int): VideoMedia = videoMedias[position]

    inner class ViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(parent.inflate(R.layout.video_list_item)) {
        fun bind(videoMedia: VideoMedia?, position: Int) = with(itemView) {
            with(videoMedia) {
                txv_file_name.text = this?.fileName?.noExtension()
                txv_file_type.text = this?.fileType?.contentType()?.toUpperCase()
                txv_created_date.text = android.text.format.DateUtils.getRelativeTimeSpanString(this?.createdDate?.time!!) //videoMedia.createdDate?.asSimpleDate()

                card_item_videoMedia.setOnClickListener { action.onClick(this, position) }
                img_play.setOnClickListener { action.onClick(this, position) }

                //val outputFile = DirectoryRepository.getVideoMediaedFile(context, videoMedia)
                val outputFile = File(videoMedia?.filePath) as File?
                if(outputFile != null && outputFile.exists()) {
                    img_thumbnail.loadThumbnailCache(outputFile)

                    //val bmThumbnail = android.media.ThumbnailUtils.createVideoThumbnail(outputFile.path, android.provider.MediaStore.Video.Thumbnails.MICRO_KIND);
                    //img_thumbnail.setImageBitmap(bmThumbnail)
                    //if(bmThumbnail != null) img_thumbnail.loadImage(bmThumbnail, R.drawable.no_thumbnail)
                    //img_thumbnail.loadThumbnailCache(outputFile)

                    val timeInMillis = outputFile.duration()
                    if(timeInMillis > 0) {
                        txv_duration.visibility = android.view.View.VISIBLE
                        txv_duration.text = kotlin.String.format("%02d:%02d:%02d",
                                TimeUnit.MILLISECONDS.toHours(timeInMillis),
                                TimeUnit.MILLISECONDS.toMinutes(timeInMillis),
                                TimeUnit.MILLISECONDS.toSeconds(timeInMillis) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMillis))
                        )
                    } else {
                        txv_duration.visibility = android.view.View.GONE
                    }
                } else {
                    img_thumbnail.setImageResource(R.drawable.no_thumbnail)
                }

                img_overview.setOnClickListener { action.onOverviewClick(img_overview, this, position) }
            }
        }
    }
}