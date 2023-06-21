package db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object Goods: IntIdTable() {
    val name: Column<String> = varchar("name", 250)
    val price: Column<Int> = integer("price")
    val count: Column<Int> = integer("count")
    val description: Column<String> = text("description")
    val availability: Column<Boolean> = bool("availability")
}