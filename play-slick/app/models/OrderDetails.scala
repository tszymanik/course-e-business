package models

import play.api.libs.json._

case class OrderDetails(
                         id: Int,
                         productId: Int,
                         unitPrice: Double,
                         quantity: Int
                       )

object OrderDetails {
  implicit val orderDetailsFormat = Json.format[Product]
}
