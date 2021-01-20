package controllers.$section;format="decap"$

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching}
import controllers.actions._
import forms.$section;format="decap"$.$className;format="cap"$FormProvider
import javax.inject.Inject
import models.{$className;format="cap"$, Mode}
import pages.$section;format="decap"$.$className;format="cap"$Page
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.$section;format="decap"$.$className;format="cap"$View
import play.api.data.Form

import scala.concurrent.Future
import navigation.$section;format="cap"$Navigator

class $className;format="cap"$Controller @Inject()(
                                  override val messagesApi: MessagesApi,
                                  val sessionRepository: SessionRepository,
                                  val navigator: $section;format="cap"$Navigator,
                                  val questionDeletionLookupService: QuestionDeletionLookupService,
                                  identify: IdentifierAction,
                                  getData: DataRetrievalAction,
                                  requireData: DataRequiredAction,
                                  formProvider: $className;format="cap"$FormProvider,
                                  val controllerComponents: MessagesControllerComponents,
                                  view: $className;format="cap"$View
                                 )(implicit appConfig: FrontendAppConfig) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm($className;format="cap"$Page, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value =>
        saveAndRedirect($className;format="cap"$Page, value, mode)
    )
  }
}
