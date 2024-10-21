package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.{ExecutionContext, Future}
import models.Torneo
import models.daos.TorneoDAO

@Singleton
class TorneoController @Inject() (val controllerComponents: ControllerComponents, torneoDAO: TorneoDAO)
                                 (implicit ec: ExecutionContext) extends BaseController {

  def createTorneo = Action.async(parse.json) { implicit request =>
    val nombre = (request.body \ "nombre").asOpt[String]

    nombre match {
      case Some(nombre) =>
        torneoDAO.createTorneo(nombre).map { id =>
          Created(Json.obj("id" -> id, "Message" -> s"Torneo created with id $id"))
        }
      case _ =>
        Future.successful(BadRequest(Json.obj("Message" -> "Invalid Data")))
    }
  }

  def getTorneos = Action.async { implicit request: Request[AnyContent] =>
    torneoDAO.getAllTorneos().map { torneos =>
      Ok(Json.toJson(torneos))
    }
  }

  def getTorneo(id: Int) = Action.async { implicit request: Request[AnyContent] =>
    torneoDAO.getTorneoById(id).map {
      case Some(torneo) => Ok(Json.toJson(torneo))
      case None => NotFound(Json.obj("message" -> "Torneo not found"))
    }
  }

  def deleteTorneo(id: Int) = Action.async { implicit request: Request[AnyContent] =>
    torneoDAO.deleteTorneo(id).map { result =>
      if (result > 0) {
        Ok(Json.obj("Message" -> s"Torneo with id $id deleted"))
      } else {
        NotFound(Json.obj("Message" -> "Torneo not found"))
      }
    }
  }

  def updateTorneo(id: Int) = Action.async(parse.json) { implicit request =>
    val nombre = (request.body \ "nombre").asOpt[String]

    nombre match {
      case Some(nombre) =>
        val torneo = Torneo(Some(id), nombre)

        torneoDAO.updateTorneo(id, torneo).map { result =>
          if (result > 0) {
            Ok(Json.obj("Message" -> s"Torneo with id $id updated"))
          } else {
            NotFound(Json.obj("Message" -> "Torneo not found"))
          }
        }
      case _ =>
        Future.successful(BadRequest(Json.obj("Message" -> "Invalid Data")))
    }
  }
}
