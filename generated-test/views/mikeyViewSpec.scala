package views

import controllers.routes
import forms.mikeyFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.viewmodels.Radios
import views.behaviours.YesNoViewBehaviours
import views.html.mikeyView

class mikeyViewSpec extends YesNoViewBehaviours  {

  val messageKeyPrefix = "mikey"

  val form = new mikeyFormProvider()()

    s"mikeyView" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = {
        val view = viewFor[mikeyView](Some(emptyUserAnswers))
        view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
      }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))

      behave like yesNoPage(form, applyView, messageKeyPrefix, routes.mikeyController.onSubmit(NormalMode).url)
    }
  }
}
