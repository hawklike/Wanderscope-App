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

    fun toIcon(): Int {
        return when (this) {
            ADMIN -> R.drawable.ic_crown_16
            EDITOR -> R.drawable.ic_pen_16
            VIEWER -> R.drawable.ic_glasses_16
        }
    }
}