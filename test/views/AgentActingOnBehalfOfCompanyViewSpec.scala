package views

import controllers.routes
import forms.AgentActingOnBehalfOfCompanyFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.nunjucks.NunjucksSupport
import uk.gov.hmrc.viewmodels.Radios
import views.behaviours.YesNoViewBehaviours
import views.html.agentActingOnBehalfOfCompanyView
import nunjucks.AgentActingOnBehalfOfCompanyTemplate
import nunjucks.AgentActingOnBehalfOfCompanyTemplate
import nunjucks.viewmodels.YesNoRadioViewModel

class agentActingOnBehalfOfCompanyViewSpec extends YesNoViewBehaviours with NunjucksSupport {

  val messageKeyPrefix = "agentActingOnBehalfOfCompany"

  val form = new AgentActingOnBehalfOfCompanyFormProvider()()

  Seq(Nunjucks, Twirl).foreach { templatingSystem =>

    s"AgentActingOnBehalfOfCompany ($templatingSystem) view" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable =
        if (templatingSystem == Nunjucks) {
          await(nunjucksRenderer.render(AgentActingOnBehalfOfCompanyTemplate, Json.toJsObject(YesNoRadioViewModel(form, NormalMode)))(fakeRequest))
        } else {
          val view = viewFor[agentActingOnBehalfOfCompanyView](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))

      behave like yesNoPage(form, applyView, messageKeyPrefix, routes.AgentActingOnBehalfOfCompanyController.onSubmit(NormalMode).url)
    }
  }
}
