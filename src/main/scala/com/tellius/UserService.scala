package com.tellius

import java.sql.{Connection, DriverManager, ResultSet}

class UserService {

  var autoincrement = false
  var connection: Connection = getConnection()


  class CustomPasswordException(message: String) extends RuntimeException {
    override val getMessage: String = message
  }

  def getConnection(): Connection = {
//    val url = "jdbc:postgresql://34.28.140.21:5432/postgres"
//    val username = "postgres"
//    val password = "Tellius@123#"
//    Class.forName("org.postgresql.Driver")
    // Create the database connection
//    val connection = DriverManager.getConnection(url, username, password)
val connection = null
    return  connection
  }

  def updateUser(uid: Int, user: Users): Boolean = {
    println("Updating user")
    val users = findUser(uid)
    if (users == null) throw new Exception("user not found")
    if (!isValidatePassword(user)) throw new CustomPasswordException("Password did not match the constraints ")

    println("Updating user")

    val sql = "UPDATE Users set name = ?  ,starttime = ? , password = ?  where id = ? "
    val statement = connection.prepareStatement(sql)

    // Set the parameter values
    statement.setString(1, user.name)
    statement.setString(2, user.startTime)
    statement.setString(3, user.password)
    statement.setInt(4, uid)
    // Execute the statement
    println("Updating user")

    statement.executeUpdate
    true

  }

  private def isValidatePassword(user: Users) = {
    val passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=]).{8,}$".r
    println("Updating user")

    val validPassword = user.password match {
      case passwordRegex() => true
      case _ => false

    }
    validPassword
  }

  def findAllUsers(): List[Users] = {

    var users: List[Users] = List.empty[Users]

    val sql = "select * from users"
    val statement = connection.prepareStatement(sql)
    println("Updating user")


    // Execute the statement
    val resultSet: ResultSet = statement.executeQuery()


    while (resultSet.next) {
      val id = resultSet.getInt("id")
      val name = resultSet.getString("name")
      val password = resultSet.getString("password")
      val startTime = resultSet.getString("starttime")
      users = Users(id, name, startTime, password) :: users
    }
    println("Updating user")

    users
  }

  def findUser(uid: Int): Users = {

    var user: Users = null
    val sql = "select * from Users where id = ? "
    val statement = connection.prepareStatement(sql)
    println("Updating user")

    // Set the parameter values
    statement.setInt(1, uid)

    // Execute the statement
    val resultSet: ResultSet = statement.executeQuery()

    while (resultSet.next) {
      val id = resultSet.getInt("id")
      val name = resultSet.getString("name")
      val email = resultSet.getString("password")
      val startTime = resultSet.getString("starttime")
      user = Users(id, name, email, startTime)
      println("Updating user")

    }
    user
  }

  def createUser(user: Users): Unit = {

    val users = findUser(user.id)
    if (users != null) throw new Exception("userId Already Exist ")
    if (!isValidatePassword(user)) throw new CustomPasswordException("Password did not match the constraints ")

    execute(user)


  }


  private def execute(user: Users): Unit = {
    val sql = "INSERT INTO Users (id, name, starttime, password) VALUES (?, ?, ?, ?)"
    val statement = connection.prepareStatement(sql)

    // Set the parameter values
    statement.setInt(1, user.id)
    statement.setString(2, user.name)
    statement.setString(3, user.startTime)
    statement.setString(4, user.password)

    // Execute the statement
    statement.executeUpdate
  }

  def validateCredentials(username: String, password: String): Boolean = {


    val sql = "select * from Users where name = ? and password = ? "
    val statement = connection.prepareStatement(sql)
    println("Updating user")
    println("Updating user")
    println("Updating user")
    println("Updating user")
    println("Updating user")
    println("Updating user")
    println("Updating user")
    println("Updating user")
    println("Updating user")
    println("Updating user")
    println("Updating user")

    // Set the parameter values
    statement.setString(1, username)
    statement.setString(2, password)

    // Execute the statement
    val resultSet: ResultSet = statement.executeQuery()
    resultSet.next()

  }

}
