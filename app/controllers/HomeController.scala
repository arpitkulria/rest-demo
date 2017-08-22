package controllers

import javax.inject.{Inject, _}

import modals.Transfer
import play.api.data.Form
import play.api.data.Forms._
import play.api.db._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc._
import services.UserService

import scala.concurrent.Future

@Singleton
class HomeController @Inject()(userService: UserService) extends InjectedController {

  def index() = Action { implicit request: Request[AnyContent] =>
    val users = userService.getAllUser
    Ok(views.html.index(users.toList))
  }

  val transferForm = Form(
    mapping(
      "from" -> nonEmptyText,
      "to" -> nonEmptyText,
      "amount" -> bigDecimal
    )(Transfer.apply)(Transfer.unapply))


  def transfer() = Action.async { implicit request =>

    (transferForm.bindFromRequest.fold(
      formWithErrors => {
       Future.successful(Redirect(routes.HomeController.index()).flashing("ERROR" -> "There was some problem with from data"))
      },
      transfer => {
        userService.transfer(transfer) map { op =>
        Redirect(routes.HomeController.index()).flashing( (if(op == "Transfer Complete") "SUCCESS" else "ERROR") -> op)
        }
      })).recover {
      case ex: Throwable =>
        Redirect(routes.HomeController.index()).flashing("ERROR" -> "There was some problem with transfer")
    }
  }
}
