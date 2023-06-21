import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import db.Authorization
import org.jetbrains.exposed.sql.Database

val authorization = Authorization()


//Окно авторизации пользователя
@Composable
@Preview
fun authorizations(currentPage: MutableState<PagesEnum>) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            //horizontalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(login, { login = it }, label = { Text("Логин") })
            OutlinedTextField(password, { password = it }, label = { Text("Пароль") })
            Button({
                if (authorization.loginUser(login, password)) {
                    if (authorization.checkAdmin(login)) {
                        currentPage.value = PagesEnum.GoodPageAdmin
                    } else {
                        currentPage.value = PagesEnum.GoodsPage
                    }
                }
            }) {
                Text("Вход")
            }
        }
    }
}


//окно регистрации пользователя
@Composable
@Preview
fun registrationUser(currentPage: MutableState<PagesEnum>) {
    var lastname by remember { mutableStateOf("") }
    var firstname by remember { mutableStateOf("") }
    var patronymic by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("manager") }
    var registrationResult by remember { mutableStateOf(false) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(lastname, { lastname = it }, label = { Text("Фамилия") })
            OutlinedTextField(firstname, { firstname = it }, label = { Text("Имя") })
            OutlinedTextField(patronymic, { patronymic = it }, label = { Text("Отчество") })
            OutlinedTextField(login, { login = it }, label = { Text("Логин") })
            OutlinedTextField(password, { password = it }, label = { Text("Пароль") })
            OutlinedTextField(role, { role = it }, label = { Text("Роль") })
            Row {
                Button({
                    registrationResult =
                        authorization.registrationUser(login, password, lastname, firstname, patronymic, role)
                    if (registrationResult) {
                        currentPage.value = PagesEnum.UsersPage
                    }
                }) {
                    Text("Регистрация")
                }
                Button({
                    currentPage.value = PagesEnum.UsersPage
                }) {
                    Text("Назад")
                }
            }
        }
    }
}

//окно добавления товара
@Composable
fun addGoodPage(currentPage: MutableState<PagesEnum>) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var count by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var availability by remember { mutableStateOf("В наличии") }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(name, { name = it }, label = { Text("Название") })
            OutlinedTextField(price, { price = it }, label = { Text("Цена") })
            OutlinedTextField(count, { count = it }, label = { Text("Количество") })
            OutlinedTextField(description, { description = it }, label = { Text("Описание") })
            OutlinedTextField(availability, { availability = it }, label = { Text("Наличие") })
            Row(Modifier.width(300.dp), horizontalArrangement = Arrangement.SpaceAround) {
                Button({
                    authorization.addGood(name, price.toInt(), count.toInt(), description)
                    currentPage.value = PagesEnum.GoodPageAdmin
                }) {
                    Text("Подтвердить")
                }
                Button({
                    currentPage.value = PagesEnum.GoodPageAdmin
                }) {
                    Text("Назад")
                }
            }
        }
    }
}

//окно добавления менеджера
@Composable
fun addGoodPageMeneger(currentPage: MutableState<PagesEnum>) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var count by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var availability by remember { mutableStateOf("В наличии") }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(name, { name = it }, label = { Text("Название") })
            OutlinedTextField(price, { price = it }, label = { Text("Цена") })
            OutlinedTextField(count, { count = it }, label = { Text("Количество") })
            OutlinedTextField(description, { description = it }, label = { Text("Описание") })
            OutlinedTextField(availability, { availability = it }, label = { Text("Наличие") })
            Row(Modifier.width(300.dp), horizontalArrangement = Arrangement.SpaceAround) {
                Button({
                    authorization.addGood(name, price.toInt(), count.toInt(), description)
                    currentPage.value = PagesEnum.GoodsPage
                }) {
                    Text("Подтвердить")
                }
                Button({
                    currentPage.value = PagesEnum.GoodsPage
                }) {
                    Text("Назад")
                }
            }
        }
    }
}

//окно просмотря товара для менеджера
@Composable
fun goodPageManager(currentPage: MutableState<PagesEnum>) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            tableView(
                listOf("Название", "Стоимость в руб.", "Количество", "Описание", "Наличие"),
                authorization.goodSelect(),
                listOf(26f, 20f, 15f, 45f, 15f),
                500f
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Button({
                    currentPage.value = PagesEnum.AddGoodPagesManager
                }) {
                    Text("Добавить товар")
                }
            }
        }
    }
}

//окно просмотра товара для админа
@Composable
fun goodPageAdmin(currentPage: MutableState<PagesEnum>) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            tableView(
                listOf("Название", "Стоимость в руб.", "Количество", "Описание", "Наличие"),
                authorization.goodSelect(),
                listOf(26f, 20f, 15f, 45f, 15f),
                500f
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Button({
                    currentPage.value = PagesEnum.UsersPage
                }) {
                    Text("Просмотр пользователей")
                }
                Button({
                    currentPage.value = PagesEnum.AddGoodPage
                }) {
                    Text("Добавить товар")
                }

                Button({
                    currentPage.value = PagesEnum.DeleteGood
                }) {
                    Text("Удалить товар")
                }
            }
        }
    }
}

//окно просмотра пользователей
@Composable
fun usersPage(currentPage: MutableState<PagesEnum>) {
    var registrationResult by remember { mutableStateOf(false) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            tableView(
                listOf("Фамилия", "Имя", "Отчество", "Логин", "Пароль", "Роль"),
                authorization.userSelect(),
                listOf(10f, 10f, 10f, 8f, 8f, 8f),
                500f
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Button({
                    currentPage.value = PagesEnum.RegistrationPage
                }) {
                    Text("Регистрация пользователя")
                }
                Button({
                    currentPage.value = PagesEnum.DeleteUser
                }) {
                    Text("Удалить пользователей")
                }
                Button({
                    currentPage.value = PagesEnum.GoodPageAdmin
                }) {
                    Text("Назад")
                }
            }
        }
    }
}

//окно удаления пользователя
@Composable
fun deleteUser(currentPage: MutableState<PagesEnum>) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(login, { login = it }, label = { Text("Логин") })
            OutlinedTextField(password, { password = it }, label = { Text("Пароль") })
            Row(Modifier.width(300.dp), horizontalArrangement = Arrangement.SpaceAround) {
                Button({ authorization.deleteUsersFromDB(login, password) }) {
                    Text("Удалить пользователя")
                }
                Button({
                    currentPage.value = PagesEnum.UsersPage
                }) {
                    Text("Назад")
                }
            }
        }
    }
}

//окно удаления товара
@Composable
fun deleteGoods(currentPage: MutableState<PagesEnum>) {
    var name by remember { mutableStateOf("") }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(name, { name = it }, label = { Text("Название") })
            Row(Modifier.width(600.dp), horizontalArrangement = Arrangement.SpaceAround) {
                Button({ authorization.deleteGoodsFromDB(name) }) {
                    Text("Удалить товар")
                }
                Button({
                    currentPage.value = PagesEnum.EgitGood
                }) {
                    Text("Редактировать информацию о товаре")
                }
                Button({
                    currentPage.value = PagesEnum.GoodPageAdmin
                }) {
                    Text("Назад")
                }
            }
        }
    }
}

@Composable
fun editGood(currentPage: MutableState<PagesEnum>) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var count by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var availability by remember { mutableStateOf("В наличии") }
    var expand = remember { mutableStateOf(false) }
    var selName = authorization.goodsSelectName()
    val serviceDropDownText = remember { mutableStateOf(selName.firstOrNull()?:"") }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(name, {name = it}, label = { Text("Название")})
            OutlinedTextField(price, { price = it }, label = { Text("Цена") })
            OutlinedTextField(count, { count = it }, label = { Text("Количество") })
            OutlinedTextField(description, { description = it }, label = { Text("Описание") })
            OutlinedTextField(availability, { availability = it }, label = { Text("Наличие") })
            Row(Modifier.width(400.dp), horizontalArrangement = Arrangement.SpaceAround) {
                Button({
                    val (oldPrice, oldCount, oldDescription) = authorization.getOldGoodValues(name)
                    authorization.editGoodsFromDB(
                        name,
                        price.toIntOrNull() ?: oldPrice.toInt(),
                        count.toIntOrNull() ?: oldCount.toInt(),
                        description.ifEmpty { oldDescription })
                }) {
                    Text("Редактировать")
                }
                Button({
                    currentPage.value = PagesEnum.DeleteGood
                }) {
                    Text("Назад")
                }
                Button({
                    currentPage.value = PagesEnum.GoodPageAdmin
                }) {
                    Text("На главную")
                }
            }
        }
    }
}


fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "ООО СЕРЧИНФОРМ") {
        val connect = Database.connect("jdbc:sqlite:data.db", "org.sqlite.JDBC")
        Authorization().initializeDb()
        val currentPage = remember { mutableStateOf(PagesEnum.EgitGood) }
        when (currentPage.value) {
            PagesEnum.AuthorizationPage -> authorizations(currentPage)
            PagesEnum.GoodsPage -> goodPageManager(currentPage)
            PagesEnum.GoodPageAdmin -> goodPageAdmin(currentPage)
            PagesEnum.AddGoodPage -> addGoodPage(currentPage)
            PagesEnum.AddGoodPagesManager -> addGoodPageMeneger(currentPage)
            PagesEnum.UsersPage -> usersPage(currentPage)
            PagesEnum.RegistrationPage -> registrationUser(currentPage)
            PagesEnum.DeleteUser -> deleteUser(currentPage)
            PagesEnum.DeleteGood -> deleteGoods(currentPage)
            PagesEnum.EgitGood -> editGood(currentPage)
        }
    }
}


//2 функции для создания таблиц
@Composable
fun RowScope.TableViewCell(text: String, weight: Float, color: Color = Color.Black) {
    Text(
        text = text,
        modifier = Modifier.border(1.dp, color)
            .weight(weight)
            .padding(8.dp)
            .fillMaxHeight(03f)
    )
}

@Composable
fun tableView(
    headers: List<String>,
    data: List<List<String>>,
    weights: List<Float> = List(headers.size) { 0.5f },
    maxHeight: Float
) {
    if (headers.size != data[0].size || headers.size != weights.size) {
        throw Exception("количество полей в header, cellWeights или в таблице не совпадает")
    }
    Column(Modifier.height(maxHeight.dp).verticalScroll(rememberScrollState())) {
        Row {
            for (i in headers.indices) {
                TableViewCell(headers[i], weights[i])
            }
        }

        Column(Modifier) {
            for (row in data) {
                Row {
                    for (i in row.indices) {
                        TableViewCell(row[i], weights[i])
                    }
                }
            }
        }
    }
}
@Composable
fun createDropDownMenu(
    text: MutableState<String>, expanded: MutableState<Boolean> = mutableStateOf(false),
    data: List<String>
) {
    Box {
        Text(
            text.value, modifier = Modifier.clickable(onClick = { expanded.value = true })
                .border(1.dp, Color.Black).height(40.dp).width(280.dp)

        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            data.forEach {
                DropdownMenuItem({
                    text.value = it
                    expanded.value = false
                }, Modifier.border(1.dp, color = Color.Black)) {
                    Text(it)
                }
            }
        }
    }
}