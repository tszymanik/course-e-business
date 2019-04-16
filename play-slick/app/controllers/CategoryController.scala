package controllers

import javax.inject._
import play.api.mvc._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class CategoryController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action {
    Ok("CategoryController")
  }

  def getCategories = Action {
    Ok("CategoryController:getCategories")
  }

  def getCategory(id: String) = Action {
    Ok("CategoryController:getCategory")
  }

  def addCategory = Action {
    Ok("CategoryController:addCategory")
  }

  def updateCategory(id: String) = Action {
    Ok("CategoryController:updateCategory")
  }

  def deleteCategory(id: String) = Action {
    Ok("CategoryController:deleteCategory")
  }

}