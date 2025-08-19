package ceui.pixiv.widgets

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import ceui.lisa.R
import ceui.lisa.databinding.DialogAlertBinding
import ceui.pixiv.utils.setOnClick
import ceui.pixiv.ui.common.viewBinding
import kotlinx.coroutines.CompletableDeferred
import java.util.UUID

class AlertPurpleDialog : PixivDialog(R.layout.dialog_alert) {

    private val args by navArgs<AlertPurpleDialogArgs>()
    private val task: CompletableDeferred<Boolean>?
        get() {
            return viewModel.alertTaskPool[args.taskUuid]
        }
    private val binding by viewBinding(DialogAlertBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title.text = args.title
        binding.ok.setOnClick {
            task?.complete(true)
            dismissAllowingStateLoss()
        }
        binding.cancel.setOnClick {
            task?.complete(false)
            dismissAllowingStateLoss()
        }
    }

    override fun performCancel() {
        super.performCancel()
        task?.cancel()
    }
}

suspend fun Fragment.alertYesOrCancel(title: String): Boolean {
    val dialogViewModel by activityViewModels<DialogViewModel>()
    val taskUUID = UUID.randomUUID().toString()
    val task = CompletableDeferred<Boolean>()
    task.invokeOnCompletion {
        dialogViewModel.alertTaskPool.remove(taskUUID)
    }
    dialogViewModel.alertTaskPool[taskUUID] = task
    AlertPurpleDialog().apply {
        arguments = AlertPurpleDialogArgs(taskUUID, title).toBundle()
    }.show(childFragmentManager, "AlertPurpleDialog-${taskUUID}")
    return task.await()
}
