package models

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

@Singleton
class OrderDetailsRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, orderRepository: OrderRepository, productRepository: ProductRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  import orderRepository.OrderTable
  import productRepository.ProductTable

  private class OrderDetailsTable(tag: Tag) extends Table[OrderDetails](tag, "Order Details") {
    def id = column[Int]("OrderId")

    def idForeignKey = foreignKey("OrderId", id, orders)(_.id)

    def productId = column[Int]("ProductId")

    def productIdForeignKey = foreignKey("ProductID", productId, products)(_.id)

    def unitPrice = column[Double]("UnitPrice")

    def quantity = column[Int]("Quantity")

    def * = (id, productId, unitPrice, quantity) <> ((OrderDetails.apply _).tupled, OrderDetails.unapply)
  }

  private val ordersDetails = TableQuery[OrderDetailsTable]
  private val orders = TableQuery[OrderTable]
  private val products = TableQuery[ProductTable]

  def getOrdersDetails(): Future[Seq[OrderDetails]] = db.run {
    ordersDetails.result
  }

  def getOrderDetailsById(orderId: Int): Future[Option[OrderDetails]] = db.run {
    ordersDetails.filter(_.id === orderId).result.headOption
  }

  def addOrderDetails(productId: Int, unitPrice: Double, quantity: Int): Future[OrderDetails] = db.run {
    (ordersDetails.map(orderDetails => (orderDetails.productId, orderDetails.unitPrice, orderDetails.quantity))
      returning ordersDetails.map(_.id)
      into { case ((productId: Int, unitPrice: Double, quantity: Int), id) => OrderDetails(id, productId, unitPrice, quantity) }
      ) += (productId, unitPrice, quantity)
  }

  def deleteOrderDetails(id: Int): Future[Unit] = db.run {
    ordersDetails.filter(_.id === id).delete.map(_ => ())
  }
}
