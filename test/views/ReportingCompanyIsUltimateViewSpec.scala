package views

import controllers.routes
import forms.ReportingCompanyResultFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.nunjucks.NunjucksSupport
import uk.gov.hmrc.viewmodels.Radios
import views.behaviours.YesNoViewBehaviours
import views.html.reportingCompanyResultView
import nunjucks.ReportingCompanyResultTemplate
import nunjucks.ReportingCompanyResultTemplate
import nunjucks.viewmodels.YesNoRadioViewModel

class reportingCompanyResultViewSpec extends YesNoViewBehaviours with NunjucksSupport {

  val messageKeyPrefix = "reportingCompanyResult"

  val form = new ReportingCompanyResultFormProvider()()

  Seq(Nunjucks, Twirl).foreach { templatingSystem =>

    s"ReportingCompanyResult ($templatingSystem) view" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable =
        if (templatingSystem == Nunjucks) {
          await(nunjucksRenderer.render(ReportingCompanyResultTemplate, Json.toJsObject(YesNoRadioViewModel(form, NormalMode)))(fakeRequest))
        } else {
          val view = viewFor[reportingCompanyResultView](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))

      behave like yesNoPage(form, applyView, messageKeyPrefix, routes.ReportingCompanyResultController.onSubmit(NormalMode).url)
    }
  }
}
