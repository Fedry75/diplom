package db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class Authorization() {
    //функция проверки совпадения логина и пароля пользователя
    fun loginUser(login: String, password: String): Boolean {
        return transaction {
            User.select {
                User.login eq login and (User.password eq password)
            }.map { it[User.login] to it[User.password] }.firstOrNull() != null
        }
    }
    //функция проверки на админа
    fun checkAdmin(login: String):Boolean{
        return transaction {
            User.select{
                User.login eq login
            }.map { it[User.isAdmin] }.first()
        }
    }

    //функция для получения логина
    private fun checkUser(login: String): Boolean {
        return transaction {
            User.select {
                User.login eq login
            }.map { it[User.login] }.firstOrNull() != null
        }
    }
    //функция регистрации пользователя
    fun registrationUser(
        login: String,
        password: String,
        lastname: String,
        firstname: String,
        patronymic: String,
        role: String
    ): Boolean {
        return if (!checkUser(login)) {
            transaction {
                User.insert {
                    it[User.login] = login
                    it[User.password] = password
                    it[isAdmin] = false
                    it[User.lastname] = lastname
                    it[User.firstname] = firstname
                    it[User.patronymic] = patronymic
                    it[User.role] = role
                }
            }
            true
        } else {
            false
        }

    }
    //фкнуция для получения имени товара
    private fun checkGoods(name: String): Boolean {    //?
        return transaction {
            Goods.select {
                Goods.name eq name
            }.map { it[Goods.name] }.firstOrNull() != null
        }
    }
    //функция добавления товара в бд
    fun addGood(
        name: String,
        price: Int,
        count: Int,
        description: String,
        availability: Boolean = true
    ): Boolean {
        return if (!checkGoods(name)) {   //?
            transaction {
                Goods.insert {
                    it[Goods.name] = name
                    it[Goods.price] = price
                    it[Goods.count] = count
                    it[Goods.description] = description
                    it[Goods.availability] = availability
                }
            }
            true
        }
        else{
            false
        }
    }

    //функция получения списка товаров
    fun goodSelect(): List<List<String>>{
        return transaction {
            Goods.selectAll().map { listOf(it[Goods.name], it[Goods.price].toString(), it[Goods.count].toString(), it[Goods.description], it[Goods.availability].toString())}
        }
    }
    fun goodsSelectName():List<String>{
        return transaction {
            Goods.selectAll().map { it[Goods.name] }
        }
    }
    //функция получения списка пользователей
    fun userSelect():List<List<String>>{
        return transaction {
            User.selectAll().map { listOf(it[User.lastname], it[User.firstname], it[User.patronymic], it[User.login], it[User.password], it[User.role])}
        }
    }
    //функция удаления пользователя из базы данных
    fun deleteUsersFromDB(login: String, password: String): Boolean{
        return if (checkUser(login)){
            transaction {
                User.deleteWhere {User.login eq login and (User.password eq password)}
            }
            true
        }
        else{
            false
        }
    }
    //функция удаления товара из базы данных
    fun deleteGoodsFromDB(name: String,
                          ): Boolean{
        return if(checkGoods(name)){
            transaction {
                Goods.deleteWhere{Goods.name eq name}
            }
            true
        }
        else{
            false
        }
    }

    //фукция редактировая данных о товаре
    fun editGoodsFromDB(
        name: String,
        price: Int,
        count: Int,
        description: String,
    ): Boolean{
        return if(checkGoods(name)){
            transaction {

                Goods.update({ Goods.name eq name }) {
                    it[Goods.price] = price
                    it[Goods.count] = count
                    it[Goods.description] = description
                    if (count > 0) {
                        it[availability] = true
                    } else {
                        false
                    }
                }
                true
            }
        }
        else{
            false
        }
    }

fun getOldGoodValues(name: String) = transaction {Goods.select{Goods.name eq name}.map { listOf<String>(it[Goods.price].toString(), it[Goods.count].toString(), it[Goods.description]) }.first()}

    //функция создания таблиц в бд
    fun initializeDb() {
        transaction {
            SchemaUtils.create(User)
            SchemaUtils.create(Goods)
        }
    }
}

