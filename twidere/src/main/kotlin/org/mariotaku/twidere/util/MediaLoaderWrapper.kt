/*
 * 				Twidere - Twitter client for Android
 * 
 *  Copyright (C) 2012-2014 Mariotaku Lee <mariotaku.lee@gmail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mariotaku.twidere.util

import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.ImageView
import org.mariotaku.kpreferences.get
import org.mariotaku.twidere.constant.mediaPreloadKey
import org.mariotaku.twidere.constant.mediaPreloadOnWifiOnlyKey
import org.mariotaku.twidere.model.*
import org.mariotaku.twidere.model.util.getActivityStatus

class MediaLoaderWrapper() {

    var isNetworkMetered: Boolean = true
    private var preloadEnabled: Boolean = false
    private var preloadOnWifiOnly: Boolean = true

    private val shouldPreload: Boolean get() = preloadEnabled && (!preloadOnWifiOnly || !isNetworkMetered)


    fun displayOriginalProfileImage(view: ImageView, user: ParcelableUser) {
        if (user.extras != null && !TextUtils.isEmpty(user.extras.profile_image_url_original)) {
            displayProfileImage(view, user.extras.profile_image_url_original)
        } else if (user.extras != null && !TextUtils.isEmpty(user.extras.profile_image_url_profile_size)) {
            displayProfileImage(view, user.extras.profile_image_url_profile_size)
        } else {
            displayProfileImage(view, Utils.getOriginalTwitterProfileImage(user.profile_image_url))
        }
    }

    fun displayProfileImage(view: ImageView, user: ParcelableUser) {
        if (user.extras != null && !TextUtils.isEmpty(user.extras.profile_image_url_profile_size)) {
            displayProfileImage(view, user.extras.profile_image_url_profile_size)
        } else {
            displayProfileImage(view, user.profile_image_url)
        }
    }

    fun displayProfileImage(view: ImageView, url: String?) {
    }

    fun displayDashboardProfileImage(view: ImageView, account: AccountDetails, drawableOnLoading: Drawable?) {
        if (account.user.extras != null && !TextUtils.isEmpty(account.user.extras.profile_image_url_profile_size)) {
            displayDashboardProfileImage(view, account.user.extras.profile_image_url_profile_size,
                    drawableOnLoading)
        } else {
            displayDashboardProfileImage(view, account.user.profile_image_url, drawableOnLoading)
        }
    }


    fun preloadStatus(status: ParcelableStatus) {
        if (!shouldPreload) return
        preloadProfileImage(status.user_profile_image_url)
        preloadProfileImage(status.quoted_user_profile_image)
        preloadMedia(status.media)
        preloadMedia(status.quoted_media)
    }

    fun preloadActivity(activity: ParcelableActivity) {
        if (!shouldPreload) return
        activity.getActivityStatus()?.let { preloadStatus(it) }
    }

    fun reloadOptions(preferences: SharedPreferences) {
        preloadEnabled = preferences[mediaPreloadKey]
        preloadOnWifiOnly = preferences[mediaPreloadOnWifiOnlyKey]
    }

    private fun displayDashboardProfileImage(view: ImageView, url: String?, drawableOnLoading: Drawable?) {
    }

    private fun preloadMedia(media: Array<ParcelableMedia>?) {
        media?.forEach { item ->
            val url = item.preview_url ?: item.media_url ?: return@forEach
            preloadPreviewImage(url)
        }
    }

    private fun preloadProfileImage(url: String?) {
    }

    private fun preloadPreviewImage(url: String?) {
    }

}
