package controllers.elections

import controllers.actions._
import forms.elections.InvestmentNameFormProvider
import javax.inject.Inject
import models.Mode
import pages.elections.InvestmentNamePage
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.elections.InvestmentNameView
import config.FrontendAppConfig
import play.api.data.Form

import config.featureSwitch.{FeatureSwitching}
import scala.concurrent.Future
import navigation.ElectionsNavigator
import services.QuestionDeletionLookupService
import controllers.BaseNavigationController

class InvestmentNameController @Inject()(
                                       override val messagesApi: MessagesApi,
                                       val sessionRepository: SessionRepository,
                                       val navigator: ElectionsNavigator,
                                       val questionDeletionLookupService: QuestionDeletionLookupService,
                                       identify: IdentifierAction,
                                       getData: DataRetrievalAction,
                                       requireData: DataRequiredAction,
                                       formProvider: InvestmentNameFormProvider,
                                       val controllerComponents: MessagesControllerComponents,
                                       view: InvestmentNameView
                                    )(implicit appConfig: FrontendAppConfig) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    answerFor(InvestmentNamePage, Some(idx))
    Ok(view(fillForm(InvestmentNamePage, formProvider()), mode))
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value =>
        saveAndRedirect(InvestmentNamePage, value, mode)
    )
  }
}
