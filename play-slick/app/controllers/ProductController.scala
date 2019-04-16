package controllers

import javax.inject._
import play.api.mvc._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class ProductController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action {
    Ok("ProductController")
  }

  def getProducts = Action {
    Ok("ProductController:getProducts")
  }

  def getProduct(id: String) = Action {
    Ok("ProductController:getProduct")
  }

  def addProduct = Action {
    Ok("ProductController:addProduct")
  }

  def updateProduct(id: String) = Action {
    Ok("ProductController:updateProduct")
  }

  def deleteProduct(id: String) = Action {
    Ok("ProductController:deleteProduct")
  }

}
