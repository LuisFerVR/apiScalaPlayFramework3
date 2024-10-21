package models.daos

import models.Jugador
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class JugadorDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                           (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  // Tabla de jugadores
  class JugadoresTable(tag: Tag) extends Table[Jugador](tag, "jugador") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def nombre = column[String]("nombre")
    def posicion = column[String]("posicion")
    def dorsal = column[Int]("dorsal")
    def equipo_id = column[Int]("equipo_id")

    def * = (id.?, nombre, posicion, dorsal, equipo_id) <> ((Jugador.apply _).tupled, Jugador.unapply)
  }

  private val jugadores = TableQuery[JugadoresTable]

  def createJugador(nombre: String, posicion: String, dorsal: Int, equipo_id: Int): Future[Int] = {
    val jugador = Jugador(None, nombre, posicion, dorsal, equipo_id)
    db.run((jugadores returning jugadores.map(_.id)) += jugador)
  }

  def getAllJugadores(): Future[Seq[Jugador]] = {
    db.run(jugadores.result)
  }

  def getJugadorById(id: Int): Future[Option[Jugador]] = {
    db.run(jugadores.filter(_.id === id).result.headOption)
  }

  def deleteJugador(id: Int): Future[Int] = {
    db.run(jugadores.filter(_.id === id).delete)
  }

  def updateJugador(id: Int, jugador: Jugador): Future[Int] = {
    db.run(jugadores.filter(_.id === id).update(jugador))
  }
}
