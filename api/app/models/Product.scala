package models

import play.api.libs.json._

case class Product(
                    id: Int,
                    name: String,
                    description: String,
                    categoryId: Int,
                    quantityPerUnit: String,
                    unitPrice: Double,
                    unitsInStock: Int
                  )

object Product {
  implicit val productFormat = Json.format[Product]
}
