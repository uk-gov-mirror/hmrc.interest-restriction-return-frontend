package controllers.$section;format="decap"$

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching}
import controllers.actions._
import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.$section;format="decap"$.$className;format="cap"$View
import controllers.BaseNavigationController
import scala.concurrent.{ExecutionContext, Future}

class $className;format="cap"$Controller @Inject()(override val messagesApi: MessagesApi,
                                      identify: IdentifierAction,
                                      getData: DataRetrievalAction,
                                      requireData: DataRequiredAction,
                                      val controllerComponents: MessagesControllerComponents,
                                      view: $className;format="cap"$View
                                     )(implicit ec: ExecutionContext, appConfig: FrontendAppConfig)
  extends FrontendBaseController with I18nSupport with FeatureSwitching {

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view())
  }
}
