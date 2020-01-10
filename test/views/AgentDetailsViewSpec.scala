package views

import controllers.routes
import forms.ReportingCompanyAppointedFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.nunjucks.NunjucksSupport
import uk.gov.hmrc.viewmodels.Radios
import views.behaviours.YesNoViewBehaviours
import views.html.agentDetailsView
import nunjucks.AgentDetailsTemplate
import nunjucks.AgentDetailsTemplate
import nunjucks.viewmodels.YesNoRadioViewModel

class agentDetailsViewSpec extends YesNoViewBehaviours with NunjucksSupport {

  val messageKeyPrefix = "agentDetails"

  val form = new ReportingCompanyAppointedFormProvider()()

  Seq(Nunjucks, Twirl).foreach { templatingSystem =>

    s"AgentDetails ($templatingSystem) view" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable =
        if (templatingSystem == Nunjucks) {
          await(nunjucksRenderer.render(AgentDetailsTemplate, Json.toJsObject(YesNoRadioViewModel(form, NormalMode)))(fakeRequest))
        } else {
          val view = viewFor[agentDetailsView](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))

      behave like yesNoPage(form, applyView, messageKeyPrefix, routes.AgentDetailsController.onSubmit(NormalMode).url)
    }
  }
}
