package models

import java.sql.Timestamp
import java.text.SimpleDateFormat

import play.api.libs.json._

case class Order(
                  id: Int,
                  userId: Int,
                  orderDate: Timestamp,
                  shippedDate: Timestamp
                )

object Order extends ((Int, Int, Timestamp, Timestamp) => Order) {
  implicit object timestampFormat extends Format[Timestamp] {
    val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'")
    def reads(json: JsValue) = {
      val str = json.as[String]
      JsSuccess(new Timestamp(format.parse(str).getTime))
    }
    def writes(ts: Timestamp) = JsString(format.format(ts))
  }

  implicit val orderFormat = Json.format[Order]
}