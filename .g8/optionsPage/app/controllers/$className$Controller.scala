package controllers

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, UseNunjucks}
import controllers.actions._
import forms.$className$FormProvider
import javax.inject.Inject
import models.{$className$, Mode}
import navigation.Navigator
import pages.$className$Page
import uk.gov.hmrc.nunjucks.NunjucksSupport
import nunjucks.Renderer
import nunjucks.$className$Template
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.$className$View
import play.api.data.Form
import play.api.libs.json.Json

import scala.concurrent.Future

class $className;format="cap"$Controller @Inject()(
                                  override val messagesApi: MessagesApi,
                                  sessionRepository: SessionRepository,
                                  navigator: Navigator,
                                  identify: IdentifierAction,
                                  getData: DataRetrievalAction,
                                  requireData: DataRequiredAction,
                                  formProvider: $className$FormProvider,
                                  val controllerComponents: MessagesControllerComponents,
                                  view: $className$View,
                                  renderer: Renderer
                                 )(implicit appConfig: FrontendAppConfig) extends BaseController with NunjucksSupport with FeatureSwitching {

  private def viewHtml(form: Form[$className$], mode: Mode)(implicit request: Request[_]) = if(isEnabled(UseNunjucks)) {
    renderer.render($className$Template, Json.obj(
      "form" -> form,
      "radios" -> $className$.options(form),
      "mode" -> mode
    ))
  } else {
    Future.successful(view(form, mode))
  }

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      viewHtml(fillForm($className$Page, formProvider()), mode).map(Ok(_))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      formProvider().bindFromRequest().fold(
        formWithErrors =>
          viewHtml(formWithErrors, mode).map(BadRequest(_)),

        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set($className$Page, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage($className$Page, mode, updatedAnswers))
      )
  }
}
