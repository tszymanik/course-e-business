package controllers

import models.ProductRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.libs.json.Json
import play.api.mvc._


/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class ProductController @Inject()(productRepository: ProductRepository, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action {
    Ok("ProductController")
  }

  def getProducts = {
    Action.async { implicit request =>
      productRepository.getProducts().map {
        product => Ok(Json.toJson(product))
      }
    }
  }

  def getProduct(id: Int) = Action.async {
    implicit request =>
      val computerAndOptions = for {
        product <- productRepository.getProductById(id)
      } yield (product)

      computerAndOptions.map { case (computer) =>
        computer match {
          case Some(product) => Ok(Json.toJson(product))
          case None => NotFound
        }
      }
  }

  val productForm: Form[ProductForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "categoryId" -> number,
      "quantityPerUnit" -> nonEmptyText,
      "unitPrice" -> of(doubleFormat),
      "unitsInStock" -> number,
    )(ProductForm.apply)(ProductForm.unapply)
  }

  def addProduct = Action.async { implicit request =>
    productForm.bindFromRequest.fold(
      _ => {
        Future.successful(BadRequest("Failed to add."))
      },
      product => {
        productRepository.addProduct(
          product.name,
          product.description,
          product.categoryId,
          product.quantityPerUnit,
          product.unitPrice,
          product.unitsInStock
        ).map { product =>
          Created(Json.toJson(product))
        }
      }
    )
  }

  def updateProduct(id: Int) =
    Action.async(parse.json) {
      implicit request =>
        productForm.bindFromRequest.fold(
          _ => {
            Future.successful(BadRequest("Failed to update."))
          },
          product => {
            productRepository.updateProduct(models.Product(
              id,
              product.name,
              product.description,
              product.categoryId,
              product.quantityPerUnit,
              product.unitPrice,
              product.unitsInStock
            )).map({ _ =>
              Ok
            })
          }
        )
    }

  def deleteProduct(id: Int) = Action.async(
    productRepository.deleteProduct(id).map(_ => Ok("Removed."))
  )
}

case class ProductForm(name: String, description: String, categoryId: Int, quantityPerUnit: String, unitPrice: Double, unitsInStock: Int)
