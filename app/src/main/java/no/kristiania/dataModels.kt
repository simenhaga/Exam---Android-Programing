package no.kristiania

data class StoredImageModel(var id: Long? = null, val uri: String)

data class StoredResultsModel(var id: Long? = null, val imageLink: String, val storedImageID: Long)

