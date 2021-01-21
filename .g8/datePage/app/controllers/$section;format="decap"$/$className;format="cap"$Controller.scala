package controllers.$section;format="decap"$

import config.FrontendAppConfig
import controllers.actions._
import forms.$section;format="decap"$.$className;format="cap"$FormProvider
import javax.inject.Inject
import models.Mode
import pages.$section;format="decap"$.$className;format="cap"$Page
import config.featureSwitch.{FeatureSwitching}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.$section;format="decap"$.$className;format="cap"$View
import play.api.data.Form
import navigation.$section;format="cap"$Navigator
import scala.concurrent.Future

class $className;format="cap"$Controller @Inject()(
                                         override val messagesApi: MessagesApi,
                                         sessionRepository: SessionRepository,
                                         navigator: $section;format="cap"$Navigator,
                                         val questionDeletionLookupService: QuestionDeletionLookupService,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: $className;format="cap"$FormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: $className;format="cap"$View
                                 )(implicit appConfig: FrontendAppConfig) extends BaseController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm($className;format="cap"$Page, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value =>
        for {
        updatedAnswers <- Future.fromTry(request.userAnswers.set($className;format="cap"$Page, value))
        _              <- sessionRepository.set(updatedAnswers)
      } yield Redirect(navigator.nextPage($className;format="cap"$Page, mode, updatedAnswers))
    )
  }
}
