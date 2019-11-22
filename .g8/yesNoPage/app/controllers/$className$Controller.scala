package controllers

import config.FrontendAppConfig
import controllers.actions._
import forms.$className$FormProvider
import javax.inject.Inject
import models.Mode
import navigation.Navigator
import pages.$className$Page
import config.featureSwitch.{FeatureSwitching, UseNunjucks}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import uk.gov.hmrc.nunjucks.NunjucksSupport
import views.html.$className;format="decap"$View
import nunjucks.Renderer
import nunjucks.viewmodels.YesNoRadioViewModel
import nunjucks.$className$Template
import play.api.data.Form
import play.api.libs.json.Json
import uk.gov.hmrc.viewmodels.Radios

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
                                         view: $className;format="decap"$View,
                                         renderer: Renderer
                                 )(implicit appConfig: FrontendAppConfig) extends BaseController with NunjucksSupport with FeatureSwitching {

  private def viewHtml(form: Form[Boolean], mode: Mode)(implicit request: Request[_]) = if(isEnabled(UseNunjucks)) {
      renderer.render($className$Template, Json.toJsObject(YesNoRadioViewModel(form, mode)))
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
