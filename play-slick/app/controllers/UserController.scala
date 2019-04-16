package controllers

import javax.inject._
import play.api.mvc._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class UserController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action {
    Ok("UserController")
  }

  def getUsers = Action {
    Ok("UserController:getUsers")
  }

  def getUser(id: String) = Action {
    Ok("UserController:getUser")
  }

  def addUser = Action {
    Ok("UserController:addUser")
  }

  def updateUser(id: String) = Action {
    Ok("UserController:updateUser")
  }

  def deleteUser(id: String) = Action {
    Ok("UserController:deleteUser")
  }

}