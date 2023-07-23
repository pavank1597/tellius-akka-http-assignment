package com.tellius

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import pdi.jwt.{JwtAlgorithm, JwtClaim, JwtSprayJson}
import spray.json.DefaultJsonProtocol
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.DurationInt
import scala.util._

case class Users(id: Int, name: String, startTime: String, password: String)

object UserJsonProcaler extends DefaultJsonProtocol {
  case class LoginRequest(username: String, password: String)

  implicit val userJsonProcaler = jsonFormat4(Users)
  implicit val loginJsonProcaler = jsonFormat2(LoginRequest)
}

object Playground extends App with SprayJsonSupport {

  import UserJsonProcaler._


  val userService = new UserService()


  case object UserDBActor {
    case class CreateUser(user: Users)

    case class UserCreated(user: Users)

    case class UserUpdated(user: Users)

    case class FailedUpdated(message: String)

    case class UpdateUser(uid: Int, user: Users)

    case class FindUser(uid: Int)

    case object FindAllUsers

  }

  val jwtSecret = "JWTSecretRandomSecret"

  import UserDBActor._

  class UserDBActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case CreateUser(user) =>
        try {
          println("Updating pavan user")
          userService.createUser(user)
          println("Updating user")
          println("Updating pavan user")


          sender() ! UserCreated(user)
        } catch {
          case e: Exception => sender() ! FailedUpdated(e.getMessage)
        }
      case FindUser(uid: Int) =>
        println("Updating pavan user")

        val user: Users = userService.findUser(uid)
        sender() ! user
      case FindAllUsers =>
        println("Updating pavan user")

        val users: List[Users] = userService.findAllUsers()
        sender() ! users
      case UpdateUser(uid: Int, user: Users) =>
        try {
          println("Updating pavan user")

          sender() ! userService.updateUser(uid, user)
        } catch {
          println("Updating pavan user")

          case e: Exception => sender() ! FailedUpdated(e.getMessage)
        }
    }


  }

  implicit val system = ActorSystem("AkkaHttpPlayground")
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  private val userDBActor: ActorRef = system.actorOf(Props[UserDBActor], "UserDBActor")

  def validateCredentials(username: String, password: String): Boolean = {
    println("Updating user")

    userService.validateCredentials(username, password)

  }

  val algorithm = JwtAlgorithm.HS256

  def createUserToken(username: String): String = {
    val jwtClaim = JwtClaim(
      issuer = Some("com.tellius.auth"),
      expiration = Some(System.currentTimeMillis() / 1000 + TimeUnit.DAYS.toSeconds(1)),
      issuedAt = Some(System.currentTimeMillis())
    )
    JwtSprayJson.encode(jwtClaim, jwtSecret, algorithm)
  }

  val authenticationRoute = pathPrefix("api") {
    post {
      (path("login") & entity(as[LoginRequest])) {
        case LoginRequest(username, password) =>
          println("Updating pavan user")

          val isValid = validateCredentials(username, password)
          if (isValid) {
            val token = createUserToken(username)
            respondWithHeader(RawHeader("Acccess-Token", token)) {

              println("Updating pavan user")
              println("Updating user");    println("Updating pavan user")
              println("Updating pavan user")


              complete(StatusCodes.OK)
            }
          } else {
            complete(StatusCodes.Unauthorized, "Invalid credentials")
          }
      } ~ (pathEndOrSingleSlash & entity(as[Users])) { user =>
        complete((userDBActor ? CreateUser(user)).mapTo[UserCreated].map(_.user))
      }

    }


  }

  def isTokenExpired(token: String): Boolean = {
    JwtSprayJson.decode(token, jwtSecret, Seq(algorithm)) match {
      case Success(claim) =>
        val exp: Long = claim.expiration.getOrElse(0)
        val current: Long = System.currentTimeMillis() / 1000
        println("Updating user")

        println("Updating pavan user")

        current > exp
      case Failure(value) =>
        true
    }
  }

  def isTokenValid(token: String): Boolean = JwtSprayJson.isValid(token, jwtSecret, Seq(algorithm))


  // Define the request handlers for the API
  implicit val timeout: Timeout = Timeout(3 seconds)


  val resourceRoute = get {
    path(IntNumber) { uid =>
      complete((userDBActor ? FindUser(uid)).mapTo[Users])
    } ~
      parameter("uid".as[Int]) { uid =>
        complete((userDBActor ? FindUser(uid)).mapTo[Users])
      } ~ pathEndOrSingleSlash {

      complete((userDBActor ? FindAllUsers).mapTo[List[Users]])
    }
  } ~ post {
    entity(as[Users]) { user =>
//      complete((userDBActor ? CreateUser(user)).mapTo[UserCreated].map(_.user))
      val createdFuture = userDBActor ? CreateUser(user)
      onComplete(createdFuture) {
        case Success(result: UserCreated) => complete(result.user)
        case Success(result: FailedUpdated) => complete(StatusCodes.BadRequest, result.message)
      }
    }
  } ~ patch {
    (parameter('uid.as[Int]) & entity(as[Users])) { (uid, user) =>
      val eventualBooleanFuture = (userDBActor ? UpdateUser(uid, user))
      onComplete(eventualBooleanFuture) {

        case Success(result: Boolean) => if (result) complete("Used updated successfully") else complete(StatusCodes.BadRequest, "User id not found")
        case Success(result: FailedUpdated) => complete(StatusCodes.BadRequest, result.message)
      }
    }
  }

  val authorizationRoute = pathPrefix("api" / "user") {
    optionalHeaderValueByName("Authorization") {
      case Some(token) if isTokenExpired(token) => complete(StatusCodes.Unauthorized, "Authorization token expired")
      case Some(token) if isTokenValid(token) => resourceRoute
      case _ => complete(HttpResponse(status = StatusCodes.Unauthorized, entity = "token is not valid"))
    }
  }

  val route = authenticationRoute ~ authorizationRoute

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8087)
}
