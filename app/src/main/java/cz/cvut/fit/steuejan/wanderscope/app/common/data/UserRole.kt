package cz.cvut.fit.steuejan.wanderscope.app.common.data

enum class UserRole {
    ADMIN, EDITOR, VIEWER;

    fun canEdit() = this in listOf(ADMIN, EDITOR)
}