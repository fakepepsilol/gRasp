package rs.fpl.grasp.background

import okhttp3.OkHttpClient
import okhttp3.Request

object UrlProcessor {

    fun removeNonIdParameters(url: String): String {
        return GlobalRegex.URL_REGEX_NAMED.find(url)?.groups["UrlWithId"]?.value ?: throw NoSuchElementException("timetable url doesn't match regex")
    }

    fun getId(url: String): String {
        return GlobalRegex.URL_REGEX_NAMED.find(url)?.groups["Id"]?.value ?: throw NoSuchElementException("timetable url doesn't match regex")
    }

    val httpClient = OkHttpClient()
    @Suppress("RedundantSuspendModifier")
    suspend fun fetchJson(url: String): String {
        if(!url.matches(GlobalRegex.URL_REGEX_NAMED)) {
            throw Exception("timetable url doesn't match regex")
        }
        val request = Request.Builder()
            .url(removeNonIdParameters(url))
            .build()
        val html = httpClient.newCall(request).execute().body.byteString().utf8()
        if(!html.contains("_DB = ")) {
            throw Exception("Timetable not yet published")
        }
        return GlobalRegex.SCRIPT_REGEX_NAMED.find(html)?.groups["Json"]?.value ?: "Unable to extract JSON from html."
    }
}