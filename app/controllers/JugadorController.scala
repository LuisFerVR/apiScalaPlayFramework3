package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.{ ExecutionContext, Future }
import models.Jugador
import models.daos.JugadorDAO

@Singleton
class JugadorController @Inject() (val controllerComponents: ControllerComponents, jugadorDAO: JugadorDAO)
                                  (implicit ec: ExecutionContext) extends BaseController {

  def createJugador = Action.async(parse.json) { implicit request =>
    val nombre = (request.body \ "nombre").asOpt[String]
    val posicion = (request.body \ "posicion").asOpt[String]
    val dorsal = (request.body \ "dorsal").asOpt[Int]
    val equipo_id = (request.body \ "equipo_id").asOpt[Int]

    (nombre, posicion, dorsal, equipo_id) match {
      case (Some(nombre), Some(posicion), Some(dorsal), Some(equipo_id)) =>
        jugadorDAO.createJugador(nombre, posicion, dorsal, equipo_id).map { id =>
          Created(Json.obj("id" -> id, "Message" -> s"Jugador created with id $id"))
        }
      case _ =>
        Future.successful(BadRequest(Json.obj("Message" -> "Invalid Data")))
    }
  }

  def getJugadores = Action.async { implicit request: Request[AnyContent] =>
    jugadorDAO.getAllJugadores().map { jugadores =>
      Ok(Json.toJson(jugadores))
    }
  }

  def getJugador(id: Int) = Action.async { implicit request: Request[AnyContent] =>
    jugadorDAO.getJugadorById(id).map {
      case Some(jugador) => Ok(Json.toJson(jugador))
      case None => NotFound(Json.obj("message" -> "Jugador not found"))
    }
  }

  def deleteJugador(id: Int) = Action.async { implicit request: Request[AnyContent] =>
    jugadorDAO.deleteJugador(id).map { result =>
      if (result > 0) {
        Ok(Json.obj("Message" -> s"Jugador with id $id deleted"))
      } else {
        NotFound(Json.obj("Message" -> "Jugador not found"))
      }
    }
  }

  def updateJugador(id: Int) = Action.async(parse.json) { implicit request =>
    val nombre = (request.body \ "nombre").asOpt[String]
    val posicion = (request.body \ "posicion").asOpt[String]
    val dorsal = (request.body \ "dorsal").asOpt[Int]
    val equipo_id = (request.body \ "equipo_id").asOpt[Int]

    (nombre, posicion, dorsal, equipo_id) match {
      case (Some(nombre), Some(posicion), Some(dorsal), Some(equipo_id)) =>
        val jugador = Jugador(Some(id), nombre, posicion, dorsal, equipo_id)

        jugadorDAO.updateJugador(id, jugador).map { result =>
          if (result > 0) {
            Ok(Json.obj("Message" -> s"Jugador with id $id updated"))
          } else {
            NotFound(Json.obj("Message" -> "Jugador not found"))
          }
        }
      case _ =>
        Future.successful(BadRequest(Json.obj("Message" -> "Invalid Data")))
    }
  }
}
