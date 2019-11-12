package controllers

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, UseNunjucks}
import controllers.actions._
import javax.inject.Inject
import nunjucks.Renderer
import nunjucks.$className$Template
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.$className$View

import scala.concurrent.{ExecutionContext, Future}

class $className$Controller @Inject()(override val messagesApi: MessagesApi,
                                      identify: IdentifierAction,
                                      getData: DataRetrievalAction,
                                      requireData: DataRequiredAction,
                                      val controllerComponents: MessagesControllerComponents,
                                      view: $className$View,
                                      renderer: Renderer
                                     )(implicit ec: ExecutionContext, appConfig: FrontendAppConfig)
  extends FrontendBaseController with I18nSupport with FeatureSwitching {

  private def renderView(implicit request: Request[_]) = if(isEnabled(UseNunjucks)) {
    renderer.render($className$Template)
  } else {
    Future.successful(view())
  }

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      renderView.map(Ok(_))
  }
}
