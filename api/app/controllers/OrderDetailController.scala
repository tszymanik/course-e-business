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
class OrderDetailController @Inject()(orderDetailRepository: OrderDetailRepository, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {
  val orderDetailForm: Form[OrderDetailForm] = Form {
    mapping(
      "orderId" -> number,
      "productId" -> number,
      "unitPrice" -> of(doubleFormat),
      "quantity" -> number,
    )(OrderDetailForm.apply)(OrderDetailForm.unapply)
  }

  def getOrdersDetails() = {
    Action.async { implicit request =>
      orderDetailRepository.getOrdersDetails().map {
        orderDetails => Ok(Json.toJson(orderDetails))
      }
    }
  }

  def getOrderDetail(id: Int) = Action.async {
    implicit request =>
      val computerAndOptions = for {
        orderDetail <- orderDetailRepository.getOrderDetailById(id)
      } yield (orderDetail)

      computerAndOptions.map { case (computer) =>
        computer match {
          case Some(orderDetail) => Ok(Json.toJson(orderDetail))
          case None => NotFound
        }
      }
  }

  def addOrderDetail = Action.async { implicit request =>
    orderDetailForm.bindFromRequest.fold(
      _ => {
        Future.successful(BadRequest("Failed to add."))
      },
      orderDetail => {
        orderDetailRepository.addOrderDetail(
          orderDetail.orderId,
          orderDetail.productId,
          orderDetail.unitPrice,
          orderDetail.quantity
        ).map { orderDetail =>
          Created(Json.toJson(orderDetail))
        }
      }
    )
  }

  def updateOrderDetail(id: Int) = {
    Action.async(parse.json) {
      implicit request =>
        orderDetailForm.bindFromRequest.fold(
          _ => {
            Future.successful(BadRequest("Failed to update."))
          },
          orderDetail => {
            orderDetailRepository.updateOrderDetail(models.OrderDetail(
              id,
              orderDetail.orderId,
              orderDetail.productId,
              orderDetail.unitPrice,
              orderDetail.quantity
            )).map({ _ =>
              Ok
            })
          }
        )
    }
  }

  def deleteOrderDetail(id: Int) = Action.async(
    orderDetailRepository.deleteOrderDetail(id).map(_ => Ok("Removed."))
  )
}

case class OrderDetailForm(orderId: Int, productId: Int, unitPrice: Double, quantity: Int)
