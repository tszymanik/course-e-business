package models

import play.api.libs.json._

case class OrderDetail(
                        id: Int,
                        orderId: Int,
                        productId: Int,
                        unitPrice: Double,
                        quantity: Int
                      )

object OrderDetail {
  implicit val orderDetailFormat = Json.format[OrderDetail]
}
