package models

import play.api.libs.json._

case class Product(
                    id: Int,
                    name: String,
                    description: String,
                    categoryId: Int,
                    photo: Array[Byte],
                    QuantityPerUnit: String,
                    UnitPrice: Double,
                    UnitsInStock: Int
                  )

object Product {
  implicit val productFormat = Json.format[Product]
}
