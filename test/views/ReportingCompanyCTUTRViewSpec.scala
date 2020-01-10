package views

import controllers.routes
import forms.ReportingCompanyCTUTRFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.nunjucks.NunjucksSupport
import uk.gov.hmrc.viewmodels.Radios
import views.behaviours.StringViewBehaviours
import views.html.reportingCompanyCTUTRView
import nunjucks.ReportingCompanyCTUTRTemplate
import nunjucks.viewmodels.BasicFormViewModel

class reportingCompanyCTUTRViewSpec extends StringViewBehaviours with NunjucksSupport {

  val messageKeyPrefix = "reportingCompanyCTUTR"

  val form = new ReportingCompanyCTUTRFormProvider()()

  Seq(Nunjucks, Twirl).foreach { templatingSystem =>

    s"ReportingCompanyCTUTR ($templatingSystem) view" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable =
        if (templatingSystem == Nunjucks) {
          await(nunjucksRenderer.render(ReportingCompanyCTUTRTemplate, Json.toJsObject(BasicFormViewModel(form, NormalMode)))(fakeRequest))
        } else {
          val view = viewFor[reportingCompanyCTUTRView](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))

      behave like stringPage(form, applyView, messageKeyPrefix, routes.ReportingCompanyCTUTRController.onSubmit(NormalMode).url)
    }
  }
}
