package models.daos

import models.Torneo
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TorneoDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                          (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  class TorneosTable(tag: Tag) extends Table[Torneo](tag, "torneo") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def nombre = column[String]("nombre")
    def * = (id.?, nombre) <> ((Torneo.apply _).tupled, Torneo.unapply)
  }

  private val torneos = TableQuery[TorneosTable]

  def createTorneo(nombre: String): Future[Int] = {
    val torneo = Torneo(None, nombre)
    db.run((torneos returning torneos.map(_.id)) += torneo)
  }

  def getAllTorneos(): Future[Seq[Torneo]] = {
    db.run(torneos.result)
  }

  def getTorneoById(id: Int): Future[Option[Torneo]] = {
    db.run(torneos.filter(_.id === id).result.headOption)
  }

  def deleteTorneo(id: Int): Future[Int] = {
    db.run(torneos.filter(_.id === id).delete)
  }

  def updateTorneo(id: Int, torneo: Torneo): Future[Int] = {
    db.run(torneos.filter(_.id === id).update(torneo))
  }
}
