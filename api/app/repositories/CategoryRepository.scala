package repositories

import javax.inject.{Inject, Singleton}
import models.Category
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CategoryTable(tag: Tag) extends Table[Category](tag, "Categories") {
    def id = column[Int]("CategoryId", O.PrimaryKey, O.AutoInc)

    def name = column[String]("CategoryName")

    def description = column[String]("CategoryDescription")

    def * = (id, name, description) <> ((Category.apply _).tupled, Category.unapply)
  }

  val categories = TableQuery[CategoryTable]

  def getCategories(): Future[Seq[Category]] = db.run {
    categories.result
  }

  def getCategoryById(categoryId: Int): Future[Option[Category]] = db.run {
    categories.filter(_.id === categoryId).result.headOption
  }

  def addCategory(name: String, description: String): Future[Category] = db.run {
    (categories.map(category => (category.name, category.description))
      returning categories.map(_.id)
      into { case ((name: String, description: String), id) => Category(id, name, description) }
      ) += (name, description)
  }

  def updateCategory(category: Category) = db.run {
    categories.insertOrUpdate(category)
  }

  def deleteCategory(id: Int): Future[Unit] = db.run {
    categories.filter(_.id === id).delete.map(_ => ())
  }
}