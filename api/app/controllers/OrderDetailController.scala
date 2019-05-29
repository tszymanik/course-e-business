package controllers

import javax.inject._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats.doubleFormat
import play.api.libs.json.Json
import play.api.mvc._
import repositories.OrderDetailRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderDetailsController @Inject()(orderDetailRepository: OrderDetailRepository, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {
  def getOrdersDetails() = {
    Action.async { implicit request =>
      orderDetailRepository.getOrdersDetails().map {
        orderDetails => Ok(Json.toJson(orderDetails))
      }
    }
  }

  val orderDetailsForm: Form[OrderDetailForm] = Form {
    mapping(
      "productId" -> number,
      "unitPrice" -> of(doubleFormat),
      "quantity" -> number,
    )(OrderDetailForm.apply)(OrderDetailForm.unapply)
  }

  def addOrderDetails = Action.async { implicit request =>
    orderDetailsForm.bindFromRequest.fold(
      _ => {
        Future.successful(BadRequest("Failed to add."))
      },
      orderDetails => {
        orderDetailRepository.addOrderDetails(
          orderDetails.productId,
          orderDetails.unitPrice,
          orderDetails.quantity
        ).map { orderDetails =>
          Created(Json.toJson(orderDetails))
        }
      }
    )
  }
}

case class OrderDetailForm(productId: Int, unitPrice: Double, quantity: Int)
