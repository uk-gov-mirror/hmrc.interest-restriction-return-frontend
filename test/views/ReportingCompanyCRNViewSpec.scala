package views

import controllers.routes
import forms.ReportingCompanyCRNFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.nunjucks.NunjucksSupport
import uk.gov.hmrc.viewmodels.Radios
import views.behaviours.StringViewBehaviours
import views.html.reportingCompanyCRNView
import nunjucks.ReportingCompanyCRNTemplate
import nunjucks.viewmodels.BasicFormViewModel

class reportingCompanyCRNViewSpec extends StringViewBehaviours with NunjucksSupport {

  val messageKeyPrefix = "reportingCompanyCRN"

  val form = new ReportingCompanyCRNFormProvider()()

  Seq(Nunjucks, Twirl).foreach { templatingSystem =>

    s"ReportingCompanyCRN ($templatingSystem) view" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable =
        if (templatingSystem == Nunjucks) {
          await(nunjucksRenderer.render(ReportingCompanyCRNTemplate, Json.toJsObject(BasicFormViewModel(form, NormalMode)))(fakeRequest))
        } else {
          val view = viewFor[reportingCompanyCRNView](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))

      behave like stringPage(form, applyView, messageKeyPrefix, routes.ReportingCompanyCRNController.onSubmit(NormalMode).url)
    }
  }
}
