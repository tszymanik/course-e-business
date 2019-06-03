package repositories

import javax.inject.{Inject, Singleton}
import models.Product
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProductRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, categoryRepository: CategoryRepository)(implicit ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  private val products = TableQuery[ProductTable]

  def getProducts(): Future[Seq[Product]] = db.run {
    products.result
  }

  def getProductById(productId: Int): Future[Option[Product]] = db.run {
    products.filter(_.id === productId).result.headOption
  }

  def getProductsByCategory(categoryId: Int): Future[Seq[Product]] = db.run {
    products.filter(_.categoryId === categoryId).result
  }

  def getProductsByCategories(categoriesId: List[Int]): Future[Seq[Product]] = db.run {
    products.filter(_.categoryId inSet categoriesId).result
  }

  def addProduct(
                  name: String,
                  description: String,
                  categoryId: Int,
                  quantityPerUnit: String,
                  unitPrice: Double,
                  unitsInStock: Int
                ): Future[Product] = db.run {
    (products.map(product => (
      product.name,
      product.description,
      product.categoryId,
      product.quantityPerUnit,
      product.unitPrice,
      product.unitsInStock
    ))
      returning products.map(_.id)
      into { case ((
      name: String,
      description: String,
      categoryId: Int,
      quantityPerUnit: String,
      unitPrice: Double,
      unitsInStock: Int
      ), id) => Product(id, name, description, categoryId, quantityPerUnit, unitPrice, unitsInStock)
    }) += (name, description, categoryId, quantityPerUnit, unitPrice, unitsInStock)
  }

  def updateProduct(product: Product) = db.run {
    products.insertOrUpdate(product)
  }

  def deleteProduct(id: Int): Future[Unit] = db.run {
    products.filter(_.id === id).delete.map(_ => ())
  }

  class ProductTable(tag: Tag) extends Table[Product](tag, "Products") {
    def * = (id, name, description, categoryId, quantityPerUnit, unitPrice, unitsInStock) <> ((Product.apply _).tupled, Product.unapply)

    def id = column[Int]("ProductId", O.PrimaryKey, O.AutoInc)

    def name = column[String]("ProductName")

    def description = column[String]("ProductDescription")

    def categoryId = column[Int]("CategoryId")

    def quantityPerUnit = column[String]("QuantityPerUnit")

    def unitPrice = column[Double]("UnitPrice")

    def unitsInStock = column[Int]("UnitsInStock")
  }
}