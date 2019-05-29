package controllers

import repositories.UserRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.Json
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

@Singleton
class UserController @Inject()(userRepository: UserRepository, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getUsers = {
    Action.async { implicit request =>
      userRepository.getUsers().map {
        user => Ok(Json.toJson(user))
      }
    }
  }

  def getUser(id: Int) = Action.async {
    implicit request =>
      val options = for {
        user <- userRepository.getUserById(id)
      } yield (user)

      options.map { case (option) =>
        option match {
          case Some(user) => Ok(Json.toJson(user))
          case None => NotFound
        }
      }
  }

  val userForm: Form[UserForm] = Form {
    mapping(
      "email" -> nonEmptyText,
      "password" -> nonEmptyText,
      "address" -> nonEmptyText,
      "city" -> nonEmptyText,
      "postalCode" -> nonEmptyText,
      "country" -> nonEmptyText,
      "phone" -> nonEmptyText,
    )(UserForm.apply)(UserForm.unapply)
  }

  def addUser = Action.async { implicit request =>
    userForm.bindFromRequest.fold(
      _ => {
        Future.successful(BadRequest("Failed to add."))
      },
      user => {
        userRepository.addUser(
          user.email,
          user.password,
          user.address,
          user.city,
          user.postalCode,
          user.country,
          user.phone
        ).map { user =>
          Created(Json.toJson(user))
        }
      }
    )
  }

  def updateUser(id: Int) =
    Action.async(parse.json) {
      implicit request =>
        userForm.bindFromRequest.fold(
          _ => {
            Future.successful(BadRequest("Failed to update."))
          },
          user => {
            userRepository.updateUser(models.User(
              id,
              user.email,
              user.password,
              user.address,
              user.city,
              user.postalCode,
              user.country,
              user.phone
            )).map({ _ =>
              Ok
            })
          }
        )
    }

  def deleteUser(id: Int) = Action.async(
    userRepository.deleteUser(id).map(_ => Ok("Removed."))
  )
}

case class UserForm(email: String, password: String, address: String, city: String, postalCode: String, country: String, phone: String)
