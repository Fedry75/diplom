package db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object User: IntIdTable() {
    val lastname:Column<String> = varchar("lastname", 150)
    val firstname:Column<String> = varchar("firstname", 150)
    val patronymic:Column<String> = varchar("otchestvo", 150)
    val login: Column<String> = varchar("login", 50).uniqueIndex()
    val password: Column<String> = varchar("password", 50)
    val role: Column<String> = varchar("post", 100)
    val isAdmin = bool("isAdmin")
}