package ceui.pixiv.ui.comments

import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import ceui.lisa.R
import ceui.lisa.annotations.ItemHolder
import ceui.lisa.databinding.CellChildCommentBinding
import ceui.lisa.databinding.CellCommentBinding
import ceui.lisa.utils.GlideUrlChild
import ceui.loxia.Comment
import ceui.loxia.DateParse
import ceui.loxia.ObjectPool
import ceui.loxia.findActionReceiverOrNull
import ceui.pixiv.ui.common.BottomDividerDecoration
import ceui.pixiv.ui.common.CommonAdapter
import ceui.pixiv.ui.common.ListItemHolder
import ceui.pixiv.ui.common.ListItemViewHolder
import ceui.pixiv.ui.user.UserActionReceiver
import ceui.pixiv.utils.ppppx
import ceui.pixiv.utils.setOnClick
import com.bumptech.glide.Glide

class CommentHolder(
    val comment: Comment,
    val illustArthurId: Long,
    val childComments: List<Comment> = listOf(),
) : ListItemHolder() {

    override fun areItemsTheSame(other: ListItemHolder): Boolean {
        return comment.id == (other as? CommentHolder)?.comment?.id
    }

    override fun areContentsTheSame(other: ListItemHolder): Boolean {
        return comment == (other as? CommentHolder)?.comment && childComments.size == (other as? CommentHolder)?.childComments?.size
    }

    override fun getItemId(): Long {
        return comment.id
    }

    val isArthurCommented: Boolean
        get() {
            return illustArthurId == comment.user.id
        }
}

@ItemHolder(CommentHolder::class)
class CommentViewHolder(bd: CellCommentBinding) :
    ListItemViewHolder<CellCommentBinding, CommentHolder>(bd) {
    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        binding.holder = holder

        binding.root.setOnClickListener { sender ->
            sender.findActionReceiverOrNull<CommentActionReceiver>()?.onClickComment(holder.comment)
        }
        if (holder.comment.stamp != null) {
            binding.commentStamp.isVisible = true
            Glide.with(context).load(GlideUrlChild(holder.comment.stamp.stamp_url))
                .placeholder(R.drawable.bg_loading_placeholder)
                .into(binding.commentStamp)
        } else {
            binding.commentStamp.isVisible = false
        }

        binding.userIcon.setOnClick {
            ObjectPool.update(holder.comment.user)
            it.findActionReceiverOrNull<UserActionReceiver>()?.onClickUser(holder.comment.user.id)
        }
        binding.userName.setOnClick {
            ObjectPool.update(holder.comment.user)
            it.findActionReceiverOrNull<UserActionReceiver>()?.onClickUser(holder.comment.user.id)
        }

        binding.arthurLabel.isVisible = holder.isArthurCommented
        binding.actionDivider.isVisible = holder.childComments.isNotEmpty()

        binding.reply.setOnClick { sender ->
            holder.comment.user.let {
                sender.findActionReceiverOrNull<CommentActionReceiver>()?.onClickReply(holder.comment, 0L)
            }
        }

        binding.showReply.setOnClick { sender ->
            sender.findActionReceiverOrNull<CommentActionReceiver>()
                ?.onClickShowMoreReply(sender, holder.comment.id)
        }

        binding.delete.setOnClick { sender ->
            sender.findActionReceiverOrNull<CommentActionReceiver>()
                ?.onClickDeleteComment(sender, holder.comment, 0)
        }

        binding.commentTime.text = DateParse.displayCreateDate(holder.comment.date)

        binding.showReply.isVisible =
            holder.comment.has_replies == true && holder.childComments.isEmpty()

        if (holder.childComments.isNotEmpty()) {
            binding.childCommentsList.isVisible = true
            lifecycleOwner?.let {
                val childAdapter = CommonAdapter(it)
                val dividerDecoration =
                    BottomDividerDecoration(context, R.drawable.list_divider_no_end, marginLeft = 24.ppppx)
                if (binding.childCommentsList.itemDecorationCount == 0) {
                    binding.childCommentsList.addItemDecoration(dividerDecoration)
                }
                binding.childCommentsList.layoutManager = LinearLayoutManager(context)
                binding.childCommentsList.adapter = childAdapter
                childAdapter.submitList(holder.childComments.map { childComment ->
                    CommentChildHolder(
                        holder.comment.id,
                        childComment,
                        holder.illustArthurId
                    )
                })
            }
        } else {
            binding.childCommentsList.isVisible = false
        }
    }
}


class CommentChildHolder(val parentCommentId: Long, val comment: Comment, val illustArthurId: Long) : ListItemHolder() {

    override fun areItemsTheSame(other: ListItemHolder): Boolean {
        return comment.id == (other as? CommentChildHolder)?.comment?.id
    }

    override fun areContentsTheSame(other: ListItemHolder): Boolean {
        return comment == (other as? CommentChildHolder)?.comment
    }

    override fun getItemId(): Long {
        return comment.id
    }

    val isArthurCommented: Boolean
        get() {
            return illustArthurId == comment.user.id
        }
}

@ItemHolder(CommentChildHolder::class)
class CellChildCommentViewHolder(bd: CellChildCommentBinding) :
    ListItemViewHolder<CellChildCommentBinding, CommentChildHolder>(bd) {
    override fun onBindViewHolder(holder: CommentChildHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        binding.holder = holder
        binding.root.setOnClickListener { sender ->
            sender.findActionReceiverOrNull<CommentActionReceiver>()?.onClickComment(holder.comment)
        }
        if (holder.comment.stamp != null) {
            binding.commentStamp.isVisible = true
            Glide.with(context).load(GlideUrlChild(holder.comment.stamp.stamp_url))
                .placeholder(R.drawable.bg_loading_placeholder)
                .into(binding.commentStamp)
        } else {
            binding.commentStamp.isVisible = false
        }
        binding.arthurLabel.isVisible = holder.isArthurCommented

        binding.userIcon.setOnClick {
            ObjectPool.update(holder.comment.user)
            it.findActionReceiverOrNull<UserActionReceiver>()?.onClickUser(holder.comment.user.id)
        }
        binding.userName.setOnClick {
            ObjectPool.update(holder.comment.user)
            it.findActionReceiverOrNull<UserActionReceiver>()?.onClickUser(holder.comment.user.id)
        }

        binding.reply.setOnClick { sender ->
            holder.comment.user.let {
                sender.findActionReceiverOrNull<CommentActionReceiver>()?.onClickReply(holder.comment, holder.parentCommentId)
            }
        }

        binding.delete.setOnClick { sender ->
            sender.findActionReceiverOrNull<CommentActionReceiver>()
                ?.onClickDeleteComment(sender, holder.comment, holder.parentCommentId)
        }

        binding.commentTime.text = DateParse.displayCreateDate(holder.comment.date)
    }
}