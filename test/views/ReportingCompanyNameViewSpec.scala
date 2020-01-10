package views

import controllers.routes
import forms.ReportingCompanyNameFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.nunjucks.NunjucksSupport
import uk.gov.hmrc.viewmodels.Radios
import views.behaviours.StringViewBehaviours
import views.html.reportingCompanyNameView
import nunjucks.ReportingCompanyNameTemplate
import nunjucks.viewmodels.BasicFormViewModel

class reportingCompanyNameViewSpec extends StringViewBehaviours with NunjucksSupport {

  val messageKeyPrefix = "reportingCompanyName"

  val form = new ReportingCompanyNameFormProvider()()

  Seq(Nunjucks, Twirl).foreach { templatingSystem =>

    s"ReportingCompanyName ($templatingSystem) view" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable =
        if (templatingSystem == Nunjucks) {
          await(nunjucksRenderer.render(ReportingCompanyNameTemplate, Json.toJsObject(BasicFormViewModel(form, NormalMode)))(fakeRequest))
        } else {
          val view = viewFor[reportingCompanyNameView](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))

      behave like stringPage(form, applyView, messageKeyPrefix, routes.ReportingCompanyNameController.onSubmit(NormalMode).url)
    }
  }
}
