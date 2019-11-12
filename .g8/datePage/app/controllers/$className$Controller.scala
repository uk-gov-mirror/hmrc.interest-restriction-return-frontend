package controllers

import config.FrontendAppConfig
import controllers.actions._
import forms.$className$FormProvider
import javax.inject.Inject
import models.Mode
import navigation.Navigator
import pages.$className$Page
import play.api.i18n.MessagesApi
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.$className$View

import scala.concurrent.{ExecutionContext, Future}

class $className$Controller @Inject()(override val messagesApi: MessagesApi,
                                      sessionRepository: SessionRepository,
                                      navigator: Navigator,
                                      identify: IdentifierAction,
                                      getData: DataRetrievalAction,
                                      requireData: DataRequiredAction,
                                      formProvider: $className$FormProvider,
                                      val controllerComponents: MessagesControllerComponents,
                                      view: $className$View
                                      )(implicit ec: ExecutionContext, appConfig: FrontendAppConfig) extends BaseController {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm($className$Page, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
      formProvider.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(formWithErrors, mode))),

        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set($className$Page, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage($className$Page, mode, updatedAnswers))
      )
  }
}
