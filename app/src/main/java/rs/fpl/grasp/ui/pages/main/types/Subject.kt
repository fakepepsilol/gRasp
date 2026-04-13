package rs.fpl.grasp.ui.pages.main.types

class Subject {
    val fullName: String
    val shortName: String
    val hexColor: String

    /**
     * @param[data] List of strings containing <fullName, shortName, hexColor> in that order.
     */
    constructor(data: List<String>) {
        if(data.size != 3) throw Exception("Incorrect Class-Subject data (List<String>) length: expected 3, actual ${data.size}")
        fullName = data[0]
        shortName = data[1]
        hexColor = data[2]
    }
}