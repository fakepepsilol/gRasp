package rs.fpl.grasp.background

object GlobalRegex {
    val URL_REGEX_NAMED = Regex("""^(?<UrlWithId>https://raspored\.rs/pub/\?pid=(?<Id>[a-zA-Z0-9]+)).*""")
    val SCRIPT_REGEX_NAMED = Regex("""_DB = (?<Json>.+) </script>""")
}