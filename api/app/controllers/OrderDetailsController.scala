package controllers

import models.OrderDetailsRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats.doubleFormat
import play.api.libs.json.Json
import play.api.mvc._


@Singleton
class OrderDetailsController @Inject()(orderDetailsRepository: OrderDetailsRepository, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {
  def getOrdersDetails() = {
    Action.async { implicit request =>
      orderDetailsRepository.getOrdersDetails().map {
        orderDetails => Ok(Json.toJson(orderDetails))
      }
    }
  }

  def getOrderDetails(id: Int) = Action.async {
    implicit request =>
      val options = for {
        orderDetails <- orderDetailsRepository.getOrderDetailsById(id)
      } yield (orderDetails)

      options.map { case (option) =>
        option match {
          case Some(orderDetails) => Ok(Json.toJson(orderDetails))
          case None => NotFound
        }
      }
  }

  val orderDetailsForm: Form[OrderDetailsForm] = Form {
    mapping(
      "productId" -> number,
      "unitPrice" -> of(doubleFormat),
      "quantity" -> number,
    )(OrderDetailsForm.apply)(OrderDetailsForm.unapply)
  }

  def addOrderDetails = Action.async { implicit request =>
    orderDetailsForm.bindFromRequest.fold(
      _ => {
        Future.successful(BadRequest("Failed to add."))
      },
      orderDetails => {
        orderDetailsRepository.addOrderDetails(
          orderDetails.productId,
          orderDetails.unitPrice,
          orderDetails.quantity
        ).map { orderDetails =>
          Created(Json.toJson(orderDetails))
        }
      }
    )
  }

  def deleteOrderDetails(id: Int) = Action.async(
    orderDetailsRepository.deleteOrderDetails(id).map(_ => Ok("Removed."))
  )
}

case class OrderDetailsForm(productId: Int, unitPrice: Double, quantity: Int)
