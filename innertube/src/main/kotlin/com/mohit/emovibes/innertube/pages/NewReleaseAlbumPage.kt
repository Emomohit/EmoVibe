/*
 * EMOVibes Project Original (2026)
 * Mohit (github.com/Mohit)
 * Licensed Under GPL-3.0 | see git history for contributors
 */

package com.mohit.emovibes.innertube.pages

import com.mohit.emovibes.innertube.models.AlbumItem
import com.mohit.emovibes.innertube.models.AlbumReleaseType
import com.mohit.emovibes.innertube.models.Artist
import com.mohit.emovibes.innertube.models.MusicTwoRowItemRenderer
import com.mohit.emovibes.innertube.models.oddElements
import com.mohit.emovibes.innertube.models.splitBySeparator

object NewReleaseAlbumPage {
    fun fromMusicTwoRowItemRenderer(renderer: MusicTwoRowItemRenderer): AlbumItem? {
        val subtitleRuns = renderer.subtitle?.runs ?: return null
        val subtitleGroups = subtitleRuns.splitBySeparator()

        return AlbumItem(
            browseId = renderer.navigationEndpoint.browseEndpoint?.browseId ?: return null,
            playlistId =
                renderer.thumbnailOverlay
                    ?.musicItemThumbnailOverlayRenderer
                    ?.content
                    ?.musicPlayButtonRenderer
                    ?.playNavigationEndpoint
                    ?.watchPlaylistEndpoint
                    ?.playlistId ?: return null,
            title =
                renderer.title.runs
                    ?.firstOrNull()
                    ?.text ?: return null,
            artists =
                subtitleGroups.getOrNull(1)?.oddElements()?.map {
                    Artist(
                        name = it.text,
                        id = it.navigationEndpoint?.browseEndpoint?.browseId,
                    )
                } ?: return null,
            year =
                subtitleRuns
                    .lastOrNull()
                    ?.text
                    ?.toIntOrNull(),
            releaseType = AlbumReleaseType.fromLabel(
                subtitleGroups.firstOrNull()?.joinToString(separator = "") { it.text }
            ),
            thumbnail = renderer.thumbnailRenderer.musicThumbnailRenderer?.getThumbnailUrl() ?: return null,
            explicit =
                renderer.subtitleBadges?.find {
                    it.musicInlineBadgeRenderer?.icon?.iconType == "MUSIC_EXPLICIT_BADGE"
                } != null,
        )
    }
}
