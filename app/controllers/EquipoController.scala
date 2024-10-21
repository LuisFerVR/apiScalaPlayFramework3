package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.{ ExecutionContext, Future }
import models.Equipo
import models.daos.EquipoDAO

@Singleton
class EquipoController @Inject() (val controllerComponents: ControllerComponents, equipoDAO: EquipoDAO)
                                 (implicit ec: ExecutionContext) extends BaseController {

  def createEquipo = Action.async(parse.json) { implicit request =>
    val nombre = (request.body \ "nombre").asOpt[String]

    nombre match {
      case Some(nombre) =>
        equipoDAO.createEquipo(nombre).map { id =>
          Created(Json.obj("id" -> id, "Message" -> s"Equipo created with id $id"))
        }
      case _ =>
        Future.successful(BadRequest(Json.obj("Message" -> "Invalid Data")))
    }
  }

  def getEquipos = Action.async { implicit request: Request[AnyContent] =>
    equipoDAO.getAllEquipos().map { equipos =>
      Ok(Json.toJson(equipos))
    }
  }

  def getEquipo(id: Int) = Action.async { implicit request: Request[AnyContent] =>
    equipoDAO.getEquipoById(id).map {
      case Some(equipo) => Ok(Json.toJson(equipo))
      case None => NotFound(Json.obj("message" -> "Equipo not found"))
    }
  }

  def deleteEquipo(id: Int) = Action.async { implicit request: Request[AnyContent] =>
    equipoDAO.deleteEquipo(id).map { result =>
      if (result > 0) {
        Ok(Json.obj("Message" -> s"Equipo with id $id deleted"))
      } else {
        NotFound(Json.obj("Message" -> "Equipo not found"))
      }
    }
  }

  def updateEquipo(id: Int) = Action.async(parse.json) { implicit request =>
    val nombre = (request.body \ "nombre").asOpt[String]

    nombre match {
      case Some(nombre) =>
        val equipo = Equipo(Some(id), nombre)

        equipoDAO.updateEquipo(id, equipo).map { result =>
          if (result > 0) {
            Ok(Json.obj("Message" -> s"Equipo with id $id updated"))
          } else {
            NotFound(Json.obj("Message" -> "Equipo not found"))
          }
        }
      case _ =>
        Future.successful(BadRequest(Json.obj("Message" -> "Invalid Data")))
    }
  }
}
