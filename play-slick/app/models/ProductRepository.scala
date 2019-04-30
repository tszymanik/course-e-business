package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{Future, ExecutionContext}

@Singleton
class ProductRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, categoryRepository: CategoryRepository)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  import categoryRepository.CategoryTable

  @Singleton
  class ProductTable(tag: Tag) extends Table[Product](tag, "products") {
    def id = column[Int]("ProductId", O.PrimaryKey, O.AutoInc)

    def name = column[String]("ProductName")
    def description = column[String]("ProductDescription")
    def photo = column[Array[Byte]]("ProductPhoto")
    def categoryId = column[Int]("CategoryId")
    def categoryIdForeignKey = foreignKey("CategoryId", categoryId, category)(_.id)
    def quantityPerUnit = column[String]("QuantityPerUnit")
    def unitPrice = column[Double]("UnitPrice")
    def unitsInStock = column[Int]("UnitsInStock")

    def * = (id, name, description, categoryId, photo, quantityPerUnit, unitPrice, unitsInStock) <> ((Product.apply _).tupled, Product.unapply)
  }

  private val product = TableQuery[ProductTable]
  private val category = TableQuery[CategoryTable]

  def create(name: String, description: String, categoryId: Int, photo: Array[Byte], quantityPerUnit: String, UnitPrice: Double, UnitsInStock: Int): Future[Product] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (product.map(p => (p.name, p.description, p.categoryId))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning product.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into { case ((name: String, description: String, categoryId: Int), id) => Product(id, name, description, categoryId, photo, quantityPerUnit, UnitPrice, UnitsInStock) }
      // And finally, insert the person into the database
      ) += (name, description, categoryId)
  }

  def list(): Future[Seq[Product]] = db.run {
    product.result
  }

  def getByCategory(categoryId: Int): Future[Seq[Product]] = db.run {
    product.filter(_.categoryId === categoryId).result
  }

  def getByCategories(categoriesId: List[Int]): Future[Seq[Product]] = db.run {
    product.filter(_.categoryId inSet categoriesId).result
  }
}
