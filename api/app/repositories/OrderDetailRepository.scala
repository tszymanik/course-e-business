package repositories

import javax.inject.{Inject, Singleton}
import models.OrderDetail
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderDetailRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, orderRepository: OrderRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class OrderDetailsTable(tag: Tag) extends Table[OrderDetail](tag, "Order Details") {
    def id = column[Int]("OrderId")

    def productId = column[Int]("ProductId")

    def unitPrice = column[Double]("UnitPrice")

    def quantity = column[Int]("Quantity")

    def * = (id, productId, unitPrice, quantity) <> ((OrderDetail.apply _).tupled, OrderDetail.unapply)
  }

  private val orderDetails = TableQuery[OrderDetailsTable]

  def getOrdersDetails(): Future[Seq[OrderDetail]] = db.run {
    orderDetails.result
  }

  def getOrderDetailsById(orderId: Int): Future[Option[OrderDetail]] = db.run {
    orderDetails.filter(_.id === orderId).result.headOption
  }

  def addOrderDetails(productId: Int, unitPrice: Double, quantity: Int): Future[OrderDetail] = db.run {
    (orderDetails.map(orderDetails => (orderDetails.productId, orderDetails.unitPrice, orderDetails.quantity))
      returning orderDetails.map(_.id)
      into { case ((productId: Int, unitPrice: Double, quantity: Int), id) => OrderDetail(id, productId, unitPrice, quantity) }
      ) += (productId, unitPrice, quantity)
  }

  def deleteOrderDetails(id: Int): Future[Unit] = db.run {
    orderDetails.filter(_.id === id).delete.map(_ => ())
  }
}