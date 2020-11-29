package ua.shishkoam.appcoockingbook.oldutils.ui

import android.content.Context
import android.view.View

/**
 * Handler of events for updating "unread targets" badge.
 * This class applies the "unread targets" badge to a [View].
 * Whenever the amount of unread targets changes, the badge is updated to show the number of
 * currently unread targets.
 *
 */
class EntityNotificationCenter {
    private var badge: BadgeView? = null

    /**
     * @param context application context
     * @param badgeBase [View] to which we apply the "unread targets count" badge
     */
    fun setBadgeBase(context: Context, badgeBase: View, unreadCount: Int = 0) {
        badge = BadgeView(context, badgeBase)
        badge?.badgePosition = BadgeView.POSITION_RIGHT
        update(unreadCount)
    }

    fun notify(unreadCount: Int) {
        update(unreadCount)
    }

    private fun update(unreadCount: Int) {
        badge?.run {
            text = unreadCount.toString()
            if (unreadCount > 0) {
                show()
            } else {
                hide()
            }
        }
    }
}