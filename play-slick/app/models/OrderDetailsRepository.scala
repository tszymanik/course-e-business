package models

import java.sql.Timestamp

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderDetailsRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, orderRepository: OrderRepository, productRepository: ProductRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  import orderRepository.OrderTable
  import productRepository.ProductTable


  private class OrderDetailsTable(tag: Tag) extends Table[OrderDetails](tag, "Order Details") {
    def id = column[Int]("OrderId")
    def idForeignKey = foreignKey("OrderId", id, order)(_.id)

    def productId = column[Int]("ProductId")
    def productIdForeignKey = foreignKey("ProductID", productId, product)(_.id)


    def unitPrice = column[Double]("UnitPrice")
    def quantity = column[Int]("Quantity")

    def * = (id, productId, unitPrice, quantity) <> ((OrderDetails.apply _).tupled, OrderDetails.unapply)
  }


  private val orderDetails = TableQuery[OrderDetailsTable]
  private val order = TableQuery[OrderTable]
  private val product = TableQuery[ProductTable]

  def create(productId: Int, unitPrice: Double, quantity: Int): Future[OrderDetails] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (orderDetails.map(oD => (oD.productId, oD.unitPrice, oD.quantity))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning orderDetails.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into { case ((productId, unitPrice, quantity), id) => OrderDetails(id, productId, unitPrice, quantity) }
      // And finally, insert the person into the database
      ) += (productId, unitPrice, quantity)
  }

  def list(): Future[Seq[OrderDetails]] = db.run {
    orderDetails.result
  }
}
