package controllers

import models.CategoryRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.Json
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class CategoryController @Inject()(categoryRepository: CategoryRepository, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action {
    Ok("CategoryController")
  }

  def getCategories = {
    Action.async { implicit request =>
      categoryRepository.getCategories().map {
        user => Ok(Json.toJson(user))
      }
    }
  }

  def getCategory(id: Int) = Action.async {
    implicit request =>
      val computerAndOptions = for {
        category <- categoryRepository.getCategoryById(id)
      } yield (category)

      computerAndOptions.map { case (computer) =>
        computer match {
          case Some(category) => Ok(Json.toJson(category))
          case None => NotFound
        }
      }
  }

  val categoryForm: Form[CategoryForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
    )(CategoryForm.apply)(CategoryForm.unapply)
  }

  def addCategory = Action.async { implicit request =>
    categoryForm.bindFromRequest.fold(
      _ => {
        Future.successful(BadRequest("Failed to add."))
      },
      category => {
        categoryRepository.addCategory(
          category.name,
          category.description
        ).map { user =>
          Created(Json.toJson(user))
        }
      }
    )
  }

  def updateCategory(id: Int) =
    Action.async(parse.json) {
      implicit request =>
        categoryForm.bindFromRequest.fold(
          _ => {
            Future.successful(BadRequest("Failed to update."))
          },
          category => {
            categoryRepository.updateCategory(models.Category(
              id,
              category.name,
              category.description,
            )).map({ _ =>
              Ok
            })
          }
        )
    }

  def deleteCategory(id: Int) = Action.async(
    categoryRepository.deleteCategory(id).map(_ => Ok("Removed."))
  )
}

case class CategoryForm(name: String, description: String)