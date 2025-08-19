package ceui.pixiv.ui.task

import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import ceui.lisa.R
import ceui.lisa.annotations.ItemHolder
import ceui.lisa.databinding.CellQueuedTaskBinding
import ceui.lisa.databinding.CellUsersYoruItemBinding
import ceui.loxia.Illust
import ceui.loxia.findActionReceiverOrNull
import ceui.pixiv.ui.bottom.UsersYoriActionReceiver
import ceui.pixiv.ui.common.ListItemHolder
import ceui.pixiv.ui.common.ListItemViewHolder

class QueuedTaskHolder(val downloadTask: DownloadTask, val illust: Illust) : ListItemHolder() {
    override fun getItemId(): Long {
        return downloadTask.taskId
    }
}

@ItemHolder(QueuedTaskHolder::class)
class QueuedTaskViewHolder(bd: CellQueuedTaskBinding) :
    ListItemViewHolder<CellQueuedTaskBinding, QueuedTaskHolder>(bd) {

    override fun onBindViewHolder(holder: QueuedTaskHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        binding.task = holder.downloadTask
    }
}

@BindingAdapter("status_desc")
fun TextView.binding_setStatusDesc(taskStatus: TaskStatus?) {
    if (taskStatus != null) {
        if (taskStatus is TaskStatus.NotStart) {
            text = "未开始"
            setTextColor(Color.parseColor("#FFB332"))
        } else if (taskStatus is TaskStatus.Executing) {
            text = "下载中 - ${taskStatus.percentage}%"
            setTextColor(Color.parseColor("#00FF94"))
        } else if (taskStatus is TaskStatus.Finished) {
            text = "完成了"
            setTextColor(Color.parseColor("#00FF94"))
        } else if (taskStatus is TaskStatus.Error) {
            text = "出错了"
            setTextColor(Color.parseColor("#FFB332"))
        } else {
            text = "taskStatus unknown"
        }
    } else {
        text = "taskStatus null"
    }
}

@BindingAdapter("status_percentage")
fun ProgressBar.binding_setStatusPercentage(taskStatus: TaskStatus?) {
    if (taskStatus != null) {
        if (taskStatus is TaskStatus.Executing) {
            // 兼容不同版本的 setProgress 调用
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // 对于 Android 7.0 及以上版本，使用 setProgress 的第二个参数进行动画
                setProgress(taskStatus.percentage, true)
            } else {
                // 对于 Android 7.0 以下的版本，使用 setProgress 不带动画
                progress = taskStatus.percentage
            }
        }
    }
}