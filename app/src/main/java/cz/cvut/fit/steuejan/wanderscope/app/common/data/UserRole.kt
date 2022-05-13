package cz.cvut.fit.steuejan.wanderscope.app.common.data

import cz.cvut.fit.steuejan.wanderscope.R

enum class UserRole {
    ADMIN, EDITOR, VIEWER;

    fun canEdit() = this in listOf(ADMIN, EDITOR)

    fun toStringRes(): Int {
        return when (this) {
            ADMIN -> R.string.admin
            EDITOR -> R.string.editor
            VIEWER -> R.string.viewer
        }
    }
}