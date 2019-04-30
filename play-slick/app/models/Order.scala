package models

import java.sql.Timestamp

import play.api.libs.json._

case class Order(
                  id: Int,
                  userId: Int,
                  orderDate: Timestamp,
                  ShippedDate: Timestamp
                )

object Order {
  implicit val orderFormat = Json.format[Product]
}