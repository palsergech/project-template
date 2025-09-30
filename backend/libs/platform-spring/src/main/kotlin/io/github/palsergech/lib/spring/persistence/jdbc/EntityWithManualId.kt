package io.github.palsergech.lib.spring.persistence.jdbc

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.annotation.Transient
import java.util.*

abstract class EntityWithManualId<T>(
    @Id
    @Column("id")
    open var entityId: T
) : Persistable<T> {

    @Transient
    private var isNew: Boolean = false

    @Transient
    override fun isNew(): Boolean {
        val isNew = this.isNew
        this.isNew = false
        return isNew
    }

    @Transient
    override fun getId(): T? {
        return entityId
    }

    @Transient
    fun setNew() {
        this.isNew = true
    }
}