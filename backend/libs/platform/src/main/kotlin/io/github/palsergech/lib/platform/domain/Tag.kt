package io.github.palsergech.lib.platform.domain

class Tag(tag: String) {

    companion object {
        private val tagNormalizer: ((String) -> String) = String::uppercase
        private val validTagCharsRegex = "[A-Z0-9_]*".toRegex()
    }

    val tag = tagNormalizer(tag)

    init {
        DomainChecks.require(this.tag.isNotBlank()) { "Tag must not be blank" }
        DomainChecks.require(this.tag.matches(validTagCharsRegex)) {
            "Tag must contain only english letters, digits and underscores"
        }
    }

    override fun toString() = tag

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tag

        return tag == other.tag
    }

    override fun hashCode() = tag.hashCode()
}
