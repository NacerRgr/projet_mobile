package com.example.thenewsapp.models

data class Source(
    val id: String?,
    val name: String?
) {
    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Source) return false

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }
}
