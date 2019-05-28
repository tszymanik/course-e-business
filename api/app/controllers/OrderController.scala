package controllers

import models.OrderRepository


import java.sql.Timestamp
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._

@Singleton
class OrderController @Inject()(orderRepository: OrderRepository, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def index = Action {
    Ok("OrderController")
  }

  def getOrders() = {
    Action.async { implicit request =>
      orderRepository.getOrders().map {
        order => Ok(Json.toJson(order))
      }
    }
  }

  def getOrder(id: Int) = Action.async {
    implicit request =>
      val options = for {
        order <- orderRepository.getOrderById(id)
      } yield (order)

      options.map { case (option) =>
        option match {
          case Some(order) => Ok(Json.toJson(order))
          case None => NotFound
        }
      }
  }

  val orderForm: Form[OrderForm] = Form {
    mapping(
      "userId" -> number,
      "orderDate" -> sqlTimestamp,
      "shippedDate" -> sqlTimestamp,
    )(OrderForm.apply)(OrderForm.unapply)
  }

  def addOrder = Action.async { implicit request =>
    orderForm.bindFromRequest.fold(
      _ => {
        Future.successful(BadRequest("Failed to add."))
      },
      order => {
        orderRepository.addOrder(
          order.userId,
          order.orderDate,
          order.shippedDate
        ).map { order =>
          Created(Json.toJson(order))
        }
      }
    )
  }

  def updateOrder(id: Int) =
    Action.async(parse.json) {
      implicit request =>
        orderForm.bindFromRequest.fold(
          _ => {
            Future.successful(BadRequest("Failed to update."))
          },
          order => {
            orderRepository.updateOrder(models.Order(
              id,
              order.userId,
              order.orderDate,
              order.shippedDate
            )).map({ _ =>
              Ok
            })
          }
        )
    }

  def deleteOrder(id: Int) = Action.async(
    orderRepository.deleteOrder(id).map(_ => Ok("Removed."))
  )
}

case class OrderForm(userId: Int, orderDate: Timestamp, shippedDate: Timestamp)
